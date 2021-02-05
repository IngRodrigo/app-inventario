package com.ajvierci.inventario.entidades;

import java.io.Serializable;

//serializable es lo que permite que el objeto sea enviado como parametros entre actividades
public class DevolucionesJSON implements Serializable {

    private int id;
    private String codCliente, descripcion, json, migrado, fecha;

    public DevolucionesJSON() {
    }

    public DevolucionesJSON(int id, String codCliente, String descripcion, String json, String migrado, String fecha) {
        this.id = id;
        this.codCliente = codCliente;
        this.descripcion = descripcion;
        this.json = json;
        this.migrado = migrado;
        this.fecha=fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getMigrado() {
        return migrado;
    }

    public void setMigrado(String migrado) {
        this.migrado = migrado;
    }
}
