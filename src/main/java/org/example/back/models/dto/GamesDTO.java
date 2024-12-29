package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.ServersModel;

import java.util.Set;

@Data
public class GamesDTO {
    private Long id;
    private String name;
    private Set<ServersModel> servers;
}
