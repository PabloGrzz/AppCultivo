package com.example.BackEndApiPlantas.Controlador;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BackEndApiPlantas.model.Imagenes;
import com.example.BackEndApiPlantas.model.Users;
import com.example.BackEndApiPlantas.repo.UserRepo;
import com.example.BackEndApiPlantas.service.ImagenesService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/")
public class ImagenesController {
    
    @Autowired
    private ImagenesService imagenService;
    
    @Autowired
    private UserRepo userRepo;
    
    @PostMapping("imagenes/generales/{plantaId}")
    public ResponseEntity<?> guardarImagenGeneral(
            @RequestParam("archivo") MultipartFile archivo,
            @PathVariable Long plantaId,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        
        try {
            // Get the authenticated user
            Users usuario = userRepo.findByUsername(authentication.getName());
            
            // Call service method with user
            Imagenes saved = imagenService.guardarImagenGeneral(archivo, plantaId, usuario);
            
            return ResponseEntity.ok().body(saved.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
   /* @PostMapping("/imagenes/personales/{plantaId}")
    public ResponseEntity<?> subirImagenPersonal(
            @PathVariable Long plantaId,
            @RequestParam("archivo") MultipartFile archivo,
            @AuthenticationPrincipal Users usuario) {
        
        try {
            Imagenes imagen = imagenService.guardarImagenPersonal(archivo, plantaId, usuario);
            return ResponseEntity.ok(imagen);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar la imagen");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }*/
    
    @GetMapping("/imagenes/{plantaId}/general")
    public ResponseEntity<List<Imagenes>> obtenerImagenesGenerales(
            @PathVariable Long plantaId) {
        
        return ResponseEntity.ok(
            imagenService.obtenerImagenesGeneralesDePlanta(plantaId));
    }
    
    @GetMapping("/imagenes/{plantaId}/personal/{usuarioId}")
    public ResponseEntity<List<Imagenes>> obtenerImagenesPersonales(
            @PathVariable Long plantaId,
            @PathVariable Long usuarioId) {
        
        return ResponseEntity.ok(
            imagenService.obtenerImagenesPersonalesDeUsuario(usuarioId, plantaId));
    }
}