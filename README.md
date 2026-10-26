# Microservices Demo with Spring Boot 4

An example microservices application that demonstrates the following best practices:
- Service Discovery (Eureka)
- Externalized configuration (Spring Cloud Config Server)
- Event-driven communication (Spring Cloud Stream)
- API Gateway for security and service access control (Spring Cloud Gateway)

## Execution result

Running the complete stack deploys Eureka, the Config Server, and the API Gateway, along with three instances each of the Movies API and Actors API. PostgreSQL, MongoDB, RabbitMQ, and Keycloak are also started as supporting services. All external requests go through the API Gateway, which validates the JWT token and uses Eureka to route each request to an available instance of the requested service.

```text
                         +-----------------+
                         |    Keycloak     |
                         |      (JWT)      |
                         +--------+--------+
                                  |
                                  v
Client requests ---> +-----------------+       +-----------------+
                     |   API Gateway   |------>|     Eureka      |
                     +--------+--------+       | Service Registry|
                              |                +-----------------+
             +----------------+----------------+
             |                                 |
             v                                 v
   +---------------------+           +---------------------+
   | Movies API (x3)     |           | Actors API (x3)     |
   +----------+----------+           +----------+----------+
              |                                 |
              v                                 v
       +-------------+                    +-------------+
       | PostgreSQL  |                    |   MongoDB   |
       +-------------+                    +-------------+

       +-----------------+       +-----------------+
       |  Config Server  |       |    RabbitMQ     |
       +-----------------+       +-----------------+
```

## Instructions

### Local execution

First, start the external services defined in the Docker Compose file in the `scripts/dev` directory. The services are:
- PostgreSQL (used by the Movies API and Config server)
- MongoDB (used by the Actors API)
- RabbitMQ (for messaging)
- Keycloak (used by the API Gateway for authorization)

To run the examples locally, you can disable Eureka and Config Server access in the default profile of the desired project. To run the complete stack, start the services in the following order:
- Eureka
- Config Server

### Full execution

To run all services, go to the `scripts/test` directory and run:

    sh ./start.sh

The script compiles all projects, builds the Docker images, and starts all services.

The stack includes:
- The Eureka server
- The Config Server
- Movies API with three instances
- Actors API with three instances
- API Gateway

Services are accessed through the API Gateway using a JWT token. The gateway uses Eureka to locate an available instance of the requested service and forwards the request to it. See the included Postman collection and the `api-gateway` directory for more details.

To stop the stack, run the following script from the same directory:

    sh ./stop.sh
