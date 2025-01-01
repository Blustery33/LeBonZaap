package org.example.back.repository;

import org.example.back.models.FavoriteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteModel, Long> {
    List<FavoriteModel> findByUserId(Long userId);

    FavoriteModel findByUserIdAndOfferId(Long userId, Long offerId);
}
