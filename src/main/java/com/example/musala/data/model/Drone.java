package com.example.musala.data.model;

import com.example.musala.data.enums.DroneState;
import com.example.musala.data.enums.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class Drone {
    @Id
    @NotNull
    @Size(max = 100, message = "Serial number must be at most 100 characters")
    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private Model model;

    @NotNull
    @Column(name = "weight_limit")
    private double weightLimit;

    @Column(name = "battery_capacity")
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;

    // Relationships
    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private List<Medication> loadedMedications;

}
