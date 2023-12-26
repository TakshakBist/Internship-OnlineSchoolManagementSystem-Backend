# Online School Management System

## Project Overview

 The system efficiently manages students, teachers, courses, and classrooms, incorporating various database relationships and robust business logic. 

## Features

### CRUD Operations

- Implemented Create, Read, Update, and Delete operations for students, teachers, courses, and classrooms.

### Pagination

- Developed pagination for efficient handling of large datasets in listing endpoints.

### Filtering

- Implemented date-time filtering (e.g., filtering courses by start date).

### Sorting

- Enabled sorting functionalities for list endpoints (e.g., sort students by name, sort teachers by join date).

### Business Logic

- Prevented students from enrolling in overlapping courses.
- Ensured classrooms are only assigned to one course at a time.
- Calculated the total number of students enrolled in a teacher's courses.
- Calculated the maximum number of courses enrolled by students.
- Prevented Students from enrolling in courses with full capacity.

## Technical Stack

- **Backend Framework:** Spring Boot
- **Database:** PostgreSQL

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/TakshakBist/Internship-OnlineSchoolManagementSystem-Backend.git
   ```
2. **Run the Application:**
    ```bash
   ./mvnw spring-boot:run
   ```
3. **Access the API:**
   Open a web browser or use tools like Postman.

4. **Testing:**
   Implemented comprehensive unit and integration tests to ensure application reliability and correctness.
   Tests can be executed using the following command:
   ```bash
   ./mvnw test
   ``` 

5. **API Documentation:**
   Utilized Swagger for documenting API endpoints. Access the API documentation by navigating to http://localhost:8080/swagger-ui/index.html


   