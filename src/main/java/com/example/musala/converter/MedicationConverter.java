package com.example.musala.converter;

import com.example.musala.data.model.Medication;
import com.example.musala.data.dto.MedicationRequestDTO;
import org.modelmapper.ModelMapper;

public class MedicationConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static MedicationRequestDTO convertToDTO(Medication medication) {
        return modelMapper.map(medication, MedicationRequestDTO.class);
    }

    public static Medication convertToEntity(MedicationRequestDTO medicationRequestDTO) {
        return modelMapper.map(medicationRequestDTO, Medication.class);
    }
}
