package com.example.musala.data.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationRequestDTO {
    private String name;
    private double weight;
    private byte[] image;
}
