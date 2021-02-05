package com.ajvierci.inventario.retrofit.articulos.model.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;


@NamespaceList({
        @Namespace(reference = "http://tempuri.org/")
})
public class RequestBody {

    @Element(name = "ObtenerArticulos")
    private RequestModel RequestArticulos;

    public void setGetRequestArticulos(RequestModel RequestArticulos) {
        this.RequestArticulos = RequestArticulos;
    }
}