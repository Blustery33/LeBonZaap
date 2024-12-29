package org.example.back.repository;

import org.example.back.models.JobsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsRepository extends JpaRepository<JobsModel, Long> {
}
