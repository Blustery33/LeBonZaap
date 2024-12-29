package org.example.back.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.controllers.WorkersController;
import org.example.back.models.dto.WorkersDTO;
import org.example.back.services.WorkersService;
import org.example.back.services.exception.ErrorResponse;
import org.example.back.services.exception.WorkerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a Spring REST controller
public class WorkersControllerImpl implements WorkersController {

    @Autowired
    // Service layer to handle business logic for jobs
    private WorkersService workersService;


    public ResponseEntity<List<WorkersDTO>> getAllWorkers() {
        try {
            // Fetch the list of jobs from the service layer
            var jobs = workersService.getAllWorkers();
            // Return a 200 ok response with the job list
            return ResponseEntity.ok().body(jobs);
        } catch (Exception e) {
            // If an error occurs, return a 404 NOT FOUND response
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    public ResponseEntity<?> getWorkerByUserId(@PathVariable Long userId) {
        try {
            // Retrieve the worker by their userId
            WorkersDTO worker = workersService.getWorkerByUserId(userId);
            // Return a 200 response with the found worker
            return ResponseEntity.ok(worker);
        } catch (WorkerNotFoundException ex) {
            // Create an ErrorResponse with a custom message for a 404 error
            ErrorResponse errorResponse = new ErrorResponse("L'artisant avec l'id : "+ userId +" n'a pas été trouvé", HttpStatus.NOT_FOUND.value());
            // Return a 404 response with the ErrorResponse in the body
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            // Create an ErrorResponse for an internal error with a generic message
            ErrorResponse errorResponse = new ErrorResponse("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
            // Return a 500 response with the ErrorResponse in the body
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
