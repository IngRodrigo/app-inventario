package com.ajvierci.inventario.bdsqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.ajvierci.inventario.entidades.Articulos;
import com.ajvierci.inventario.entidades.Cliente;
import com.ajvierci.inventario.entidades.DetalleTemp;

import java.util.ArrayList;

public class CrearJSON implements Runnable {
    int idDevolucion=0;
    int idDetalleTemp=0;
    ArrayList<Cliente> clientes;
    ArrayList<Articulos> articulos;
    ArrayList<String> ListaDatosTemp;
    ArrayList<DetalleTemp> ListaDetalleTemp;
    int cantidadDevuelta;
    Context contexto;
    ConexionSQLiteHelper conexion;
    String clienteCod;
    boolean accion;

    public CrearJSON(String clienteCod, ArrayList<Cliente> clientes, ArrayList<Articulos> articulos, int cantidadDevuelta, Context contexto, ConexionSQLiteHelper conexion, boolean accion){
            this.clientes=clientes;
            this.articulos=articulos;
            this.cantidadDevuelta=cantidadDevuelta;
            this.contexto=contexto;
            this.conexion=conexion;
            this.clienteCod=clienteCod;
            this.accion=accion;
    }
    public CrearJSON(String clienteCod, ArrayList<Cliente> clientes, ArrayList<Articulos> articulos, Context contexto, ConexionSQLiteHelper conexion, boolean accion){
        this.clientes=clientes;
        this.articulos=articulos;
        this.contexto=contexto;
        this.conexion=conexion;
        this.clienteCod=clienteCod;
        this.accion=accion;
    }
    String codigoClienteTemp="";

    //devuelve una cadena en formato json para enviar como detalle al servicio
    public String getDetalle(){

        ListaDetalleTemp =getClientes(clienteCod);
        String[] detalle= new String[ListaDetalleTemp.size()];
        for (int i = 0; i <ListaDetalleTemp.size() ; i++) {
             detalle[i]="{\"KCOO\":\""+ListaDetalleTemp.get(i).getKCOO()+"\", " +
                    "\"DCTO\":\"CD\", " +
                    "\"DOCO\":" + ListaDetalleTemp.get(i).getDOCO() + ", " +
                    "\"LNID\":"+(ListaDetalleTemp.get(i).getCod_cli_temp()*1000)+", " +
                    "\"AN8\":" + ListaDetalleTemp.get(i).getAN8() + ", " +
                    "\"AITM\":\""+ListaDetalleTemp.get(i).getAITM()+"\", " +
                    "\"UORG\":-"+ListaDetalleTemp.get(i).getUORG()+", " +
                    "\"LPRC\":0, " +
                    "\"UOM\":\"UN\", " +
                    "\"i55DEPR\":0, " +
                    "\"DRQJ\":"+ListaDetalleTemp.get(i).getDRQJ()+", " +
                    "\"UPC3\":\" \", " +
                    "\"s55PROMFM\":\" \", " +
                    "\"LOCN\":\" \"}";
        }
        StringBuffer cadena = new StringBuffer();
        for (int x=0;x<detalle.length;x++){
            cadena =cadena.append(detalle[x]+", ");
        }

        return  cadena.toString();

    }


