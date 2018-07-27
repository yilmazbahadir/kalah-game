package com.github.yilmazbahadir.kalah.registry.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Provides a gateway for eureka clients' swagger documentation</h1>
 * This class helps to show all service swagger API documents in one place.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Component
@EnableAutoConfiguration
public class GatewaySwaggerResourcesProvider implements SwaggerResourcesProvider {

    @Override
    public List get() {
        List resources = new ArrayList();
        resources.add(swaggerResource("kalah-game-service", "/", "2.0"));

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
