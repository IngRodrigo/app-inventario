package com.ajvierci.inventario.retrofit.articulos.model.response;

import org.simpleframework.xml.Element;


public class ArticulosResponseBody {

    @Element(name = "ObtenerArticulosResponse", required = false)
    private ArticuloResponseModel ArticuloResponseModel;

    public ArticuloResponseModel getArticuloResponseModel() {
        return ArticuloResponseModel;
    }

    public void setWeatherResponseModel(ArticuloResponseModel ArticuloResponseModel) {
        this.ArticuloResponseModel = ArticuloResponseModel;
    }
}