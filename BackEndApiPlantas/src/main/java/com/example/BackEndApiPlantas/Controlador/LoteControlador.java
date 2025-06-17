package com.example.BackEndApiPlantas.Controlador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.Planta;
import com.example.BackEndApiPlantas.model.ProcesoConFecha;
import com.example.BackEndApiPlantas.service.LoteServicio;
import com.example.BackEndApiPlantas.service.QRCodeService;

@CrossOrigin(maxAge=3360) // las solicitudes CORS previas serán cacheadas durante 3360 segundos
@RestController
@RequestMapping("/api/v1/")
public class LoteControlador {
	
	@Autowired
	private LoteServicio loteServicio;
	
	@Autowired
    private QRCodeService qrCodeService;

	@GetMapping("lote/{id}")
	public ResponseEntity<Lote> getLoteById(@PathVariable("id") Long id){
		try {
			Lote lote = loteServicio.findById(id);
			return ResponseEntity.ok(lote);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@PostMapping("lote/crear")
	public ResponseEntity<Lote> createLote(@RequestBody Lote lote) {
	    try {
	        Lote nuevoLote = loteServicio.createLote(lote);
	        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLote);
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}

	private void guardarQRComoArchivo(Long loteId, byte[] qrBytes) {
	    try {
	        String fileName = "qr_lote_" + loteId + ".png";
	        Path path = Paths.get("qrcodes", fileName);
	        Files.createDirectories(path.getParent());
	        Files.write(path, qrBytes);
	    } catch (IOException e) {
	    	System.out.println("Error guardando QR como archivo: " + e.getMessage());
	    }
	}
	
	@GetMapping("lote/{id}/qr-image")
	public ResponseEntity<byte[]> getQRImage(@PathVariable Long id) {
	    Lote lote = loteServicio.findById(id);
	    byte[] imageBytes = Base64.getDecoder().decode(lote.getQrCode());
	    return ResponseEntity.ok()
	        .contentType(MediaType.IMAGE_PNG)
	        .body(imageBytes);
	}
	
	@DeleteMapping("lote/{id}")
	public ResponseEntity<String> deleteLote(@PathVariable("id") Long id){
		try {
			loteServicio.deleteLote(id);
			return ResponseEntity.ok("Lote eliminado exitosamente.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lote no encontrado.");
		}
	}
	
	@PostMapping("lote/{id}/proceso")
    public ResponseEntity<Lote> agregarProcesoAElLote(@PathVariable("id") Long id, @RequestBody ProcesoConFecha procesoConFecha) {
        try {
            Lote lote = loteServicio.findById(id);
            if (lote == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            lote.getProcesos().add(procesoConFecha);
            loteServicio.saveLote(lote);
            return ResponseEntity.status(HttpStatus.CREATED).body(lote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
