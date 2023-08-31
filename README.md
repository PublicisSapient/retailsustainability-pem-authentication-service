
# Authentication Service

The Authentication Service is a Spring Boot application that provides user authentication functionalities such as login and logout. It utilizes JSON Web Tokens (JWT) for secure authentication.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Error Messages](#error-messages)
- [Contributing](#contributing)
- [License](#license)

## Features

- To authenticate a user and obtain a JWT token
- To log out a user and invalidate their JWT token
- API documentation using Swagger
- Error messages for common scenarios



## Technologies Used

- Java 17
- Spring Boot 3.0.5
- MongoDB
- Swagger (OpenAPI)
- Actuator
- Jacoco



## Getting Started

To get started with the Product Service, follow these steps:

1. Clone the repository: `git clone https://github.com/PublicisSapient/retailsustainability-pem-authentication-service.git`
2. Clone the repository: `git clone https://github.com/PublicisSapient/retailsustainability-pem-common-framework.git`
3. Navigate to the common framework directory: `cd common-framework`
4. Build the common-framework: `mvn clean build`
5. Configure the environment variables to your environment (see [Configuration](#configuration))
6. Build the project: `mvn clean build`
7. Navigate to the target directory: `cd target`
8. Run the application: `java -jar authentication-service.jar`

The service will start running on the configured port (default: 9003). You can access the APIs using the base URL `http://localhost:9003/api/v1/authentication-service` and Swagger UI: `http://localhost:9003/api/v1/authentication-service/swagger-ui`



## Configuration

The application can be configured using the following properties:

- **mongodb-root-password**: The Password for MongoDB database
- **MONGODB_SERVICE_HOST**: The host of MongoDB (e.g. localhost)
- **MONGODB_SERVICE_PORT**: The port of the MongoDB database (e.g. 27017)

Ensure that you have MongoDB installed and running before starting the service.



## API Documentation

The Authentication Service provides API documentation using Swagger (OpenAPI). You can access the Swagger UI by navigating to `http://localhost:9003/api/v1/swagger-ui.html` in your web browser. This UI provides detailed information about the available API endpoints, request/response schemas, and allows for testing the endpoints directly.



## Error Messages

The application defines a set of error messages for common scenarios. These messages are configurable and can be found in the `application.yml` file. You can customize the error messages according to your needs.



## Contributing

Contributions to the Product Service are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

1. Fork the repository.
2. Create your feature branch: `git checkout -b feature/my-new-feature`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/my-new-feature`.
5. Submit a pull request.



## License

The Authentication Service is open-source and available under the [MIT License](https://opensource.org/licenses/MIT).

Feel free to modify and adapt the code to suit your needs.
