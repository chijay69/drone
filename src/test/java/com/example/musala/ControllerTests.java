package com.example.musala;
import com.example.musala.controller.DroneController;
import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.enums.DroneState;
import com.example.musala.data.enums.Model;
import com.example.musala.data.model.Drone;
import com.example.musala.service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ControllerTests {

    @Mock
    private DroneService droneService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DroneController droneController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDrone_ValidData() throws Exception {
        DroneRequestDTO requestDTO = new DroneRequestDTO();
        requestDTO.setSerialNumber(DroneRequestDTO.generateSSN());
        requestDTO.setModel(Model.Middleweight); // Example weight limit
        requestDTO.setBatteryCapacity(80); // Example battery capacity
        requestDTO.setState(DroneState.IDLE); // Example state
        requestDTO.setLoadedMedications(new ArrayList<>()); // Empty list of loaded medications

        Drone registeredDrone = new Drone(/* provide registered drone */);
        when(droneService.registerDrone(any(DroneRequestDTO.class))).thenReturn(registeredDrone);

        when(objectMapper.writeValueAsString(any())).thenReturn("Serialized DTO");

        ResponseEntity<Object> responseEntity = droneController.registerDrone(requestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Serialized DTO", responseEntity.getBody());
    }

    @Test
    void testRegisterDrone_InvalidData() {
        DroneRequestDTO requestDTO = new DroneRequestDTO(/* provide invalid data */);

        ResponseEntity<Object> responseEntity = droneController.registerDrone(requestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void testGetAvailableDronesForLoading_NoDronesAvailable() {
        when(droneService.getAvailableDronesForLoading()).thenReturn(new ArrayList<>());

        ResponseEntity<Object> responseEntity = droneController.getAvailableDronesForLoading();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testGetAvailableDronesForLoading_DronesAvailable() {
        List<DroneRequestDTO> availableDrones = new ArrayList<>();
        availableDrones.add(new DroneRequestDTO(/* Provide drone data */));
        when(droneService.getAvailableDronesForLoading()).thenReturn(availableDrones);

        ResponseEntity<Object> responseEntity = droneController.getAvailableDronesForLoading();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}