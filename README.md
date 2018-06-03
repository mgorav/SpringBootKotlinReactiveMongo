# Spring Boot Odyssey With Reavtive Kotlin + MongoDB

This service exposes a Blog APIs build using Spring Boot + Kotlin + Mongo DB using reactive programing.

## Requirements

1. Java - >= 1.8

2. Reactive Mongo 

3. Spring Boot 2.0.2.RELEASE

4. Kotlin - 1.2.41

5. spring-boot-starter-data-mongodb-reactive

6. spring-boot-starter-webflux

7. spring-cloud-starter-gateway (Finchley.M3)

8. spring-cloud-gateway-kotlin-extensions

## Instruction to setup

###1. Clone the application

```bash
git@github.com:mgorav/SpringBootKotlinReactiveMongo.git
```

###2. Run MongoDB docker image

```bash
 docker run -p 27017:27017 -d mongo
```

###3. Running the App


Use following command to run the application -

```bash
mvn spring-boot:run
```


##### Glossary of all the APIs exposed

    GET /api/blogs
    GET /api/blogs/{id}
    

