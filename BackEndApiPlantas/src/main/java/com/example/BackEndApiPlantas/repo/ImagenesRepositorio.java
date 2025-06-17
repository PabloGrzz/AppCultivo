package com.example.BackEndApiPlantas.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BackEndApiPlantas.model.Imagenes;
import com.example.BackEndApiPlantas.model.Imagenes.TipoImagen;



public interface ImagenesRepositorio extends JpaRepository<Imagenes, Long>{

	List<Imagenes> findByPlantaIdAndTipo(Long plantaId, TipoImagen general);

	List<Imagenes> findByUsuarioIdAndPlantaIdAndTipo(Long usuarioId, Long plantaId, TipoImagen personal);

}
