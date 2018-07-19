package com.github.yilmazbahadir.kalah.exception;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class GameStatusException extends BaseKalahException {

    public GameStatusException(String status) {
        super(String.format("Game is in an invalid state to play. Status is %s", status)); //TODO string internationalization
    }
}
