package org.example.back.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.controllers.ServersController;
import org.example.back.models.dto.ServersDTO;
import org.example.back.services.ServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Servers API") // OpenAPI tag for categorizing this controller as "Servers API"
@RestController // Marks this class as a Spring REST controller
@RequestMapping("/servers") // Base URL for all endpoints in this controller
public class ServersControllerImpl implements ServersController {

    // Service layer to handle business logic for servers
    @Autowired
    private ServersService serversService;

    @GetMapping("/list") // Maps this method to GET requests at /api/servers/list
    public ResponseEntity<List<ServersDTO>> getAllServers() {
        try {
            // Fetch the list of servers from the service layer
            var servers = serversService.getAllServers();
            // Return a 200 OK response with the server list
            return ResponseEntity.ok().body(servers);
        } catch (Exception e) {
            // If an error occurs, return a 404 NOT FOUND response
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
