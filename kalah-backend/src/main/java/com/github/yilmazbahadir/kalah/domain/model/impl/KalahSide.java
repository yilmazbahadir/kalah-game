package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.Pit;
import com.github.yilmazbahadir.kalah.domain.model.Side;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kalah implementation of the Side model.
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
                        if (Pit.PitEventType.PIT_SOWED == e.getType()) {
                            this.totalNumOfStonesInPits.incrementAndGet();
                        } else if(Pit.PitEventType.PIT_PICKED == e.getType()) {
                                this.totalNumOfStonesInPits.accumulateAndGet(e.getBeforeNumOfStones(), (x, y) -> x - y);
                        }
                    });
        }
        this.house = new KalahPit(0, i, KalahPit.PitType.HOUSE, t -> {});

    }

    public Pit getPit(int pitIndex) {
        return this.pits[pitIndex];
    }

}
