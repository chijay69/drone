package com.example.musala.repository;

import com.example.musala.data.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, String> {
    List<Medication> findMedicationsByCodeIn(List<String> medicationCodes);
}
