# Architecture Diagram

```mermaid
classDiagram
    class UserService {
        +UserService.java
    }
    class User {
        +User.java
    }
    class StringHelper {
        +StringHelper.java
    }


    UserService --> StringHelper
```