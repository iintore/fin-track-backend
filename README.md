# FinTrack app

**Author:** Manzi Tony Lewis
**StudentID:** 26904

FinTrack helps people follow their personal spending. Account holders record expenses made with merchants, each merchant trades from a market stall and holds expense categories, and receipt lines are recorded once an expense is settled.

Built with Spring Boot, Spring Data JPA and PostgreSQL.

## Requirements

- Java 27
- PostgreSQL running on `localhost:5432`

## Steps to run the API

1. Clone the repository:
   ```bash
   git clone https://github.com/iintore/fin-track-backend.git
   cd fin-track-backend
   ```
2. Create the database:
   ```bash
   createdb fintrack
   ```
3. Set your PostgreSQL username and password in `src/main/resources/application.properties` if they differ from the defaults.
4. Start the API:
   ```bash
   ./mvnw spring-boot:run
   ```
   Hibernate creates the tables on first start.
5. The API runs on `http://localhost:8080`. Import `fin-track-midterm.postman_collection.json` into Postman to try every endpoint.
