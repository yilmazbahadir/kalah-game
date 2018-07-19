package com.github.yilmazbahadir.kalah.controller;

import com.github.yilmazbahadir.kalah.KalahGameApp;
import com.jayway.restassured.http.ContentType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahGameApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private int nextPlayer;

    @Test
    public void test1_ListGamesAsEmpty() throws Exception {
        when().get("kalah/")
                .then()
                .statusCode(200)
                .body(is("{\"success\":true,\"errCode\":0,\"message\":\"\",\"data\":[]}"));
    }

    @Test
    public void test2_CreateGame() throws Exception {
        GameController.GameCreateRequest req = new GameController.GameCreateRequest();
        req.setName("Game 1");
        req.setNumOfPits(6);
        req.setNumOfPlayers(2);
        req.setNumOfStonesInEachPit(6);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(req)
        .when()
            .post("kalah/")
        .then()
            .statusCode(200)
            .body("data.name", equalTo("Game 1"));
    }

    @Test
    public void test3_PlayersJoinGame() {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("kalah/0/bahadir/join/")
        .then()
            .statusCode(200)
            .body("data.name", equalTo("bahadir"));

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("kalah/0/alp/join/")
        .then()
            .statusCode(200)
            .body("data.name", equalTo("alp"));
    }

    @Test
    public void test4_StartGame() {
        this.nextPlayer = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("kalah/0/start")
                .then()
                .statusCode(200)
                .extract().path("data.status.nextPlayer");
    }

    @Test
    public void test5_MakeAMoveFreeTurn(){
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("kalah/0/{playerId}/play/0", nextPlayer)
        .then()
            .statusCode(200)
            .body("data.status.nextPlayer", equalTo(nextPlayer)); // same player, free turn
        //TODO check stones in pits
    }

    @Test
    public void test6_MakeInvalidMove_InvalidPitInx(){
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("kalah/0/{playerId}/play/7", nextPlayer)
        .then()
            .statusCode(400)
            .body("message", startsWith("Invalid pit index"));
    }

    @Test
    public void test7_MakeInvalidMove_PlayerInvalidTurn(){
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("kalah/0/{playerId}/play/5", (nextPlayer + 1)%2) // since we build the game for 2 players
        .then()
            .statusCode(400)
            .body("message", startsWith("It is not your turn to play!"));
    }

    @Test
    public void test8_MakeAMove(){
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("kalah/0/{playerId}/play/5", nextPlayer)
                .then()
                .statusCode(200)
                .body("data.status.nextPlayer", equalTo((nextPlayer + 1)%2)); // next player
    }
}