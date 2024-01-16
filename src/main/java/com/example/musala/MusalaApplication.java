package com.example.musala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EntityScan(basePackages = "com.example.musala.data.model")
public class MusalaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusalaApplication.class, args);
	}

}
