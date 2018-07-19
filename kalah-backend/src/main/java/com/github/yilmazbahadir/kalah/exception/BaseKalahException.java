package com.github.yilmazbahadir.kalah.exception;

/**
 * Base exception class
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public class BaseKalahException extends Exception {

    public BaseKalahException(){
        super();
    }

    public BaseKalahException(String message) {
        super(message);
    }

}
