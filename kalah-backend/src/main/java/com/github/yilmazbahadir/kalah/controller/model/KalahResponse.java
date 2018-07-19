package com.github.yilmazbahadir.kalah.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <h1>Contorller(Rest API) Response Model</h1>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Data
@AllArgsConstructor
@Builder
public class KalahResponse<T> {

    private boolean success = true;
    private int errCode;
    private String message = "";
    private T data;

    public KalahResponse(T data) {
        this.data = data;
    }
}
