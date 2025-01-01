package org.example.back.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "favorites")
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private OffersModel offer;

    @Column(name = "is_favorite")
    @NotNull
    private Boolean isFavorite;
}
