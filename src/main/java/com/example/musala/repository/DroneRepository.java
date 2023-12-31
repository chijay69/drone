package com.example.musala.repository;

import com.example.musala.data.model.Drone;
import com.example.musala.data.enums.DroneState;
import com.example.musala.data.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {

    List<Drone> findByStateIn(List<DroneState> states);
}
