package com.ajvierci.inventario.retrofit.clientes.net;

import com.ajvierci.inventario.retrofit.clientes.model.request.RequestEnvelope;
import com.ajvierci.inventario.retrofit.clientes.model.response.ResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RemoteService {

    @Headers({"Content-Type: text/xml;charset=UTF-8", "SOAPAction: http://tempuri.org/IWSDevoluciones/ObtenerClientes"})
    @POST("WSDevoluciones.svc?wsdl")
    Call<ResponseEnvelope> getClientes(@Body RequestEnvelope requestEnvelope);
}