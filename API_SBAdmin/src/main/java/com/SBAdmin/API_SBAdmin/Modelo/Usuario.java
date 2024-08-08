package com.SBAdmin.API_SBAdmin.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Setter @Getter @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter @Getter @Column(name = "nombre")
    private String nombre;
    @Setter @Getter @Column(name = "telefono")
    private String telefono;
    @Setter @Getter @Column(name = "email")
    private String email;
    @Setter @Getter @Column(name = "contraseña")
    private String pass;

    public Usuario(Long id, String nombre, String telefono, String email, String pass) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.pass = pass;
    }

    // Constructor vacio para que cuando hibernate traiga los usuarios de la base de datos
    // no tenga problema
    public Usuario(){

    }
}
