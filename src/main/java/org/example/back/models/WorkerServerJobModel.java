package org.example.back.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.example.back.services.exception.JobLevelOutOfRangeException;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_server_job")
public class WorkerServerJobModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to WorkerServer
    @NotNull
    @ManyToOne
    @JoinColumn(name = "worker_server_id", referencedColumnName = "id")
    private WorkerServerModel workerServer;

    // Reference to Job
    @NotNull
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private JobsModel job;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private WorkersModel worker;

    @Getter
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "level")
    @Max(value=200)
    @Min(value=0)
    private Integer level;

    // La relation One-to-One avec l'offre
    @OneToOne(mappedBy = "workerServerJob")
    private OffersModel offer;

    @Override
    public String toString() {
        return "WorkerServerJobModel(id=" + id + ", workerServer=" + workerServer + ", job=" + job + ", level=" + level + ")";
    }
}
