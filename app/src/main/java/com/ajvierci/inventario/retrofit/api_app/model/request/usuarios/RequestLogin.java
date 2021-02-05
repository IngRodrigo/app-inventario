package com.ajvierci.inventario.retrofit.api_app.model.request.usuarios;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLogin {

    @SerializedName("accion")
    @Expose
    private String accion;
    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("password")
    @Expose
    private String password;

    public RequestLogin(String usuario, String password){
        this.accion="acceso_usuario";
        this.usuario=usuario;
        this.password=password;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestLogin{" +
                "accion='" + accion + '\'' +
                ", usuario='" + usuario + '\'' +
                ", password=" + password +
                '}';
    }
}