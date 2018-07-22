# Kalah Game


## Introduction


Kalah a.k.a Mancala is a generic name for a family of 2-player turn-based strategy board games played with small stones,
beans, or seeds and rows of holes or pits in the earth, a board or other playing surface.

For further information please visit [Wikipedia Kalah Game Page](https://en.wikipedia.org/wiki/Kalah)


## Project


This project is a *n* player, *m* pits, *k* stones online multiplayer implementation of the original game.

Kalah-Game multi module project consists of the following modules:

* kalah-backend : spring boot, maven, junit, lombok, docker, swagger, aspect, websocket (spring cloud - not implemented yet)
* kalah-frontend : spring boot, maven
    * kalah-ui : react, node, npm, webpack, websocket(sockjs, stompjs)
* kalah-service-gateway: spring cloud(eureka, zuul, config) - not implemented yet


## Usage


1. Using docker

    ```
    docker run -p8080:8080 bahadiryilmaz/kalah-backend
    ```

    Backend Swagger UI => http://localhost:8080/swagger-ui.html

    ```
    docker run -p3060:3060 bahadiryilmaz/kalah-frontend
    ```

    Frontend => http://localhost:3060/kalah

2. Build from source

    Prerequisites:
    * Java 8
    * Maven 3
    * Node v10 & npm v6



    ```
    git clone https://github.com/yilmazbahadir/kalah-game.git

    cd kalah-game
    mvn clean install

    cd ./kalah-backend/target
    java -jar kalah-backend-1.0-SNAPSHOT.jar

    cd ./kalah-frontend/target
    java -jar kalah-frontend-1.0-SNAPSHOT.jar
    ```

    Backend Swagger UI => http://localhost:8080/swagger-ui.html
    
    Frontend => http://localhost:3060/kalah


## How to play


1. List games or create one

    ```
    http://localhost:3060/kalah
    ```

    
