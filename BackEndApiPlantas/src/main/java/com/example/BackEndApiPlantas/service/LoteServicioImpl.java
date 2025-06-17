package com.example.BackEndApiPlantas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.Planta;
import com.example.BackEndApiPlantas.model.ProcesoConFecha;
import com.example.BackEndApiPlantas.repo.LoteRepositorio;
import com.example.BackEndApiPlantas.repo.PlantaRepositorio;

@Service
public class LoteServicioImpl implements LoteServicio {
	
    private static final Logger log = LoggerFactory.getLogger(LoteServicioImpl.class);
    
    @Autowired
    private LoteRepositorio loteRepositorio;
    
    @Autowired
    private PlantaRepositorio plantaRepository;
    
    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public Lote findById(Long id) {
        return loteRepositorio.findById(id).orElseThrow(() -> 
            new IllegalArgumentException("Lote no encontrado con ID: " + id));
    }

    public Lote createLote(Lote lote) {
        log.info("Iniciando creación de lote para planta ID: {}", lote.getPlanta().getId());
        
        // Validar y obtener la planta
        log.info("Buscando planta con ID: {}", lote.getPlanta().getId());
        Planta planta = plantaRepository.findById(lote.getPlanta().getId())
                .orElseThrow(() -> {
                    log.error("Planta no encontrada con ID: {}", lote.getPlanta().getId());
                    return new RuntimeException("Planta no encontrada");
                });
        
        log.info("Planta encontrada: {}", planta.getNombre());
        
        // Calcular próximo número de lote
        log.info("Calculando número de lote para planta ID: {}", planta.getId());
        Integer numeroLote = loteRepositorio.findMaxNumeroLoteByPlantaId(planta.getId());
        numeroLote = (numeroLote == null) ? 1 : numeroLote + 1;
        log.info("Número de lote asignado: {}", numeroLote);
        
        lote.setNumeroLote(numeroLote);
        lote.setPlanta(planta);
        
        log.info("Guardando lote en base de datos...");
        Lote loteGuardado = this.saveLote(lote);
        log.info("Lote guardado con ID temporal: {}", loteGuardado.getId());
        
        // Generar QR
        log.info("Generando código QR para lote ID: {}", loteGuardado.getId());
        String qrCodeData = String.valueOf(loteGuardado.getId());
        QRCodeService.QRCodeResult qrResult = qrCodeService.generarQRCode(qrCodeData);
        
        log.info("QR generado: {}", qrResult != null);
        loteGuardado.setQrCode(qrResult.base64());
        log.info("Actualizando lote con QR...");

        Lote loteActualizado = loteRepositorio.save(loteGuardado);
        log.info("Lote actualizado con QR: ID {}", loteActualizado.getId());

        return loteActualizado;
    }
    
    public Lote crearLoteID(Long plantaId, List<ProcesoConFecha> procesos) {
        Planta planta = plantaRepository.findById(plantaId)
                .orElseThrow(() -> new RuntimeException("Planta no encontrada"));

        // Calcular próximo número de lote
        Integer numeroLote = loteRepositorio.findMaxNumeroLoteByPlantaId(planta.getId());
        numeroLote = (numeroLote == null) ? 1 : numeroLote + 1;

        // Crear y guardar lote inicial
        Lote lote = new Lote(procesos, planta);
        lote.setNumeroLote(numeroLote);
        Lote loteGuardado = loteRepositorio.save(lote);

        // Generar QR
        String qrCodeData = "LOTE:" + loteGuardado.getId() + 
                           "|PLANTA:" + planta.getNombre() + 
                           "|NUM:" + loteGuardado.getNumeroLote();
        QRCodeService.QRCodeResult qrResult = qrCodeService.generarQRCode(qrCodeData);

        // Actualizar con QR y guardar
        loteGuardado.setQrCode(qrResult.base64());
        return loteRepositorio.save(loteGuardado);
    }

    public Lote crearLotePorNombre(String nombrePlanta, List<ProcesoConFecha> procesos) {
        Planta planta = plantaRepository.findByNombre(nombrePlanta);
        if (planta == null) {
            throw new RuntimeException("Planta no encontrada con nombre: " + nombrePlanta);
        }

        // Calcular próximo número de lote
        Integer numeroLote = loteRepositorio.findMaxNumeroLoteByPlantaId(planta.getId());
        numeroLote = (numeroLote == null) ? 1 : numeroLote + 1;

        // Crear y guardar lote inicial
        Lote lote = new Lote(procesos, planta);
        lote.setNumeroLote(numeroLote);
        Lote loteGuardado = loteRepositorio.save(lote);

        // Generar QR
        String qrCodeData = loteGuardado.getId() + ""; // convierto el id a String
        
        QRCodeService.QRCodeResult qrResult = qrCodeService.generarQRCode(qrCodeData);

        // Actualizar con QR y guardar
        loteGuardado.setQrCode(qrResult.base64());
        return loteRepositorio.save(loteGuardado);
    }

    @Override
    public String deleteLote(Long id) {
        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado con ID: " + id));
        loteRepositorio.delete(lote);
        return "Lote eliminado con éxito";
    }
    
    @Override
    public Lote saveLote(Lote lote) {
        return loteRepositorio.save(lote);
    }
}