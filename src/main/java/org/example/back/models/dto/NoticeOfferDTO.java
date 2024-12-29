package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;

import java.time.LocalDateTime;

@Data
public class NoticeOfferDTO {
    private Long id;
    private Long userId;
    private OffersDTO offer;
    private String description;
    private Integer note;
    private LocalDateTime createdAt;
}
