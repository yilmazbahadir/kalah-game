package com.github.yilmazbahadir.kalah.domain;

import com.github.yilmazbahadir.kalah.domain.model.Game;
import com.github.yilmazbahadir.kalah.domain.model.GameConfig;
import com.github.yilmazbahadir.kalah.domain.model.GameStatus;
import com.github.yilmazbahadir.kalah.domain.model.GameStatusType;
import com.github.yilmazbahadir.kalah.domain.model.impl.KalahGame;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public class KalahGameTest {

    public static GameConfig config;
    public static Game.GameEventListener gameEventListener;

    @BeforeClass
    public static void constructGameConfig() {
        config = GameConfig.builder().numOfPlayers(2).numOfPits(6).numOfStonesInEachPit(6).build();
        gameEventListener = (event) -> {
            System.out.println(event); // TODO change here!
        };

    }

    @Test
    public void testConstruct() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        assertEquals(game.getId(), 1);
        assertEquals(game.getName(), "Game 1");
        assertEquals(game.getConfig(), config);
        assertEquals(game.getStatus().getStatusType(), GameStatusType.NOT_STARTED);
    }

    @Test
    public void testStart() {
        Game game = new KalahGame(1, "Game 1", gameEventListener, config);
        game.start();
        assertEquals(game.getStatus().getStatusType(), GameStatusType.STARTED);
    }

    @Test
    public void testJoin() {

    }

    @Test void testPlay() {

    }
}
