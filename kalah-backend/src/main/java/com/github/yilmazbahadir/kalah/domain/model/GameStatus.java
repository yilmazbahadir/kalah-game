package com.github.yilmazbahadir.kalah.domain.model;

/**
 * Status of the game
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface GameStatus extends BaseModel {

    public void setCurrentPlayer(int currentPlayer);

    public int getCurrentPlayer();

    public void setNextPlayer(int nextPlayer);

    public int getNextPlayer();

    public void setStatusType(GameStatusType statusType);

    public GameStatusType getStatusType();
}