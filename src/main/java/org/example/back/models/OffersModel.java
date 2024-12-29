package org.example.back.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offers")
public class OffersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "workerServer_id")
    private WorkerServerModel workerServer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_server_job_id")
    private WorkerServerJobModel workerServerJob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_server_service_id")
    private WorkerServerServiceModel workerServerService;

    // Relation Many-to-Many with User for Favorites
//    @ManyToMany(mappedBy = "favoriteOffers")
//    @Builder.Default
//    private Set<UserModel> favoritedByUsers = new HashSet<>();

    // Relation One-to-Many with NoticeOffer
//    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private List<NoticeOfferModel> notices;

    @NotNull
    @Column(name = "title_offer")
    private String titleOffer;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private String price;

    @NotNull
    @Column(name = "pseudo_in_game")
    private String pseudoInGame;

    @NotNull
    @Column(name = "offer_hidden")
    private Boolean offerHidden;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "OfferModel(id=" + id + ", workerServerService=" + workerServerService + ")";
    }


    // Method to set the creation date at the time of review creation
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
