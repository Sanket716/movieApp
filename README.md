# MovieApp

## Prerequisites
To run this project, ensure you have the following installed on your system:

- **JDK 21** - Required to run the backend developed using Spring Boot 3.
- **Spring Boot 3** - Backend framework for handling API requests and business logic.
- **Angular 16** - Frontend framework for building the user interface.
- **Node.js (Latest LTS version)** - Required to run Angular.
- **NPM** - Package manager for Angular dependencies.
- **MySQL** (or any database) - To store movie details and user authentication data. Update the yaml file based on your DB.
- **Postman** (Optional) - To test backend APIs.
- Update the username and password for email validation in application.yaml file after cloning the project by generating through your google account

---

## Features
### User Roles
There are two major user roles in the MovieApp:

#### **1. Admin**
- Can **add** new movies.
- Can **delete** existing movies.
- Can **update** movie details such as title, genre, and poster.
- Can **view** the complete list of movies.
- Has complete access to manage movie content.

#### **2. User**
- Can **view** the list of available movies.
- Cannot modify movie details.
- Has limited access to application functionalities.

### Authentication & Authorization
- Authentication is implemented using **JWT (JSON Web Token)**.
- Role-based authorization is enforced to differentiate Admin and User privileges.
- Secure login and signup functionalities with encrypted password storage.

### Password Management
The application includes secure password management features:

#### **Forgot Password**
If a user forgets their password, they can follow these steps to reset it:
1. Click on the **Forgot Password** link on the login page.
2. Enter their registered email address.
3. An **OTP (One-Time Password)** is sent to the email for verification.
4. After validating the OTP, the user is allowed to update their password.

#### **Reset Password**
For authenticated users who wish to change their password:
1. Navigate to the **Reset Password** section in the application.
2. Enter the **current password** for verification.
3. Provide the **new password** and confirm it.
4. Submit the request, and the password will be updated securely.

### Security Measures
- Passwords are securely hashed before storing in the database.
- **JWT tokens** ensure secure communication between frontend and backend.
- Proper validation and exception handling mechanisms are in place to prevent unauthorized access.

---

## Installation & Setup
### Backend (Spring Boot 3)
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository/movieApp.git
   ```
2. Navigate to the backend directory:
   ```bash
   cd movie-backend
   ```
3. Build and run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend (Angular 16)
1. Navigate to the frontend directory:
   ```bash
   cd movie-frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the Angular application:
   ```bash
   ng serve
   ```
4. Open `http://localhost:4200` in your browser to access the application.

---

## Technologies Used
- **Backend:** Spring Boot 3, JDK 21, Hibernate, MySQL/PostgreSQL
- **Frontend:** Angular 16, TypeScript, HTML, CSS
- **Authentication:** JWT (JSON Web Token)
- **Deployment:** Docker (Optional)

---

Enjoy using **MovieApp**! 🎬🍿
