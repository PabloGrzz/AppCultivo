package com.example.BackEndApiPlantas.service;

import java.util.List;

import com.example.BackEndApiPlantas.model.Planta;


public interface PlantaServicio {

	List<Planta> fetchAllPlantas();
	
	List<Planta> fetchPlantasPorNombre(String nombre);
	
	Planta findById(Long id);
	
	Planta createPlanta(Planta planta);
	
	Planta updatePlanta(Planta planta);
	
	String deletePlanta(Long id);

	Planta findPlantaByName(String nombre);
	
}
