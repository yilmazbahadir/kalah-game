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
* kalah-service-registry: spring registry - netflix eureka
* kalah-service-gateway: spring gateway - netflix zuul 


```
Microservices components are implemented in this branch (feature/kalah-cloud-services). The reason to prepare this branch is
to provide scalibity and ease of management in microservices environment. kalah-backend services can be scaled up to manage the load.
Each instance of kalah-backend will register itself to kalah-service-registry. And will be accessible through kalah-service-gateway.
```

## Usage


1. Using docker (not available for now, work in progress)

    ```
    cd kalah-game
    docker-compose up
    ```
    or 
    
    ```
    docker run -p8080:8080 bahadiryilmaz/kalah-backend
    docker run -p3060:3060 bahadiryilmaz/kalah-frontend
    ```
    Backend Swagger UI => http://localhost:8080/swagger-ui.html
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

    cd ./kalah-service-registry/target
    java -jar kalah-service-registry-1.0-SNAPSHOT.jar
    
    cd ./kalah-backend/target
    java -jar kalah-backend-1.0-SNAPSHOT.jar

    cd ./kalah-service-gateway/target
    java -jar kalah-service-gateway-1.0-SNAPSHOT.jar
    
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

    ![No game found! Create Game](/images/no_game_found.PNG)

2. Create a game

   ```
   Game is configurable, you can set
   * Number of players
   * Number of pits
   * Number of stones in each pit
   ```
   
   ![Create new Game](/images/create_new_game.PNG)

3. List and join the created game

    ```
    Set a player name. But inside the game assigned player ID will be used.
    ```
    ![Join Game](/images/join_game.PNG)

4. Play game - make a move

    ![Play Game](/images/play_game.PNG)
    
5. When game is finished

    ![Game is finished](/images/game_finished.PNG)

