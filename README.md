Here's a comprehensive `README.md` file for your Spring Boot Security application:

---

# Security Application

This is a Spring Boot application that demonstrates the use of **JWT (JSON Web Token)** authentication and authorization with **OAuth2 Resource Server** capabilities. It integrates with an OAuth2 identity provider (like **Keycloak**) to secure REST APIs using role-based access control.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Installation](#installation)
- [Configuration](#configuration)
- [Endpoints](#endpoints)
- [JWT Configuration](#jwt-configuration)
- [Customization](#customization)
- [Contributing](#contributing)
- [License](#license)

## Overview

This application uses **Spring Security** to protect REST endpoints. The authentication mechanism relies on a JWT issued by an external OAuth2 provider. The JWT is parsed and verified by Spring Security, and roles are extracted to control access to the API endpoints.

### Key Components
- **JWT Token Validation:** Using OAuth2 Resource Server with JWT tokens.
- **Role-based Access Control (RBAC):** Different API endpoints are restricted to different user roles.
- **Custom JWT Converter:** Converts JWT tokens into Spring Security's authentication tokens with roles extracted from claims.

## Features

- **OAuth2 Resource Server**: JWT token parsing and validation.
- **Role-based Authorization**: Protect endpoints based on user roles such as `client_user` and `client_admin`.
- **Spring Boot**: Easy to run and configure.
- **Custom JWT Converter**: Extracts roles from JWT claims.

## Installation

### Prerequisites
Ensure you have the following installed:
- Java 11+
- Maven 3.6+
- Keycloak or any OAuth2 Authorization Server (or use any OpenID Connect provider)

### Steps to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/security-application.git
   cd security-application
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can run the generated `.jar` file directly:
   
   ```bash
   java -jar target/security-application-1.0.jar
   ```

The application will start on port `8081` by default.

## Configuration

### Key Properties
The application is configured using the `application.properties` file located in `src/main/resources`.

Key configurations include:
- **Server Port**: The application runs on port `8081`.
- **OAuth2 Resource Server**: Set up for JWT validation using issuer URI and JWK set URI from an OAuth2 provider.
  
```properties
spring.application.name=security
server.port=8081

# OAuth2 Resource Server JWT configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/Topgee
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/Topgee/protocol/openid-connect/certs

# JWT Converter settings
jwt.auth.converter.resource-id=topgee_authenthication
jwt.auth.converter.principal-attribute=preferred_username
```

### OAuth2 Server Setup
You need to have an OAuth2 authorization server (like **Keycloak**) running and configured with:
- A realm named `Topgee`
- A client that issues JWT tokens with `resource_access` and roles for `topgee_authenthication`.

For testing locally, you can use **Keycloak** running on `localhost:8080`.

## Endpoints

The following API endpoints are exposed by the application:

| Endpoint               | Method | Role Required     | Description                         |
|------------------------|--------|-------------------|-------------------------------------|
| `/api/v1/demo`          | GET    | `client_user`      | Returns "Hello Daiki" message       |
| `/api/v1/demo/hello`    | GET    | `client_admin`     | Returns "Hello Baki" message        |

### Access Control
- `/api/v1/demo`: Accessible only to users with the role `client_user`.
- `/api/v1/demo/hello`: Accessible only to users with the role `client_admin`.

### Example Responses

- **Success**: If the user has the correct role:
  
  ```json
  {
    "message": "Hello Daiki"
  }
  ```

- **Unauthorized**: If the user lacks the necessary role:

  ```json
  {
    "error": "Forbidden",
    "status": 403
  }
  ```

## JWT Configuration

### Custom `JwtConverter`
The custom `JwtConverter` class, located in `topg.security.security.JwtConverter`, converts a JWT into a `JwtAuthenticationToken`. It extracts roles from the `resource_access` claim in the token.

The converter:
- Extracts the principal name from the `preferred_username` claim.
- Extracts roles from the `resource_access` claim and assigns them with the prefix `ROLE_`.

Example JWT Structure:

```json
{
  "resource_access": {
    "topgee_authentication": {
      "roles": ["client_user", "client_admin"]
    }
  },
  "preferred_username": "daiki"
}
```

The `JwtConverter` will map these roles to `ROLE_client_user` and `ROLE_client_admin`.

### JWT Token Validation
The application uses the `issuer-uri` and `jwk-set-uri` properties to validate incoming JWT tokens. Make sure these URIs point to a valid authorization server (such as Keycloak).

## Customization

### Changing Roles
To modify the roles required for each endpoint, you can update the `@PreAuthorize` annotations in the `SecurityController` class:

```java
@GetMapping
@PreAuthorize("hasRole('client_user')")
public String Hello() {
    return "Hello Daiki";
}
```

### JWT Claims
You can adjust which JWT claims are used for principal extraction by modifying the `jwt.auth.converter.principal-attribute` property in `application.properties`.

## Contributing

Contributions to improve the project are welcome! Feel free to submit pull requests or open issues to discuss bugs or feature suggestions.

### Development Environment Setup

1. Fork the repository.
2. Clone your fork.
3. Create a new branch for your feature/bugfix.
4. Submit a pull request when your changes are ready.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

This `README.md` provides an overview of your Spring Boot Security application, how to configure it, and details about its endpoints and JWT handling. Let me know if you'd like to include anything else!
