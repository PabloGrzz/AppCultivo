package com.example.BackEndApiPlantas.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class ProcesoConFecha {
    
    private String descripcion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;
    
    // Constructor vacío requerido por JPA
    public ProcesoConFecha() {
    }
    
    public ProcesoConFecha(String descripcion, LocalDateTime fecha) {
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @JsonIgnore
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    // Método adicional para serializar la fecha como timestamp (long) para Android
    @JsonProperty("fecha")
    public Long getFechaAsTimestamp() { 
        return fecha != null ? fecha.toInstant(ZoneOffset.UTC).toEpochMilli() : null;
    }
    
    // Método para deserializar desde timestamp
    @JsonProperty("fecha")
    public void setFechaFromTimestamp(Long timestamp) {
        if (timestamp != null) {
            this.fecha = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);
        }
    }
}