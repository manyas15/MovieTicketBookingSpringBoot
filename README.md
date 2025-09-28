# Movie Ticket Booking System

A simple web-based movie ticket booking system built with **Spring Boot** - perfect for learning Spring Boot basics.

## What I Learned

- **Spring Boot Basics**: Created a web application using Spring Boot framework
- **REST APIs**: Built simple REST endpoints for booking and retrieving data
- **Thymeleaf Templates**: Used Thymeleaf for server-side HTML rendering
- **MVC Pattern**: Implemented Model-View-Controller architecture
- **Dependency Injection**: Used Spring's @Autowired for connecting services
- **Data Storage**: Implemented in-memory data storage using HashMap

## Project Structure

```
src/main/java/com/example/movieticket/
├── MovieticketApplication.java    # Main Spring Boot application
├── controller/
│   ├── BookingController.java     # REST API endpoints
│   └── WebController.java         # Web page routing
├── service/
│   ├── MovieService.java          # Movie business logic
│   └── BookingService.java        # Booking business logic
├── model/
│   ├── Movie.java                 # Movie data model
│   ├── Show.java                  # Show data model
│   └── Booking.java               # Booking data model
└── runner/
    └── AppRunner.java             # Application startup tasks
```

## Technologies Used

- **Java 17+**
- **Spring Boot 3.5.6**
- **Thymeleaf** (for HTML templates)
- **Maven** (for build management)
- **HTML/CSS/JavaScript** (for frontend)

## Features

- View available movies
- Book movie tickets for specific shows  
- Select seats for booking
- View all bookings
- Responsive web interface
- Currency conversion (USD to INR)
## How to Run

1. Make sure Java 17+ is installed
2. Open terminal in project directory
3. Run: `mvn spring-boot:run`
4. Open browser and go to: `http://localhost:8080`

## Learning Notes

This project demonstrates basic Spring Boot concepts:
- `@SpringBootApplication` - Main application class
- `@RestController` - For REST API endpoints  
- `@Service` - For business logic services
- `@Component` - For Spring-managed components
- `@GetMapping/@PostMapping` - For HTTP request mapping

The project uses simple Java concepts and avoids advanced Spring features to focus on core learning.

## Key Learning Points

- **Spring Boot Auto-Configuration**: No complex XML configuration needed
- **Embedded Tomcat**: Web server runs automatically  
- **Thymeleaf Integration**: Easy template rendering
- **REST APIs**: Simple JSON endpoints for data exchange
- **Dependency Injection**: Spring manages object creation and wiring
- **MVC Pattern**: Clear separation between controller, service, and model layers
- Welcome message and quick navigation
- Featured movies display
- Quick booking options

### **2. Movies Page** (`/movies`)
- Browse all available movies
- View movie details (title, genre, duration, shows)
- Direct booking buttons

### **3. Booking Page** (`/booking`)
**5-Step Booking Process:**
1. **Customer Information** - Enter your name
2. **Select Movie** - Choose from available movies
3. **Select Show** - Pick show time
4. **Select Seats** - Interactive seat selection
5. **Review & Confirm** - Confirm booking details

### **4. Bookings Page** (`/bookings`)
- View all bookings
- Search by customer name
- Detailed booking information
- Print tickets functionality
- Booking statistics

---

## 🖥️ CLI Interface Usage

When you run the application, you'll see a menu-driven interface:

```
🎬 Welcome to Movie Ticket Booking System! 🎬
==================================================

Available Movies:
1. The Matrix (Action) - 136 minutes
2. Inception (Sci-Fi) - 148 minutes
3. The Dark Knight (Action) - 152 minutes

Menu:
1. View all movies
2. View movie shows
3. Book tickets
4. View bookings
5. Cancel booking
6. Exit

Enter your choice: 
```

### **Example CLI Workflow:**
1. Choose option **1** to view movies
2. Choose option **3** to book tickets
3. Enter customer name: `John Doe`
4. Select movie ID: `1`
5. Select show ID: `1`
6. Select seats: `1 2 3`
7. Booking confirmed!

---

## 🏗️ Project Structure

```
movieticket/
├── src/
│   ├── main/
│   │   ├── java/com/example/movieticket/
│   │   │   ├── MovieticketApplication.java      # Main application entry point
│   │   │   ├── model/                           # Data models (Movie, Show, Booking)
│   │   │   ├── service/                         # Business logic layer
│   │   │   ├── controller/                      # REST API endpoints & web controllers
│   │   │   ├── repository/                      # Data access layer
│   │   │   └── runner/                          # CLI interface
│   │   └── resources/
│   │       ├── static/                          # Web assets (CSS, JavaScript)
│   │       ├── templates/                       # HTML templates
│   │       └── application.properties           # App configuration
│   └── test/                                    # Unit tests
└── pom.xml                                      # Maven configuration
```

