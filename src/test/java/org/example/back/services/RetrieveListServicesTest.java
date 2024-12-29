package org.example.back.services;

import org.example.back.models.dto.ServicesDTO;
import org.example.back.services.impl.ServicesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RetrieveListServicesTest {

    @Autowired
    private MockMvc mockMvc; // Dependency injection of MockMvc into the test

    @MockBean
    private ServicesServiceImpl servicesService; // Creating a mock of the ServicesServiceImpl service

    private ServicesDTO service1; // Declaration of ServicesDTO object for the first service
    private ServicesDTO service2; // Declaration of ServicesDTO object for the second service

    @BeforeEach // Method that runs before each test
    void setUp() {

        // Initializing service data for the test
        service1 = new ServicesDTO();
        service1.setId(1L);
        service1.setName("Archimonstre");
        service1.setServiceType("Packs");
        service1.setServiceGame("Dofus");

        service2 = new ServicesDTO();
        service2.setId(2L);
        service2.setName("Métier");
        service2.setServiceType("Packs");
        service2.setServiceGame("Dofus");
    }

    @WithMockUser(username = "guillaume@gmail.com", roles = {"CUSTOMER"}) // Allows you to simulate a user connection to pass the tests
    @Test
    void testGetAllServices() throws Exception {

        // Preparing the data to be returned by the service
        List<ServicesDTO> services = Arrays.asList(service1, service2);

        // Mocking the service's behavior to return services
        when(servicesService.getAllServices()).thenReturn(services);

        // Performing the GET request on the API and validating the response
        mockMvc.perform(get("/services/list")) // Performing a GET request on the /services/list endpoint
                .andExpect(status().isOk()) // Verifying that the HTTP status is 200 (OK)
                .andExpect(jsonPath("$.size()").value(2)) // Verifying that the response contains 2 elements
                .andExpect(jsonPath("$[0].name").value("Archimonstre")) // Check that the name of the first service is correct
                .andExpect(jsonPath("$[0].serviceType").value("Packs")) // Check that the serviceType of the first service is correct
                .andExpect(jsonPath("$[0].serviceGame").value("Dofus")) // Check that the serviceGame of the first service is correct
                .andExpect(jsonPath("$[1].name").value("Métier")) // Check that the name of the second service is correct
                .andExpect(jsonPath("$[1].serviceType").value("Packs")) // Check that the serviceType of the second service is correct
                .andExpect(jsonPath("$[1].serviceGame").value("Dofus")); // Check that the serviceGame of the second service is correct
    }
}
