package com.example.finalusuario;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {

    private int id;
    private String nombreUsuario;
    private String idUsuario;
    private String idRestaurante;
    private String restaurante;
    private Date dia;
    private String turno;
    private String hora;
    private int comensales;

    public Reserva() {
    }



    public Reserva(int id,String idUsuario, String nombreUsuario, String idRestaurante, String restaurante, Date dia, String turno, String hora, int comensales) {
        this.id=id;
        this.idUsuario=idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idRestaurante = idRestaurante;
        this.restaurante = restaurante;
        this.dia = dia;
        this.turno = turno;
        this.hora = hora;
        this.comensales = comensales;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }



    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getComensales() {
        return comensales;
    }

    public void setComensales(int comensales) {
        this.comensales = comensales;
    }

    public String getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(String restaurante) {
        this.restaurante = restaurante;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
