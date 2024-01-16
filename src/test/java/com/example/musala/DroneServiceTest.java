package com.example.musala;

import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.model.Medication;
import com.example.musala.repository.DroneRepository;
import com.example.musala.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DroneServiceTest {
    private DroneRepository droneRepository;
    DroneRequestDTO droneRequest;
    Medication medication;

    @BeforeEach
    void setMedication () {
        medication.setCode();
        medication.setName("Paracetamol");
        medication.setImage(null);
        medication.setWeight(400.0);
    }

    @BeforeEach
    void setDroneRequest () {
        droneRequest.setBatteryCapacity(30);
//        droneRequest.setModelWeight(400.0);
//        droneRequest.setSerialNumber();
//        droneRequest.setBatteryCapacity(30);
//        droneRequest.setBatteryCapacity(30);
//        droneRequest.setBatteryCapacity(30);
    }

    @Autowired
    private DroneService droneService;

//    @Test
//    public void registerDroneTest () {
//
//        droneService.registerDrone(droneRequest);
//        assertTrue(true);
//    }
}