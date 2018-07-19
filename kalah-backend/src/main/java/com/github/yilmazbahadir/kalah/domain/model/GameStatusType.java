package com.github.yilmazbahadir.kalah.domain.model;

/**
 * Represent the states that a game can be in.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
public enum GameStatusType {
    NOT_STARTED,
    STARTED,
    PLAYER_PLAYING,
    WAITING_FOR_NEXT_PLAYER,
    FINISHED
}