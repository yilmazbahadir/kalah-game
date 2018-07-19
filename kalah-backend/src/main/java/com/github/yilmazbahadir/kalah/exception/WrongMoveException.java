package com.github.yilmazbahadir.kalah.exception;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class WrongMoveException extends BaseKalahException {

    public WrongMoveException(String msg) {
        super(String.format("Wrong move! %s", msg)); //TODO string internationalization
    }
}
