package com.ajvierci.inventario.retrofit.DOCO.model.response;

import org.simpleframework.xml.Element;

/**
 * 描述：
 * Created by PHJ on 2019/5/5.
 */

public class ResponseEnvelope {
    @Element(name = "Body")
    private DOCOResponseBody responseBody;

    public DOCOResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(DOCOResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}