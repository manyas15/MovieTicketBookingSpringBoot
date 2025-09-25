# 🎬 Movie Ticket Booking System

A beginner-friendly Java Spring Boot application for booking movie tickets through a command-line interface (CLI). This project demonstrates core Java and Spring Boot concepts in a simple, easy-to-understand way.

## 📖 Project Overview

This Movie Ticket Booking System is specifically designed for beginners learning Java programming and Spring Boot framework. It provides a complete working application with a user-friendly command-line interface where users can:

- View available movies and their show timings
- Book tickets by selecting seats
- View their bookings
- Cancel existing bookings
- Interact through a clean, menu-driven interface

## 🎯 Learning Objectives

By studying and working with this project, you will learn:

### **Core Java Concepts:**
- Object-Oriented Programming (Classes, Objects, Inheritance)
- Collections (ArrayList, HashMap)
- Basic data structures and algorithms
- Exception handling and input validation
- Scanner for user input handling

### **Spring Boot Framework:**
- Spring Boot application structure
- Dependency Injection (@Component, @Service)
- CommandLineRunner interface
- Spring Boot auto-configuration
- Application properties configuration

### **Software Development Practices:**
- Clean code organization
- Separation of concerns (Model-View-Service architecture)
- Error handling and user feedback
- Menu-driven application design

## 🚀 How to Run the Project

### Prerequisites
- **Java 17 or higher** installed on your system
- **Maven** (usually comes with your IDE like IntelliJ IDEA or VS Code)
- A code editor or IDE (VS Code, IntelliJ IDEA, Eclipse)

### Steps to Run
1. **Clone or Download** the project to your local machine
2. **Open terminal/command prompt** and navigate to the project folder:
   ```bash
   cd path/to/movieticket
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