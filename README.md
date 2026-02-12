# Spring Calculator Client

Spring Boot application that acts as a client for the EJB 4 Calculator service. It provides REST endpoints to perform calculator operations via both EJB lookup and proxy-based approaches.

## Prerequisites

- Java 17+
- Maven 3.6+
- WildFly server with the calculator-ejb deployed

## Build

```bash
mvn clean package
```

## Run

```bash
mvn spring-boot:run
```

The application runs on port 9090 by default.

## Configuration

Configure EJB connection in `src/main/resources/application.properties`:

- `jndi.providerUrl` - WildFly remote URL (e.g., `http-remoting://localhost:8080`)
- `jndi.calculatorLookup` - JNDI name for the Calculator EJB
