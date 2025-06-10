# kitchensink

Web-enabled database application adapted from [a JBoss quickstart project](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink) to use Java 21 and other, more modern technologies, such as Spring Boot & MongoDB

## What is it?

This project is the result of a simplified migration exercise of a Java-based web application.

### Migration Overview
#### Technologies Introduced

The following technologies were introduced per explicit requirements or out of necessity for following **only** specific requirements

* Java 21
* Spring Boot
  * Replaces direct Jakarta CDI API and Jakarta REST service
* MongoDB (integrated via Spring Data)
  * Replaces in-memory, relational H2 database integrated via JNDI
* JoinFaces
  * Enables simplified Spring Boot integration with JSF 
* TestContainers
  * Enables ephemeral MongoDB Docker container when running unit tests

#### Technologies Retained

The following were retained in order to make as few assumptions as possible beyond the requirements provided:

* Maven
* Wildfly
* Jakarta (several libraries)
* JSF
* JUnit

## Building, Deploying, & Testing the Application

### Prerequisites
* [OpenJDK 21](https://openjdk.org/install/)
* [Maven 3](https://maven.apache.org/download.cgi)
* [JBoss EAP/Wildfly Server](https://www.redhat.com/en/technologies/jboss-middleware/application-platform)
  * Local or remote
  * Tested with JBoss EAP 8.0
* [MongoDB](https://www.mongodb.com/docs/manual/administration/install-community/)
  * Local or remote
* [Docker](https://docs.docker.com/engine/install/) (needed when running tests)

### Build
* Prior to deploying the application, you must have a MongoDB instance running (local or remote) and its connection details configured within `application.properties`
  * **NOTE:** The MongoDB instance details pre-populated within `application.properties` are only applicable to a locally running instance. Remote instance details should never be committed to version control
* To build the `.war` file necessary for running the application, run `mvn package`
  * When rerunning the build step, you can precede this command with `mvn clean` to delete the previous build's artifacts (i.e., the `target/` directory)

### Deploy
* The application can be deployed to a Wildfly server (local or remote) by running `mvn deploy`
  * Location of Wildfly server can be specified in `EAP_HOME` for local deployment and within the `configuration` tag of the Wildfly Maven plugin for a remote deployment
  * **NOTE:** Dependencies have been largely extracted from reliance on JBoss EAP. Deploying the application to another application server (e.g., simple Tomcat server) should largely be a matter of:
    1. Specifying the `spring-boot-start-tomcat` dependency in `pom.xml` as either provided by the new application server or bundled with the application itself
    2. Removing the `org.jboss.weld.servlet` exclusion from the JoinFaces dependency in `pom.xml`
    3. Specifying the application context path in a manner dictated by the target application server (e.g., in `application.properties`, in a `.xml` file in `webapp/WEB-INF`, etc.)

### Test
* The tests included with this application can be run with either `mvn verify` or `mvn integration-test`
  * `MemberRegistrationIT` requires that the Docker daemon is running so that the test can execute absent an independently running MongoDB instance
  * The `RemoteMemberRegistrationIT` test expects the application to either be running locally or on a remote host (configured in the `SERVER_HOST` environment variable)

## Considerations for a Larger Scale Migration

The following categorizes the improvements that could be made to this application in the context of a larger migration performed by a larger, dedicated team over an extended period of time.

### Documentation

* All classes and methods thoroughly documented with Javadocs

### Testing

* Exhaustive integration tests written and validated prior to migration and leveraged post-migration
* Unit tests to increase code coverage (within reason)

### Code Standards

* Linting tools configured
* Code-style enforcement tools configured
* Pre-commit hooks to enforce those standards before reaching remote version control

### Data Migration

* A realistic application would presumably have a production relational database rather an in-memory database – MongoDB's relational DB migration tool could be used to transition away from that database

### Automation

* Continuous integration pipelines to catch and address code issues (e.g., failed tests, code standard violations, compilation failures, etc.)
* Continuous delivery pipeline pushing to an artifact repository – organized by an agreed upon semantic versioning system – for traceability and deployment reversions
* Continuous deployment pipeline – at the very least to lower environment(s) – so that new versions can be interacted with and tested quickly  

### UI

* While still using JSF, improving error and exception handling
* Replace JSF with something that integrates more neatly with Spring Boot – JoinFaces is fragile and frequently breaks with new versions of both Spring Boot and JSF

### Miscellaneous Optimizations

* `docker-compose` setup to allow developers to run and iterate on application quickly without having to worry as much about external infrastructure dependencies (e.g., MongoDB)
* Server-agnostic application/dependencies (i.e., not reliant on deployment to JBoss EAP/Wildfly) such that are little-to-no changes required to deploy to disparate application servers
* Consolidation of some source code (e.g., combining `MemberListProducer` with `MemberController`)
