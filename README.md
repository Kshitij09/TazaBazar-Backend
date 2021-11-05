# TazaBazar-Backend

TazaBazar backend is an MVP API Server for Grocery Store. It's built with Spring Boot and uses [Spring Data JDBC](https://spring.io/projects/spring-data-jdbc) for modelling database entities in a Domain Driven manner. The project currently focuses on serving bootstrap data taken from [www.siddhagirinaturals.com](https://www.siddhagirinaturals.com/) and supports rudimentary REST APIs to build client applications for a Grocery Shop. Supported features are as follows:

- Get product list
    - Filter by Product Category
    - Search by Product Name (Full Text Search)
- Create New User Account (email as unique identifier)
- JWT Authentication (with refresh tokens)
- Sync User Cart
- Place Order
- Check User Orders

#### _Start browsing the Swagger UI [here](http://tazabazaar.ddns.net:8080/swagger-ui)_
> [Swagger UI Instructions](https://github.com/Kshitij09/TazaBazar-Backend/wiki/Swagger-UI-Instructions)

#### _Checkout Current Database Schema [here](https://github.com/Kshitij09/TazaBazar-Backend/wiki/TazaBazar-Database-Schema-(v1))_

### Highlights

- **Asymmetric JWT**
    
    The backend, while currently monolith, is written with Microservice mindset and Asymmetric JWT is one of the key aspects to enable the same. We use an asymmetric key pair to generate and validate the JWT token. This allows [`JwtCreateService`](springboot-app/src/main/java/com/kshitijpatil/tazabazar/security/JwtCreateService.java) to be pulled off into a Microservice and use private-key to generate the token, while other Microservices in the project can share a common implementation of [`JwtValidateService`](springboot-app/src/main/java/com/kshitijpatil/tazabazar/security/JwtValidateService.java) and public-key or use it as an API Gateway for validation purposes.
    
- **Authorized / Authenticated APIs**
    
    Thanks to JWT and Spring Boot's excellent support for the same, TazaBazar-backend supports role-based security for REST endpoints. At the moment, three roles (admin, user, vendor) are created by the Initializer and we restrict reading all user details or deleting a user account to admin only. Moreover, certain APIs, such as reading order detail is first approved by matching principal email from the JWT and the Order owner's email, and those details are provided only when the identities match. Some APIs are authenticated using the bearer authentication method.
    
- **Well tested**
    
    Given the early stage of development, the project tries to maximize the coverage by adding more integration tests than unit tests. v1 APIs started off with an in-memory Database providing a solid foundation for the next iteration, v2 continued with integration with Postgres Server for an extended set of APIs. Lastly, complex scenarios such as 1) creating an account, 2) logging in with those credentials, and 3) performing some action with the access token, are covered in the Controller Tests.
    
- **End to end deployable with Docker**
    
    The project uses [paulschwarz/spring-dotenv](https://github.com/paulschwarz/spring-dotenv) to provide a seed environment in the development while conforming to [The Twelve-Factor App](https://12factor.net/), uses Environment Variables to store the production configurations.
    
    The project doesn't have the most streamlined setup yet, but following certain steps, you can get the entire stack up and running with a `docker compose up`. Check out [Install.md](Install.md) to know more about deployment.
    
    It's in fact has been deployed using the same instructions on a single EC2 spot instance, and DNS resolved to [http://tazabazaar.ddns.net](http://tazabazaar.ddns.net:8080) (No https as of now 😅)
    
- OpenApi3 API Documentation
    
    We use [springdoc-openapi](https://springdoc.org/) for auto-generating the API documentation. Head over to [swagger-ui](http://tazabazaar.ddns.net:8080/swagger-ui) to start interacting with the APIs right from your browser or you can get an openapi3 JSON schema by visiting [api-docs](http://tazabazaar.ddns.net:8080/v3/api-docs). Save the response as a '.json' file to load it in an API Platform such as [Postman](https://www.postman.com/)

## **Disclaimer**
_All the content served by this project is taken from [siddhagirinaturals.com](https://www.siddhagirinaturals.com/) and is only used for educational purpose. We do not seek to act on behalf of them and have no official business relations with siddhagiri naturals._

License
-------

    Copyright 2021 Kshitij Patil

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
