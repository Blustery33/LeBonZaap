package org.example.back.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import org.example.back.models.enums.UserType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserType userType;
    private List<NoticeOfferDTO> notices;
    private Boolean online;
    private String profilPicture;
    private String profilBanner;
    private LocalDateTime created_at;
}
