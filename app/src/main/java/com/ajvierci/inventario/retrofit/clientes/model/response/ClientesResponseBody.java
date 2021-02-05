package com.ajvierci.inventario.retrofit.clientes.model.response;

import org.simpleframework.xml.Element;


public class ClientesResponseBody {

    @Element(name = "ObtenerClientesResponse", required = false)
    private ClienteResponseModel clienteResponseModel;

    public ClienteResponseModel getClientesResponseModel() {
        return clienteResponseModel;
    }

    public void setClientesResponseModel(ClienteResponseModel clienteResponseModel) {
        this.clienteResponseModel = clienteResponseModel;
    }
}