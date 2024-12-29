package org.example.back.services;

import org.example.back.models.dto.JobsDTO;
import org.example.back.services.impl.JobsServiceImpl;
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
public class RetrieveListJobsTest {

    @Autowired
    private MockMvc mockMvc; // Dependency injection of MockMvc into the test

    @MockBean
    private JobsServiceImpl jobsService; // Creating a mock of the JobsServiceImpl service

    private JobsDTO job1; // Declaration of JobsDTO object for the first job

    private JobsDTO job2;  // Declaration of JobsDTO object for the second job

    @BeforeEach // Method that runs before each test
    void setUp() {

        // Initializing job data for the test
        job1 = new JobsDTO();
        job1.setId(1L);
        job1.setName("Alchimiste");
        job1.setJobType("Récolte");
        job1.setJobGame("Dofus");

        job2 = new JobsDTO();
        job2.setId(2L);
        job2.setName("Bijoutier");
        job2.setJobType("Fabrication");
        job2.setJobGame("Dofus");
    }

    @WithMockUser(username = "guillaume@gmail.com", roles = {"CUSTOMER"}) // Allows you to simulate a user connection to pass the tests
    @Test
    void testGetAllJobs() throws Exception {

        // Preparing the data to be returned by the service
        List<JobsDTO> jobs = Arrays.asList(job1, job2);

        // Mocking the service's behavior to return jobs
        when(jobsService.getAllJobs()).thenReturn(jobs);

        // Performing the GET request on the API and validating the response
        mockMvc.perform(get("/jobs/list")) // Perform the GET request on the specified URL
                .andExpect(status().isOk())  // Check that the HTTP status code is 200 OK
                .andExpect(jsonPath("$.size()").value(2))  // Check that the list size is 2
                .andExpect(jsonPath("$[0].name").value("Alchimiste"))  // Check that the name of the first job is correct
                .andExpect(jsonPath("$[0].jobType").value("Récolte"))  // Check that the jobType of the first job is correct
                .andExpect(jsonPath("$[0].jobGame").value("Dofus")) // Check that the jobGame of the first job is correct
                .andExpect(jsonPath("$[1].name").value("Bijoutier")) // Check that the name of the second job is correct
                .andExpect(jsonPath("$[1].jobType").value("Fabrication"))  // Check that the jobType of the first job is correct
                .andExpect(jsonPath("$[1].jobGame").value("Dofus")); // Check that the JobGame of the second job is correct

    }
}
