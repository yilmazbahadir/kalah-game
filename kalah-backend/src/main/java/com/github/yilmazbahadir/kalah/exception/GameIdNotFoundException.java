package com.github.yilmazbahadir.kalah.exception;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class GameIdNotFoundException extends BaseKalahException {

    public GameIdNotFoundException(long id) {
        super(String.format("Game ID:%s  not found! Please try another one.", id)); //TODO string internationalization
    }
}
