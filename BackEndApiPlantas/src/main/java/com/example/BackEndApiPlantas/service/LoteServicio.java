package com.example.BackEndApiPlantas.service;

import java.util.List;

import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.ProcesoConFecha;


public interface LoteServicio {
	
	//falta upgrade de los procesos
	
	Lote findById(Long id);
	
	Lote createLote(Lote lote);
	
	String deleteLote(Long id);
	
	Lote saveLote(Lote lote);
	
	Lote crearLoteID(Long plantaId, List<ProcesoConFecha> procesos);

}
