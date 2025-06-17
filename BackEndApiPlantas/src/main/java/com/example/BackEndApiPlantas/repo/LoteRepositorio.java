package com.example.BackEndApiPlantas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.Planta;


@Repository
public interface LoteRepositorio extends JpaRepository<Lote, Long>{
	
	@Query("SELECT MAX(l.numeroLote) FROM Lote l WHERE l.planta.id = :plantaId")
    Integer findMaxNumeroLoteByPlantaId(@Param("plantaId") Long plantaId);

}
