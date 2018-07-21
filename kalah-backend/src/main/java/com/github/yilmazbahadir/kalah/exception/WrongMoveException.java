package com.github.yilmazbahadir.kalah.exception;

/**
 * Should be thrown when the player tries to play an empty pit.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class WrongMoveException extends BaseKalahException {

    public WrongMoveException(String msg) {
        super(String.format("Wrong move! %s", msg));
    }
}
