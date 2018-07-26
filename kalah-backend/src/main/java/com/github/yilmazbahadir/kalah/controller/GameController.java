package com.github.yilmazbahadir.kalah.controller;

import com.github.yilmazbahadir.kalah.controller.model.KalahResponse;
import com.github.yilmazbahadir.kalah.domain.model.Game;
import com.github.yilmazbahadir.kalah.domain.model.Player;
import com.github.yilmazbahadir.kalah.exception.*;
import com.github.yilmazbahadir.kalah.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author  Bahadir Yilmaz
 * */

@Api(value = "/kalah", description =  "Kalah Game Operations")
@RequestMapping("/kalah")
@RestController
@CrossOrigin(origins = "http://localhost:3060")
public class GameController {

    @Autowired
    private GameService gameService;

    @ApiOperation(value = "Create a game")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> createGame(@RequestBody GameCreateRequest req) throws GameNameAlreadyUsedException {
        Game game = this.gameService.create(req.getName(), req.getNumOfPlayers(), req.getNumOfPits(), req.getNumOfStonesInEachPit());

        return ResponseEntity.ok(new KalahResponse(game));
    }

    @ApiOperation(value = "Start a game previously created")
    @RequestMapping(value = "/{gameId}/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> startGame(@PathVariable long gameId) throws GameIdNotFoundException {
        Game game = this.gameService.start(gameId);

        return ResponseEntity.ok(new KalahResponse(game));
    }

    @ApiOperation(value = "Join a game")
    @RequestMapping(value = "/{gameId}/{playerName}/join", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Player>> joinGame(@PathVariable long gameId, @PathVariable String playerName) throws NoAvailableSeatsException, GameIdNotFoundException {
        Player player = this.gameService.join(gameId, playerName);

        return ResponseEntity.ok(new KalahResponse(player));
    }

    @ApiOperation(value = "Play/Make a move in a game")
    @RequestMapping(value = "/{gameId}/{playerId}/play/{pitInx}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> play(@PathVariable long gameId, @PathVariable int playerId, @PathVariable int pitInx) throws PlayerInvalidTurnException, GameStatusException, WrongMoveException, GameIdNotFoundException, InvalidPitIndexException {
        Game game = this.gameService.play(gameId, playerId, pitInx);

        return ResponseEntity.ok(new KalahResponse(game));
    }

    @ApiOperation(value = "List all games")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> listGames() {
        Collection<Game> games = this.gameService.list();

        return ResponseEntity.ok(new KalahResponse(games));
    }

    @CrossOrigin(origins = "http://localhost:3060")
    @ApiOperation(value = "Get a game")
    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> getGame(@PathVariable long gameId) throws GameIdNotFoundException {
        Game game = this.gameService.getGame(gameId);

        return ResponseEntity.ok(new KalahResponse(game));
    }

    @ApiOperation(value = "Leave a game")
    @RequestMapping(value = "/{gameId}/{playerId}/leave", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KalahResponse<Game>> leaveGame(@PathVariable long gameId, @PathVariable int playerId) throws GameIdNotFoundException {
        Game game = this.gameService.leave(gameId, playerId);

        return ResponseEntity.ok(new KalahResponse(game));
    }


    /**
     * Model class for the createGame rest service
     * */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GameCreateRequest {
        String name;
        int numOfPlayers;
        int numOfPits;
        int numOfStonesInEachPit;
    }
}