---

## 🔧 Technical Implementation

### **Backend Components**

#### **1. Data Models**
```java
// Movie.java - Represents a movie
public class Movie {
    private int id;
    private String title;
    private String genre;
    private int duration;
    private List<Show> shows;
}

// Show.java - Represents a movie show  
public class Show {
    private int id;
    private int movieId;
    private String showTime;
    private int totalSeats;
    private List<Integer> availableSeats;
}

// Booking.java - Represents a ticket booking
public class Booking {
    private int id;
    private String customerName;
    private int movieId;
    private int showId;
    private List<Integer> seats;
    private double totalPrice;
}
```

#### **2. Service Layer**
```java
@Service
public class MovieService {
    public List<Movie> getAllMovies()
    public Movie getMovieById(int id)
    public List<Show> getShowsForMovie(int movieId)
}

@Service
public class BookingService {
    public Booking createBooking(BookingRequest request)
    public List<Booking> getAllBookings()
    public List<Booking> getBookingsByCustomer(String customerName)
}
```

#### **3. REST API Endpoints**
```java
@RestController
@RequestMapping("/api")
public class MovieController {
    GET /api/movies              # Get all movies
    GET /api/movies/{id}         # Get movie by ID
    GET /api/movies/search       # Search movies
}

@RestController
@RequestMapping("/api")
public class BookingController {
    GET /api/bookings            # Get all bookings
    POST /api/bookings           # Create new booking
    GET /api/bookings/{id}       # Get booking by ID
    GET /api/bookings/customer/{name} # Get bookings by customer
}
```

---

## 🎨 UI/UX Features

### **Design Highlights:**
- 🎬 **Movie theme** - Netflix-inspired red and black color scheme
- 📱 **Responsive** - Works perfectly on mobile, tablet, and desktop
- ♿ **Accessible** - Keyboard navigation, screen reader support
- 🎯 **User-friendly** - Clear navigation, intuitive booking flow
- ⚡ **Fast loading** - Optimized CSS and JavaScript
- 🔔 **Notifications** - Real-time feedback for user actions

### **Interactive Elements:**
- **Seat Selection** - Visual seat map with availability status
- **Step-by-step Booking** - Guided 5-step process
- **Search & Filter** - Find bookings by customer name
- **Modal Dialogs** - Detailed booking information
- **Print Tickets** - Printable ticket format

---

## 🧪 Testing the Application

### **Manual Testing Scenarios:**

#### **CLI Testing:**
1. Start application
2. Test each menu option (1-6)
3. Book tickets with valid data
4. Try booking unavailable seats
5. View and cancel bookings

#### **Web Testing:**
1. Visit http://localhost:8080
2. Navigate through all pages
3. Complete booking workflow
4. Test responsive design (resize browser)
5. Test with different browsers

#### **API Testing:**
```bash
# Get all movies
curl http://localhost:8080/api/movies

# Create a booking
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Test User","movieId":1,"showId":1,"seats":[1,2]}'

# Get all bookings
curl http://localhost:8080/api/bookings
```

---

## 🔧 Configuration & Customization

### **application.properties**
```properties
# Server configuration
server.port=8080

# Web configuration  
spring.web.resources.static-locations=classpath:/static/
spring.thymeleaf.cache=false

# Logging
logging.level.com.example.movieticket=INFO
```

### **Customizing the Application:**

#### **Adding New Movies:**
Edit `MovieService.java` constructor:
```java
movies.add(new Movie(4, "Your Movie", "Genre", 120));
```

#### **Changing Seat Price:**
Edit `BookingService.java`:
```java
private static final double PRICE_PER_SEAT = 15.00; // Change from 10.00
```

#### **Modifying Colors:**
Edit `style.css` CSS variables:
```css
:root {
    --primary-color: #your-color;
}
```

---

## 📚 Learning Outcomes

By working with this project, you'll learn:

### **Spring Boot Concepts:**
- ✅ **Auto-configuration** - How Spring Boot simplifies setup
- ✅ **Dependency Injection** - Using @Service, @Repository, @Controller
- ✅ **REST APIs** - Creating and consuming web services
- ✅ **MVC Pattern** - Model-View-Controller architecture
- ✅ **Configuration** - Using application.properties

