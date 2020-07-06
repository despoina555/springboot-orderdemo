# Getting Started

This is demo project to evaluate Java Developer proficiency in Spring Boot.  

The context of demo is about an ordering application. User can submit an order and view any order registered on system via OrderAPI.

Maven 3 and Java 8 are required in order to build the project. 

The demo app uses H2 in memory database. As a result, you don't need to install any database server at all. 
Database migration is performed automatically by JPA (see application.yml) 

Main tasks:

1. Fill in implementation of Order API methods in order to fetch all orders, fetch a specific order and submit a new order
2. Write unit tests according to TODO notes within unit test files. When writing unit tests keep in mind 
that the ultimate goal is to check that API responses are correct.

To submit your code, you need to clone project code and create a new GitHub repository under your name. Then you can email to us the link of the project in GitHub.

NOTE
----
 
If you do have time and want to add more features to the project then you can add the notion of customer. An order is placed by a customer. 

Order API, in this case, will use an additional parameter (customerId) which can be used to fetch orders of a certain customer.   

Good luck!


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

