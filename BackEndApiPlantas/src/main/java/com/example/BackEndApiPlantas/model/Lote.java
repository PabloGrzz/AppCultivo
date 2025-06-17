package com.example.BackEndApiPlantas.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "lotes")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "lote_procesos", joinColumns = @JoinColumn(name = "lote_id"))
    private List<ProcesoConFecha> procesos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "planta_id", nullable = false)
    @JsonIgnoreProperties({"lotes", "usuarios", "imagenes", "hibernateLazyInitializer", "handler"})
    private Planta planta;

    @Column(name = "numero_lote")
    private Integer numeroLote;
    
    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;
    
    // Constructores
    public Lote() {
        this.procesos = new ArrayList<>();
    }
    
    public Lote(List<ProcesoConFecha> procesos, Planta planta) {
        this.procesos = procesos != null ? procesos : new ArrayList<>();
        this.planta = planta;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProcesoConFecha> getProcesos() {
        if (procesos == null) {
            procesos = new ArrayList<>();
        }
        return procesos;
    }

    public void setProcesos(List<ProcesoConFecha> procesos) {
        this.procesos = procesos;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}