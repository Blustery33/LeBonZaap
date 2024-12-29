package org.example.back.services;

import org.example.back.models.dto.ServicesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicesService {
    List<ServicesDTO> getAllServices();
}