    @Override
    public void run() {

        if(!accion){
            System.out.println("Insertar");
            int clienteCod=getIdDevolucion()+1;
            int idDetalleTemp=getCantidadFilasDetalleTemp()+1;
            System.out.println("ID detalle_temp: "+idDetalleTemp);
           if( insertarTablaTemporalDetalle(conexion, clientes, articulos,clienteCod, (cantidadDevuelta)*100, idDetalleTemp)){
               Toast.makeText(contexto, "Se agrego el articulo al detalle", Toast.LENGTH_LONG).show();
           }

        }
            if (accion) {
                String fin_detalle;
                System.out.println("agregar");
                String detalle = "";
                detalle += getDetalle();
                fin_detalle="["+detalle.substring(0, detalle.length()-2)+"]";
                String peticionFinal=peticionJSON(fin_detalle);
                System.out.println(peticionFinal);
                //System.out.println(detalle);
                String codCliente= String.valueOf(clientes.get(0).getNroCuenta());
                String descripcionCliente=clientes.get(0).getRazonSocial();
                String jsonRequest=peticionFinal;
                String migrado="no";
                String fecha;
                System.out.println("codigo devolucion: "+getIdDevolucion());
                System.out.println("Cantidad de lineas: "+getCantidadFilasDetalleTemp());
                MovimientoBD insertar= new MovimientoBD();
                if(insertar.insertarDevolucionesJSON(conexion, codCliente, descripcionCliente, jsonRequest, migrado)){
                    if(insertar.insertarIdDevolucion(conexion, getIdDevolucion()+1)){
                        if(insertar.borrarTablaTemporal(conexion)){
                            Toast.makeText(contexto, "Exito al insertar", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }

    }

    private boolean insertarTablaTemporalDetalle(ConexionSQLiteHelper conexion, ArrayList<Cliente> cliente, ArrayList<Articulos> articulos, int codigoClinte, int cantidadDevuelta, int idDetalleTemp) {
       MovimientoBD insertar=new MovimientoBD();
       return  insertar.insertarTemporalDetalle(conexion, cliente, articulos,codigoClinte, cantidadDevuelta, idDetalleTemp);
    }
    public int getIdDevolucion(){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select count(id) from devoluciones_id", null);
        while (cursor.moveToNext()){
            idDevolucion=cursor.getInt(0);
        }
        return idDevolucion;
    }
    public int getCantidadFilasDetalleTemp(){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select count(id) from detalle_temp", null);
        while (cursor.moveToNext()){
            idDevolucion=cursor.getInt(0);
        }
        return idDevolucion;
    }
    public int getIdDetalleTemp(){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select count(id) from detalle_temp", null);
        while (cursor.moveToNext()){
            idDevolucion=cursor.getInt(0);
        }
        return idDevolucion;
    }

    public ArrayList<DetalleTemp> getClientes(String codigoClienteTemp){
        SQLiteDatabase db=conexion.getReadableDatabase();
        DetalleTemp cliente=null;
        ListaDetalleTemp= new ArrayList<DetalleTemp>();
        System.out.println(codigoClienteTemp);
        Cursor cursor= db.rawQuery("select * from detalle_temp where AN8 like '%"+codigoClienteTemp+"%'", null);

        while (cursor.moveToNext()){
            cliente= new DetalleTemp();
            cliente.setCod_cli_temp(cursor.getInt(0));
            cliente.setKCOO(cursor.getString(1));
            cliente.setDCTO(cursor.getString(2));
            cliente.setDOCO(cursor.getInt(3));
            cliente.setLNID(cursor.getInt(0)*1000);
            cliente.setAN8(cursor.getInt(5));
            cliente.setAITM(cursor.getString(6));
            cliente.setUORG(cursor.getInt(7));
            cliente.setLPRC(cursor.getInt(8));
            cliente.setUOM(cursor.getString(9));
            cliente.setI55DEPR(cursor.getInt(10));
            cliente.setDRQJ(cursor.getInt(11));
            cliente.setUPC3(cursor.getString(12));
            cliente.setS55PROMFM(cursor.getString(13));
            cliente.setLOCN(cursor.getString(14));


           ListaDetalleTemp.add(cliente);
           db.close();
        }
        return ListaDetalleTemp;
    }

    public String peticionJSON(String json){
        int idDevolucion=1;
        int cantidadFilas=0;//cantidad de filas del detalle
        //DRQJ es ka fecha de devolucion, tiene que convertirse en juliano JDE

        String jsonObject = "[{\"KCOO\":" + "\"" + clientes.get(0).getCodEmpresa() + "\", " +
                "\"DCTO\":\"CD\"," +
                "\"DOCO\":" + (getIdDevolucion()+1) + ", " +
                "\"VR02\":\" \", " +
                "\"AN8\":" + clientes.get(0).getNroCuenta() + ", " +
                "\"DRQJ\":"+MovimientoBD.obtenerFecha()+", " +
                "\"CRCD\":\"GUA\", " +
                "\"d55DECL\":0, " +
                "\"ALPH\":\"" + clientes.get(0).getRazonSocial() + "\", " +
                "\"TAX\":\""+clientes.get(0).getRUC()+"\", " +
                "\"ADD1\":\""+clientes.get(0).getDireccion()+"\", " +
                "\"ADD2\":\" \", " +
                "\"CREG\":"+(getCantidadFilasDetalleTemp()*100)+", " +
                "\"s55PROCES\":0, " +
                "\"detalle\":"+json+"}]";

        return jsonObject;
    }
}
