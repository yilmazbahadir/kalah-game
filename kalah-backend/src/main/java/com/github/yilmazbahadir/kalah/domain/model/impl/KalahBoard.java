package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.Board;
import com.github.yilmazbahadir.kalah.domain.model.Side;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Getter
@Setter
@ToString
public class KalahBoard implements Board {

    private Side[] sides;


    /**
     * @param numOfSides           total number of sides / players
     * @param numOfPits            number of pits including houses.
     * @param numOfStonesInEachPit number of stones in each pit
     */
    public KalahBoard(int numOfSides, int numOfPits, int numOfStonesInEachPit) {
        this.sides = new KalahSide[numOfSides];
        for (int i = 0; i < numOfSides; i++) {
            this.sides[i] = new KalahSide(i, numOfPits, numOfStonesInEachPit);
        }
    }

    public Side getSide(int sideId) {
        return this.sides[sideId];
    }

}
