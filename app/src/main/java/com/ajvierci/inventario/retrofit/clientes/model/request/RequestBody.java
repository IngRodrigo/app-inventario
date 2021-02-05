package com.ajvierci.inventario.retrofit.clientes.model.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;


@NamespaceList({
        @Namespace(reference = "http://tempuri.org/")
})
public class RequestBody {

    @Element(name = "ObtenerClientes")
    private RequestModel RequestClientes;

    public void setGetRequestClientes(RequestModel RequestClientes) {
        this.RequestClientes = RequestClientes;
    }
}