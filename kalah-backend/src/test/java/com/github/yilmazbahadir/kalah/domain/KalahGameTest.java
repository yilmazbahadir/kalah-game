package com.github.yilmazbahadir.kalah.domain;

import com.github.yilmazbahadir.kalah.domain.model.*;
import com.github.yilmazbahadir.kalah.domain.model.impl.KalahGame;

import com.github.yilmazbahadir.kalah.domain.model.impl.KalahPlayer;
import com.github.yilmazbahadir.kalah.exception.GameStatusException;
import com.github.yilmazbahadir.kalah.exception.InvalidPitIndexException;
import com.github.yilmazbahadir.kalah.exception.PlayerInvalidTurnException;
import com.github.yilmazbahadir.kalah.exception.WrongMoveException;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Kalah Game Unit Test Class
 *
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public class KalahGameTest {

    private static final Logger LOG = LoggerFactory.getLogger(KalahGameTest.class);

    public static GameConfig config;
    public static Game.GameEventListener gameEventListener;

    @BeforeClass
    public static void constructGameConfig() {
        config = GameConfig.builder().numOfPlayers(2).numOfPits(6).numOfStonesInEachPit(6).build();
        gameEventListener = (event) -> {
            LOG.debug("", event); // just logging
        };

    }

    /**
     * Tests game constructs
     */
    @Test
    public void testConstruct() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        assertEquals(1, game.getId());
        assertEquals("Game 1", game.getName());
        assertEquals(config, game.getConfig());
        assertEquals(GameStatusType.NOT_STARTED, game.getStatus().getStatusType());
    }

    @Test
    public void testStart() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        game.start();
        assertEquals(GameStatusType.STARTED, game.getStatus().getStatusType());
    }

    @Test
    public void testJoin() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        game.start();
        assertEquals(GameStatusType.STARTED, game.getStatus().getStatusType());
        Player player = new KalahPlayer(0, "Player 1");
        game.join("Player 1");
        assertEquals(1, game.getPlayers().size());
        Player gamePlayer = game.getPlayers().get(0);
        assertEquals(player, gamePlayer);
    }

    @Test
    public void testLeave() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        game.start();
        Player player = new KalahPlayer(0, "Player 1");
        game.join("Player 1");
        game.leave(player.getId());
        assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testPlay() throws GameStatusException, WrongMoveException, PlayerInvalidTurnException, InvalidPitIndexException {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        int nextPlayer = game.start();
        Player player1 = game.join("Player 1");
        Player player2 = game.join("Player 2");

        // after joins nextPlayer is currentPlayer now
        Player currentPlayer = nextPlayer == player1.getId() ? player1 : player2;

        GameStatus status = game.play(currentPlayer.getId(), 0);
        assertEquals(currentPlayer.getId(), status.getNextPlayer()); //free turn

        status = game.play(currentPlayer.getId(), 1);
        assertEquals(2, ((KalahGame) game).getBoard().getSide(nextPlayer).getHouse().count());
        nextPlayer = status.getNextPlayer();
        assertNotEquals(currentPlayer.getId(), nextPlayer); // turn changed

        status = game.play(nextPlayer, 0);
        assertNotEquals(nextPlayer, status.getNextPlayer()); // turn changed
        nextPlayer = status.getNextPlayer();

        status = game.play(nextPlayer, 0);
        assertNotEquals(nextPlayer, status.getNextPlayer()); // turn changed
        // at this point because of last stone the empty own pit rule,
        // last player's house must contain 10 stones, let's assert that
        assertEquals(10, ((KalahGame) game).getBoard().getSide(nextPlayer).getHouse().count());
    }

    // test exceptions
}
