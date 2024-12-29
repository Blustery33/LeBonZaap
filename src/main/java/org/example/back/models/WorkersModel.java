package org.example.back.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workers")
public class WorkersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to link Artisan to User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

//    @Override
//    public String toString() {
//        return "WorkersModel(id=" + id + ", user=" + user + ")";
//
//        // Ne pas appeler UserModel ici pour éviter la récursion infinie
//    }

    // Added One-to-Many relationship with WorkerServerModel
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkerServerModel> workerServers;
}