### **Web Development:**
- ✅ **HTML5** - Modern semantic markup
- ✅ **CSS3** - Flexbox, Grid, animations, responsive design
- ✅ **JavaScript** - DOM manipulation, API calls, event handling
- ✅ **AJAX** - Asynchronous web requests

### **Software Engineering:**
- ✅ **Layered Architecture** - Service, Repository, Controller layers
- ✅ **Separation of Concerns** - Each class has a specific purpose
- ✅ **Error Handling** - Proper exception management
- ✅ **User Experience** - Intuitive interface design

---

## 🎓 Next Steps & Extensions

### **Beginner Extensions:**
1. **Add movie ratings** (1-5 stars)
2. **Add movie descriptions** and posters
3. **Implement seat categories** (Premium, Standard)
4. **Add booking timestamps**

### **Intermediate Extensions:**
1. **Database integration** (H2, MySQL)
2. **User authentication** (login/register)
3. **Payment integration** (Stripe API)
4. **Email notifications** (booking confirmations)

### **Advanced Extensions:**
1. **Microservices architecture**
2. **Redis caching** for performance
3. **JWT authentication** for security
4. **Docker containerization**
5. **CI/CD pipeline** with GitHub Actions

---

## 🚀 Deployment Options

### **1. Local Deployment**
```bash
mvn clean package
java -jar target/movieticket-0.0.1-SNAPSHOT.jar
```

### **2. Docker Deployment**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/movieticket-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **3. Cloud Deployment**
- **Heroku**: Push to Heroku with Procfile
- **AWS**: Deploy to Elastic Beanstalk
- **Azure**: Deploy to App Service
- **Railway**: Connect GitHub repository

---

## 🛠️ Troubleshooting

### **Common Issues:**

#### **Port Already in Use**
```
Error: Port 8080 is already in use
Solution: Change server.port=8081 in application.properties
```

#### **CSS/JS Not Loading**
```
Problem: Static files not found
Solution: Ensure files are in src/main/resources/static/
```

#### **API Calls Failing**
```
Problem: 404 errors on API endpoints
Solution: Check @RequestMapping annotations in controllers
```

#### **Build Failures**
```bash
# Clean and rebuild
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

---

## 📞 Quick Reference

### **Important URLs:**
- **Application**: http://localhost:8080
- **Home Page**: http://localhost:8080/
- **Movies**: http://localhost:8080/movies
- **Booking**: http://localhost:8080/booking
- **My Bookings**: http://localhost:8080/bookings

### **API Endpoints:**
- `GET /api/movies` - All movies
- `GET /api/movies/{id}` - Single movie
- `POST /api/bookings` - Create booking
- `GET /api/bookings` - All bookings
- `GET /api/bookings/customer/{name}` - Customer bookings

### **Key Commands:**
```bash
mvn spring-boot:run          # Start application
mvn clean compile           # Build project
mvn clean package           # Create JAR file
java -jar target/movieticket-0.0.1-SNAPSHOT.jar  # Run JAR
```

---

## 🎉 Conclusion

This Movie Ticket Booking System demonstrates a **complete full-stack application** using modern technologies while remaining **beginner-friendly**. It showcases:

- **Backend development** with Spring Boot
- **Frontend development** with HTML/CSS/JavaScript  
- **API design** and implementation
- **User interface** design and experience
- **Software architecture** best practices

The project is designed to be **educational**, **practical**, and **extensible** - perfect for learning web development fundamentals while building something useful and impressive!

**Happy Coding! 🎬✨**
   ```
3. **Run the application** using Maven:
   ```bash
   mvn spring-boot:run
   ```
4. **Follow the on-screen menu** to interact with the application

### Alternative Method (using IDE):
1. Open the project in your IDE
2. Run the `MovieticketApplication.java` file
3. The application will start in the console

## 🏗️ Project Structure

```
movieticket/
├── src/main/java/com/example/movieticket/
│   ├── MovieticketApplication.java    # Main Spring Boot application
│   ├── model/                         # Data model classes
│   │   ├── Movie.java                 # Movie entity
│   │   ├── Show.java                  # Show/timing entity
│   │   └── Booking.java               # Booking entity
│   ├── service/                       # Business logic layer
│   │   ├── MovieService.java          # Movie management
│   │   └── BookingService.java        # Booking management
│   └── runner/
│       └── AppRunner.java             # CLI interface & main logic
├── src/main/resources/
│   └── application.properties         # Configuration settings
├── src/test/java/
│   └── MovieticketApplicationTests.java
├── pom.xml                           # Maven dependencies
└── README.md                         # This file
```

