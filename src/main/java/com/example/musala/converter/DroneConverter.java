package com.example.musala.converter;

import com.example.musala.data.model.Drone;
import com.example.musala.data.dto.DroneRequestDTO;
import org.modelmapper.ModelMapper;

public class DroneConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static DroneRequestDTO convertToDTO(Drone drone) {
        return modelMapper.map(drone, DroneRequestDTO.class);
    }

    public static Drone convertToEntity(DroneRequestDTO droneRequestDTO) {
        return modelMapper.map(droneRequestDTO, Drone.class);
    }
}
