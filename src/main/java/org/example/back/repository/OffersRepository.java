package org.example.back.repository;

import org.example.back.models.OffersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffersRepository extends JpaRepository<OffersModel, Long> {
    List<OffersModel> findByWorkerServerJobId(Long id);
}
