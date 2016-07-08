Multi User Test Runner Examples
===============================

Simple example project and example tests.

## Basic Examples

### Spring Service Layer

* [Basic Example with Roles](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceIT.java)
* [Basic Example with Existing Users](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceWithUsersIT.java)
* [Basic Example with RunWithUsers.WITH_PRODUCER_ROLE](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceProducerRoleIT.java)
* [Basic Configuration](src/test/java/fi/vincit/mutrproject/configuration/TestMultiUserConfig.java)
* [Common base class for basic examples](src/test/java/fi/vincit/mutrproject/testconfig/AbstractConfiguredMultiRoleIT.java)

### REST Assured

* [REST Assured Example](src/test/java/fi/vincit/mutrproject/feature/todo/RestAssuredIT.java)
* [REST Assured Configuration Class](src/test/java/fi/vincit/mutrproject/configuration/TestMultiUserRestConfig.java)

## Advanced Assertions

### Java 8

* [Advanced Assertion Example](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceJava8IT.java)

### Role Aliases

* [Role Alias Example](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceRoleAliasIT.java)
* [Role Alias Configuration](src/test/java/fi/vincit/mutrproject/configuration/TestMultiUserAliasConfig.java)

### Multi Role Support

* [Multi Role Example](src/test/java/fi/vincit/mutrproject/feature/todo/TodoServiceMultiRoleIT.java)
* [Multi Role Configuration](src/test/java/fi/vincit/mutrproject/configuration/TestMultiRoleConfig.java)
