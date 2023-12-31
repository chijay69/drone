package com.example.musala.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Medication {
    @Id
    @Pattern(regexp = "^[A-Z0-9_]*$", message = "Serial number must contain only uppercase letters, underscore, and numbers")
    private String code;
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Name must contain only letters, numbers, '-', and '_'")
    private String name;
    private double weight;
    private byte[] image;
    // Relationship
    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone drone;

    public Medication(String name, double weight, byte[] image) {
        this.name = name;
        this.weight = weight;
        this.image = image;
        this.code = generateCode();
    }

    private static String generateCode() {
        final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
        final SecureRandom secureRandom = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int randomIndex = secureRandom.nextInt(ALLOWED_CHARACTERS.length());
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }


    public String getCode() {
        return code;
    }

    public void setCode() {
        this.code = generateCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

}
