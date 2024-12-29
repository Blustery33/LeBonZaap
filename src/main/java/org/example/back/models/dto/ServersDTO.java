package org.example.back.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.back.models.GamesModel;

@Data
public class ServersDTO {
    private Long id;
    private String name;
    private Long gameId;
    private String gameName;
}
