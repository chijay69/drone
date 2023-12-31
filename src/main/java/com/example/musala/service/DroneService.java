package com.example.musala.service;

import com.example.musala.data.dto.MedicationRequestDTO;
import com.example.musala.data.model.Drone;
import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.model.Medication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DroneService {
    Drone registerDrone(DroneRequestDTO drone);
    boolean loadMedicationItems(String serialNumber, List<MedicationRequestDTO> medicationsDTO);
    List<DroneRequestDTO> getAvailableDronesForLoading();
    int checkDroneBatteryLevel(String serialNumber);
    List<Medication> getLoadedMedicationsForDrone(String serialNumber);

    List<Medication> findMedicationsByCodes(List<String> medicationCodes);

}
