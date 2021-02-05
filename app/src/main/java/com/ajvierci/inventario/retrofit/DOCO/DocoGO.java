package com.ajvierci.inventario.retrofit.DOCO;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.retrofit.DOCO.model.request.RequestBody;
import com.ajvierci.inventario.retrofit.DOCO.model.request.RequestEnvelope;
import com.ajvierci.inventario.retrofit.DOCO.model.request.RequestModel;
import com.ajvierci.inventario.retrofit.DOCO.model.response.ResponseEnvelope;
import com.ajvierci.inventario.retrofit.DOCO.net.Network;
import com.ajvierci.inventario.utilidades.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocoGO {
    private JSONObject jsonObject;
private ConexionSQLiteHelper conexion;
private Context contexto;
private int contador=0;
public DocoGO(Context contexto){
        this.contexto=contexto;
}
Constantes constantes= new Constantes(contexto);
MovimientoBD db=new MovimientoBD();

public JSONObject getDOCO() {
    String DocoId;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        //requestModel.setCodcia("00001");
        requestBody.setGetRequestClientes();
        requestEnvelop.setRequestBody(requestBody);



        Call<ResponseEnvelope> call = Network.remote().getDoco(requestEnvelop);
        call.enqueue(new Callback<ResponseEnvelope>() {
            @Override
            public void onResponse(Call<ResponseEnvelope> call, Response<ResponseEnvelope> response) {
                if(response.isSuccessful()){
                    ResponseEnvelope responseEnvelope = response.body();
                    List<String> DOCOResult =responseEnvelope.getResponseBody().getDOCOResponseModel().getResult();
                    String []json= DOCOResult.get(2).replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\{","").split("\\},");
                    try {
                         jsonObject=new JSONObject("{"+json[0]+"}");
                         int codigoDoco=Integer.parseInt(jsonObject.get("SiguienteDOCO").toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("DOCO: "+ArticulosResult.get(2));
                    Toast.makeText(contexto,"Exito", Toast.LENGTH_LONG).show();
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
                System.out.println(t.getMessage());
            }
        });
        return jsonObject;
    }
}
