package com.SBAdmin.API_SBAdmin.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter @Column(name = "id")
    private Long id;
    @Setter @Getter @Column(name = "nombre_usuario")
    private String nombre_usuario;
    @Setter @Getter @Column(name = "cuenta")
    private String cuenta;
    @Setter @Getter @Column(name = "pass")
    private String pass;
    @Setter @Getter @Column(name = "id_usuario")
    private Long id_usuario;    

    public Cuenta(Long id, String nombre_usuario, String cuenta, String pass) {
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.cuenta = cuenta;
        this.pass = pass;
    }
    public Cuenta(){}
}
