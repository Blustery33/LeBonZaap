package org.example.back.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.back.models.enums.Roles;
import org.example.back.models.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements UserDetails {

    // Unique identifier for each user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    // Username or pseudonym of the user
    @Column
    @NotNull
    private String username;

    // User's email address
    @Column
    @NotNull
    @Email(message = "invalid email format")
    private String email;

    // User's password (should be stored securely)
    @Column
    @NotNull
    private String password;

    // Type of user, defaulting to 'CUSTOMER'
    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private UserType userType = UserType.CUSTOMER;

    // List of notices posted by the user, managed by cascading all operations and lazy loading
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoticeOfferModel> notices;

    // Status indicating if the user is currently online
    @Column
    private Boolean online;


    // URL or file path to the user's profile picture
    @Column
    private String profil_picture;

    // URL or file path to the user's profile banner image
    @Column
    private String profil_banner;

    // Timestamp of when the user account was created
    @Column
    @NotNull
    private LocalDateTime created_at;


//    @JoinTable(name="favorites",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns =@JoinColumn(name="offer_id"))
//    private Set<OffersModel> favoriteOffers=new HashSet<>();

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
//    private Set<OffersModel> favoriteOffers;


    // Association with the WorkersModel, if the user is a worker; cascade all operations
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WorkersModel workers;

    @Override
    public String toString() {
        return "UserModel(id=" + id + ", username=" + username + ", email=" + email + ", userType=" + userType + ", refreshTokens=" + refreshToken + ")";
    }




    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;
    // Getter for the workers field, only returning it if the userType is 'WORKERS'
//    public WorkersModel getWorkers() {
//        return userType == UserType.WORKERS ? workers : null;
//    }


    // Authorities can be use to spring security with .hasRole("ROLE_xxx")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+ userType.name()));
    }

}
