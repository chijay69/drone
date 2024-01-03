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
import com.example.musala.service.DroneService;
import com.example.musala.service.DroneServiceImpl;
import com.example.musala.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTests {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationService medicationService;

    @Mock
    private DroneConverter droneConverter;
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
//        requestDTO.setLoadedMedications(new ArrayList<>()); // Empty list of loaded medications
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

        List<Medication> medications = new ArrayList<>(3);

        for (int i = 0; i <2; i++) {
            medications.add(MedicationConverter.convertToEntity(createMedicationDTO(2)));
        }

        System.out.println(medications);

        droneRequestDTO.setLoadedMedications(medications); //add medication list to droneDTO

        // Mock behavior of the repository or any other necessary mock setup
        assertThrows(RuntimeException.class, () -> droneService.registerDrone(droneRequestDTO));

    }

    // Test cases for loadMedicationItems method
    @Test
    void testLoadMedicationItems_ValidScenario() {
        // Arrange
        DroneRequestDTO droneRequestDTO = createDroneRequestDTO();
        // Register the drone properly
        Drone registeredDrone = droneService.registerDrone(droneRequestDTO);
        System.out.println(registeredDrone);

//        assertNotNull(registeredDrone); // Ensure the drone is registered properly
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
