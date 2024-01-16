package com.example.musala.service;

import com.example.musala.data.model.Medication;
import com.example.musala.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicationService {
    public void saveMedication(Medication medication);

    List<Medication> findMedicationsByCodeIn(List<String> medicationCodes);
}
