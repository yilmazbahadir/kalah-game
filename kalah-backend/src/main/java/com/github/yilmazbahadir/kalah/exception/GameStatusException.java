package com.github.yilmazbahadir.kalah.exception;

/**
 * Should be thrown when the game is an invalid state to play.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class GameStatusException extends BaseKalahException {

    public GameStatusException(String status) {
        super(String.format("Game is in an invalid state to play. Status is %s", status));
    }
}
