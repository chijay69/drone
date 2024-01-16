//package com.example.junit;
//
//import com.example.musala.data.dto.DroneRequestDTO;
//import com.example.musala.data.enums.DroneState;
//import com.example.musala.data.model.Drone;
//import com.example.musala.service.DroneService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class MusalaAppliationServiceImplTests {
//    @Autowired
//    private DroneService droneService;
//
//    @Test
//    void registerDrone() {
//        DroneRequestDTO requestDTO = new DroneRequestDTO();
//        requestDTO.setLoadedMedications();
//        Drone registeredDrone = droneService.registerDrone(drone);
//        assertNotNull(registeredDrone);
//        assertEquals(DroneState.IDLE, registeredDrone.getState());
//    }
//
//    @Test
//    void loadMedicationItems() {
//    }
//
//    @Test
//    void getLoadedMedicationsForDrone() {
//    }
//
//    @Test
//    void getAvailableDronesForLoading() {
//    }
//
//    @Test
//    void checkDroneBatteryLevel() {
//    }
//}