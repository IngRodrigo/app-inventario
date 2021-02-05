package com.ajvierci.inventario.utilidades;

import android.content.Context;

import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Devoluciones;

import java.util.ArrayList;

public class Constantes {

    //public static final String API_BASE="http://192.168.12.46:8080/api-devoluciones/";
    public static final String API_BASE="http://192.168.18.45/api-devoluciones/";

    private Context contexto;
    private ConexionSQLiteHelper conexion;



    public Constantes(Context contexto){
        this.contexto=contexto;
    }
    public ConexionSQLiteHelper getConexion(){
       return  conexion=new ConexionSQLiteHelper(contexto,"bd_inventario", null, 1);
    }

    public static String mensajeErrorJSONException(){
        return "Error al intentar convertir el string";
    }
    public static String mensajeErrorConexionServidor(){
        return "Error al intentar conectarse al servidor. Detalles: ";
    }
    public static String mensajeExitoSincronización(){
        return "Sincronización exitosa";
    }

    public static String peticionJSON(String json, ArrayList<Devoluciones> devoluciones, int codigoDoco){
        int idDevolucion=1;
        int cantidadFilas=0;
        String jsonObject="";

        jsonObject= "[{\"KCOO\":" + "\"" + devoluciones.get(0).getKCOO() + "\", " +
                "\"DCTO\":\"CD\"," +
                "\"DOCO\":" + codigoDoco  + ", " +
                "\"VR02\":\" \", " +
                "\"AN8\":" + devoluciones.get(0).getAN8() + ", " +
                "\"DRQJ\":"+ MovimientoBD.obtenerFecha()+", " +
                "\"CRCD\":\"GUA\", " +
                "\"d55DECL\":0, " +
                "\"ALPH\":\"" + devoluciones.get(0).getALPH() + "\", " +
                "\"TAX\":\""+devoluciones.get(0).getTAX()+"\", " +
                "\"ADD1\":\""+devoluciones.get(0).getADD1()+"\", " +
                "\"ADD2\":\" \", " +
                "\"CREG\":"+devoluciones.get(0).getCREG()+", " +
                "\"s55PROCES\":0, " +
                "\"detalle\":"+json+"}]";


        return jsonObject;
    }


}
