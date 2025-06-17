package com.example.BackEndApiPlantas.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.BackEndApiPlantas.model.Imagenes;
import com.example.BackEndApiPlantas.model.Planta;
import com.example.BackEndApiPlantas.model.Users;
import com.example.BackEndApiPlantas.repo.ImagenesRepositorio;
import com.example.BackEndApiPlantas.repo.PlantaRepositorio;
import com.example.BackEndApiPlantas.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ImagenesService {
	
	
	
	@Autowired
    private ImagenesRepositorio imagenesRepositorio;
    
    @Autowired
    private PlantaRepositorio plantaRepository;
    
    @Autowired
    private UserRepo usuarioRepository;
    
    public Imagenes guardarImagenGeneral(MultipartFile archivo, Long plantaId, Users subidoPor) throws IOException {
        Planta planta = plantaRepository.findById(plantaId)
            .orElseThrow(() -> new EntityNotFoundException("Planta no encontrada"));
        
        Imagenes imagen = new Imagenes();
        imagen.setTipo(Imagenes.TipoImagen.GENERAL);
        imagen.setPlanta(planta);
        imagen.setUsuario(subidoPor);
        imagen.setNombreArchivo(archivo.getOriginalFilename());
        imagen.setTipoMime(archivo.getContentType());
        imagen.setDatos(archivo.getBytes());
        imagen.setFechaSubida(LocalDateTime.now());
        
        return imagenesRepositorio.save(imagen);
    }
    
    public Imagenes guardarImagenPersonal(MultipartFile archivo, Long plantaId, Users usuario) throws IOException {
        Planta planta = plantaRepository.findById(plantaId)
            .orElseThrow(() -> new EntityNotFoundException("Planta no encontrada"));
        
        Imagenes imagen = new Imagenes();
        imagen.setTipo(Imagenes.TipoImagen.PERSONAL);
        imagen.setPlanta(planta);
        imagen.setUsuario(usuario);
        imagen.setNombreArchivo(archivo.getOriginalFilename());
        imagen.setTipoMime(archivo.getContentType());
        imagen.setDatos(archivo.getBytes());
        imagen.setFechaSubida(LocalDateTime.now());
        
        return imagenesRepositorio.save(imagen);
    }
    
    public List<Imagenes> obtenerImagenesGeneralesDePlanta(Long plantaId) {
        return imagenesRepositorio.findByPlantaIdAndTipo(plantaId, Imagenes.TipoImagen.GENERAL);
    }
    
    public List<Imagenes> obtenerImagenesPersonalesDeUsuario(Long usuarioId, Long plantaId) {
        return imagenesRepositorio.findByUsuarioIdAndPlantaIdAndTipo(
            usuarioId, plantaId, Imagenes.TipoImagen.PERSONAL);
    }
}
