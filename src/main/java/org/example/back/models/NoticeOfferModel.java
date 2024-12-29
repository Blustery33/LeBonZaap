package org.example.back.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice_offer")
public class NoticeOfferModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation Many-to-One with User
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    // Relation Many-to-One with Offer
    @NotNull
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private OffersModel offer;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "note")
    @Max(value=5)
    @Min(value=1)
    private Integer note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
