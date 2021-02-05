package com.ajvierci.inventario.retrofit.DOCO.model.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;


@NamespaceList({
        @Namespace(reference = "http://tempuri.org/")
})
public class RequestBody {

    @Element(name = "ObtenerSiguienteDOCO", required= false)
    private RequestModel RequestDOCO;

    public void setGetRequestClientes() {
        //this.RequestDOCO = RequestDOCO;
    }
}