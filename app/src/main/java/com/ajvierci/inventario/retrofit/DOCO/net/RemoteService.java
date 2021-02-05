package com.ajvierci.inventario.retrofit.DOCO.net;

import com.ajvierci.inventario.retrofit.DOCO.model.request.RequestEnvelope;
import com.ajvierci.inventario.retrofit.DOCO.model.response.ResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RemoteService {

    @Headers({"Content-Type: text/xml;charset=UTF-8", "SOAPAction: http://tempuri.org/IWSDevoluciones/ObtenerSiguienteDOCO"})
    @POST("WSDevoluciones.svc?wsdl")
    Call<ResponseEnvelope> getDoco(@Body RequestEnvelope requestEnvelope);
}