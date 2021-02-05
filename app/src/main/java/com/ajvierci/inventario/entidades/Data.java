package com.ajvierci.inventario.entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("documento")
    @Expose
    private String documento;
    @SerializedName("celular")
    @Expose
    private String celular;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("creado")
    @Expose
    private String creado;
    @SerializedName("actualiado")
    @Expose
    private String actualiado;
    @SerializedName("id_estado")
    @Expose
    private String idEstado;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("id_rol")
    @Expose
    private String idRol;
    @SerializedName("rol")
    @Expose
    private String rol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getActualiado() {
        return actualiado;
    }

    public void setActualiado(String actualiado) {
        this.actualiado = actualiado;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Detalle{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", documento='" + documento + '\'' +
                ", celular='" + celular + '\'' +
                ", userName='" + userName + '\'' +
                ", creado='" + creado + '\'' +
                ", actualiado='" + actualiado + '\'' +
                ", idEstado='" + idEstado + '\'' +
                ", estado='" + estado + '\'' +
                ", idRol='" + idRol + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
