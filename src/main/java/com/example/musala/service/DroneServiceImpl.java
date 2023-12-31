package com.example.musala.service;

import com.example.musala.converter.DroneConverter;
import com.example.musala.converter.MedicationConverter;
import com.example.musala.data.dto.DroneRequestDTO;
import com.example.musala.data.dto.MedicationRequestDTO;
import com.example.musala.data.enums.DroneState;
import com.example.musala.data.model.Drone;
import com.example.musala.data.model.Medication;
import com.example.musala.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.CannotProceedException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.musala.data.dto.DroneRequestDTO.generateSSN;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationService medicationService;

    private <T> double calculateTotalWeight(List<T> medications) {
        return medications.stream()
                .mapToDouble(obj -> {
                    if (obj instanceof MedicationRequestDTO) {
                        return ((MedicationRequestDTO) obj).getWeight();
                    } else if (obj instanceof Medication) {
                        return ((Medication) obj).getWeight();
                    } else {
                        // If the object type is not supported, return 0
                        return 0.0;
                    }
                })
                .sum();
    }

    private Drone prepareAndSaveDrone(DroneRequestDTO droneRequest) {
        Drone newDrone = prepareDrone(droneRequest);
        newDrone.setState(DroneState.IDLE);
        return droneRepository.save(newDrone);
    }

    private Drone prepareDrone(DroneRequestDTO droneRequest) {
        droneRequest.setSerialNumber(generateSSN());
        droneRequest.setState(DroneState.IDLE);
        droneRequest.setWeightLimit();
        return DroneConverter.convertToEntity(droneRequest);
    }

    private Medication prepareAndSaveMedication(MedicationRequestDTO medicationRequestDTO, Drone drone) {
        Medication medication = MedicationConverter.convertToEntity(medicationRequestDTO);
        medication.setCode();
        medication.setDrone(drone);
        medicationService.saveMedication(medication);
        return medication;
    }

    private boolean validateDroneCapacity(Drone drone, List<MedicationRequestDTO> medicationsDTO) {
        double totalLoadedWeight = calculateTotalWeight(drone.getLoadedMedications());
        double totalNewWeight = calculateTotalWeight(medicationsDTO);
        double totalMedicationWeight = totalLoadedWeight + totalNewWeight;


        if (totalMedicationWeight > 0 && totalMedicationWeight <= 500) {
            return drone.getWeightLimit() > totalMedicationWeight;
        } else {
            throw new RuntimeException("Invalid medication weight: exceeds max Drone Weight - 500g");
        }
    }

    @Override
    public Drone registerDrone(DroneRequestDTO droneRequest) {
        List<Medication> medications = findMedicationsByCodes(droneRequest.getMedicationIds());

        if (medications.isEmpty()) {
            return prepareAndSaveDrone(droneRequest);
        } else {
            throw new RuntimeException("Medications cannot be added during drone initialization");
        }
    }

    @Override
    public boolean loadMedicationItems(String serialNumber, List<MedicationRequestDTO> medicationsDTO) {
        try {
            Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);
            if (optionalDrone.isEmpty()) {
                return false;
            }

            Drone drone = optionalDrone.get();
            drone.setState(DroneState.LOADING);

            if (!validateDroneCapacity(drone, medicationsDTO)) {
                return false;
            }

            int batteryCapacity = drone.getBatteryCapacity();
            if (batteryCapacity < 25) {
                throw new CannotProceedException("Drone's battery level is too low for delivery");
            } else if (batteryCapacity > 25 && batteryCapacity <= 100) {
                try {
                    for (MedicationRequestDTO medicationDTO : medicationsDTO) {
                        prepareAndSaveMedication(medicationDTO, drone);
                    }
                    drone.setState(DroneState.LOADED); // Update DroneState here
                    droneRepository.save(drone); // Save the updated drone state
                    return true;
                } catch (Exception e) {
                    throw new UnsupportedOperationException("Cannot add medication to Drone");
                }
            } else {
                throw new RuntimeException("Irregular Battery capacity");
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Medication> getLoadedMedicationsForDrone(String serialNumber) {
        try {
            Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);

            if (optionalDrone.isPresent()) {
                // Drone found
                Drone drone = optionalDrone.get();
                // Retrieve loaded medications for the given drone from the repository
                List<Medication> loadedMedications = drone.getLoadedMedications();
                return Collections.unmodifiableList(loadedMedications);
            }

            // Drone not found, return null or throw an exception to handle this case appropriately
            return Collections.emptyList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<DroneRequestDTO> getAvailableDronesForLoading() {
        try {
            List<Drone> drones = droneRepository.findByStateIn(Arrays.asList(DroneState.IDLE, DroneState.RETURNING));
            return drones.stream()
                    .map(DroneConverter::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int checkDroneBatteryLevel(String serialNumber) {
        try {
            Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);

            if (optionalDrone.isPresent()) {
                // Drone found
                Drone drone = optionalDrone.get();
                return drone.getBatteryCapacity();
            }

            throw new NoSuchElementException("Drone not found"); // Drone not found

        } catch (Exception e) {
            return  -1;
        }
    }

    @Override
    public List<Medication> findMedicationsByCodes(List<String> medicationCodes) {
        return medicationService.findMedicationsByCodeIn(medicationCodes);
    }
}
