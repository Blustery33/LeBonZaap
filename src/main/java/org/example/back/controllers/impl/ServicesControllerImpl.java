package org.example.back.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.controllers.ServicesController;
import org.example.back.models.dto.ServicesDTO;
import org.example.back.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Services API") // OpenAPI tag for categorizing this controller as "Services API"
@RestController // Marks this class as a Spring REST controller
@RequestMapping("/services") // Base URL for all endpoints in this controller
public class ServicesControllerImpl implements ServicesController {

    // Service layer to handle business logic for services
    @Autowired
    private ServicesService servicesService;

    @GetMapping("/list") // Maps this method to GET requests at /api/services/list
    public ResponseEntity<List<ServicesDTO>> getAllServices() {
        try {
            // Fetch the list of services from the service layer
            var services = servicesService.getAllServices();
            // Return a 200 OK response with the server list
            return ResponseEntity.ok().body(services);
        } catch (Exception e) {
            // If an error occurs, return a 404 NOT FOUND response
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
