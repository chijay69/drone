package com.example.musala.data.dto;

import com.example.musala.data.model.Medication;
import com.example.musala.data.enums.DroneState;
import com.example.musala.data.enums.Model;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@RequiredArgsConstructor

public class DroneRequestDTO {
    private String serialNumber;
    private Model model;
    private double weightLimit;
    private int batteryCapacity;
    private DroneState state;
    private List<Medication> loadedMedications;

    public static String generateSSN() {
        int minNumber = 10;
        int maxNumber = 100;

        String uuid =  UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        return uuid.substring(minNumber, Math.min(maxNumber, uuid.length()));
    }

    public void setModelWeight(Double totalWeightOfMedications) {

        if (totalWeightOfMedications >= 0.0 && totalWeightOfMedications <= 100.0) {
            this.setModel(Model.Lightweight);
            this.setWeightLimit(weightLimit = 100.0);
        } else if (totalWeightOfMedications <= 200.0) {
            this.setModel(Model.Middleweight);
            this.setWeightLimit(weightLimit = 200.0);
        } else if (totalWeightOfMedications <= 300.0) {
            this.setModel(Model.Cruiserweight);
            this.setWeightLimit(weightLimit = 300.0);
        } else if (totalWeightOfMedications <= 500.0) {
            this.setModel(Model.Heavyweight);
            this.setWeightLimit(weightLimit = 500.0);
        } else {
            throw new IllegalArgumentException("Weight spectrum exceeded: max weight is 500g.");
        }
    }

    public void setWeightLimit() {
        switch (this.model) {
            case Lightweight:
                this.weightLimit = 100.0;
                break;
            case Middleweight:
                this.weightLimit = 200.0;
                break;
            case Cruiserweight:
                this.weightLimit = 300.0;
                break;
            case Heavyweight:
                this.weightLimit = 500.0;
                break;
            default:
                throw new IllegalStateException("Drone model state not recognized");
        }
    }

    public void setState() {
        this.state = DroneState.IDLE;
    }

    private int calculateBatteryPercentage() {
        // Assuming the maximum battery capacity is 100 units
        return (batteryCapacity * 100) / 100; // Formula to convert units to percentage
    }
//
//    public List<String> getMedicationIds() {
//        return loadedMedications.stream()
//                .map(Medication::getCode)
//                .collect(Collectors.toList());
//    }
}
