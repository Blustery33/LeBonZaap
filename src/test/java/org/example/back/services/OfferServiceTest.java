package org.example.back.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.back.config.JwtService;
import org.example.back.models.*;
import org.example.back.models.dto.OffersDTO;
import org.example.back.models.Data.ServerInfo;
import org.example.back.models.Data.WorkerServerJob;
import org.example.back.models.enums.UserType;
import org.example.back.repository.*;
import org.example.back.services.impl.AuthServiceImpl;
import org.example.back.services.impl.OffersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  //Permet de rien garder en mémoire
class OfferServiceTest {

    @MockBean
    private JwtService jwtService;  // Mock le service JWT pour les tests

    @MockBean
    private AuthServiceImpl authService;

    @Autowired
    private OffersServiceImpl offerService; // Creating a mock of the OfferService service

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WorkerServerJobRepository workerServerJobRepository;

    @MockBean
    private WorkersRepository workersRepository;

    @MockBean
    private OffersRepository offersRepository;

    @MockBean
    private ServersRepository serversRepository;

    @MockBean
    private WorkerServerRepository workerServerRepository;

    @MockBean
    private JobsRepository jobsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    @Test
    @WithMockUser(username = "alex", roles = {"CUSTOMER"})
    public void shouldCreateOffer() {
        // Préparation des données de test
        UserModel currentUser = new UserModel();
        currentUser.setId(1L);
        currentUser.setUsername("alex");
        currentUser.setEmail("alex@gmail.com");
        currentUser.setUserType(UserType.CUSTOMER);
        currentUser.setOnline(true);
        currentUser.setProfil_picture("profile1.jpg");
        currentUser.setProfil_banner("banner1.jpg");
        currentUser.setCreated_at(LocalDateTime.now());


        GamesModel gameModel = new GamesModel();
        gameModel.setId(1L);
        gameModel.setName("Dofus");

        ServersModel serverModel = new ServersModel();
        serverModel.setId(1L);
        serverModel.setName("Draconiros");
        serverModel.setGame(gameModel);

        JobsModel jobModel = new JobsModel();
        jobModel.setId(1L);
        jobModel.setName("Mineur");

        WorkersModel worker = new WorkersModel();
        worker.setId(1L);
        worker.setUser(currentUser);
        currentUser.setWorkers(worker);

        WorkerServerModel workerServer = new WorkerServerModel();
        workerServer.setId(1L);
        workerServer.setWorker(worker);
        workerServer.setServer(serverModel);

        OffersDTO offerDTO = new OffersDTO();
        offerDTO.setWorkerServerWorkerId(1L);
        offerDTO.setTitleOffer("Titre de mon offre alex");
        offerDTO.setDescription("Description de mon offre alex");
        offerDTO.setPrice("1500000");
        offerDTO.setPseudoInGame("alex");
        offerDTO.setOfferHidden(false);

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setId(serverModel.getId());
        serverInfo.setServerId(serverModel.getId());
        serverInfo.setName(serverModel.getName());
        serverInfo.setGameName(serverModel.getGame().getName());
        offerDTO.setServer(serverInfo);

        WorkerServerJob workerServerJobDTO = new WorkerServerJob();
        workerServerJobDTO.setLevel(50);
        workerServerJobDTO.setJobId(1L);
        workerServerJobDTO.setJobName("Mineur");
        offerDTO.setWorkerServerJob(workerServerJobDTO);

        // Mock des comportements des repositories


        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(serversRepository.findById(offerDTO.getServer().getServerId())).thenReturn(Optional.of(serverModel));
        when(jobsRepository.findById(1L)).thenReturn(Optional.of(jobModel));
        when(workersRepository.findByUserId(currentUser.getId())).thenReturn(Optional.of(worker)); // No worker yet
        when(workerServerRepository.findByWorkerIdAndServerId(worker.getId(), serverModel.getId())).thenReturn(Optional.of(workerServer));
        when(workerServerJobRepository.findByJobIdAndWorkerId(1L, 1L)).thenReturn(Collections.emptyList());

        OffersModel expectedOffer = new OffersModel();
        expectedOffer.setId(1L);
        expectedOffer.setTitleOffer("Titre de mon offre alex");
        expectedOffer.setDescription("Description de mon offre alex");
        expectedOffer.setPrice("1500000");
        expectedOffer.setPseudoInGame("alex");
        expectedOffer.setOfferHidden(false);
        expectedOffer.setWorkerServer(workerServer);

        when(offersRepository.save(any(OffersModel.class))).thenReturn(expectedOffer);

        // Appel de la méthode à tester
        OffersModel createdOffer = offerService.createOffer(offerDTO, currentUser);

        // Assertions
        assertThat(createdOffer.getTitleOffer()).isEqualTo("Titre de mon offre alex");
        assertThat(createdOffer.getDescription()).isEqualTo("Description de mon offre alex");
        assertThat(createdOffer.getPrice()).isEqualTo("1500000");
        assertThat(createdOffer.getPseudoInGame()).isEqualTo("alex");
        assertThat(createdOffer.getOfferHidden()).isFalse();
        assertThat(createdOffer.getWorkerServer().getServer().getId()).isEqualTo(1L);

        // Vérification des interactions
        verify(serversRepository, times(1)).findById(1L);
        verify(offersRepository, times(1)).save(any(OffersModel.class));
    }

