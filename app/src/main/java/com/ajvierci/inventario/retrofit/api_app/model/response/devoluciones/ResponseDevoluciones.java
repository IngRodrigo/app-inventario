package com.ajvierci.inventario.retrofit.api_app.model.response.devoluciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDevoluciones {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detalle")
    @Expose
    private String detalle;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseDevoluciones() {
    }

    /**
     *
     * @param status
     * @param detalle
     */
    public ResponseDevoluciones(String status, String detalle) {
        super();
        this.status = status;
        this.detalle = detalle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}