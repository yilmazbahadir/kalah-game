package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.GameStatus;
import com.github.yilmazbahadir.kalah.domain.model.GameStatusType;
import lombok.ToString;

/**

 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@ToString
public class KalahGameStatus implements GameStatus {

    private static final long serialVersionUID = -2180814633373595106L;

    private int currentPlayer;
    private int nextPlayer;
    private GameStatusType statusType;

    public KalahGameStatus(final GameStatusType statusType) {
        this.statusType = statusType;
        this.currentPlayer = -1;
        this.nextPlayer = 0;
    }

    @Override
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    @Override
    public GameStatusType getStatusType() {
        return statusType;
    }

    @Override
    public void setStatusType(GameStatusType statusType) {
        this.statusType = statusType;
    }
}