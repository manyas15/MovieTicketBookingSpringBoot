/**
 * MOVIE TICKET BOOKING SYSTEM - MAIN JAVASCRIPT FILE
 * ==================================================
 * 
 * This file contains all the JavaScript functionality for the movie ticket booking system,
 * including API calls, DOM manipulation, event handling, and utility functions.
 * 
 * Features:
 * - API communication with the Spring Boot backend
 * - Dynamic content loading and rendering
 * - Form validation and user interaction
 * - Local storage management
 * - Error handling and user feedback
 * - Responsive design support
 * 
 * Author: Movie Ticket Booking System
 * Version: 1.0.0
 */

/* ===========================================
   GLOBAL CONFIGURATION
   =========================================== */

const CONFIG = {
    API_BASE_URL: '/api',
    ENDPOINTS: {
        movies: '/movies',
        bookings: '/bookings',
        shows: '/shows',
        system: '/system'
    },
    DEFAULT_TIMEOUT: 10000,
    RETRY_ATTEMPTS: 3,
    NOTIFICATIONS: {
        duration: 5000,
        position: 'top-right'
    },
    VALIDATION: {
        minNameLength: 2,
        maxNameLength: 50,
        minSeats: 1,
        maxSeats: 10
    },
    PRICE_PER_SEAT: 500.00,
    CURRENCY: 'INR'
};

/* ===========================================
   GLOBAL STATE MANAGEMENT
   =========================================== */

const AppState = {
    currentUser: null,
    cart: {
        items: [],
        total: 0
    },
    cache: {
        movies: null,
        bookings: null,
        shows: null,
        lastUpdated: null
    },
    ui: {
        loading: false,
        errors: [],
        currentPage: null
    }
};

/* ===========================================
   UTILITY FUNCTIONS
   =========================================== */

/**
 * Escapes HTML characters to prevent XSS attacks
 * @param {string} text - Text to escape
 * @returns {string} Escaped text
 */
function escapeHtml(text) {
    if (typeof text !== 'string') return '';
    
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    
    return text.replace(/[&<>"']/g, function(m) { 
        return map[m]; 
    });
}

/**
 * Formats currency values
 * @param {number} amount - Amount to format
 * @param {string} currency - Currency code (default: USD)
 * @returns {string} Formatted currency
 */
function formatCurrency(amount, currency = CONFIG.CURRENCY) {
    if (typeof amount !== 'number' || isNaN(amount)) {
        return '$0.00';
    }
    
    try {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: currency
        }).format(amount);
    } catch (error) {
        console.warn('Currency formatting failed:', error);
        return `$${amount.toFixed(2)}`;
    }
}

/**
 * Formats date and time
 * @param {Date|string} date - Date to format
 * @param {object} options - Formatting options
 * @returns {string} Formatted date
 */
