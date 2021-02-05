package com.ajvierci.inventario.retrofit.articulos.net;

import com.ajvierci.inventario.retrofit.articulos.model.response.ResponseEnvelope;
import com.ajvierci.inventario.retrofit.articulos.model.request.RequestEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RemoteService {

    @Headers({"Content-Type: text/xml;charset=UTF-8", "SOAPAction: http://tempuri.org/IWSDevoluciones/ObtenerArticulos"})
    @POST("WSDevoluciones.svc?wsdl")
    Call<ResponseEnvelope> getArticulos(@Body RequestEnvelope requestEnvelope);
}