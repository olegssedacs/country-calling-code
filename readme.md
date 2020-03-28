# Country calling codes detection Test application
`Country calling codes detection Test application` is a homework for the Java Senior developer position for Neotech Development.

## Technology stack
- [Java 11 LTS](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle 5](https://gradle.org/whats-new/gradle-5/)
- [SpringBoot 2x WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/), [Allure Reports](http://allure.qatools.ru)
- [VueJS](https://vuejs.org)

## Design
- Hexagonal Architecture (DDD principles)
- RESTfull
- Full-reactive
- Multi-module

## Modules
- domain (entities, business logic, interfaces)
- infrastructure
    - wiki (imeplentation of data repositories)
    - in-memory-cache (imeplentation of data repositories)
- presentation
    - rest-api (RESTfull application interface)
    - js-frontend (javascript web application)
- application (combination of domain + implementations + APIs)
## API principles 
- Versioned API ( url-based )
- Actuator
    - Fetching data from Wiki executes in non-blocking way. While data is being downloaded from the Wiki, the application continues to initialization. Enpoint `/actuator/health` returns `DOWN` while data from Wiki not ready, that allows an orchestrator await until application is ready to use.
- Google Json Style

### Bla-bla-bla .... thats f'ing boring !!11

# How to open project 
`!!! Enable annotation proccessor !!!`
# How to run
#### All tests + reports
Runs unit tests, integration tests, opens resports in browser
```
./gradlew clean test itest allureServeReport --continue
```
- clean = delete previous build/tests/reports result
- test = run unit tests
- itest = run integration tests (optional)
- allureServeReport = aggregate and open report in browser
##### How to run application
`!!! IMPORTANT !!!`
`Run with`
```
./gradlew bootRun
```
`Otherwise frontend will not be deployed and served`

Another options : 
1. build Jar with
```
./gradlew build
```
2. Run executiable jar 
```
java -jar /path/to/application.jar
```