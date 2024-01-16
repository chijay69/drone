package com.example.musala.utils;

import com.example.musala.data.model.Drone;
import com.example.musala.repository.DroneRepository;
import com.example.musala.service.DroneService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DroneBatteryLogger {

    private static final String LOG_FILE_PATH = "battery_log.txt";
    @Autowired
    DroneRepository droneRepository;
    @Autowired
    DroneService droneService;

    public DroneBatteryLogger(DroneRepository droneRepository, DroneService droneService) {
        this.droneRepository = droneRepository;
        this.droneService = droneService;
    }

    public void startLogging() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 30 minutes
        executor.scheduleAtFixedRate(this::checkBattery, 0, 60, TimeUnit.MINUTES);
    }

    private void checkBattery() {
        List<Drone> drones = droneRepository.findAll();

        if (drones.isEmpty()) {
            // Log a message indicating no drones are registered
            logToFile("No drones registered at " + LocalDateTime.now() + "\n");
            return; // Skip further processing
        }

        for (Drone drone : drones) {
            int batteryLevel = droneService.checkDroneBatteryLevel(drone.getSerialNumber());
            logToFile("Drone ID: " + drone.getSerialNumber() + " - Battery level: " + batteryLevel + "% at " + LocalDateTime.now() + "\n");
        }
    }

    private void logToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
