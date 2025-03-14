# Payment Microservice

## Overview

The Payment Microservice is responsible for handling payment processing, transaction management, and integration with external payment gateways. It is built using Java, Spring Boot, and MySQL.

## Tech Stack

- Java 17
- Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Security, Spring Cloud Config)
- MySQL (AWS RDS for production)
- Docker (for containerization)
- Postman (for API Documentation)
- Lombok (for boilerplate code reduction)
- JUnit & Mockito (for unit testing)

## Features

- Secure payment processing
- Transaction history and logging
- Support for multiple payment methods (credit/debit cards)
- Integration with external payment gateways
- API documentation using Postman

## API Endpoints

| Method | Endpoint                      | Description                   |
| ------ | ----------------------------- | ----------------------------- |
| POST   | /api/payments/process         | Process a payment transaction |
| GET    | /api/payments/{transactionId} | Get payment details           |
| GET    | /api/payments/user/{userId}   | Get payment history of a user |
| POST   | /api/payments/refund          | Initiate a refund request     |

## Architecture

- Controller Layer: Handles API requests.
- Service Layer: Business logic implementation.
- Repository Layer: Database interaction using Spring Data JPA.

## Database Schema

The service uses MySQL with tables like:

- users (User data, authentication details)
- transactions (Stores payment transactions)
- refunds (Stores refund requests)

## Running Locally

### Prerequisites

- Install Java 17 and Maven
- Set up MySQL database
- Configure environment variables

### Steps

1. Clone the repository:
   bash
   git clone https://github.com/AnupamC16/BookOnTheGo_Payment_Microservice.git
   cd BookOnTheGo_Payment_Microservice
2. Build and run the application:
   bash
   mvn clean install
   mvn spring-boot:run

## Testing

Run unit and integration tests:
bash
mvn test

## Contributors

- Anupam Chopra - https://github.com/AnupamC16/BookOnTheGo_Payment_Microservice

## License

This project is licensed under the MIT License - see the LICENSE file for details.
