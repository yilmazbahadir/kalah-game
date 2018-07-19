package com.github.yilmazbahadir.kalah.exception;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
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
