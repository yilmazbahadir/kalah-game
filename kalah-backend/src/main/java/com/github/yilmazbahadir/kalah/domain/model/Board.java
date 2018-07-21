package com.github.yilmazbahadir.kalah.domain.model;

/**
 * Board model of a game
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Board extends BaseModel {

    public Side getSide(int sideId);

    public Side[] getSides();
}