function formatDate(date, options = {}) {
    if (!date) return 'Not specified';
    
    const defaultOptions = {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    
    try {
        const dateObj = date instanceof Date ? date : new Date(date);
        return dateObj.toLocaleDateString('en-US', { ...defaultOptions, ...options });
    } catch (error) {
        console.warn('Date formatting failed:', error);
        return date.toString();
    }
}

/**
 * Debounces function calls
 * @param {Function} func - Function to debounce
 * @param {number} wait - Wait time in milliseconds
 * @returns {Function} Debounced function
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Throttles function calls
 * @param {Function} func - Function to throttle
 * @param {number} limit - Time limit in milliseconds
 * @returns {Function} Throttled function
 */
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

/**
 * Generates a unique ID
 * @returns {string} Unique identifier
 */
function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

/**
 * Deep clones an object
 * @param {object} obj - Object to clone
 * @returns {object} Cloned object
 */
function deepClone(obj) {
    if (obj === null || typeof obj !== 'object') return obj;
    if (obj instanceof Date) return new Date(obj.getTime());
    if (obj instanceof Array) return obj.map(item => deepClone(item));
    if (typeof obj === 'object') {
        const copy = {};
        Object.keys(obj).forEach(key => {
            copy[key] = deepClone(obj[key]);
        });
        return copy;
    }
}

/* ===========================================
   LOCAL STORAGE MANAGEMENT
   =========================================== */

const Storage = {
    /**
     * Sets an item in localStorage with JSON serialization
     * @param {string} key - Storage key
     * @param {any} value - Value to store
     */
    set(key, value) {
        try {
            localStorage.setItem(`movieApp_${key}`, JSON.stringify(value));
        } catch (error) {
            console.warn('Failed to save to localStorage:', error);
        }
    },
    
    /**
     * Gets an item from localStorage with JSON parsing
     * @param {string} key - Storage key
     * @param {any} defaultValue - Default value if not found
     * @returns {any} Stored value or default
     */
    get(key, defaultValue = null) {
        try {
            const item = localStorage.getItem(`movieApp_${key}`);
            return item ? JSON.parse(item) : defaultValue;
        } catch (error) {
            console.warn('Failed to read from localStorage:', error);
            return defaultValue;
        }
    },
    
    /**
     * Removes an item from localStorage
     * @param {string} key - Storage key
     */
    remove(key) {
        try {
            localStorage.removeItem(`movieApp_${key}`);
        } catch (error) {
            console.warn('Failed to remove from localStorage:', error);
        }
    },
    
    /**
     * Clears all app-related localStorage items
     */
    clear() {
        try {
            Object.keys(localStorage).forEach(key => {
                if (key.startsWith('movieApp_')) {
                    localStorage.removeItem(key);
                }
            });
        } catch (error) {
            console.warn('Failed to clear localStorage:', error);
        }
    }
};

/* ===========================================
   HTTP CLIENT
   =========================================== */

const ApiClient = {
    /**
     * Makes an HTTP request with error handling and retries
     * @param {string} url - Request URL
     * @param {object} options - Request options
     * @returns {Promise} Response promise
     */
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            timeout: CONFIG.DEFAULT_TIMEOUT
        };
        
        const finalOptions = { ...defaultOptions, ...options };
        
        // Merge headers properly
        if (options.headers) {
            finalOptions.headers = { ...defaultOptions.headers, ...options.headers };
        }
        
        let lastError = null;
        
        for (let attempt = 1; attempt <= CONFIG.RETRY_ATTEMPTS; attempt++) {
            try {
                console.log(`API Request (attempt ${attempt}):`, url, finalOptions);
                
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), finalOptions.timeout);
                
                const response = await fetch(url, {
                    ...finalOptions,
                    signal: controller.signal
                });
                
                clearTimeout(timeoutId);
                
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }
                
                const contentType = response.headers.get('content-type');
                
                if (contentType && contentType.includes('application/json')) {
                    const data = await response.json();
                    console.log(`API Response:`, data);
                    return data;
                } else {
                    const text = await response.text();
                    console.log(`API Response (text):`, text);
                    return text;
                }
                
            } catch (error) {
                lastError = error;
                console.warn(`API request failed (attempt ${attempt}):`, error);
                
                if (attempt === CONFIG.RETRY_ATTEMPTS) {
                    break;
                }
                
                // Wait before retry (exponential backoff)
                await new Promise(resolve => setTimeout(resolve, Math.pow(2, attempt) * 1000));
            }
        }
        
        throw lastError;
    },
    
    /**
     * Makes a GET request
     * @param {string} url - Request URL
     * @param {object} options - Request options
     * @returns {Promise} Response promise
     */
    get(url, options = {}) {
        return this.request(url, { ...options, method: 'GET' });
    },
    
    /**
     * Makes a POST request
     * @param {string} url - Request URL
     * @param {any} data - Request body
     * @param {object} options - Request options
     * @returns {Promise} Response promise
     */
    post(url, data, options = {}) {
        return this.request(url, {
            ...options,
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    /**
     * Makes a PUT request
     * @param {string} url - Request URL
     * @param {any} data - Request body
     * @param {object} options - Request options
     * @returns {Promise} Response promise
     */
    put(url, data, options = {}) {
        return this.request(url, {
            ...options,
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    /**
     * Makes a DELETE request
     * @param {string} url - Request URL
     * @param {object} options - Request options
     * @returns {Promise} Response promise
     */
    delete(url, options = {}) {
        return this.request(url, { ...options, method: 'DELETE' });
    }
};

/* ===========================================
   API SERVICES
   =========================================== */

const MovieService = {
    /**
     * Fetches all movies
     * @returns {Promise<Array>} Array of movies
     */
    async getAll() {
        try {
            const movies = await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.movies}`);
            AppState.cache.movies = movies;
            AppState.cache.lastUpdated = new Date();
            return movies;
        } catch (error) {
            console.error('Failed to fetch movies:', error);
            throw new Error('Unable to load movies. Please try again later.');
        }
    },
    
    /**
     * Fetches a single movie by ID
     * @param {number} id - Movie ID
     * @returns {Promise<object>} Movie object
     */
    async getById(id) {
        try {
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.movies}/${id}`);
        } catch (error) {
            console.error(`Failed to fetch movie ${id}:`, error);
            throw new Error('Unable to load movie details. Please try again later.');
        }
    },
    
    /**
     * Searches movies by title
     * @param {string} query - Search query
     * @returns {Promise<Array>} Array of matching movies
     */
    async search(query) {
        try {
            const encodedQuery = encodeURIComponent(query);
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.movies}/search?q=${encodedQuery}`);
        } catch (error) {
            console.error('Failed to search movies:', error);
            throw new Error('Search failed. Please try again.');
        }
    }
};

const BookingService = {
    /**
     * Creates a new booking
     * @param {object} bookingData - Booking information
     * @returns {Promise<object>} Created booking
     */
    async create(bookingData) {
        try {
            const booking = await ApiClient.post(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.bookings}`, bookingData);
            // Clear bookings cache
            AppState.cache.bookings = null;
            return booking;
        } catch (error) {
            console.error('Failed to create booking:', error);
            throw new Error('Booking failed. Please check your details and try again.');
        }
    },
    
    /**
     * Fetches all bookings
     * @returns {Promise<Array>} Array of bookings
     */
    async getAll() {
        try {
            const bookings = await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.bookings}`);
            AppState.cache.bookings = bookings;
            AppState.cache.lastUpdated = new Date();
            return bookings;
        } catch (error) {
            console.error('Failed to fetch bookings:', error);
            throw new Error('Unable to load bookings. Please try again later.');
        }
    },
    
    /**
     * Fetches a single booking by ID
     * @param {number} id - Booking ID
     * @returns {Promise<object>} Booking object
     */
    async getById(id) {
        try {
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.bookings}/${id}`);
        } catch (error) {
            console.error(`Failed to fetch booking ${id}:`, error);
            throw new Error('Unable to load booking details. Please try again later.');
        }
    },
    
    /**
     * Searches bookings by customer name
     * @param {string} customerName - Customer name to search for
     * @returns {Promise<Array>} Array of matching bookings
     */
    async searchByCustomer(customerName) {
        try {
            const encodedName = encodeURIComponent(customerName);
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.bookings}/customer/${encodedName}`);
        } catch (error) {
            console.error('Failed to search bookings:', error);
            throw new Error('Search failed. Please try again.');
        }
    },
    
    /**
     * Cancels a booking
     * @param {number} id - Booking ID
     * @returns {Promise<boolean>} Success status
     */
    async cancel(id) {
        try {
            await ApiClient.delete(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.bookings}/${id}`);
            // Clear bookings cache
            AppState.cache.bookings = null;
            return true;
        } catch (error) {
            console.error(`Failed to cancel booking ${id}:`, error);
            throw new Error('Unable to cancel booking. Please try again later.');
        }
    }
};

