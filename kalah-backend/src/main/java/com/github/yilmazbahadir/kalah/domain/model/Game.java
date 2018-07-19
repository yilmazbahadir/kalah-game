package com.github.yilmazbahadir.kalah.domain.model;

import com.github.yilmazbahadir.kalah.exception.GameStatusException;
import com.github.yilmazbahadir.kalah.exception.InvalidPitIndexException;
import com.github.yilmazbahadir.kalah.exception.PlayerInvalidTurnException;
import com.github.yilmazbahadir.kalah.exception.WrongMoveException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Game extends BaseModel {


    public long getId();

    public GameStatus getStatus();

    /**
     * Decides which player should start(strategy: random) and starts the game
     */
    public void start();

    public GameStatus play(int playerId, int pitInx) throws GameStatusException, PlayerInvalidTurnException,
            WrongMoveException, InvalidPitIndexException;

    public Player join(String playerName);

    public List<Player> leave(long playerId);

    public List<Player> getPlayers();

    public GameConfig getConfig();

    public interface GameEventListener {
        public void handle(GameEvent e);
    }

    @Getter
    @Setter
    public class GameEvent implements Serializable {
        private GameEventType type;
        private Game game;
    }

    public enum GameEventType {
        GAME_REFRESH_EVENT
        //TODO ? SCORE_EVENT, GAME_STATUS_EVENT, ALL_PLAYERS_JOINED, PLAYER_LEFT, PLAYER_FREE_TURN, GAME_FINISHED
    }
}
