package com.ajvierci.inventario.retrofit.api_app.net;

import com.ajvierci.inventario.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiDevolucionesCliente {

    private static ApiDevolucionesCliente instance=null;

    private ApiDevolucionesService apiDevolucionesService;
    private Retrofit retrofit;

    public ApiDevolucionesCliente(){
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();
        this.retrofit=new Retrofit.Builder()
                .baseUrl(Constantes.API_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiDevolucionesService =this.retrofit.create(ApiDevolucionesService.class);
    }

    public static ApiDevolucionesCliente getInstance(){
        if(instance==null){
            instance=new ApiDevolucionesCliente();
        }
        return instance;
    }

    public ApiDevolucionesService getApiDevolucionesService(){

        return apiDevolucionesService;
    }
}
