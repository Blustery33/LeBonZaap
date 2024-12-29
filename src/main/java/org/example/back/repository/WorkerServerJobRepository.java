package org.example.back.repository;

import org.example.back.models.OffersModel;
import org.example.back.models.WorkerServerJobModel;
import org.example.back.models.WorkerServerServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerServerJobRepository extends JpaRepository<WorkerServerJobModel, Long> {

    List<WorkerServerJobModel> findByJobIdAndWorkerId(Long jobId, Long workerId);

    Optional<WorkerServerJobModel> findByWorkerServerIdAndJobId(Long workerServerId, Long jobId);
}
