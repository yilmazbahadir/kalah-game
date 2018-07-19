package com.github.yilmazbahadir.kalah.exception;

/**
 * Should be thrown when a player requests to play in invalid pit number.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class InvalidPitIndexException extends BaseKalahException {

    public InvalidPitIndexException(int pitInx) {
        super(String.format("Invalid pit index:%s", pitInx)); //TODO string internationalization
    }
}
