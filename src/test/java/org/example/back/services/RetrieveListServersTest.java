package org.example.back.services;

import org.example.back.models.dto.ServersDTO;
import org.example.back.services.impl.ServersServiceImpl;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RetrieveListServersTest {


    @Autowired
    private MockMvc mockMvc; // Dependency injection of MockMvc into the test

    @MockBean
    private ServersServiceImpl serversService; /// Creating a mock of the ServersServiceImpl service

    private ServersDTO server1; // Declaration of ServersDTO object for the first server
    private ServersDTO server2;  // Declaration of ServersDTO object for the second server

    @BeforeEach // Method that runs before each test
    void setUp() {

        // Initializing server data for the test
        server1 = new ServersDTO();
        server1.setId(1L);
        server1.setName("Draconiros");
        server1.setGameId(1L);
        server1.setGameName("Dofus");

        server2 = new ServersDTO();
        server2.setId(2L);
        server2.setName("Hell Mina");
        server2.setGameId(1L);
        server2.setGameName("Dofus");
    }

    @WithMockUser(username = "guillaume@gmail.com", roles = {"CUSTOMER"}) // Allows you to simulate a user connection to pass the tests}
    @Test
    void testGetAllServers() throws Exception {
        // Preparing the data to be returned by the service
        List<ServersDTO> servers = Arrays.asList(server1, server2);

        // Mocking the service's behavior to return servers
        when(serversService.getAllServers()).thenReturn(servers);

        // Performing the GET request on the API and validating the response
        mockMvc.perform(get("/servers/list"))  // Perform the GET request on the specified URL
                .andExpect(status().isOk())  // Check that the HTTP status code is 200 OK
                .andExpect(jsonPath("$.size()").value(2))  // Check that the list size is 2
                .andExpect(jsonPath("$[0].name").value("Draconiros"))  // Check that the name of the first server is correct
                .andExpect(jsonPath("$[0].gameName").value("Dofus")) // Check that the game name of the first server is correct
                .andExpect(jsonPath("$[1].name").value("Hell Mina"));  // Check that the name of the second server is correct
    }
}