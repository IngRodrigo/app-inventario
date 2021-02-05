package com.ajvierci.inventario.retrofit.clientes.model.request;

import org.simpleframework.xml.Element;


public class RequestModel {

    @Element(name = "codCia")
    public String codCia;

    public void setCodcia(String codCia) {
        this.codCia = codCia;
    }
}