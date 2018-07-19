package com.github.yilmazbahadir.kalah.controller;

import com.github.yilmazbahadir.kalah.controller.model.ErrCode;
import com.github.yilmazbahadir.kalah.controller.model.KalahResponse;
import com.github.yilmazbahadir.kalah.exception.BaseKalahException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@ControllerAdvice
public class GameControllerExceptionHandler {

    @ExceptionHandler(BaseKalahException.class)
    public ResponseEntity<KalahResponse> handleException(BaseKalahException e) {
        //TODO feed the event pusher (websocket) ?
        return ResponseEntity.badRequest().body(
                KalahResponse.builder()
                    .success(false)
                    .errCode(ErrCode.valueOf(e.getClass().getSimpleName()).getCode())
                    .message(e.getMessage()).build());
    }

    // TODO Other type of exceptions, NullPointerException etc. ?
}
