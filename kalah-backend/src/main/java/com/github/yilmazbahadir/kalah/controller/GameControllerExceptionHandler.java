package com.github.yilmazbahadir.kalah.controller;

import com.github.yilmazbahadir.kalah.controller.model.ErrCode;
import com.github.yilmazbahadir.kalah.controller.model.KalahResponse;
import com.github.yilmazbahadir.kalah.exception.BaseKalahException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <h1>Game Controller Exception Handler</h1>
 * It catches the exceptions thrown by the controllers
 * and returns system friendly, structured response with error code and the message
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@ControllerAdvice
public class GameControllerExceptionHandler {

    @ExceptionHandler(BaseKalahException.class)
    public ResponseEntity<KalahResponse> handleGameException(BaseKalahException e) {
        return ResponseEntity.badRequest().body( // http status 400
                KalahResponse.builder()
                    .success(false)
                    .errCode(ErrCode.valueOf(e.getClass().getSimpleName()).getCode())
                    .message(e.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<KalahResponse> handleException(Exception e) {
        return ResponseEntity.badRequest().body( // http status 400
                KalahResponse.builder()
                        .success(false)
                        .errCode(ErrCode.GenericException.getCode())
                        .message("Unexpected Error:" + e.getMessage()).build());
    }
}
