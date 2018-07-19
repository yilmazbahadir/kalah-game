package com.github.yilmazbahadir.kalah;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * Main class of the Game Kalah (Kalahi/Mancala)
 *
 * Kalah is a multiplayer(2n sides) game. It consists of
 * x number of pits and y number of stones in pits. This is the
 * main class of Kalah Backend Services.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

@SpringBootApplication
public class KalahGameApp {

    public static final Logger LOG = LoggerFactory.getLogger(KalahGameApp.class);


    public static void main(String[] args) {
        LOG.info("Kalah Game is starting ...");
        SpringApplication.run(KalahGameApp.class, args);
        LOG.info("Kalah Game has started.");
    }

}