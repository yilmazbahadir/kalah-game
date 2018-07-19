package com.github.yilmazbahadir.kalah.domain.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

public interface Side extends BaseModel {

    public Pit getPit(int pitIndex);

    public Pit getHouse();

    public AtomicInteger getTotalNumOfStonesInPits();
}
