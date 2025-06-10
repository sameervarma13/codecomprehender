# Project Architecture

## `ApplicationConfiguration`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/config/ApplicationConfiguration.java`
- ⚙️ **Implements**: `ApplicationListener`
- 📦 **Key Imports**: `org.jboss.as.quickstarts.kitchensink.model.DatabaseSequence`, `org.jboss.as.quickstarts.kitchensink.model.Member`, `org.springframework.beans.factory.annotation.Autowired`, `org.springframework.boot.context.event.ApplicationReadyEvent`, `org.springframework.context.ApplicationListener` (and 5 more)

## `DatabaseSequence`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/model/DatabaseSequence.java`
- 📦 **Key Imports**: `org.springframework.data.annotation.Id`, `org.springframework.data.mongodb.core.mapping.Document`, `java.math.BigInteger`

## `Main`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/Main.java`
- 🔗 **Extends**: `SpringBootServletInitializer`
- 📦 **Key Imports**: `org.jboss.as.quickstarts.kitchensink.config.ApplicationConfiguration`, `org.jboss.as.quickstarts.kitchensink.data.MemberRepository`, `org.slf4j.LoggerFactory`, `org.springframework.boot.SpringApplication`, `org.springframework.boot.autoconfigure.SpringBootApplication` (and 4 more)

## `Member`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/model/Member.java`
- ⚙️ **Implements**: `Serializable`
- 📦 **Key Imports**: `jakarta.validation.constraints.Digits`, `jakarta.validation.constraints.Email`, `jakarta.validation.constraints.NotEmpty`, `jakarta.validation.constraints.NotNull`, `jakarta.validation.constraints.Pattern` (and 7 more)

## `MemberController`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/controller/MemberController.java`
- 📦 **Key Imports**: `jakarta.annotation.PostConstruct`, `jakarta.faces.application.FacesMessage`, `jakarta.faces.context.FacesContext`, `jakarta.faces.view.ViewScoped`, `org.jboss.as.quickstarts.kitchensink.data.MemberListProducer` (and 4 more)

## `MemberListProducer`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/data/MemberListProducer.java`
- 📦 **Key Imports**: `jakarta.annotation.PostConstruct`, `jakarta.enterprise.event.Observes`, `jakarta.enterprise.event.Reception`, `org.jboss.as.quickstarts.kitchensink.model.Member`, `org.springframework.beans.factory.annotation.Autowired` (and 1 more)

## `MemberRegistration`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/service/MemberRegistration.java`
- 📦 **Key Imports**: `com.mongodb.MongoWriteException`, `com.mongodb.client.MongoClient`, `org.jboss.as.quickstarts.kitchensink.data.MemberRepository`, `org.jboss.as.quickstarts.kitchensink.model.DatabaseSequence`, `org.jboss.as.quickstarts.kitchensink.model.Member` (and 8 more)

## `MemberRegistrationIT`
- 📄 **File**: `src/test/java/org/jboss/as/quickstarts/kitchensink/test/MemberRegistrationIT.java`
- 📦 **Key Imports**: `org.jboss.as.quickstarts.kitchensink.Main`, `org.jboss.as.quickstarts.kitchensink.model.Member`, `org.jboss.as.quickstarts.kitchensink.service.MemberRegistration`, `org.jboss.as.quickstarts.kitchensink.test.config.MongoDBConfig`, `org.junit.jupiter.api.Test` (and 7 more)

## `MemberResourceRESTService`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/rest/MemberResourceRESTService.java`
- 📦 **Key Imports**: `jakarta.validation.ValidationException`, `org.jboss.as.quickstarts.kitchensink.data.MemberRepository`, `org.jboss.as.quickstarts.kitchensink.model.Member`, `org.jboss.as.quickstarts.kitchensink.service.MemberRegistration`, `org.springframework.beans.factory.annotation.Autowired` (and 11 more)

## `MongoDBConfig`
- 📄 **File**: `src/test/java/org/jboss/as/quickstarts/kitchensink/test/config/MongoDBConfig.java`
- 📦 **Key Imports**: `org.springframework.context.annotation.Configuration`, `org.testcontainers.containers.MongoDBContainer`, `org.testcontainers.junit.jupiter.Container`

## `RemoteMemberRegistrationIT`
- 📄 **File**: `src/test/java/org/jboss/as/quickstarts/kitchensink/test/RemoteMemberRegistrationIT.java`
- 📦 **Key Imports**: `jakarta.json.Json`, `jakarta.json.JsonObject`, `org.jboss.as.quickstarts.kitchensink.model.Member`, `org.json.JSONObject`, `org.junit.Assert` (and 8 more)

## `Resources`
- 📄 **File**: `src/main/java/org/jboss/as/quickstarts/kitchensink/utils/Resources.java`
- 📦 **Key Imports**: `org.springframework.beans.factory.InjectionPoint`, `org.springframework.context.annotation.Bean`, `org.springframework.context.annotation.Configuration`, `org.springframework.context.annotation.Scope`

---
**Total Classes**: 12
**Classes with Inheritance**: 1
**Classes with Interfaces**: 2