package com.github.yilmazbahadir.kalah.controller.model;

import lombok.Getter;

import java.util.Arrays;

/**
 * <h1>Error Codes</h1>
 * Enumeration of the error codes mapped to exceptions
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public enum ErrCode {
    Success(0),
    GenericException(1),
    GameIdNotFoundException(1000),
    GameNameAlreadyUsedException(1001),
    GameStatusException(1002),
    InvalidPitIndexException(103),
    NoAvailableSeatsException(1004),
    PlayerInvalidTurnException(1005),
    WrongMoveException(10006);

    @Getter
    private int code;

    ErrCode(int code) {
        this.code = code;
    }

    int intValue(ErrCode errCode) {
        return Arrays.stream(values()).filter(i -> i.equals(errCode)).findFirst().orElse(ErrCode.GenericException).getCode();
    }
}
