package com.github.yilmazbahadir.kalah.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
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
