package com.example.musala.service;

import com.example.musala.data.model.Medication;
import com.example.musala.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService{
    @Autowired
    MedicationRepository medicationRepository;
    @Override
    public void saveMedication(Medication medication) {
        medicationRepository.save(medication);
    }

    @Override
    public List<Medication> findMedicationsByCodeIn(List<String> medicationCodes) {
        return medicationRepository.findMedicationsByCodeIn(medicationCodes);
    }
}
