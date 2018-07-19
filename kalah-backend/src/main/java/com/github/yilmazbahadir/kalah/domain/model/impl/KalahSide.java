package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.Pit;
import com.github.yilmazbahadir.kalah.domain.model.Side;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

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
public class KalahSide implements Side {

    private long sideId;
    private Pit house;
    private Pit[] pits;
    private AtomicInteger totalNumOfStonesInPits;

    public KalahSide(int sideId, int numOfPits, int numOfStonesInEachPit) {
        this.sideId = sideId;
        this.pits = new KalahPit[numOfPits];
        this.totalNumOfStonesInPits = new AtomicInteger(numOfPits * numOfStonesInEachPit);
        int i = 0;
        for (; i < numOfPits; i++) {
            this.pits[i] = new KalahPit(numOfStonesInEachPit, i,
                    e -> {
                        switch (e.getType()) {
                            case PIT_SOWED:
                                this.totalNumOfStonesInPits.incrementAndGet();
                                break;
                            case PIT_PICKED:
                                this.totalNumOfStonesInPits.accumulateAndGet(e.getBeforeNumOfStones(), (x, y) -> x - y);
                                break;
                        }
                    });
        }
        this.house = new KalahPit(0, i, KalahPit.PitType.HOUSE, t -> {
            // TODO fire score event
        });

    }

    public Pit getPit(int pitIndex) {
        return this.pits[pitIndex];
    }
}
