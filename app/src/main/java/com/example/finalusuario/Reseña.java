package com.example.finalusuario;

import java.io.Serializable;
import java.util.Date;

public class Reseña implements Serializable {

    private int id;
    private String idUsuario;
    private String idRestaurante;
    private String nombreUsuario;
    private String textoReseña;
    private int valoracion;
    private Date fecha;

    public Reseña(){}

    public Reseña(int id,String idUsuario, String idRestaurante, String nombreUsuario, String textoReseña, int valoracion,Date fecha) {
        this.idUsuario=idUsuario;
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.nombreUsuario = nombreUsuario;
        this.textoReseña = textoReseña;
        this.valoracion = valoracion;
        this.fecha=fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
