package com.example.BackEndApiPlantas.Controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BackEndApiPlantas.model.Lote;
import com.example.BackEndApiPlantas.model.Planta;
import com.example.BackEndApiPlantas.model.UserPrincipal;
import com.example.BackEndApiPlantas.model.Users;
import com.example.BackEndApiPlantas.repo.PlantaRepositorio;
import com.example.BackEndApiPlantas.service.PlantaServicio;

@CrossOrigin(maxAge=3360)//las solicitudes CORS previas serán cacheadas durante 3360 segundos
@RestController
@RequestMapping("/api/v1/")
public class PlantaControlador {
	
	@Autowired
	private PlantaServicio servicio;
	
	@PostMapping("/operacion-sensible")
    public ResponseEntity<?> operacionSensible(Authentication authentication) {
        // Verificar autenticación primero
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        // Obtener el UserPrincipal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Users user = userPrincipal.getUser();

        // Verificar rol
        if (!user.getRole().equals(Users.Role.ROLE_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Se requiere rol ADMIN");
        }

        // Lógica para admin
        return ResponseEntity.ok("Operación realizada");
    }
	
	@GetMapping("plantas")
	public List<Planta> listarTodasLasPlantas(){
		//return repositorio.findAll();
		return servicio.fetchAllPlantas();
	}
	
	@GetMapping("plantas/{id}")
    public Optional<Planta> getPlantaById(@PathVariable Long id) {
        //return repositorio.findById(id); 
		return Optional.of(servicio.findById(id));
    }
	
	@DeleteMapping("plantas/{id}")
	public ResponseEntity<String> deletePlanta(@PathVariable("id") Long id) {
		try {
            servicio.deletePlanta(id);
            return ResponseEntity.ok("Planta eliminada exitosamente.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Planta no encontrada.");
        }
	}
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("plantas/crear")
	public ResponseEntity<Planta> crearPlanta(
			@RequestBody Planta planta
			){
		try {
			Planta nuevaPlanta = servicio.createPlanta(planta);
			return ResponseEntity.status(HttpStatus.CREATED).body(planta);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
