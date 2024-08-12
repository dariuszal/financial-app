# Application Overview

This application is designed to handle loan applications and interact with two different providers to process the applications. Below is a brief overview of the key features and functionality:

- **Loan Application Processing**: The application accepts loan applications from users and sends the request to two different providers. These providers are responsible for processing the application and returning offers.
- **Notification of Offers**: Once all offers from the providers are ready, the user will receive an email with the details of the offers.
- **Single Application Rule**: Users cannot submit another application using the same email address if the previous application has not yet been completed. This ensures that only one application per email address is processed at a time.
- **Scheduled Tasks**: A scheduled task runs periodically to check with the providers if the offer is ready or has been rejected. This allows the application to update the user promptly once the offer status changes.

# Application Startup Guide

## Prerequisites

Before you begin, ensure that you have the following software installed on your machine:

- **Docker**: Docker should be installed and running. You can download it from [Docker's official website](https://www.docker.com/get-started).
- **Docker Compose**: Docker Compose should be installed. Docker Desktop usually comes with Docker Compose, but if needed, you can refer to [the Docker Compose installation guide](https://docs.docker.com/compose/install/).

## Getting Started

This guide will help you start the application using Docker Compose. The process includes setting up a MySQL database that the application will use.

### Step 1: Clone the Repository

First, clone the application's repository to your local machine:

```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```

### Step 2: Start MySQL Database Container
Make sure that Docker Daemon is running, and execute this:
```bash
docker-compose up --build
```
### Step 3: Application Configuration
You have to configure your SMTP client with your preferred mail provider service in application.yml file. (I was using mailgun)
```yaml
  mail:
    host: smtp.mailgun.org
    port: 587
    username: your-username
    password: your-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```
### Step 4: Compile Jar
```bash
mvn clean install
```

### Step 5: Start Application 
   ```bash
java -jar target/financing-0.0.1-SNAPSHOT.jar
```

# API Documentation
Documentation is reachable when application is running on the following url:
http://localhost:8080/swagger-ui/index.html
