package com.ajvierci.inventario.retrofit.clientes.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;


@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "xsi"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "xsd"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
})
public class RequestEnvelope {
    @Element(name = "soap:Body")
    private RequestBody requestBody;

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }
}