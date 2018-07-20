package com.github.yilmazbahadir.kalah.domain.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <H1>Model of the player's side</H1>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Side extends BaseModel {

    public long getSideId();

    public Pit getPit(int pitIndex);

    public Pit[] getPits();

    public Pit getHouse();

    public AtomicInteger getTotalNumOfStonesInPits();
}
