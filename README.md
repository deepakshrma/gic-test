# GIC Cinemas - Booking System

__GIC Cinemas Booking System is a Command Line Interface (CLI) and Web-based application that allows users to book seats
conveniently.__

## Directory Structure

```
|- app 
    |- App.kt            # Main CLI version of the ticket booking system
    
|- data                 # Static content and demo files

|- shared               # Shared resources for CLI and Web versions
    |- exceptions
    |- model
    |- repositories
    |- service
    
|- web                  # Web version of the ticket booking system
    |- Routing.kt       # Application routing
    |- WebApplication.kt # Main web application
    
    |- static           # Static assets
        |- css
        |- js
        |- index.html
        
-api.http   #Http API documentation

```

## How to Run 🚀

1. CLI Version
   Run the main function in [App.kt](app%2Fsrc%2Fmain%2Fkotlin%2Fcom%2Fgic%2Fcinemas%2FApp.kt):

```bash
kotlinc App.kt -include-runtime -d app.jar
java -jar app.jar
```

2. Web UI Version

Run the main function in [WebApplication.kt](web%2Fsrc%2Fmain%2Fkotlin%2Fcom%2Fgic%2Fcinemas%2Fui%2FWebApplication.kt):

```bash
./gradlew run
```

Then, open http://localhost:8080 in your browser.

## Demo 🎥

### CLI Version

[📽️ Watch CLI Demo](data%2Fcli_demo.mp4)

### Web UI Version

[📽️ Watch Web UI Demo](data%2Fui_demo.mp4)

## Technologies Used 🛠️

The current version of GIC Cinemas is built using Kotlin CLI and Kotlin Ktor Web Application.

* Ktor – Web framework
* Native JavaScript – For frontend
* Kotlin Test (JUnit Alternative) – For testing

## Improvements and Future Roadmap

The algorithm is quite naive. It can be further improved to avoid scalability issue.

[ROADMAP.md](ROADMAP.md), Read more [here](ROADMAP.md) for future planing