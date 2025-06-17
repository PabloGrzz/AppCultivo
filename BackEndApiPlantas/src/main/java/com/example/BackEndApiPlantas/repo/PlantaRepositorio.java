package com.example.BackEndApiPlantas.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.Planta;


@Repository
public interface PlantaRepositorio extends JpaRepository<Planta, Long>{

	//creo q tengo q poner esta aqui porque los demas los tiene la clase JpaRepository por defecto ;D
	List<Planta> findByNombreContainingIgnoreCase(String nombre);
	
	Planta findByNombre(String nombre);

}
