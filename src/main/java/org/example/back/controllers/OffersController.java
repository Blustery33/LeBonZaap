package org.example.back.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.annotations.CurrentUser;
import org.example.back.models.UserModel;
import org.example.back.models.dto.OffersDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Offers", description = "The Offers API") // OpenAPI tag for Offers-related endpoints
@RequestMapping("/offers") // Base URL for all Offers endpoints
public interface OffersController {

    /**
     * Endpoint: GET /list
     * Summary: Fetches a list of all offers.
     * OpenAPI Documentation:
     * - @Operation: Provides a summary and description for API docs.
     * - @ApiResponses: Defines possible responses and their descriptions.
     * Implementation:
     * - Calls the service layer to get the list of servers.
     * - Returns a 200 OK with the list of servers if successful.
     * - Returns a 404 NOT FOUND if an exception occurs.
     */

    @Operation(summary = "List of offers", description = "List of offers") // OpenAPI operation summary and description
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers list", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OffersDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Liste des offres non trouvées.")
    })
    @GetMapping("/list") // Maps this method to GET requests at /offers/list
    ResponseEntity<List<OffersDTO>> getAllOffers();

    @Operation(summary = "Create a new offer", description = "Create a new offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OffersDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/create")
    ResponseEntity<?> createOffer(
            @RequestBody OffersDTO offerDTO,
            @Parameter(hidden = true) @CurrentUser UserModel currentUser);

    @Operation(summary = "Get an offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OffersDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<OffersDTO> getOfferById(@PathVariable Long id);

    @Operation(summary = "Update an existing offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OffersDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<?> updateOffer(@PathVariable Long id, @RequestBody OffersDTO offerDTO, @Parameter(hidden = true) @CurrentUser UserModel currentUser);



    @Operation(summary = "Delete an offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Offer deleted successfully", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteOffer(@PathVariable Long id);
}