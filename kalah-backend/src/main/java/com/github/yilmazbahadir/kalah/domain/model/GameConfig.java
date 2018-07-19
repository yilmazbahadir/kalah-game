package com.github.yilmazbahadir.kalah.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Builder
@Getter
@Setter
public class GameConfig {

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
