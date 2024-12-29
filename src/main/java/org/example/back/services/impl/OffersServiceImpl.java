package org.example.back.services.impl;

import jakarta.persistence.EntityManager;
import lombok.Data;
import org.example.back.annotations.CurrentUser;
import org.example.back.models.*;
import org.example.back.models.dto.OffersDTO;
import org.example.back.models.enums.UserType;
import org.example.back.models.mapper.OffersMapper;
import org.example.back.repository.*;
import org.example.back.services.OffersService;
import org.example.back.services.exception.JobLevelOutOfRangeException;
import org.example.back.services.exception.OfferAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class OffersServiceImpl implements OffersService {

    private final OffersRepository offersRepository;

    private final OffersMapper offersMapper; // A mapper to convert data between Offers entities and Data Transfer Objects (DTOs).

    private final WorkerServerRepository workerServerRepository;

    private final WorkerServerJobRepository workerServerJobRepository;

    private final WorkerServerServiceRepository workerServerServiceRepository;

    private final JobsRepository jobsRepository;

    private final ServicesRepository servicesRepository;

    private final ServersRepository serversRepository;

    private final UserRepository userRepository;

    private final WorkersRepository workersRepository;

    private final EntityManager entityManager;


    public OffersServiceImpl(EntityManager entityManager, OffersRepository offersRepository, OffersMapper offersMapper, WorkerServerRepository workerServerRepository, WorkerServerJobRepository workerServerJobRepository, WorkerServerServiceRepository workerServerServiceRepository, JobsRepository jobsRepository, ServicesRepository servicesRepository, ServersRepository serversRepository, UserRepository userRepository, WorkersRepository workersRepository) {
        this.offersRepository = offersRepository;
        this.offersMapper = offersMapper;
        this.workerServerRepository = workerServerRepository;
        this.workerServerJobRepository = workerServerJobRepository;
        this.workerServerServiceRepository = workerServerServiceRepository;
        this.jobsRepository = jobsRepository;
        this.servicesRepository = servicesRepository;
        this.serversRepository = serversRepository;
        this.userRepository = userRepository;
        this.workersRepository = workersRepository;
        this.entityManager = entityManager;
    }


    @Override
    public List<OffersDTO> getAllOffers() {
        // Retrieves all Offers entities from the database, maps them to DTOs, and collects them into a list.
        return offersRepository.findAll()  // Finds all offers in the repository (database).
                .stream()  // Converts the list of offers into a stream to perform operations on each item.
                .map(offersMapper::toDto)  // Maps each Offers entity to its corresponding OffersDTO using the offersMapper.
                .collect(Collectors.toList());  // Collects the mapped OffersDTO objects into a list and returns it.
    }

    @Override
    @Transactional
    public OffersModel createOffer(OffersDTO offerDTO, @CurrentUser UserModel currentUser) {

        // bridé nombre d'offres par utilisateur !!!!!

        // Check if an offer already exists for the job
        List<WorkerServerJobModel> existingOffers = null;

        // Check if the offerDTO contains a WorkerServerJob
        if (offerDTO.getWorkerServerJob() != null) {
            // Retrieve existing offers by job ID and worker ID from the repository
            existingOffers = workerServerJobRepository.findByJobIdAndWorkerId(
                    offerDTO.getWorkerServerJob().getJobId(),
                    currentUser.getWorkers().getId()
            );

            // Get the level of the job from the offerDTO
            var level = offerDTO.getWorkerServerJob().getLevel();

            // Validate that the job level is within the acceptable range (1 to 200)
            if(level < 1 || level > 200) {
                // Throw an exception if the level is out of bounds
                throw new JobLevelOutOfRangeException("Le niveau du métier doit être compris entre 0 et 200");
            }
        }
        // Check if there are existing offers for the same job
        if (existingOffers != null && !existingOffers.isEmpty()) {
            // Throw an exception if an offer already exists for this job
            throw new OfferAlreadyExistsException("L'offre existe déjà pour ce métier");
        }

        // Update userType to WORKER and save to WorkerModel if not already done
        if (currentUser.getUserType() != UserType.WORKERS) {
            currentUser.setUserType(UserType.WORKERS); // Set the user type to WORKERS
            userRepository.save(currentUser); // Save the updated user to the database

            WorkersModel worker = new WorkersModel();
            worker.setUser(currentUser); // Associate the user with type WORKERS
            workersRepository.save(worker);  // Save the WorkersModel to the database

            ServersModel server = serversRepository.findById(offerDTO.getServer().getServerId())
                    .orElseThrow(() -> new RuntimeException("Server not found"));


            WorkerServerModel newWorkerServer = new WorkerServerModel();
            newWorkerServer.setWorker(worker); // Associate the worker
            newWorkerServer.setServer(server); // Associate the selected server
            workerServerRepository.save(newWorkerServer); // Save the new WorkerServer


            OffersModel offer = new OffersModel();
            offer.setTitleOffer(offerDTO.getTitleOffer());
            offer.setDescription(offerDTO.getDescription());
            offer.setPrice(offerDTO.getPrice());
            offer.setPseudoInGame(offerDTO.getPseudoInGame());
            offer.setOfferHidden(false);
            offer.setWorkerServer(newWorkerServer); // Link the WorkerServer to the offer

            // Declare variables for WorkerServerJob and WorkerServerService
            WorkerServerJobModel workerServerJob = null;
            WorkerServerServiceModel workerServerService = null;

            // Check if job data is present in the DTO
            if (offerDTO.getWorkerServerJob() != null) {
                workerServerJob = new WorkerServerJobModel();
                workerServerJob.setLevel(offerDTO.getWorkerServerJob().getLevel()); // Set the job level
                workerServerJob.setJob(jobsRepository.findById(offerDTO.getWorkerServerJob().getJobId())
                        .orElseThrow(() -> new RuntimeException("Job not found"))); // Link the job

                workerServerJob.setWorkerServer(newWorkerServer); // Associate the job with the WorkerServer
                workerServerJob.setWorker(worker); // Associate the job with the worker
                // Save the WorkerServerJob to the database
                workerServerJob = workerServerJobRepository.save(workerServerJob);
                offer.setWorkerServerJob(workerServerJob); // Link the WorkerServerJob to the offer
            }

            // Check if service data is present in the DTO
            if (offerDTO.getWorkerServerService() != null) {

                // Retrieve the service from the database
                ServicesModel service = servicesRepository.findById(offerDTO.getWorkerServerService().getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found"));

                workerServerService = new WorkerServerServiceModel();
                workerServerService.setService(service); // Set the service for WorkerServerServiceModel

                // Check if the service name is "Métier"
                if ("Métier".equalsIgnoreCase(service.getName())) {
                    // Set the job if the service is "Métier"
                    workerServerService.setJob(jobsRepository.findById(offerDTO.getWorkerServerService().getJobId())
                            .orElseThrow(() -> new RuntimeException("Job not found")));
                } else {
                    workerServerService.setJob(null); // Otherwise, set job to null
                }

                workerServerService.setWorkerServer(newWorkerServer); // Associate the WorkerServer with the service

                // Save the WorkerServerService to the database
                workerServerService = workerServerServiceRepository.save(workerServerService);
                offer.setWorkerServerService(workerServerService); // Link the WorkerServerService to the offer
            }

            return offersRepository.save(offer);

        } else {
            // Retrieve the worker associated with the current user from the database
            WorkersModel worker = workersRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Worker not found for the user"));

            // Retrieve the worker-server relationship based on worker ID and server ID from the offerDTO
            WorkerServerModel workerServer = workerServerRepository.findByWorkerIdAndServerId(worker.getId(), offerDTO.getServer().getServerId())
                    .orElseGet(() -> {
                        // If the worker-server relationship is not found, create a new one
                        WorkerServerModel newWorkerServer = new WorkerServerModel();
                        newWorkerServer.setWorker(worker); // Associate the worker
                        newWorkerServer.setServer(serversRepository.findById(offerDTO.getServer().getServerId())
                                .orElseThrow(() -> new RuntimeException("Server not found"))); // Associate the selected server
                        workerServerRepository.save(newWorkerServer); // Save the new WorkerServer
                        return newWorkerServer; // Return the newly created WorkerServer
                    });


            // Create a new offer and populate it with data from the offerDTO
            OffersModel offer = new OffersModel();
            offer.setTitleOffer(offerDTO.getTitleOffer());
            offer.setDescription(offerDTO.getDescription());
            offer.setPrice(offerDTO.getPrice());
            offer.setPseudoInGame(offerDTO.getPseudoInGame());
            offer.setOfferHidden(false);
            offer.setWorkerServer(workerServer);  // Link the offer to the worker-server relationship

            WorkerServerJobModel workerServerJob;
            WorkerServerServiceModel workerServerService;

            // If the offerDTO contains job information, create and save a WorkerServerJob
            if (offerDTO.getWorkerServerJob() != null) {
                workerServerJob = new WorkerServerJobModel();
                workerServerJob.setLevel(offerDTO.getWorkerServerJob().getLevel());
                // Find the job by ID and throw an error if not found
                workerServerJob.setJob(jobsRepository.findById(offerDTO.getWorkerServerJob().getJobId())
                        .orElseThrow(() -> new RuntimeException("Job not found")));
                workerServerJob.setWorkerServer(workerServer);  // Associate job with worker-server
                workerServerJob.setWorker(worker);  // Link job to worker
                workerServerJob = workerServerJobRepository.save(workerServerJob);  // Save the job
                offer.setWorkerServerJob(workerServerJob);  // Attach the job to the offer
            }

            // If the offerDTO contains service information, create and save a WorkerServerService
            if (offerDTO.getWorkerServerService() != null) {
                // Find the service by ID and throw an error if not found
                ServicesModel service = servicesRepository.findById(offerDTO.getWorkerServerService().getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found"));

                workerServerService = new WorkerServerServiceModel();
                workerServerService.setService(service);

                // If the service name is "Métier", associate a job with the service
                if ("Métier".equalsIgnoreCase(service.getName())) {
                    workerServerService.setJob(jobsRepository.findById(offerDTO.getWorkerServerService().getJobId())
                            .orElseThrow(() -> new RuntimeException("Job not found")));
                } else {
                    workerServerService.setJob(null);  // No job association for other services
                }

                workerServerService.setWorkerServer(workerServer);  // Link service to worker-server
                workerServerService = workerServerServiceRepository.save(workerServerService);  // Save the service
                offer.setWorkerServerService(workerServerService);  // Attach the service to the offer
            }

            // Save and return the new offer to the database
            return offersRepository.save(offer);
        }

    }


    @Override
    @Transactional
    public OffersModel updateOffer(Long offerId, OffersDTO offerDTO, @CurrentUser UserModel currentUser) {
        // Validate job level from the DTO
        validateJobLevel(offerDTO);

        // Retrieve the WorkerServer object by its ID from the DTO
        WorkerServerModel workerServer = workerServerRepository.findById(offerDTO.getServer().getId())
                .orElseThrow(() -> new RuntimeException("WorkerServer not found"));

        // Retrieve the existing offer to update by its offerId
        OffersModel existingOffer = offersRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // Check if the current user is authorized to update the offer (they must be the owner of the worker)
        if (!existingOffer.getWorkerServer().getWorker().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this offer");
        }

        // Update the offer details with data from the DTO
        existingOffer.setTitleOffer(offerDTO.getTitleOffer());
        existingOffer.setDescription(offerDTO.getDescription());
        existingOffer.setPrice(offerDTO.getPrice());
        existingOffer.setPseudoInGame(offerDTO.getPseudoInGame());
        existingOffer.setOfferHidden(false);

        // If a new server is provided, update the worker-server relationship
        if(offerDTO.getServer() != null) {
            workerServer.setServer(serversRepository.findById(offerDTO.getServer().getServerId())
                    .orElseThrow(() -> new RuntimeException("WorkerServer not found")));
            workerServerRepository.save(workerServer);  // Save the updated worker-server
            existingOffer.setWorkerServer(workerServer);  // Link it to the offer
        }

        // If a job is provided in the DTO, update the job associated with the worker-server
        if (offerDTO.getWorkerServerJob() != null) {
            Optional<WorkerServerJobModel> optionalWorkerServerJob = workerServerJobRepository
                    .findByWorkerServerIdAndJobId(workerServer.getId(), offerDTO.getWorkerServerJob().getJobId());

            WorkerServerJobModel workerServerJob;
            if (optionalWorkerServerJob.isEmpty()) {
                // If no job exists, create a new one
                workerServerJob = createWorkerServerJob(offerDTO, workerServer.getWorker(), workerServer);
            } else {
                // If a job exists, update it
                workerServerJob = optionalWorkerServerJob.get();
                workerServerJob.setLevel(offerDTO.getWorkerServerJob().getLevel());
                workerServerJob.setJob(jobsRepository.findById(offerDTO.getWorkerServerJob().getJobId())
                        .orElseThrow(() -> new RuntimeException("Job not found")));
                if (!workerServer.equals(workerServerJob.getWorkerServer())) {
                    workerServerJob.setWorkerServer(workerServer);  // Update worker-server relationship if changed
                }
            }

            // Save and link the job to the offer
            workerServerJob = workerServerJobRepository.save(workerServerJob);
            existingOffer.setWorkerServerJob(workerServerJob);
        }

        // If a service is provided, update the worker-server service
        if (offerDTO.getWorkerServerService() != null) {
            // Retrieve the worker-server service associated with the offer
            WorkerServerServiceModel workerServerService = workerServerServiceRepository
                    .findById(existingOffer.getWorkerServerService().getId())
                    .orElseThrow(() -> new RuntimeException("WorkerServerService not found"));

            // Get the new service ID and find the corresponding service
            Long newServiceId = offerDTO.getWorkerServerService().getServiceId();
            ServicesModel newService = servicesRepository.findById(newServiceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            // Update the worker-server service with the new service
            workerServerService.setService(newService);

            // If the service is of type "Métier", associate a job with it
            if ("Métier".equalsIgnoreCase(newService.getName())) {
                workerServerService.setJob(jobsRepository.findById(offerDTO.getWorkerServerService().getJobId())
                        .orElseThrow(() -> new RuntimeException("Job not found")));
            } else {
                workerServerService.setJob(null);  // No job association if the service is not "Métier"
            }

            // Save the updated service and link it to the offer
            workerServerServiceRepository.save(workerServerService);
            existingOffer.setWorkerServerService(workerServerService);
        }

        // Save and return the updated offer
        return offersRepository.save(existingOffer);
    }




    @Override
    public OffersDTO getOfferById(Long id) {
        OffersModel offer = offersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));
        return offersMapper.toDto(offer);
    }

    @Transactional
    public void deleteOffer(Long offerId) {
        // Retrieve the WorkerServerService ID
        List<Long> serviceIds = entityManager.createQuery(
                        "SELECT o.workerServerService.id FROM OffersModel o WHERE o.id = :offerId", Long.class)
                .setParameter("offerId", offerId)
                .getResultList(); // Use getResultList() instead of getSingleResult() to avoid exceptions if no results are found.

        if (!serviceIds.isEmpty()) {
            Long serviceId = serviceIds.get(0); // Retrieve the first element of the list if available
            entityManager.createQuery("DELETE FROM WorkerServerServiceModel w WHERE w.id = :workerServerServiceId")
                    .setParameter("workerServerServiceId", serviceId)
                    .executeUpdate();
        }

        // Retrieve the WorkerServerJob ID
        List<Long> jobIds = entityManager.createQuery(
                        "SELECT o.workerServerJob.id FROM OffersModel o WHERE o.id = :offerId", Long.class)
                .setParameter("offerId", offerId)
                .getResultList(); // Use getResultList() to handle cases with no results gracefully.

        if (!jobIds.isEmpty()) {
            Long jobId = jobIds.get(0); // Retrieve the first element of the list if available
            entityManager.createQuery("DELETE FROM WorkerServerJobModel w WHERE w.id = :workerServerJobId")
                    .setParameter("workerServerJobId", jobId)
                    .executeUpdate();
        }

        // Delete the offer itself
        entityManager.createQuery("DELETE FROM OffersModel o WHERE o.id = :offerId")
                .setParameter("offerId", offerId)
                .executeUpdate();
    }


    // ---- Private Methode ----

    private void validateJobLevel(OffersDTO offerDTO) {
        if (offerDTO.getWorkerServerJob() != null) {
            int level = offerDTO.getWorkerServerJob().getLevel();
            if (level < 1 || level > 200) {
                throw new JobLevelOutOfRangeException("Le niveau du métier doit être compris entre 1 et 200");
            }
        }
    }

    private WorkerServerJobModel createWorkerServerJob(OffersDTO offerDTO, WorkersModel worker, WorkerServerModel workerServer) {
        WorkerServerJobModel workerServerJob = new WorkerServerJobModel();

        // Set the job level from the DTO to the new WorkerServerJobModel
        workerServerJob.setLevel(offerDTO.getWorkerServerJob().getLevel());

        // Find the job by ID from the repository. If not found, throw an exception.
        workerServerJob.setJob(jobsRepository.findById(offerDTO.getWorkerServerJob().getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found")));

        // Associate the job with the corresponding WorkerServer and Worker entities
        workerServerJob.setWorkerServer(workerServer);
        workerServerJob.setWorker(worker);

        // Persist the new job entity in the database and return the saved instance
        return workerServerJobRepository.save(workerServerJob);
    }
}
