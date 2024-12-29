package org.example.back.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_server")
public class WorkerServerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation many-to-one with Worker
    @NotNull
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private WorkersModel worker;

    // Relation many-to-one with Server
    @NotNull
    @ManyToOne
    @JoinColumn(name = "server_id", referencedColumnName = "id")
    private ServersModel server;

    @OneToMany(mappedBy = "workerServer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OffersModel> offers;

    // Relation One-to-Many with WorkerServerJobModel
    @OneToMany(mappedBy = "workerServer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkerServerJobModel> workerServerJobs;

    // Relation One-to-Many with WorkerServerServiceModel
    @OneToMany(mappedBy = "workerServer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkerServerServiceModel> workerServerServices;

    @Override
    public String toString() {
        return "WorkerServerModelId(id=" + id + ")";
    }
}
