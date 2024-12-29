package org.example.back.services;

import org.example.back.models.dto.ServersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServersService {

    List<ServersDTO> getAllServers();
}
