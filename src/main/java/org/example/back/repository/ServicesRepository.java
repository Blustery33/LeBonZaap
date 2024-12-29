package org.example.back.repository;

import org.example.back.models.ServicesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<ServicesModel, Long> {
}
