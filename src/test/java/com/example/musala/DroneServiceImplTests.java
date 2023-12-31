package com.example.musala;

import com.example.musala.converter.DroneConverter;
import com.example.musala.converter.MedicationConverter;
import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.dto.MedicationRequestDTO;
import com.example.musala.data.enums.DroneState;
import com.example.musala.data.enums.Model;
import com.example.musala.data.model.Drone;
import com.example.musala.data.model.Medication;
import com.example.musala.repository.DroneRepository;
import com.example.musala.service.DroneServiceImpl;
import com.example.musala.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceImplTests {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private DroneServiceImpl droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public DroneRequestDTO createDroneRequestDTO() {
        DroneRequestDTO requestDTO = new DroneRequestDTO();
        requestDTO.setSerialNumber(DroneRequestDTO.generateSSN());
        requestDTO.setModel(Model.Middleweight); // Example weight limit
        requestDTO.setBatteryCapacity(80); // Example battery capacity
        requestDTO.setState(DroneState.IDLE); // Example state
        requestDTO.setLoadedMedications(new ArrayList<>()); // Empty list of loaded medications
        return requestDTO;
    }

    public MedicationRequestDTO createMedicationDTO (int item) {
        MedicationRequestDTO medicationRequestDTO = new MedicationRequestDTO();
        medicationRequestDTO.setWeight(10);
        medicationRequestDTO.setName("Med_"+ item);
        medicationRequestDTO.setImage(null);
        return medicationRequestDTO;
    }

    // Test cases for registerDrone method
    @Test
    void testRegisterDrone_EmptyMedicationsList() {
        // Arrange
        DroneRequestDTO droneRequestDTO = createDroneRequestDTO();

        // Mock behavior of the repository or any other necessary mock setup
        when(droneRepository.findById(anyString())).thenReturn(Optional.empty());
        when(droneRepository.save(any(Drone.class))).thenReturn(new Drone(/* provide a registered drone */));

        // Act
        Drone registeredDrone = droneService.registerDrone(droneRequestDTO);

        // Assert
        assertNotNull(registeredDrone);
        // Add more assertions based on the expected behavior when an empty list of medications is provided
    }

    // Test cases for loadMedicationItems method
    @Test
    void testLoadMedicationItems_ValidScenario() {
        // Arrange
        DroneRequestDTO droneRequestDTO = createDroneRequestDTO();
        // Register the drone properly
        Drone registeredDrone = droneRepository.save(DroneConverter.convertToEntity(droneRequestDTO));

        assertNotNull(registeredDrone); // Ensure the drone is registered properly
        String serialNumber = registeredDrone.getSerialNumber(); // Get the valid serial number

        List<MedicationRequestDTO> medicationsDTO = new ArrayList<>();
        // Add valid MedicationRequestDTO objects to medicationsDTO
        medicationsDTO.add(createMedicationDTO(1));
        medicationsDTO.add(createMedicationDTO(2));
        medicationsDTO.add(createMedicationDTO(3));
        int initialMedicationCount = medicationsDTO.size(); // Capture the initial count

        // Mock behavior to return the registered drone
        Optional<Drone> optionalDrone = Optional.of(registeredDrone);
        when(droneRepository.findById(serialNumber)).thenReturn(optionalDrone);

        // Mock behavior for medicationService.saveMedication method
        doNothing().when(medicationService).saveMedication(any(Medication.class));
        // Assuming medicationService.saveMedication is a void method accepting a Medication object

        // Act
        boolean isLoaded = droneService.loadMedicationItems(serialNumber, medicationsDTO);

        // Assert
        assertTrue(isLoaded);

        // For instance, verify that the saveMedication method is called exactly initialMedicationCount times
        // verify(medicationService, times(initialMedicationCount)).saveMedication(any(Medication.class));
    }

}
