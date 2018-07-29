package com.github.yilmazbahadir.kalah.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <h1>Configuration of the game</h1>
 * This class is responsible for the configration of the game to be played.
 * Number of players(sides), number of pits in each side, number of stones in each pit can be configured
 * (even the direction of the game)
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class GameConfig implements Serializable {

    private static final long serialVersionUID = -2671702752038961736L;

    private static final int DEFAULT_NUM_OF_PITS = 6;
    private static final int DEFAULT_NUM_OF_STONES_IN_EACH_PIT = 6;
    private static final int DEFAULT_NUM_OF_PLAYERS = 2;

    private int numOfPits = DEFAULT_NUM_OF_PITS;
    private int numOfStonesInEachPit  = DEFAULT_NUM_OF_STONES_IN_EACH_PIT;
    private int numOfPlayers = DEFAULT_NUM_OF_PLAYERS;
    @Builder.Default private Direction direction = Direction.COUNTER_CLOCK_WISE;


    public enum Direction {
        CLOCK_WISE,
        COUNTER_CLOCK_WISE;
    }

}
