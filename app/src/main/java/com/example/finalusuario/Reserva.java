package com.example.finalusuario;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {
    private String nombreUsuario;

    private String restaurante;
    private Date dia;
    private String turno;
    private String hora;
    private int comensales;

    public Reserva() {
    }

    public Reserva(String nombreUsuario, String restaurante, Date dia, String turno, String hora, int comensales) {
        this.nombreUsuario = nombreUsuario;
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
}
