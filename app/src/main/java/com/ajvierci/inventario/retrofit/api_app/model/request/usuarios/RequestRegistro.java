package com.ajvierci.inventario.retrofit.api_app.model.request.usuarios;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestRegistro {

    public RequestRegistro( String nombre, String apellido, String ci, String cel, String userName, String userPassword) {
        this.accion="crear_usuario";
        this.nombre = nombre;
        this.apellido = apellido;
        this.ci = ci;
        this.cel = cel;
        this.userName = userName;
        this.userPassword = userPassword;
        this.idRol = 4;
        this.idEstado = 2;
    }

    @SerializedName("accion")
    @Expose
    private String accion;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("ci")
    @Expose
    private String ci;
    @SerializedName("cel")
    @Expose
    private String cel;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPassword")
    @Expose
    private String userPassword;
    @SerializedName("id_rol")
    @Expose
    private Integer idRol;
    @SerializedName("id_estado")
    @Expose
    private Integer idEstado;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

}