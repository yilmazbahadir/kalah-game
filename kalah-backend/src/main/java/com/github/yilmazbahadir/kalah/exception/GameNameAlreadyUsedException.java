package com.github.yilmazbahadir.kalah.exception;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public class GameNameAlreadyUsedException extends BaseKalahException {

    public GameNameAlreadyUsedException(String name) {
        super(String.format("%s name is already used! Pick another please.", name)); //TODO string internationalization
    }
}
