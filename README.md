
# Loan Simulator

Backend application built in Java and Spring Boot to simulate loans with fixed installments, considering the client's age range for interest calculation.

To run this project, you need: 
- Java 21+
- Maven 3.9.10+
- Docker or Colima
- Postman (optional, for testing APIs)

## Executing the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/Creditas/backend-challenge-vlc.git
   ```
2. Navigate to the project directory:
   ```bash
   cd loan-simulator
   ```
3. Build the project using Maven:
   ```bash
    mvn clean install
    ```
4. Run the project using Docker:
   ```bash
   docker compose up --build
   ```
5. To run the application locally, execute:
   ```bash
   mvn spring-boot:run
   ```
6. Access the Swagger application at:
   ```bash
    http://localhost:8080/swagger-ui/index.html
    ```
## Tests

To run the unit tests, execute:
   ```bash
   mvn test
   ```

To run Integration tests, execute:
   ```bash
   mvn test-compile failsafe:integration-test
   ```

To run performance tests, execute:
   ```bash
   mvn test -Pperformance-tests
   ```

## Endpoint

| HTTP Method | Route                |
|-------------|----------------------|
| POST        | `/v1/loans/simulate` |

### Request Example

```bash
curl --location 'http://localhost:8080/v1/loans/simulate' \
--header 'Content-Type: application/json' \
--data '{
    "client_id": "684c9598-0cdb-4a02-ae75-78abc1e8d00d",
    "loan_amount": 1000.00,
    "client_birth_date": "1990-09-29",
    "loan_term_in_months": 2
}'
```

### Response Example

```json
{
   "id": "684c9598-0cdb-4a02-ae75-78abc1e8d00d",
   "client_id": "684c9598-0cdb-4a02-ae75-78abc1e8d00d",
   "loan_amount": 1000.00,
   "interest_rate": 3.00,
   "loan_term_in_months": 2,
   "total_amount_payable": 1003.75,
   "monthly_installment": 501.88,
   "total_interest_paid": 3.75
}
```

## Architecture

- MVC (Model-View-Controller) com DDD (Domain-Driven Design)
- Strategy Pattern
- Dependency Injection
- Clean Architecture
- Error Handling