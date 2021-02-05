package com.ajvierci.inventario.retrofit.clientes.model.response;

import org.simpleframework.xml.Element;

/**
 * 描述：
 * Created by PHJ on 2019/5/5.
 */

public class ResponseEnvelope {
    @Element(name = "Body")
    private ClientesResponseBody responseBody;

    public ClientesResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ClientesResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}