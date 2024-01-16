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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
        List<Medication> medications = new ArrayList<>(3);
        for (int i = 0; i <2; i++) {
            medications.add(MedicationConverter.convertToEntity(createMedicationDTO(2)));
        }
        droneRequestDTO.setLoadedMedications(medications); //add medication list to droneDTO
        // Mock behavior of the repository or any other necessary mock setup
        assertThrows(RuntimeException.class, () -> droneService.registerDrone(droneRequestDTO));
    }

    // Test cases for loadMedicationItems method
    @Test
    void testRegisterDrone_EmptyMedicationsList_AssertMessage() {
        // Arrange
        DroneRequestDTO droneRequestDTO = createDroneRequestDTO();
        List<Medication> medications = new ArrayList<>(3);
        for (int i = 0; i < 2; i++) {
            medications.add(MedicationConverter.convertToEntity(createMedicationDTO(2)));
        }
        droneRequestDTO.setLoadedMedications(medications); // add medication list to droneDTO

        Drone drone = DroneConverter.convertToEntity(createDroneRequestDTO());
        // Mock behavior of the repository or any other necessary mock setup
        // when(droneRepository.save(any(Drone.class))).thenReturn(drone);
        // Use lenient strictness
        Mockito.lenient().when(droneRepository.save(any(Drone.class))).thenReturn(drone);

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> droneService.registerDrone(droneRequestDTO));

        // Assert the exception message
        assertEquals("Medications cannot be added during drone initialization", exception.getMessage());
    }

    @Test
    void testLoadMedicationItems_DroneNotFound() {
        // Arrange
        String serialNumber = "123456";
        Optional<Drone> optionalDrone = Optional.empty();

        // Stubbing repository behavior
        when(droneRepository.findById(serialNumber)).thenReturn(optionalDrone);

        // Act
        boolean isLoaded = droneService.loadMedicationItems(serialNumber, List.of());

        // Assert
        assertFalse(isLoaded);
    }

}
