package com.github.yilmazbahadir.kalah.exception;

/**
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class GameIdNotFoundException extends BaseKalahException {

    public GameIdNotFoundException(long id) {
        super(String.format("Game ID:%s  not found! Please try another one.", id)); //TODO string internationalization
    }
}
