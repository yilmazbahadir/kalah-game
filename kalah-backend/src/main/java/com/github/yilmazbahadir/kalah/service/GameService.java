package com.github.yilmazbahadir.kalah.service;

import com.github.yilmazbahadir.kalah.domain.model.*;
import com.github.yilmazbahadir.kalah.domain.model.impl.KalahGame;
import com.github.yilmazbahadir.kalah.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
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
        this.gamesMap = new ConcurrentHashMap<>();
    }

    public Game create(String name, int numOfPlayers, int numOfPits, int numOfStonesInEachPit)
            throws GameNameAlreadyUsedException {
        Game game = new KalahGame(gameIdCounter.getAndIncrement(), name,
                e -> sendGameEvent(e), GameConfig.builder().numOfPlayers(numOfPlayers).numOfPits(numOfPits)
                .numOfStonesInEachPit(numOfStonesInEachPit).build());
        this.gamesMap.put(game.getId(), game);

        return game;
    }

    public Game start(long gameId) throws GameIdNotFoundException, NoAvailableSeatsException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        game.start();

        return game;
    }


    /**
     * Removes the game with the given name parameter
     *
     * @param gameId Game's ID
     * @return The Game deleted
     */
    public Game delete(long gameId) {
        return this.gamesMap.remove(gameId);
    }

    public Player join(long gameId, String playerName) throws GameIdNotFoundException, NoAvailableSeatsException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }
        if(game.getPlayers().size() == game.getConfig().getNumOfPlayers()) {
            throw new NoAvailableSeatsException();
        }

        Player player = game.join(playerName);
        game.getStatus().setStatusType(GameStatusType.WAITING_FOR_NEXT_PLAYER); //TODO change here ??!
        return player;
    }
    public Game leave(long gameId, int playerId) throws GameIdNotFoundException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }

        game.leave(playerId);
        return game;
    }

    /**
     * List all games
     *
     * @return collection of all games
     */
    public List<Game> list() {
        return new ArrayList<Game>(this.gamesMap.values());
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

    public Game play(long gameId, int playerId, int pitInx) throws GameIdNotFoundException, PlayerInvalidTurnException,
            GameStatusException, WrongMoveException, InvalidPitIndexException {
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new GameIdNotFoundException(gameId);
        }

        GameStatus gameStatus = game.play(playerId, pitInx);
        return game;
    }

    private void sendGameEvent(Game.GameEvent e) {
        this.messagingTemplate.convertAndSend("/topic/kalah", e);
    }
}
