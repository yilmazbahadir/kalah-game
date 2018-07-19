package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.yilmazbahadir.kalah.domain.model.Pit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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
public class KalahPit implements Pit {

    private PitType type = PitType.NORMAL;

    @Getter
    private int index;
    private AtomicInteger numOfStones;
    @JsonIgnore
    private PitChangeListener listener;

    public KalahPit(int numOfStones, int index, PitChangeListener listener) {
        this.numOfStones = new AtomicInteger(numOfStones);
        this.index = index;
        this.listener = listener;
    }

    public KalahPit(int numOfStones, int index, PitType type, PitChangeListener listener) {
        this(numOfStones, index, listener);
        this.type = type;
    }

    @Override
    public int count() {
        return this.numOfStones.intValue();
    }

    @Override
    public int sow() {
        return sow(1);
    }

    @Override
    public int sow(int stones) {
        int before = this.numOfStones.get();
        int after = this.numOfStones.addAndGet(stones);
        this.listener.handle(new PitEvent(PitEventType.PIT_SOWED, before, after));
        return after;
    }

    @Override
    public int pick() {
        int result = this.numOfStones.get();
        this.numOfStones.set(0);
        this.listener.handle(new PitEvent(PitEventType.PIT_PICKED, result, 0));

        return result;
    }
}
