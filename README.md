# Create a README.md file with the given content
readme_content = """
# Class Booking System API

This is a Spring Boot-based REST API for managing fitness class bookings, waitlists, and user packages. Users can book classes if available, or be added to a waitlist if a class is full.

## Features

- User registration and login
- Class schedule management
- Booking system with package-based credit deduction
- Automatic waitlisting when classes are full
- Package management with expiry and remaining credits

## Technologies Used

- Java 17+
- Spring Boot
- Hibernate / JPA
- PostgreSQL / MySQL (configurable)
- Maven

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.6+
- A relational database (PostgreSQL or MySQL)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/kyawlatt14/booking-system-v2.git
   cd class-booking-api
