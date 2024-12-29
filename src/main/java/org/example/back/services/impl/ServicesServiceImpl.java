package org.example.back.services.impl;

import lombok.Data;
import org.example.back.models.dto.ServicesDTO;
import org.example.back.models.mapper.ServicesMapper;
import org.example.back.repository.ServicesRepository;
import org.example.back.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository servicesRepository;  // A repository that handles database operations for Services. It is marked as final to ensure it's injected via the constructor.

    @Autowired
    private ServicesMapper servicesMapper;  // A mapper to convert data betweenServices entities and Data Transfer Objects (DTOs).

    // Constructor-based dependency injection for the ServicesRepository.
    public ServicesServiceImpl(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;  // Assigns the provided repository to the servicesRepository field.
    }

    // Implements the method defined in ServicesService interface.
    @Override
    public List<ServicesDTO> getAllServices() {
        // Retrieves allServices entities from the database, maps them to DTOs, and collects them into a list.
        return servicesRepository.findAll()  // Finds all service in the repository (database).
                .stream()  // Converts the list of services into a stream to perform operations on each item.
                .map(servicesMapper::toDto)  // Maps eachServices entity to its corresponding ServicesDTO using the servicesMapper.
                .collect(Collectors.toList());  // Collects the mapped ServicesDTO objects into a list and returns it.
    }
}

