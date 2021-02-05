package com.ajvierci.inventario.retrofit.api_app.net;


import com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones.RequestDevoluciones;
import com.ajvierci.inventario.retrofit.api_app.model.request.usuarios.RequestLogin;
import com.ajvierci.inventario.retrofit.api_app.model.request.usuarios.RequestRegistro;
import com.ajvierci.inventario.retrofit.api_app.model.response.devoluciones.ResponseDevoluciones;
import com.ajvierci.inventario.retrofit.api_app.model.response.usuarios.ResponseLogin;
import com.ajvierci.inventario.retrofit.api_app.model.response.usuarios.ResponseRegistro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiDevolucionesService {

    @POST("ws_devoluciones.php")
    Call<ResponseRegistro>registrarUsuario(@Body RequestRegistro requestRegistro);

    @POST("ws_devoluciones.php")
    Call<ResponseLogin>loginUser(@Body RequestLogin requestLogin);


    @POST("ws_devoluciones.php")
    Call<ResponseDevoluciones>registrarDevolucion(@Body RequestDevoluciones requestDevoluciones);

}
