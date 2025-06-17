package com.example.BackEndApiPlantas.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING) // Si el rol es un Enum (ej: ADMIN, USER)
    @Column(name = "role", nullable = false) 
    private Role role = Role.ROLE_USER;

    @ManyToMany
    @JoinTable(
        name = "usuario_planta",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "planta_id")
    )
    private List<Planta> plantasUsuario = new ArrayList<>();
     
    @ManyToOne
    @JoinColumn(name = "planta_id")
    @JsonBackReference("planta-usuario")
    private Planta planta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-imagen") // Mantenemos la referencia a Imagenes
    private List<Imagenes> imagenes = new ArrayList<>();
    
    public enum Role {
    	ROLE_USER, ROLE_ADMIN
    }

    public Users() {
    	this.plantasUsuario = new ArrayList<>();
        this.imagenes = new ArrayList<>();
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
        this.plantasUsuario = new ArrayList<>();
        this.imagenes = new ArrayList<>();
    }
    
    public Users(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.plantasUsuario = new ArrayList<>();
        this.imagenes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Planta> getPlantasUsuario() {
        return plantasUsuario;
    }

    public void setPlantasUsuario(List<Planta> plantasUsuario) {
        this.plantasUsuario = plantasUsuario;
    }

    public List<Imagenes> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagenes> imagenes) {
        this.imagenes = imagenes;
    }

    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

	public void agregarImagenPersonal(Imagenes imagen, Planta planta) {
        imagen.setTipo(Imagenes.TipoImagen.PERSONAL);
        imagen.setPlanta(planta);
        imagen.setUsuario(this);
        imagenes.add(imagen);
    }

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}
}