const ShowService = {
    /**
     * Fetches all shows
     * @returns {Promise<Array>} Array of shows
     */
    async getAll() {
        try {
            const shows = await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.shows}`);
            AppState.cache.shows = shows;
            AppState.cache.lastUpdated = new Date();
            return shows;
        } catch (error) {
            console.error('Failed to fetch shows:', error);
            throw new Error('Unable to load shows. Please try again later.');
        }
    },
    
    /**
     * Fetches shows for a specific movie
     * @param {number} movieId - Movie ID
     * @returns {Promise<Array>} Array of shows
     */
    async getByMovieId(movieId) {
        try {
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.shows}/movie/${movieId}`);
        } catch (error) {
            console.error(`Failed to fetch shows for movie ${movieId}:`, error);
            throw new Error('Unable to load show times. Please try again later.');
        }
    }
};

const SystemService = {
    /**
     * Fetches system statistics
     * @returns {Promise<object>} System stats
     */
    async getStats() {
        try {
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.system}/stats`);
        } catch (error) {
            console.error('Failed to fetch system stats:', error);
            throw new Error('Unable to load statistics.');
        }
    },
    
    /**
     * Checks system health
     * @returns {Promise<object>} Health status
     */
    async checkHealth() {
        try {
            return await ApiClient.get(`${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.system}/health`);
        } catch (error) {
            console.error('System health check failed:', error);
            throw new Error('System health check failed.');
        }
    }
};

/* ===========================================
   NOTIFICATION SYSTEM
   =========================================== */

const NotificationManager = {
    container: null,
    notifications: new Map(),
    
    /**
     * Initializes the notification system
     */
    init() {
        if (!this.container) {
            this.container = document.createElement('div');
            this.container.id = 'notification-container';
            this.container.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 9999;
                pointer-events: none;
            `;
            document.body.appendChild(this.container);
        }
    },
    
    /**
     * Shows a notification
     * @param {string} message - Notification message
     * @param {string} type - Notification type (success, error, warning, info)
     * @param {number} duration - Duration in milliseconds
     * @returns {string} Notification ID
     */
    show(message, type = 'info', duration = CONFIG.NOTIFICATIONS.duration) {
        this.init();
        
        const id = generateId();
        const notification = document.createElement('div');
        notification.id = `notification-${id}`;
        notification.className = `notification ${type}`;
        notification.style.cssText = `
            background: var(--${type}-color, #333);
            color: white;
            padding: 12px 20px;
            margin-bottom: 10px;
            border-radius: 6px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            transform: translateX(400px);
            transition: transform 0.3s ease;
            pointer-events: auto;
            cursor: pointer;
            max-width: 400px;
            word-wrap: break-word;
        `;
        
        // Add close button
        const closeBtn = document.createElement('span');
        closeBtn.innerHTML = '&times;';
        closeBtn.style.cssText = `
            float: right;
            margin-left: 15px;
            font-weight: bold;
            cursor: pointer;
            font-size: 18px;
        `;
        closeBtn.onclick = () => this.hide(id);
        
        const messageDiv = document.createElement('div');
        messageDiv.textContent = message;
        messageDiv.style.marginRight = '25px';
        
        notification.appendChild(messageDiv);
        notification.appendChild(closeBtn);
        notification.onclick = () => this.hide(id);
        
        this.container.appendChild(notification);
        this.notifications.set(id, notification);
        
        // Animate in
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
        }, 10);
        
        // Auto-hide after duration
        if (duration > 0) {
            setTimeout(() => this.hide(id), duration);
        }
        
        return id;
    },
    
    /**
     * Hides a notification
     * @param {string} id - Notification ID
     */
    hide(id) {
        const notification = this.notifications.get(id);
        if (notification && notification.parentNode) {
            notification.style.transform = 'translateX(400px)';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
                this.notifications.delete(id);
            }, 300);
        }
    },
    
    /**
     * Clears all notifications
     */
    clear() {
        this.notifications.forEach((notification, id) => {
            this.hide(id);
        });
    }
};

