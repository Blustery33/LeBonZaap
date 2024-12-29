package org.example.back.repository;

import org.example.back.models.WorkerServerJobModel;
import org.example.back.models.WorkerServerServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerServerServiceRepository extends JpaRepository<WorkerServerServiceModel, Long> {

    List<WorkerServerServiceModel> findByServiceIdAndWorkerServerId(Long serviceId, Long workerServerId);

    Optional<WorkerServerServiceModel> findByJobIdAndServiceIdAndWorkerServerId(Long jobId, Long serviceId, Long workerServerId);
}
