package org.example.back.services.impl;

import lombok.Data;
import org.example.back.models.dto.ServersDTO;
import org.example.back.models.mapper.ServersMapper;
import org.example.back.repository.ServersRepository;
import org.example.back.services.ServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class ServersServiceImpl implements ServersService {

    private final ServersRepository serversRepository;  // A repository that handles database operations for Servers. It is marked as final to ensure it's injected via the constructor.

    @Autowired
    private ServersMapper serversMapper;  // A mapper to convert data between Server entities and Data Transfer Objects (DTOs).

    // Constructor-based dependency injection for the ServersRepository.
    public ServersServiceImpl(ServersRepository serversRepository) {
        this.serversRepository = serversRepository;  // Assigns the provided repository to the serversRepository field.
    }

    // Implements the method defined in ServersService interface.
    @Override
    public List<ServersDTO> getAllServers() {
        // Retrieves all Server entities from the database, maps them to DTOs, and collects them into a list.
        return serversRepository.findAll()  // Finds all servers in the repository (database).
                .stream()  // Converts the list of servers into a stream to perform operations on each item.
                .map(serversMapper::toDto)  // Maps each Server entity to its corresponding ServerDTO using the serversMapper.
                .collect(Collectors.toList());  // Collects the mapped ServerDTO objects into a list and returns it.
    }
}

