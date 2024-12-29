package org.example.back.repository;

import org.example.back.models.ServersModel;
import org.example.back.models.WorkerServerModel;
import org.example.back.models.WorkersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerServerRepository extends JpaRepository<WorkerServerModel, Long> {
    Optional<WorkerServerModel> findByWorkerIdAndServerId(Long workerId, Long serverId);
}
