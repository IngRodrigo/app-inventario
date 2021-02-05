package com.ajvierci.inventario.utilidades;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;

import com.ajvierci.inventario.entidades.Devoluciones;

import com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones.Detalle;
import com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones.Json;
import com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones.RequestDevoluciones;

import com.ajvierci.inventario.retrofit.api_app.model.response.devoluciones.ResponseDevoluciones;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesCliente;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuardarDevolucionesSincronizadas {
private Context context;
private ConexionSQLiteHelper conexion;
private ApiDevolucionesCliente apiDevolucionesCliente;
private ApiDevolucionesService apiDevolucionesService;

private ArrayList<Devoluciones>devolucionesLista;
private ArrayList<Devoluciones>devoluciones=null;

private ArrayList<Detalle>detallesLista;



private List<Detalle> listaDetalleEnviar;
private RequestDevoluciones requestDevoluciones=new RequestDevoluciones();
private Json json;
private int contadorBucle=0;
private  int contador=0;
private  int docoInicio=0;
private String id;


    public GuardarDevolucionesSincronizadas(Context context, String id){

        this.conexion=new ConexionSQLiteHelper(context,"bd_inventario", null, 1);
        retrofit();
        this.id=id;

    }

    public boolean iniciarOperaciones(){
        boolean resultado=false;
        devoluciones=getDevoluciones();
        if(devoluciones!=null){
            if(insertarDevoluciones()){
                resultado=true;
            }
        }else{
            this.devoluciones=null;
        }
        return resultado;
    }

    private void retrofit(){
        apiDevolucionesCliente= ApiDevolucionesCliente.getInstance();
        //acceso a todos los servicios http de la instancia
        apiDevolucionesService=apiDevolucionesCliente.getApiDevolucionesService();
    }

    private ArrayList<Devoluciones> getDevoluciones(){
        Devoluciones devoluciones=null;
        SQLiteDatabase db=this.conexion.getReadableDatabase();
        devolucionesLista= new ArrayList<>();
        Cursor cursor=db.rawQuery("select d.KCOO, d.DCTO, d.DOCO, d.VR02, d.AN8, d.DRQJ, d.CRCD, d.d55DECL, d.ALPH, d.TAX, d.ADD1, d.ADD2, d.CREG, d.s55PROCES\n" +
                " from devoluciones as d  where d.migrado='si'", null);

        while (cursor.moveToNext()){
            devoluciones=new Devoluciones();
            devoluciones.setKCOO(cursor.getString(0));
            devoluciones.setDCTO(cursor.getString(1));
            devoluciones.setDOCO(cursor.getInt(2));
            devoluciones.setVR02(cursor.getString(3));
            devoluciones.setAN8(cursor.getInt(4));
            devoluciones.setDRQJ(cursor.getInt(5));
            devoluciones.setCRCD(cursor.getString(6));
            devoluciones.setD55DECL(cursor.getString(7));
            devoluciones.setALPH(cursor.getString(8));
            devoluciones.setTAX(cursor.getString(9));
            devoluciones.setADD1(cursor.getString(10));
            devoluciones.setADD2(cursor.getString(11));
            devoluciones.setCREG(cursor.getInt(12));
            devoluciones.setS55PROCES(cursor.getInt(13));
            devolucionesLista.add(devoluciones);
            db.close();
        }
        return devolucionesLista;
    }

    private List<Detalle>getDetalle(int doco){
        Detalle detalle =null;
        SQLiteDatabase db=this.conexion.getReadableDatabase();
        detallesLista= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from devoluciones_detalle as dtl\n" +
                "inner join articulos as a on dtl.CODARTICULO=a.NroArticuloERP\n" +
                "where doco ="+doco, null);

        while (cursor.moveToNext()){
            detalle=new Detalle();
            detalle.setKCOO(cursor.getString(1));
            detalle.setDCTO(cursor.getString(2));
            detalle.setDOCO(cursor.getInt(3));
            detalle.setLNID(cursor.getInt(4));
            detalle.setAN8(cursor.getInt(5));
            detalle.setAITM(cursor.getString(6));
            detalle.setUORG(cursor.getString(7));
            detalle.setLPRC(cursor.getInt(8));
            detalle.setUOM(cursor.getString(9));
            detalle.setI55DEPR(cursor.getInt(10));
            detalle.setDRQJ(cursor.getInt(11));
            detalle.setUPC3(cursor.getString(12));
            detalle.setS55PROMFM(cursor.getString(13));
            detalle.setLOCN(cursor.getString(14));
            detalle.setCodArticulo(cursor.getString(15));
            detalle.setDescripcion(cursor.getString(19));
            detallesLista.add(detalle);
            db.close();
        }
        return detallesLista;
    }


    public boolean insertarDevoluciones(){

        boolean resultado=false;


        for (int i = 0; i < devoluciones.size(); i++) {
            listaDetalleEnviar=new ArrayList<>();
           docoInicio= devoluciones.get(i).getDOCO();
            listaDetalleEnviar=getDetalle(docoInicio);
//            System.out.println("listaDetalleEnviar = " + listaDetalleEnviar.get(i).getDescripcion());
            json=new Json(devoluciones.get(i).getKCOO(),
                    devoluciones.get(i).getDCTO(),
                    devoluciones.get(i).getDOCO(),
                    devoluciones.get(i).getVR02(),
                    devoluciones.get(i).getAN8(),
                    devoluciones.get(i).getDRQJ(),
                    devoluciones.get(i).getCRCD(),
                    Integer.parseInt(devoluciones.get(i).getD55DECL()),
                    devoluciones.get(i).getALPH(),
                    devoluciones.get(i).getTAX(),
                    devoluciones.get(i).getADD1(),
                    devoluciones.get(i).getADD2(),
                    devoluciones.get(i).getCREG(),
                    devoluciones.get(i).getS55PROCES(),
                    listaDetalleEnviar
            );

            requestDevoluciones.setId(this.id);
            requestDevoluciones.setAccion("insertar_devolucion");
            requestDevoluciones.setJson(json);

            System.out.println("requestDevoluciones.toString() = " + requestDevoluciones.toString());
            Call<ResponseDevoluciones>call=apiDevolucionesService.registrarDevolucion(requestDevoluciones);
            call.enqueue(new Callback<ResponseDevoluciones>() {
                @Override
                public void onResponse(Call<ResponseDevoluciones> call, Response<ResponseDevoluciones> response) {
                    if(response.isSuccessful()){
                        System.out.println("Resultado de peticion: "+response.body().getDetalle());
                        if(response.body().getStatus().equals("ok")){
                            String doco=response.body().getDetalle();
                            if(String.valueOf(docoInicio).equals(doco)){
                                contador++;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDevoluciones> call, Throwable t) {
                    System.out.println("Error: "+t);

                }
            });
            contadorBucle++;

        }



            if(contadorBucle==devoluciones.size()){
                resultado=true;
            }
            return resultado;
    }
}
