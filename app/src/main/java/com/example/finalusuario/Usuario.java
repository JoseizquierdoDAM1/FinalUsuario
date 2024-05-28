package com.example.finalusuario;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String id;
    private String nombreUsuario;
    private String correo;
    private String contraseña;
    private String dni;
    private String tipUser;
    private String urlImagen;


    public Usuario() {
    }

    public Usuario(String id,String nombreUsuario, String correo, String contraseña, String dni, String tipUser, String imageURL) {
        this.id=id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.dni=dni;
        this.tipUser = tipUser;
        this.urlImagen = imageURL;
    }
    
    public Usuario(String id,String nombreUsuario, String correo, String contraseña, String dni, String tipUser) {
        this.id=id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.dni=dni;
        this.tipUser = tipUser;
    }

    public Usuario(String nombre, String correo, String dni, String tipoUser) {
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipUser() {
        return tipUser;
    }

    public void setTipUser(String tipUser) {
        this.tipUser = tipUser;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;


    }
}
