package com.example.BackEndApiPlantas.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="plantas")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Planta {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"planta", "hibernateLazyInitializer", "handler"})
    private List<Lote> lotes = new ArrayList<>();

    @OneToMany(mappedBy = "planta", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"planta", "hibernateLazyInitializer", "handler"})
    private List<Users> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "planta", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"planta", "usuario", "hibernateLazyInitializer", "handler"})
    private List<Imagenes> imagenes = new ArrayList<>();

    @Column(name="nombre", nullable=false)
    private String nombre;
    
    @Column(name="nombre_cientifico")
    private String nombreCientifico;
    
    @Column(name="familia")
    private String familia;
    
    @Column(name="frecuencia_riego")
    private String frecuenciaRiego;
    
    @Column(name="temperatura")
    private String temperatura;
    
    @Column(name="humedad")
    private String humedad;
    
    @Column(name="suelo")
    private String suelo;
    
    @Column(name="fertilizacion")
    private String fertilizacion;

    @Column(name="beneficios")
    private String beneficios;
    
    @Column(name="toxicidad")
    private String toxicidad;
    
    @Column(name="precio")
    private Double precio;
    
    // Constructor vacío
    public Planta() {
        this.lotes = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.imagenes = new ArrayList<>();
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }

    public List<Users> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Users> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Imagenes> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagenes> imagenes) {
        this.imagenes = imagenes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getFrecuenciaRiego() {
        return frecuenciaRiego;
    }

    public void setFrecuenciaRiego(String frecuenciaRiego) {
        this.frecuenciaRiego = frecuenciaRiego;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getSuelo() {
        return suelo;
    }

    public void setSuelo(String suelo) {
        this.suelo = suelo;
    }

    public String getFertilizacion() {
        return fertilizacion;
    }

    public void setFertilizacion(String fertilizacion) {
        this.fertilizacion = fertilizacion;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getToxicidad() {
        return toxicidad;
    }

    public void setToxicidad(String toxicidad) {
        this.toxicidad = toxicidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}