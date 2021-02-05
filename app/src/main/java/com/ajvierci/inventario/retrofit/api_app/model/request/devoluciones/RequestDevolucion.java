package com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestDevolucion {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("accion")
    @Expose
    private String accion;
    @SerializedName("json")
    @Expose
    private String json;


    public RequestDevolucion(String id, String accion, String json) {
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\"=\"" + id + "\"" +
                ", \"accion\"=\"" + accion + "\"" +
                ", \"json\"=" + json  + "}";
    }
}
