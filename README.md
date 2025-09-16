# Employee Management Web Application

A full-stack web application to **create, list, modify, and delete employees**. The project uses **React TypeScript** with **ChakraUI** for the frontend, and a **Spring Boot Java REST API** with **MySQL** for the backend. It also includes schema validation, logging, and testing frameworks.

**Project Board & Tasks:** [JIRA CCS Board](https://janitech2025.atlassian.net/jira/software/projects/CCS/boards/1)

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Backend API](#backend-api)
- [Testing](#testing)
- [Logging](#logging)
- [Project Management](#project-management)
- [License](#license)

---

## Features

- Create, view, update, and delete employee records
- Employee data validated using **Zod schemas** on the frontend
- **RESTful API** with Spring Boot
- **MySQL database** for persistence
- **End-to-end tests** using JUnit and Mockito
- Clean and responsive UI built with **ChakraUI**
- Logging with **SLF4J**
- Modular and maintainable architecture

---

## Tech Stack

![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![ChakraUI](https://img.shields.io/badge/ChakraUI-319795?style=for-the-badge&logo=chakra-ui&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-00C853?style=for-the-badge&logo=mockito&logoColor=white)
![SLF4J](https://img.shields.io/badge/SLF4J-6DB33F?style=for-the-badge)
![Zod](https://img.shields.io/badge/Zod-6C63FF?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-FF5C00?style=for-the-badge&logo=cloudinary&logoColor=white)

---

## Project Structure

backend/
├─ src/main/java/com/example/employee
│ ├─ controller/
│ ├─ service/
│ ├─ repository/
│ ├─ entity/
│ ├─ dto/
│ └─ mapper/
└─ src/test/java/com/example/employee
└─ ... (JUnit & Mockito tests)

frontend/
├─ src/
│ ├─ components/
│ ├─ pages/
│ ├─ hooks/
│ ├─ services/
│ └─ validation/ (Zod schemas)
└─ package.json

## UI Design inspirations

https://janitech2025.atlassian.net/wiki/spaces/CCS/pages/2064386/UI+design+inspirations

## Employee Creator UI snapshots

### Employee List Page

![Employee List](images/main.png)

### List contracts and highlight active contract

![UpdateRecent Contract](images/UpdateRecentContract.png)

### Create Employee Form

![Create Employee](images/CreateEmployee.png)

### Edit Employee Details

![Edit Employee](images/UpdateEmployeeDetails.png)

### Add contract to employee

![Add contract](images/AddContracttoEmployee.png)

### Update Recent contract details

![Update recent contract](images/UpdateRecentContract.png)

### Delete employee record and display toast message

![Delete employee record](images/DeleteEmployeeToastMsg.png)

### Generate employee report

![Generate employee report](images/GenerateReportDropDown.png)

## Getting Started

1. Clone the repository:

   ```bash
   git clone <repo-url>

   ```

2. Backend: configure MySQL database in application.properties

3. Backend: build and run:
   mvn clean install
   mvn spring-boot:run

4. Frontend: install dependencies and start:
   cd frontend
   npm install
   npm start

5. Access the web app at http://localhost:5173
   Backend runs at http://localhost:8080

## Environment Variables

The application requires the following environment variables to be set for proper configuration of the database and Cloudinary image uploads. You can create a `.env` file in the root directory or set them in your system environment.

| Variable      | Description                                    |
| ------------- | ---------------------------------------------- |
| `DB_NAME`     | Name of the MySQL database                     |
| `DB_USERNAME` | MySQL database username                        |
| `DB_PASSWORD` | MySQL database password                        |
| `CLOUD_NAME`  | Cloudinary cloud name                          |
| `API_KEY`     | Cloudinary API key                             |
| `API_SECRET`  | Cloudinary API secret                          |
| `FOLDER_NAME` | Cloudinary folder name where images are stored |

## API Endpoints

| Operation       | HTTP Method & Endpoint                          | Description                                 |
| --------------- | ----------------------------------------------- | ------------------------------------------- |
| Create Employee | `POST /employees`                               | Add a new employee.                         |
| List Employees  | `GET /employees`                                | Retrieve a list of all employees.           |
| Update Employee | `PUT /employees/{id}`                           | Update details of an existing employee.     |
| Delete Employee | `DELETE /employees/{id}`                        | Remove an employee by ID.                   |
| Add Contract    | `POST /employees/{id}/contracts`                | Add a new contract for an employee.         |
| Update Contract | `PUT /employees/{id}/contracts/{contractId}`    | Update an existing contract of an employee. |
| Delete Contract | `DELETE /employees/{id}/contracts/{contractId}` | Delete a contract for an employee.          |

# Testing

Unit & Integration Tests: JUnit 5
Mocking: Mockito
End-to-End REST Tests: RestAssured

# Logging

SLF4J is used for logging across backend services
