package com.example.BackEndApiPlantas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.BackEndApiPlantas.model.Planta;
import com.example.BackEndApiPlantas.repo.PlantaRepositorio;

@Service
public class PlantaServicioImpl implements PlantaServicio {
	
	@Autowired
	private PlantaRepositorio plantaRepositorio;
	
	@Override
	public List<Planta> fetchAllPlantas() {
		return (List<Planta>) plantaRepositorio.findAll();
	}
	
	@Override
	public Planta findById(Long id) {
		return plantaRepositorio.findById(id).orElseThrow(() -> 
                new IllegalArgumentException("Planta no encontrado con ID: " + id));
	}
	
	@Override
	public Planta createPlanta(Planta planta) {
	    // Asociar planta en cada lote
	    if (planta.getLotes() != null) {
	        planta.getLotes().forEach(lote -> lote.setPlanta(planta));
	    }
	    // Asociar planta en cada imagen
	    if (planta.getImagenes() != null) {
	        planta.getImagenes().forEach(img -> img.setPlanta(planta));
	    }
	    return plantaRepositorio.save(planta);
	}
	
	@Override
	public Planta updatePlanta(Planta planta) {
	    // Verificar que la planta existe
	    Planta plantaExistente = plantaRepositorio.findById(planta.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Planta no encontrada con ID: " + planta.getId()));
	    
	    // Actualizar los campos
	    plantaExistente.setNombre(planta.getNombre());
	    plantaExistente.setNombreCientifico(planta.getNombreCientifico());
	    plantaExistente.setFamilia(planta.getFamilia());
	    plantaExistente.setFrecuenciaRiego(planta.getFrecuenciaRiego());
	    plantaExistente.setTemperatura(planta.getTemperatura());
	    plantaExistente.setHumedad(planta.getHumedad());
	    plantaExistente.setSuelo(planta.getSuelo());
	    plantaExistente.setFertilizacion(planta.getFertilizacion());
	    plantaExistente.setBeneficios(planta.getBeneficios());
	    plantaExistente.setToxicidad(planta.getToxicidad());
	    plantaExistente.setPrecio(planta.getPrecio());
	    
	    // Manejo de relaciones (si es necesario)
	    if (planta.getLotes() != null) {
	        plantaExistente.setLotes(planta.getLotes());
	        plantaExistente.getLotes().forEach(lote -> lote.setPlanta(plantaExistente));
	    }
	    
	    if (planta.getImagenes() != null) {
	        plantaExistente.setImagenes(planta.getImagenes());
	        plantaExistente.getImagenes().forEach(img -> img.setPlanta(plantaExistente));
	    }
	    
	    if (planta.getUsuarios() != null) {
	        plantaExistente.setUsuarios(planta.getUsuarios());
	        plantaExistente.getUsuarios().forEach(user -> user.setPlanta(plantaExistente));
	    }
	    
	    return plantaRepositorio.save(plantaExistente);
	}
	
	@Override
	public String deletePlanta(Long id) {
		Planta p = plantaRepositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("La Planta con id : "+id+" no encontrada."));
		
		plantaRepositorio.delete(p);
		return "Planta eliminada con exito";
	}
	
	@Override
	public List<Planta> fetchPlantasPorNombre(String nombre) {
		return plantaRepositorio.findByNombreContainingIgnoreCase(nombre);
	}
	
	@Override
	public Planta findPlantaByName(String nombre) {
		return plantaRepositorio.findByNombre(nombre);
	}
}