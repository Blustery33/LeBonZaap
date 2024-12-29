# Back End
# Spring Boot Project Best Practices

## 1. Project Structure
- **Layered Architecture:** Use a clear layered architecture with separate layers for the controller (API), service (business logic), and repository (data access). This improves readability, maintainability, and testability.
    - `controller`: handles HTTP requests.
    - `service`: contains business logic.
    - `repository`: interacts with the database.
- **Consistent Naming:** Use intuitive naming conventions for classes, methods, and packages (e.g., `UserController`, `UserService`, `UserRepository`).

## 2. Dependency Management
- **Use Maven/Gradle efficiently:** Avoid overloading your project with unnecessary dependencies. Ensure library versions are compatible.
- **Versioning dependencies:** Regularly check for updates in dependencies to avoid vulnerabilities.

## 3. Configuration
- **`application.properties` or `application.yml`:** Store application configuration in one place and use environment-specific config files (e.g., `application-dev.yml`, `application-prod.yml`).
- **Environment variables:** Avoid hardcoding sensitive values like API keys or passwords. Use environment variables or tools like **Spring Cloud Config**.

## 4. Error Handling
- **Global Exception Handling:** Implement a global exception handler using `@ControllerAdvice` to centralize error management.
- **Clear error messages:** Provide understandable error messages for end users and detailed logs for developers.

## 5. Input Validation
- Use validation annotations such as `@Valid`, `@NotNull`, `@Size`, etc., to validate incoming data (DTOs).
- **Entity validation:** Ensure entities are valid before saving them to the database.

## 6. JPA/Hibernate Best Practices
- **Efficient queries:** Prefer JPQL or Criteria API over native queries when possible for portability.
- **Lazy vs Eager Loading:** Use lazy loading (`@ManyToOne(fetch = FetchType.LAZY)`) to avoid memory overload with unnecessary data.

## 7. Testing
- **Unit Testing:** Write unit tests for business logic using libraries like **JUnit** and **Mockito**.
- **Integration Testing:** Ensure services work well together with integration tests. Spring Boot provides annotations like `@SpringBootTest` to facilitate this.

## 8. Security
- **Spring Security:** Implement authentication and authorization with **Spring Security** to protect your API endpoints.
- **JWT Tokens:** For RESTful APIs, use JWT tokens to secure communication and avoid server-side session storage.

## 9. Logging
- **Centralized logging:** Use a logging framework like **Logback** or **SLF4J** to unify logs.
- **Appropriate log levels:** Use the correct log levels (`DEBUG`, `INFO`, `WARN`, `ERROR`) to avoid overloading production log files.

## 10. Performance Optimization
- **Caching:** Implement caching (using `@Cacheable`, **EhCache**, or **Redis**) to store frequently accessed data and reduce repeated database queries.
- **Async Processing:** Use asynchronous execution (`@Async`) and **CompletableFuture** for long or intensive tasks.

## 11. Transaction Management
- Use `@Transactional` to ensure that database operations are atomic (either all succeed or all fail).

## 12. Documentation
- **Swagger/OpenAPI:** Use **Swagger** to automatically generate REST API documentation, improving communication with front-end teams and clients.
- **Code comments:** Comment the code meaningfully without cluttering it with obvious comments.

## SWAGGER

Annotations utiles dans vos contrôleurs
Utilisez les annotations Swagger pour enrichir votre documentation :

- @Operation : Décrire un endpoint.
- @Parameter : Décrire les paramètres de requête.
- @ApiResponse : Documenter les réponses possibles.

```java
@RestController
@Tag(name = "Utilisateur", description = "Endpoints liés aux utilisateurs")
public class UserController {

    @GetMapping("/users/{id}")
    @Operation(summary = "Récupérer un utilisateur", description = "Retourne un utilisateur par son ID")
    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé")
    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    public String getUserById(
            @Parameter(description = "ID de l'utilisateur", required = true)
            @PathVariable Long id) {
        return "Utilisateur ID: " + id;
    }
}