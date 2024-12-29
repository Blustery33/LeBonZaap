package org.example.back.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.controllers.JobsController;
import org.example.back.models.dto.JobsDTO;
import org.example.back.services.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Jobs API") // OpenAPI tag for categorizing this controller as "Jobs API"
@RestController // Marks this class as a Spring REST controller
@RequestMapping("/jobs") // Base URL for all endpoints in this controller
public class JobsControllerImpl implements JobsController {

    @Autowired
    // Service layer to handle business logic for jobs
    private JobsService jobsService;

    @GetMapping("/list") // Maps this method to GET requests at /api/jobs/list
    public ResponseEntity<List<JobsDTO>> getAllJobs() {
        try {
            // Fetch the list of jobs from the service layer
            var jobs = jobsService.getAllJobs();
            // Return a 200 ok response with the job list
            return ResponseEntity.ok().body(jobs);
        } catch (Exception e) {
            // If an error occurs, return a 404 NOT FOUND response
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
