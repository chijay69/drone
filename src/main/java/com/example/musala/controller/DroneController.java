package com.example.musala.controller;

import com.example.musala.converter.DroneConverter;
import com.example.musala.converter.MedicationConverter;
import com.example.musala.data.dto.MedicationRequestDTO;
import com.example.musala.data.model.Drone;
import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.model.Medication;
import com.example.musala.service.DroneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private final DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<String> registerDrone(@RequestBody DroneRequestDTO droneRequest) {
        // Validate the incoming drone registration request
        if (!validateDroneRequest(droneRequest)) {
            return ResponseEntity.badRequest().body("Invalid drone registration data");
        }
        try {
            // Register the drone using DroneService
            Drone registeredDrone = droneService.registerDrone(droneRequest);
            if (registeredDrone != null) {
                DroneRequestDTO requestDTO = DroneConverter.convertToDTO(registeredDrone);
                return ResponseEntity.ok("Drone registered successfully. Serial number: " + requestDTO.getSerialNumber());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register drone");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<String> loadMedicationItems(
            @PathVariable String serialNumber,
            @RequestBody List<MedicationRequestDTO> medicationsDTO) {
        boolean isLoaded = droneService.loadMedicationItems(serialNumber, medicationsDTO);
        if (isLoaded) {
            return ResponseEntity.ok("Medication items loaded successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load medication items");
        }
    }

    @GetMapping("/{serialNumber}/battery-level")
    public ResponseEntity<Integer> checkDroneBatteryLevel(@PathVariable String serialNumber) {
        int batteryLevel = droneService.checkDroneBatteryLevel(serialNumber);
        if (batteryLevel == -1) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(batteryLevel);
    }


    @GetMapping("/available-for-loading")
    public ResponseEntity<List<DroneRequestDTO>> getAvailableDronesForLoading() {
        List<DroneRequestDTO> availableDrones = droneService.getAvailableDronesForLoading();

        if (availableDrones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(availableDrones);
        }
    }

    @GetMapping("/{serialNumber}/loaded-medications")
    public ResponseEntity<?> getLoadedMedications(@PathVariable String serialNumber) {
        try {
            List<Medication> loadedMedications = droneService.getLoadedMedicationsForDrone(serialNumber);

            // Convert Medication list to MedicationDTO list
            List<MedicationRequestDTO> medicationDTOs = loadedMedications.stream()
                    .map(MedicationConverter::convertToDTO)
                    .collect(Collectors.toList());

            // Return the MedicationDTO list as JSON
            return ResponseEntity.ok().body(medicationDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving medications: " + e.getMessage());
        }
    }


    // Validate the incoming drone registration request
    private boolean validateDroneRequest(DroneRequestDTO droneRequest) {
        if (droneRequest == null) {
            return false; // Object is null, validation fails
        }

        // Check individual attributes for validity
        boolean validBatteryCapacity = isValidBatteryCapacity(droneRequest.getBatteryCapacity());
        boolean validWeightLimit = isValidWeightLimit(droneRequest.getWeightLimit());

        return validBatteryCapacity && validWeightLimit;
    }
    private boolean isValidBatteryCapacity(int batteryCapacity) {
        // Validate battery capacity range
        return batteryCapacity > 0 && batteryCapacity <= 100;
    }

    private boolean isValidWeightLimit(double weightLimit) {
        // Validate weight limit range
        return weightLimit>=0 && weightLimit <= 500;
    }

}


