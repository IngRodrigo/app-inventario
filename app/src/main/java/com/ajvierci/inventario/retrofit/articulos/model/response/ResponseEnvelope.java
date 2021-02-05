package com.ajvierci.inventario.retrofit.articulos.model.response;

import org.simpleframework.xml.Element;

/**
 * 描述：
 * Created by PHJ on 2019/5/5.
 */

public class ResponseEnvelope {
    @Element(name = "Body")
    private ArticulosResponseBody responseBody;

    public ArticulosResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ArticulosResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}