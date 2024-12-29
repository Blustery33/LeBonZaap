package org.example.back.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_server_service")
public class WorkerServerServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation many-to-one to WorkerServer
    @NotNull
    @ManyToOne
    @JoinColumn(name = "worker_server_id", referencedColumnName = "id")
    private WorkerServerModel workerServer;

    // Relation many-to-one to Job
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private JobsModel job;

    // Relation many-to-one to Service
    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private ServicesModel service;

    // La relation One-to-One avec l'offre
    @OneToOne(mappedBy = "workerServerService")
    private OffersModel offer;

    @Override
    public String toString() {
        return "WorkerServerServiceId(id=" + id + ", workerServer=" + workerServer + ", job=" + job + ", service=" + service + ")";
    }
}
