package com.xpto.distancelearning.course.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Henrique: Controller just for testing purposes.
// The idea is to test the cases where I have a configuration (dl-course-service.yml) in the config-server-repo and I want to refresh it without restarting this service.
// I can do it by calling the /POST http://localhost:8082XXXXXXXX/dl-course/actuator/refresh endpoint.
@RestController
@RefreshScope // This annotation is used to refresh the configuration without restarting the service.
public class RefreshScopeController {

    // This is coming from the dl-course-service.yml file, that is in the config-server-repo (Global Config Pattern).
    @Value("${course.refreshscope.name}")
    private String name;

    @RequestMapping("/refreshscope")
    public String refreshscope() {
        return this.name;
    }
}