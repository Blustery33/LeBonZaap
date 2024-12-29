package org.example.back.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.models.dto.WorkersDTO;
import org.example.back.services.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Workers API") // OpenAPI tag for categorizing this controller as "Jobs API"
@RequestMapping("/worker") // Base URL for all endpoints in this controller
public interface WorkersController {

    /**
     * Endpoint: GET /list
     * Summary: Fetches a list of all servers.
     * OpenAPI Documentation:
     * - @Operation: Provides a summary and description for API docs.
     * - @ApiResponses: Defines possible responses and their descriptions.
     * Implementation:
     * - Calls the service layer to get the list of worker.
     * - Returns a 200 OK with the list of worker if successful.
     * - Returns a 404 NOT FOUND if an exception occurs.
     */

    @Operation(summary = "List of workers", description = "List of workers") // OpenAPI operation summary and description
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workers list", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WorkersDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Liste des artisans non trouvés.")
    })
    @GetMapping("/workers-list") // Maps this method to GET requests at /api/worker/workers-list
    ResponseEntity<List<WorkersDTO>> getAllWorkers();


    /**
     * Endpoint: GET /worker/{userId}
     * Summary: Fetches worker details for a specific user by userId.
     * OpenAPI Documentation:
     * - @Operation: Provides a summary and description for API docs.
     * - @ApiResponses: Defines possible responses and their descriptions.
     * Implementation:
     * - Calls the service layer to get the worker for the given userId.
     * - Returns a 200 OK with the worker details if found.
     * - Returns a 404 NOT FOUND if the worker is not found for the given userId.
     */
    @Operation(summary = "Get worker by user ID", description = "Fetches worker details for a specific user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Worker details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkersDTO.class))),
            @ApiResponse(responseCode = "404", description = "L'artisan n'a pas été trouvé", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{userId}")
    ResponseEntity<?> getWorkerByUserId(@PathVariable Long userId);
}
