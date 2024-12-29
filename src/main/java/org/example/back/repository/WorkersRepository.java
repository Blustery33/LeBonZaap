package org.example.back.repository;

import org.example.back.models.WorkersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkersRepository extends JpaRepository<WorkersModel, Long> {


    // Rechercher un travailleur par l'ID de l'utilisateur
    Optional<WorkersModel> findByUserId(Long id);
}
