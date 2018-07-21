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
 * Model of the whole game
 *
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Game extends BaseModel {


    public long getId();

    public String getName();
    
    public GameStatus getStatus();

    public GameConfig getConfig();

    /**
     * Decides which player should start(strategy: random) and starts the game
     */
    public void start();

    public GameStatus play(int playerId, int pitInx) throws GameStatusException, PlayerInvalidTurnException,
            WrongMoveException, InvalidPitIndexException;

    public Player join(String playerName);

    public List<Player> leave(long playerId);

    public List<Player> getPlayers();

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
        // SCORE_EVENT, GAME_STATUS_EVENT, ALL_PLAYERS_JOINED, PLAYER_LEFT, PLAYER_FREE_TURN, GAME_FINISHED

        /* almost all types of events require game to be refreshed(gamestatus, sides, pits), sending
            all events separately would be inefficient; so decided to use GAME_REFRESH_EVENT only. */
    }
}
