package com.example.finalusuario;

import java.io.Serializable;

public class Reseña implements Serializable {

    private int id;
    private String idRestaurante;
    private String nombreUsuario;
    private String textoReseña;
    private int valoracion;

    public Reseña(){}

    public Reseña(int id, String idRestaurante, String nombreUsuario, String textoReseña, int valoracion) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.nombreUsuario = nombreUsuario;
        this.textoReseña = textoReseña;
        this.valoracion = valoracion;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTextoReseña() {
        return textoReseña;
    }

    public void setTextoReseña(String textoReseña) {
        this.textoReseña = textoReseña;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
