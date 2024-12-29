package org.example.back.repository;

import org.example.back.models.ServersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServersRepository extends JpaRepository<ServersModel, Long> {
    Optional<ServersModel> findFirstByOrderByIdAsc();
}
