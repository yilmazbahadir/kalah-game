package com.github.yilmazbahadir.kalah.service;

import com.github.yilmazbahadir.kalah.domain.model.*;
import com.github.yilmazbahadir.kalah.domain.model.impl.KalahGame;
import com.github.yilmazbahadir.kalah.exception.*;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <h1>Game Service</h1>
 * Contains the kalah game application logic
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Service
@Scope(value = "singleton")
@Slf4j
public class GameService {

    private Map<Long, Game> gamesMap;
    private AtomicLong gameIdCounter = new AtomicLong(0);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(new Config());
        this.gamesMap = hz.getMap("games");
    }

    /**
     * Creates a game with the parameters.
     * It is just constructed it should be start-ed in order to play.
     *
     * @param name name of the game
     * @param numOfPlayers number of the players
     * @param numOfPits number of the pits in each side(excluding house - every side has 1 house(kalah)
     * @param numOfStonesInEachPit number of stones in each pit
     * @return The Game configured with status NOT_STARTED
     * @throws GameNameAlreadyUsedException
     */
    public Game create(String name, int numOfPlayers, int numOfPits, int numOfStonesInEachPit) {
        Game game = new KalahGame(gameIdCounter.getAndIncrement(), name,
                this::sendGameEvent, GameConfig.builder().numOfPlayers(numOfPlayers).numOfPits(numOfPits)
                .numOfStonesInEachPit(numOfStonesInEachPit).build());
        this.gamesMap.put(game.getId(), game);

        return game;
    }

    /**
     * Starts the game.
     *
     * @param gameId
     * @return
     * @throws GameIdNotFoundException
     * @throws NoAvailableSeatsException
     */
    public Game start(long gameId) throws GameIdNotFoundException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        game.start();
        this.gamesMap.put(gameId, game);
        return game;
    }


    /**
     * Removes the game with the given name parameter
     *
     * @param gameId ID of the game
     * @return Game deleted
     */
    public Game delete(long gameId) {
        return this.gamesMap.remove(gameId);
    }

    /**
     * Joins a player to a game with a playerName.
     *
     * @param gameId ID of the game
     * @param playerName Name of the player
     * @return The model of the Player
     * @throws GameIdNotFoundException
     * @throws NoAvailableSeatsException
     */
    public Player join(long gameId, String playerName) throws GameIdNotFoundException, NoAvailableSeatsException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        if(game.getPlayers().size() == game.getConfig().getNumOfPlayers()) {
            throw new NoAvailableSeatsException();
        }
        game.setEventListener(this::sendGameEvent);
        Player player = game.join(playerName);
        this.gamesMap.put(gameId, game);
        return player;
    }

    /**
     * Makes the player leave the game
     *
     * @param gameId
     * @param playerId
     * @return
     * @throws GameIdNotFoundException
     */
    public Game leave(long gameId, int playerId) throws GameIdNotFoundException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        game.setEventListener(this::sendGameEvent);
        game.leave(playerId);
        return game;
    }

    /**
     * List all games
     *
     * @return collection of all games
     */
    public List<Game> list() {
        return new ArrayList<>(this.gamesMap.values());
    }

    /**
     * Get a game by gameId
     *
     * @return the game object
     */
    public Game getGame(long gameId) throws GameIdNotFoundException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        return game;
    }

    /**
     * Makes move for a player
     *
     * @param gameId ID of the Game
     * @param playerId ID of the player
     * @param pitInx Index of the Pit to be picked( range 0(inclusive) - numOfPits(exclusive) )
     * @return
     * @throws GameIdNotFoundException
     * @throws PlayerInvalidTurnException
     * @throws GameStatusException
     * @throws WrongMoveException
     * @throws InvalidPitIndexException
     */
    public Game play(long gameId, int playerId, int pitInx) throws GameIdNotFoundException, PlayerInvalidTurnException,
            GameStatusException, WrongMoveException, InvalidPitIndexException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        game.setEventListener(this::sendGameEvent); // since sendGameEvent - SimpMessagingTemplate is not Serializable
        // we need to set listener after we get the game
        game.play(playerId, pitInx);
        this.gamesMap.put(gameId, game);
        return game;
    }

    /**
     * Sends the Game Events to Subscribers
     *
     * @param e Event
     */
    private void sendGameEvent(Game.GameEvent e) {
        this.messagingTemplate.convertAndSend(String.format("/topic/kalah/%s", e.getGame().getId()), e);
    }
}