## 📚 Key Components Explained

### 🎭 **Model Classes (Data Layer)**
- **Movie.java**: Represents a movie with properties like ID, title, genre, duration, and associated shows
- **Show.java**: Represents a movie showing with timing, seat availability, and booking management
- **Booking.java**: Represents a ticket booking with customer details, seats, and pricing

### ⚙️ **Service Classes (Business Logic)**
- **MovieService.java**: Handles movie and show management, loads sample data
- **BookingService.java**: Manages ticket booking, cancellation, and seat availability

### 🖥️ **Runner Class (User Interface)**
- **AppRunner.java**: Provides the command-line menu interface, handles user input and displays results

### 🔧 **Configuration**
- **application.properties**: Contains application settings and configuration
- **pom.xml**: Maven build file with dependencies

## 🎮 Application Features & Usage

### **Main Menu Options:**
1. ** View All Movies**: Shows all available movies with their details and show timings
2. ** Book Movie Tickets**: Step-by-step booking process with seat selection
3. ** View My Bookings**: Displays all current bookings with details
4. ** Cancel Booking**: Cancel existing bookings by ID
5. ** Exit**: Close the application

### **Sample Usage Flow:**
```
1. Start the application
2. View movies to see what's available
3. Book tickets by:
   - Entering your name
   - Selecting movie ID
   - Choosing show time
   - Selecting available seats
4. View your booking confirmation
5. Optionally cancel bookings if needed
```

## 🎓 Beginner-Friendly Features

### **What makes this project perfect for beginners:**

✅ **Simple Architecture**: Clear separation between data, business logic, and user interface  
✅ **No Database Complexity**: Uses in-memory storage (HashMap, ArrayList)  
✅ **Plain Java**: No advanced features like streams, lambdas, or complex design patterns  
✅ **Clear Error Messages**: User-friendly error handling and validation  
✅ **Step-by-step Interface**: Guided user experience with clear prompts  
✅ **Readable Code**: Well-commented and organized code structure  



## 🛠️ Technical Details

### **Dependencies Used:**
- **Spring Boot Starter**: Core Spring Boot functionality
- **Spring Boot DevTools**: Development-time features (auto-restart)
- **Spring Boot Test**: Testing framework

### **Key Java Concepts Demonstrated:**
- Constructor injection
- ArrayList and HashMap usage
- Exception handling with try-catch
- Scanner for input handling
- Method organization and naming
- Basic validation logic

### **Spring Boot Features Used:**
- `@SpringBootApplication` - Main application class
- `@Component` - Spring-managed components
- `@Service` - Service layer annotation
- `CommandLineRunner` - Application startup hook

## � Enhancement Ideas for Practice

Try adding these features to improve your skills:

### **Easy Enhancements:**
1. Add movie ratings (1-5 stars)
2. Add different price categories for seats
3. Add movie descriptions
4. Add show dates along with times
5. Add input validation for customer names

### **Medium Enhancements:**
1. Save bookings to a text file
2. Load movie data from a file
3. Add customer email validation
4. Add booking history with timestamps
5. Implement seat selection with a visual layout

### **Advanced Challenges:**
1. Add a web interface using Spring MVC
2. Integrate with a database (H2, MySQL)
3. Add REST API endpoints
4. Implement user authentication
5. Add email confirmation for bookings


## 📞 Learning Support

### **How to Study This Code:**
1. Start with `MovieticketApplication.java` (entry point)
2. Understand the model classes (Movie, Show, Booking)
3. Study the service classes (business logic)
4. Examine the AppRunner (user interface)
5. Try modifying small features and see results

### **Debugging Tips:**
- Add `System.out.println()` statements to trace execution
- Use your IDE's debugger to step through code
- Read error messages carefully - they usually point to the issue
- Test one feature at a time when making changes

## � Next Steps

After mastering this project:
1. Learn about databases and JPA
2. Explore web development with Spring MVC
3. Study REST API development
4. Learn about testing with JUnit
5. Explore microservices architecture

---

## 🤝 Contributing

This project is designed for learning. Feel free to:
- Fork the repository and experiment
- Add new features for practice
- Improve the user interface
- Add more comprehensive error handling
- Share your enhancements with others

## 📄 License

This project is created for educational purposes. Feel free to use it for learning and teaching Java and Spring Boot concepts.

---

**Happy Coding! 🚀**

*Remember: The best way to learn programming is by doing. Don't just read the code - run it, modify it, break it, and fix it!*
