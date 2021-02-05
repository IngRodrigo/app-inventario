package com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RequestDevoluciones {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("accion")
    @Expose
    private String accion;
    @SerializedName("json")
    @Expose
    private Json json;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestDevoluciones() {
    }

    /**
     *
     * @param accion
     * @param json
     * @param id
     */
    public RequestDevoluciones(String id, String accion, Json json) {
        super();
        this.id = id;
        this.accion = accion;
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "RequestDevoluciones{" +
                "id='" + id + '\'' +
                ", accion='" + accion + '\'' +
                ", json=" + json +
                '}';
    }
}