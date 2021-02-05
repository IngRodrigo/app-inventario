package com.ajvierci.inventario.retrofit.clientes;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.retrofit.clientes.model.request.RequestBody;
import com.ajvierci.inventario.retrofit.clientes.model.request.RequestEnvelope;
import com.ajvierci.inventario.retrofit.clientes.model.request.RequestModel;
import com.ajvierci.inventario.retrofit.clientes.model.response.ResponseEnvelope;
import com.ajvierci.inventario.retrofit.clientes.net.Network;
import com.ajvierci.inventario.utilidades.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesGO {
private ConexionSQLiteHelper conexion;
private Context contexto;
private int contador=0;
public ClientesGO(Context contexto){
        this.contexto=contexto;
}
Constantes constantes= new Constantes(contexto);
MovimientoBD db=new MovimientoBD();

public void getClientes() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        requestModel.setCodcia("00001");
        requestBody.setGetRequestClientes(requestModel);
        requestEnvelop.setRequestBody(requestBody);



        Call<ResponseEnvelope> call = Network.remote().getClientes(requestEnvelop);
        call.enqueue(new Callback<ResponseEnvelope>() {
            @Override
            public void onResponse(Call<ResponseEnvelope> call, Response<ResponseEnvelope> response) {
                if(response.isSuccessful()){
                    ResponseEnvelope responseEnvelope = response.body();
                    conexion= new ConexionSQLiteHelper(contexto,"bd_inventario", null, 1);
                    if(responseEnvelope!=null){
                        if(db.borrarTablaClientes(conexion)){
                            List<String> ArticulosResult =responseEnvelope.getResponseBody().getClientesResponseModel().getResult();
                            try {
                                String[] json=ArticulosResult.get(2).replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\{","").split("\\},");
                                for (int i = 0; i <json.length ; i++) {
                                    JSONObject jsonObject=new JSONObject("{"+json[i]+"}");
                                    conexion= new ConexionSQLiteHelper(contexto,"bd_inventario", null, 1);
                                    if(db.sincronizarClientes(conexion,jsonObject)){
                                        contador++;
                                    }

                                }
                                if(contador==100){
                                    Toast.makeText(contexto,constantes.mensajeExitoSincronización()+": Clientes",Toast.LENGTH_LONG).show();
                                }
                            }catch (JSONException e){
                                Toast.makeText(contexto,constantes.mensajeErrorJSONException()+e,Toast.LENGTH_LONG).show();
                            }
                        }
                        }

                }else{
                    Toast.makeText(contexto,"Ocurrio algo inesperado migrando los clientes, intentelo de nuevo",Toast.LENGTH_LONG).show();
                }
                if(!response.isSuccessful()){
                    Toast.makeText(contexto,"Ocurrio algo inesperado migrando los clientes, intentelo de nuevo",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEnvelope> call, Throwable t) {
                Toast.makeText(contexto, constantes.mensajeErrorConexionServidor()+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
