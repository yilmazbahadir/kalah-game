package com.github.yilmazbahadir.kalah.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <h1>Model of the pit</h1>
 * Pickable, sowable model of the pit takes place in each side.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Pit extends BaseModel {

    public int getIndex();

    public int count();

    public int sow();

    public int sow(int stones);

    public int pick();

    public PitType getType();

    public enum PitType {
        NORMAL, // regular
        HOUSE;
    }

    @FunctionalInterface
    public interface PitChangeListener {
        public void handle(PitEvent e);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class PitEvent implements Serializable {
        private PitEventType type;
        private int beforeNumOfStones;
        private int afterNumOfStones;
    }

    public enum PitEventType {
        PIT_SOWED,
        PIT_PICKED;

    }

}
