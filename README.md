# circuit-breaker-springboot-resilience4J
Sample project implementing circuit breaker pattern using spring-boot and resilience4j

##How to build?
Run ./gradlew clean build (on Linux based OS)
Run gradlew clean build(on Windows)

## Order and Payment services
There are two modules in this repository. Client-api and server-api. 
The Client API runs on port 8082. The client hold Order endpoints which talk to Payment endoint in service-api.

The supported order endpoints are:

http://localhost:8082/order-circuit-breaker
http://localhost:8082/order-bulkhead
http://localhost:8082/order-rate-limiter
http://localhost:8082/order-retry

The supported payment endpoint is:
http://localhost:8081/payment

## Health endpoint
http://localhost:8082/actuator/health
 