    @Test
    @WithMockUser(username = "alex", roles = {"WORKERS"})
    public void shouldUpdateOffer() {
        // Préparation des données de test
        UserModel currentUser = new UserModel();
        currentUser.setId(1L);

        ServersModel serverModel = new ServersModel();
        serverModel.setId(1L);

        WorkersModel worker = new WorkersModel();
        worker.setId(1L);
        worker.setUser(currentUser);

        WorkerServerModel workerServer = new WorkerServerModel();
        workerServer.setId(1L);
        workerServer.setWorker(worker);
        workerServer.setServer(serverModel);

        OffersModel existingOffer = new OffersModel();
        existingOffer.setId(1L);
        existingOffer.setWorkerServer(workerServer);

        OffersDTO offerDTO = new OffersDTO();
        offerDTO.setTitleOffer("Updated Title");
        offerDTO.setDescription("Updated Description");
        offerDTO.setPrice("2000000");
        offerDTO.setPseudoInGame("alexUpdated");
        offerDTO.setOfferHidden(true);

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setId(1L);
        serverInfo.setServerId(1L);
        offerDTO.setServer(serverInfo);

        // Mock des comportements
        when(offersRepository.findById(1L)).thenReturn(Optional.of(existingOffer));
        when(workerServerRepository.findById(1L)).thenReturn(Optional.of(workerServer));
        when(serversRepository.findById(1L)).thenReturn(Optional.of(serverModel));
        when(offersRepository.save(any(OffersModel.class))).thenReturn(existingOffer);

        // Appel de la méthode à tester
        OffersModel updatedOffer = offerService.updateOffer(1L, offerDTO, currentUser);

        // Assertions
        assertThat(updatedOffer.getTitleOffer()).isEqualTo("Updated Title");
        assertThat(updatedOffer.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedOffer.getPrice()).isEqualTo("2000000");
        assertThat(updatedOffer.getPseudoInGame()).isEqualTo("alexUpdated");
        assertThat(updatedOffer.getOfferHidden()).isFalse();

        // Vérification des interactions
        verify(offersRepository, times(1)).findById(1L);
        verify(offersRepository, times(1)).save(any(OffersModel.class));
    }

    @Test
    @Transactional
    @WithMockUser(username = "alex", roles = {"WORKERS"})
    public void testDeleteOffer() {
        // Création et persistance des entités dans le test
        OffersModel offer = new OffersModel();
        entityManager.persist(offer);

        WorkerServerServiceModel service = new WorkerServerServiceModel();
        service.setOffer(offer);
        entityManager.persist(service);

        WorkerServerJobModel job = new WorkerServerJobModel();
        job.setOffer(offer);
        entityManager.persist(job);

        // Récupération de l'ID de l'offre persistance
        Long offerId = offer.getId(); // Utiliser l'ID de l'offre nouvellement persistée

        // Vérification de la présence des entités avant la suppression
        Long serviceCountBefore = entityManager.createQuery("SELECT COUNT(w) FROM WorkerServerServiceModel w", Long.class)
                .getSingleResult();
        Long jobCountBefore = entityManager.createQuery("SELECT COUNT(w) FROM WorkerServerJobModel w", Long.class)
                .getSingleResult();
        Long offerCountBefore = entityManager.createQuery("SELECT COUNT(o) FROM OffersModel o", Long.class)
                .getSingleResult();

        // Appel de la méthode de suppression
        offerService.deleteOffer(offerId);

        // Vérification de la suppression des entités
        Long serviceCountAfter = entityManager.createQuery("SELECT COUNT(w) FROM WorkerServerServiceModel w", Long.class)
                .getSingleResult();
        Long jobCountAfter = entityManager.createQuery("SELECT COUNT(w) FROM WorkerServerJobModel w", Long.class)
                .getSingleResult();
        Long offerCountAfter = entityManager.createQuery("SELECT COUNT(o) FROM OffersModel o", Long.class)
                .getSingleResult();

        // Assertions : les entités devraient avoir disparu
        assertEquals(serviceCountBefore, serviceCountAfter);
        assertEquals(jobCountBefore, jobCountAfter);
        assertEquals(offerCountBefore - 1, offerCountAfter);
    }

}

