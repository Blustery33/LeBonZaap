package org.example.back.services;

import org.example.back.annotations.CurrentUser;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;
import org.example.back.models.dto.OffersDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface OffersService {

    List<OffersDTO> getAllOffers();

    OffersModel createOffer(OffersDTO offerDTO, UserModel currentUser);

    OffersDTO getOfferById(Long id);

    OffersModel updateOffer(Long offerId, OffersDTO offerDTO, UserModel currentUser);

    void deleteOffer(Long id);
}
