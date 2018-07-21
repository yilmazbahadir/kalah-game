package com.github.yilmazbahadir.kalah.exception;

/**
 * Should be thrown when it is not the requesting player's turn
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class PlayerInvalidTurnException extends BaseKalahException {

    public PlayerInvalidTurnException(String playerName) {
        super(String.format("It is not your turn to play! Next player is %s ", playerName));
    }
}