// Convenience methods for different notification types
window.showNotification = (message, type = 'info', duration) => 
    NotificationManager.show(message, type, duration);

window.showSuccess = (message, duration) => 
    NotificationManager.show(message, 'success', duration);

window.showError = (message, duration) => 
    NotificationManager.show(message, 'error', duration);

window.showWarning = (message, duration) => 
    NotificationManager.show(message, 'warning', duration);

window.showInfo = (message, duration) => 
    NotificationManager.show(message, 'info', duration);

/* ===========================================
   FORM VALIDATION
   =========================================== */

const Validator = {
    /**
     * Validates a customer name
     * @param {string} name - Name to validate
     * @returns {object} Validation result
     */
    validateCustomerName(name) {
        const result = { valid: true, errors: [] };
        
        if (!name || typeof name !== 'string') {
            result.valid = false;
            result.errors.push('Name is required');
            return result;
        }
        
        const trimmedName = name.trim();
        
        if (trimmedName.length < CONFIG.VALIDATION.minNameLength) {
            result.valid = false;
            result.errors.push(`Name must be at least ${CONFIG.VALIDATION.minNameLength} characters long`);
        }
        
        if (trimmedName.length > CONFIG.VALIDATION.maxNameLength) {
            result.valid = false;
            result.errors.push(`Name cannot exceed ${CONFIG.VALIDATION.maxNameLength} characters`);
        }
        
        // Check for valid characters (letters, spaces, hyphens, apostrophes)
        if (!/^[a-zA-Z\s\-']+$/.test(trimmedName)) {
            result.valid = false;
            result.errors.push('Name can only contain letters, spaces, hyphens, and apostrophes');
        }
        
        return result;
    },
    
    /**
     * Validates seat selection
     * @param {Array} seats - Selected seats
     * @param {Array} availableSeats - Available seats
     * @returns {object} Validation result
     */
    validateSeatSelection(seats, availableSeats = []) {
        const result = { valid: true, errors: [] };
        
        if (!Array.isArray(seats)) {
            result.valid = false;
            result.errors.push('Invalid seat selection');
            return result;
        }
        
        if (seats.length < CONFIG.VALIDATION.minSeats) {
            result.valid = false;
            result.errors.push(`Please select at least ${CONFIG.VALIDATION.minSeats} seat(s)`);
        }
        
        if (seats.length > CONFIG.VALIDATION.maxSeats) {
            result.valid = false;
            result.errors.push(`Cannot select more than ${CONFIG.VALIDATION.maxSeats} seats`);
        }
        
        // Check for duplicate seats
        const uniqueSeats = [...new Set(seats)];
        if (uniqueSeats.length !== seats.length) {
            result.valid = false;
            result.errors.push('Duplicate seats selected');
        }
        
        // Check if all selected seats are available
        const unavailableSeats = seats.filter(seat => !availableSeats.includes(seat));
        if (unavailableSeats.length > 0) {
            result.valid = false;
            result.errors.push(`Seats ${unavailableSeats.join(', ')} are not available`);
        }
        
        return result;
    },
    
    /**
     * Validates booking data
     * @param {object} bookingData - Booking data to validate
     * @returns {object} Validation result
     */
    validateBookingData(bookingData) {
        const result = { valid: true, errors: [] };
        
        if (!bookingData) {
            result.valid = false;
            result.errors.push('Booking data is required');
            return result;
        }
        
        // Validate customer name
        const nameValidation = this.validateCustomerName(bookingData.customerName);
        if (!nameValidation.valid) {
            result.valid = false;
            result.errors.push(...nameValidation.errors);
        }
        
        // Validate movie ID
        if (!bookingData.movieId || typeof bookingData.movieId !== 'number') {
            result.valid = false;
            result.errors.push('Valid movie selection is required');
        }
        
        // Validate show ID
        if (!bookingData.showId || typeof bookingData.showId !== 'number') {
            result.valid = false;
            result.errors.push('Valid show selection is required');
        }
        
        // Validate seats
        const seatValidation = this.validateSeatSelection(bookingData.seats);
        if (!seatValidation.valid) {
            result.valid = false;
            result.errors.push(...seatValidation.errors);
        }
        
        return result;
    }
};

/* ===========================================
   ERROR HANDLING UTILITIES
   =========================================== */

/**
 * Displays form validation errors
 * @param {HTMLElement} errorElement - Element to display errors in
 * @param {string|Array} errors - Error message(s) to display
 */
function showError(errorElement, errors) {
    if (!errorElement) return;
    
    const errorMessages = Array.isArray(errors) ? errors : [errors];
    errorElement.textContent = errorMessages.join('. ');
    errorElement.style.display = 'block';
    errorElement.classList.add('visible');
    
    // Add error styling to parent form group
    const formGroup = errorElement.closest('.form-group');
    if (formGroup) {
        formGroup.classList.add('has-error');
    }
    
    // Scroll error into view if needed
    setTimeout(() => {
        errorElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, 100);
}

/**
 * Clears form validation errors
 * @param {HTMLElement} errorElement - Error element to clear
 */
function clearError(errorElement) {
    if (!errorElement) return;
    
    errorElement.textContent = '';
    errorElement.style.display = 'none';
    errorElement.classList.remove('visible');
    
    // Remove error styling from parent form group
    const formGroup = errorElement.closest('.form-group');
    if (formGroup) {
        formGroup.classList.remove('has-error');
    }
}

/**
 * Global error handler for unhandled promise rejections
 */
window.addEventListener('unhandledrejection', function(event) {
    console.error('Unhandled promise rejection:', event.reason);
    showError('An unexpected error occurred. Please refresh the page and try again.');
});

/**
 * Global error handler for JavaScript errors
 */
window.addEventListener('error', function(event) {
    console.error('JavaScript error:', event.error);
    showError('An unexpected error occurred. Please refresh the page and try again.');
});

/* ===========================================
   LOADING STATE MANAGEMENT
   =========================================== */

const LoadingManager = {
    loadingCount: 0,
    
    /**
     * Shows loading state
     * @param {string} message - Loading message
     */
    show(message = 'Loading...') {
        this.loadingCount++;
        
        let loadingEl = document.getElementById('global-loading');
        
        if (!loadingEl) {
            loadingEl = document.createElement('div');
            loadingEl.id = 'global-loading';
            loadingEl.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 9998;
                backdrop-filter: blur(2px);
            `;
            
            const content = document.createElement('div');
            content.style.cssText = `
                background: white;
                padding: 30px;
                border-radius: 10px;
                text-align: center;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
                color: #333;
            `;
            
            const spinner = document.createElement('div');
            spinner.style.cssText = `
                border: 4px solid #f3f3f3;
                border-top: 4px solid #e50914;
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 1s linear infinite;
                margin: 0 auto 15px;
            `;
            
            const messageEl = document.createElement('div');
            messageEl.id = 'loading-message';
            messageEl.textContent = message;
            
            // Add CSS animation for spinner
            const style = document.createElement('style');
            style.textContent = `
                @keyframes spin {
                    0% { transform: rotate(0deg); }
                    100% { transform: rotate(360deg); }
                }
            `;
            document.head.appendChild(style);
            
            content.appendChild(spinner);
            content.appendChild(messageEl);
            loadingEl.appendChild(content);
            document.body.appendChild(loadingEl);
        } else {
            document.getElementById('loading-message').textContent = message;
            loadingEl.style.display = 'flex';
        }
    },
    
    /**
     * Hides loading state
     */
    hide() {
        this.loadingCount = Math.max(0, this.loadingCount - 1);
        
        if (this.loadingCount === 0) {
            const loadingEl = document.getElementById('global-loading');
            if (loadingEl) {
                loadingEl.style.display = 'none';
            }
        }
    }
};

/* ===========================================
   INITIALIZATION AND PAGE SETUP
   =========================================== */

/**
 * Initializes the application
 */
function initializeApp() {
    console.log('Initializing Movie Ticket Booking System...');
    
    // Set up global error handling
    setupErrorHandling();
    
    // Initialize notification system
    NotificationManager.init();
    
    // Set up responsive navigation
    setupResponsiveNav();
    
    // Set up keyboard shortcuts
    setupKeyboardShortcuts();
    
    // Set up service worker for offline support (if available)
    setupServiceWorker();
    
    // Detect current page and run page-specific initialization
    const currentPage = detectCurrentPage();
    AppState.ui.currentPage = currentPage;
    
    console.log(`Current page: ${currentPage}`);
    
    // Run page-specific initialization
    switch (currentPage) {
        case 'home':
            initializeHomePage();
            break;
        case 'movies':
            initializeMoviesPage();
            break;
        case 'booking':
            initializeBookingPage();
            break;
        case 'bookings':
            initializeBookingsPage();
            break;
        default:
            console.log('Unknown page, running default initialization');
            break;
    }
    
    // Show welcome notification for first-time users
    if (!Storage.get('hasVisited', false)) {
        setTimeout(() => {
            showSuccess('Welcome to Movie Ticket Booking System! 🎬');
            Storage.set('hasVisited', true);
        }, 1000);
    }
    
    console.log('Application initialized successfully');
}

/**
 * Sets up global error handling
 */
function setupErrorHandling() {
    window.addEventListener('error', (event) => {
        console.error('Global error:', event.error);
    });
    
    window.addEventListener('unhandledrejection', (event) => {
        console.error('Unhandled promise rejection:', event.reason);
    });
}

/**
 * Sets up responsive navigation
 */
function setupResponsiveNav() {
    const nav = document.querySelector('.nav');
    if (!nav) return;
    
    // Add mobile menu toggle if needed
    if (window.innerWidth <= 768) {
        // Mobile navigation setup code would go here
    }
    
    // Handle window resize
    let resizeTimeout;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            // Handle responsive layout changes
        }, 250);
    });
}

/**
 * Sets up keyboard shortcuts
 */
function setupKeyboardShortcuts() {
    document.addEventListener('keydown', (e) => {
        // Escape key closes modals
        if (e.key === 'Escape') {
            const modals = document.querySelectorAll('.modal[style*="display: flex"]');
            modals.forEach(modal => {
                if (typeof closeModal === 'function') {
                    closeModal();
                }
            });
        }
        
        // Ctrl/Cmd + K opens search
        if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
            e.preventDefault();
            const searchInput = document.querySelector('input[type="search"], input[placeholder*="search" i]');
            if (searchInput) {
                searchInput.focus();
            }
        }
        
        // F5 refresh with cache clear
        if (e.key === 'F5' && e.ctrlKey) {
            e.preventDefault();
            AppState.cache = { movies: null, bookings: null, shows: null, lastUpdated: null };
            location.reload();
        }
    });
}

/**
 * Sets up service worker for offline support
 */
function setupServiceWorker() {
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/sw.js')
            .then(registration => {
                console.log('Service worker registered:', registration);
            })
            .catch(error => {
                console.log('Service worker registration failed:', error);
            });
    }
}

/**
 * Detects the current page based on URL and DOM elements
 * @returns {string} Current page name
 */
function detectCurrentPage() {
    const path = window.location.pathname.toLowerCase();
    
    if (path.includes('/movies')) return 'movies';
    if (path.includes('/booking')) return 'booking';
    if (path.includes('/bookings')) return 'bookings';
    if (path === '/' || path.includes('/index') || path.includes('/home')) return 'home';
    
    // Fallback: check for page-specific elements
    if (document.querySelector('.movies-grid')) return 'movies';
    if (document.querySelector('.booking-step')) return 'booking';
    if (document.querySelector('.bookings-list')) return 'bookings';
    
    return 'unknown';
}

/**
 * Initialize home page
 */
function initializeHomePage() {
    console.log('Initializing home page...');
    
    // Load recent movies for home page display
    loadRecentMoviesForHome();
    
    // Set up any home page specific functionality
    setupHomePageFeatures();
}

/**
 * Initialize movies page
 */
function initializeMoviesPage() {
    console.log('Initializing movies page...');
    // Movies page specific initialization is handled in movies.html script
}

/**
 * Initialize booking page
 */
function initializeBookingPage() {
    console.log('Initializing booking page...');
    // Booking page specific initialization is handled in booking.html script
}

/**
 * Initialize bookings page
 */
function initializeBookingsPage() {
    console.log('Initializing bookings page...');
    // Bookings page specific initialization is handled in bookings.html script
}

/**
 * Load recent movies for home page
 */
async function loadRecentMoviesForHome() {
    try {
        const movies = await MovieService.getAll();
        const recentMovies = movies.slice(0, 6); // Show first 6 movies
        
        const container = document.querySelector('.recent-movies');
        if (container && recentMovies.length > 0) {
            displayMoviesGrid(container, recentMovies);
        }
    } catch (error) {
        console.error('Failed to load recent movies for home:', error);
    }
}

/**
 * Set up home page features
 */
function setupHomePageFeatures() {
    // Set up quick booking buttons
    const quickBookButtons = document.querySelectorAll('.quick-book-btn');
    quickBookButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const movieId = e.target.dataset.movieId;
            if (movieId) {
                window.location.href = `/booking?movieId=${movieId}`;
            }
        });
    });
    
    // Set up featured movie carousel if present
    setupMovieCarousel();
}

/**
 * Set up movie carousel
 */
function setupMovieCarousel() {
    const carousel = document.querySelector('.movie-carousel');
    if (!carousel) return;
    
    let currentSlide = 0;
    const slides = carousel.querySelectorAll('.carousel-slide');
    const totalSlides = slides.length;
    
    if (totalSlides === 0) return;
    
    // Auto-advance carousel
    setInterval(() => {
        currentSlide = (currentSlide + 1) % totalSlides;
        carousel.style.transform = `translateX(-${currentSlide * 100}%)`;
    }, 5000);
}

/**
 * Generic function to display movies in a grid
 * @param {HTMLElement} container - Container element
 * @param {Array} movies - Array of movies to display
 */
function displayMoviesGrid(container, movies) {
    if (!container || !Array.isArray(movies)) return;
    
    container.innerHTML = movies.map(movie => `
        <div class="movie-card">
            <div class="movie-title">
                <h3>${escapeHtml(movie.title)}</h3>
            </div>
            <div class="movie-info">
                <p><i class="fas fa-tag"></i> ${escapeHtml(movie.genre || 'Unknown')}</p>
                <p><i class="fas fa-clock"></i> ${movie.duration || 0} minutes</p>
                <p><i class="fas fa-calendar"></i> ${movie.shows ? movie.shows.length : 0} shows available</p>
            </div>
            <div class="movie-actions">
                <a href="/booking?movieId=${movie.id}" class="btn btn-primary">
                    <i class="fas fa-ticket-alt"></i> Book Tickets
                </a>
                <a href="/movies#movie-${movie.id}" class="btn btn-secondary">
                    <i class="fas fa-info-circle"></i> View Details
                </a>
            </div>
        </div>
    `).join('');
}

/* ===========================================
   APPLICATION STARTUP
   =========================================== */

// Initialize the application when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}

// Export functions for use in other scripts
window.MovieTicketApp = {
    // Services
    MovieService,
    BookingService,
    ShowService,
    SystemService,
    
    // Utilities
    ApiClient,
    Storage,
    Validator,
    LoadingManager,
    NotificationManager,
    
    // Helper functions
    escapeHtml,
    formatCurrency,
    formatDate,
    debounce,
    throttle,
    generateId,
    deepClone,
    showError,
    clearError,
    displayMoviesGrid,
    
    // Global state
    AppState,
    CONFIG
};

console.log('Movie Ticket Booking System JavaScript loaded successfully! 🎬');