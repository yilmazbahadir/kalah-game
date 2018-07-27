package com.github.yilmazbahadir.kalah.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <h1>Service registry for services</h1>
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApp {

    public static void main(String[] args){
        SpringApplication.run(ServiceRegistryApp.class, args);
    }
}
