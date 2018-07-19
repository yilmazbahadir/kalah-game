package com.github.yilmazbahadir.kalah;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * Main class of the Game Kalah (Kalahi/Mancala)
 *
 * @author Bahadir Yilmaz
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