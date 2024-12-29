package org.example.back.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.back.models.dto.JobsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface JobsController {

    /**
     * Endpoint: GET /list
     * Summary: Fetches a list of all servers.
     * OpenAPI Documentation:
     * - @Operation: Provides a summary and description for API docs.
     * - @ApiResponses: Defines possible responses and their descriptions.
     * Implementation:
     * - Calls the service layer to get the list of servers.
     * - Returns a 200 OK with the list of servers if successful.
     * - Returns a 404 NOT FOUND if an exception occurs.
     */

    @Operation(summary = "List of jobs", description = "List of jobs") // OpenAPI operation summary and description
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jobs list", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JobsDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Liste des jobs non trouvés.")
    })
    ResponseEntity<List<JobsDTO>> getAllJobs();
}
