package com.ajvierci.inventario.retrofit.articulos.model.response;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * 描述：
 * Created by PHJ on 2019/5/5.
 */

public class ArticuloResponseModel {

    @ElementList(name = "ObtenerArticulosResult")
    private List<String> result;


    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}