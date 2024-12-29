package org.example.back.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.back.annotations.CurrentUser;
import org.example.back.controllers.OffersController;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;
import org.example.back.models.dto.OffersDTO;
import org.example.back.models.mapper.OffersMapper;
import org.example.back.services.OffersService;
import org.example.back.services.exception.ErrorResponse;
import org.example.back.services.exception.JobLevelOutOfRangeException;
import org.example.back.services.exception.OfferAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a Spring REST controller
@Slf4j // Lombok annotation to generate a logger field
public class OffersControllerImpl implements OffersController {

    @Autowired
    private OffersService offersService; // Service layer to handle business logic for Offers

    @Autowired
    private OffersMapper offersMapper;

    public ResponseEntity<List<OffersDTO>> getAllOffers() {
        try {
            // Fetch the list of offers from the service layer
            var offers = offersService.getAllOffers();
            // Return a 200 OK response with the offers list
            return ResponseEntity.ok().body(offers);
        } catch (Exception e) {
            // If an error occurs, return a 404 NOT FOUND response
            log.error("Error occurred while find all offer: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<OffersDTO> getOfferById(Long id) {
        try {
            // Retrieve the offer by its ID from the service layer
            OffersDTO offer = offersService.getOfferById(id);
            // Return a response with HTTP status 200 (OK) and the retrieved offer as the response body
            return ResponseEntity.ok(offer);
        } catch (Exception e) {
            // If the offer is not found or another error occurs, return a response with HTTP status 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createOffer(OffersDTO offerDTO, UserModel currentUser) {
        try {
            // Call the service to create the offer
            var createdOffer = offersService.createOffer(offerDTO, currentUser);

            // Convert the created offer entity to a DTO
            OffersDTO response = offersMapper.toDto(createdOffer);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (OfferAlreadyExistsException e) {
            // If the offer already exists, return a 409 Conflict response with the error message
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (JobLevelOutOfRangeException e){
            // If job level out of range, return a 409 Conflict response with the error message
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Une erreur est survenue", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<?> updateOffer(@PathVariable Long id, @RequestBody OffersDTO offerDTO, @CurrentUser UserModel currentUser) {
        try {
            // Update the offer with the given ID using the provided offerDTO in the service layer
            OffersModel updatedOffer = offersService.updateOffer(id, offerDTO, currentUser);
            OffersDTO response = offersMapper.toDto(updatedOffer);
            // Return a response with HTTP status 200 (OK) and the updated offer as the response body
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If the offer is not found or another error occurs, return a response with HTTP status 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        try {
            offersService.deleteOffer(id);
            return ResponseEntity.ok("Offer with ID " + id + " has been successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Offer with ID " + id + " not found.");
        }
    }


}
