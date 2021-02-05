package com.ajvierci.inventario.bdsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ajvierci.inventario.entidades.Articulos;
import com.ajvierci.inventario.entidades.Cliente;
import com.ajvierci.inventario.entidades.Detalle;
import com.ajvierci.inventario.entidades.DetalleTemp;
import com.ajvierci.inventario.entidades.Devoluciones;
import com.ajvierci.inventario.entidades.DevolucionesJSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;



public class MovimientoBD {

    ConexionSQLiteHelper conexion;
    boolean resultado=false;
    private ArrayList<DevolucionesJSON>devolucionesJSONLista;
    private ArrayList<Devoluciones>devolucionesLista;
    private ArrayList<Detalle>ListaDetalle;
    public boolean sincronizarClientes(ConexionSQLiteHelper conexion, JSONObject datos){

        try {
            String CodEmpresa=datos.get("CodEmpresa").toString();
            String NroCuentaERP=datos.get("NroCuentaERP").toString();
            String RazonSocial=datos.get("RazonSocial").toString();
            String Direccion=datos.get("Direccion").toString();
            String CodVendedor=datos.get("CodVendedor").toString();
            String CodZona=datos.get("CodZona").toString();
            String CodRamoERP=datos.get("CodRamoERP").toString();
            String CodCanalERP=datos.get("CodCanalERP").toString();
            String Telefono=datos.get("Telefono").toString();
            String RUC=datos.get("RUC").toString();
            String NroListaPrecioERP=datos.get("NroListaPrecioERP").toString();
            String LimiteCredito=datos.get("LimiteCredito").toString();
            String SaldoGuaranies=datos.get("SaldoGuaranies").toString();
            String SaldoDolares=datos.get("SaldoDolares").toString();
            String Estado=datos.get("Estado").toString();
            String Ubicacion=datos.get("Ubicacion").toString();
            String Email=datos.get("Email").toString();
            String CodCondicionVentaERP=datos.get("CodCondicionVentaERP").toString();

            //abrimos la base de datos para escribir en ella
            SQLiteDatabase db=conexion.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("CodEmpresa",CodEmpresa);
            values.put("NroCuentaERP",NroCuentaERP);
            values.put("RazonSocial",RazonSocial);
            values.put("Direccion",Direccion);
            values.put("CodVendedor",CodVendedor);
            values.put("CodZona",CodZona);
            values.put("CodRamoERP",CodRamoERP);
            values.put("CodCanalERP",CodCanalERP);
            values.put("Telefono",Telefono);
            values.put("RUC",RUC);
            values.put("NroListaPrecioERP",NroListaPrecioERP);
            values.put("LimiteCredito",LimiteCredito);
            values.put("SaldoGuaranies",SaldoGuaranies);
            values.put("SaldoDolares",SaldoDolares);
            values.put("Estado",Estado);
            values.put("Ubicacion",Ubicacion);
            values.put("Email",Email);
            values.put("CodCondicionVentaERP",CodCondicionVentaERP);


            Long idResultado=db.insert("clientes","id", values);
            Log.i("dev","what ->"+idResultado);
            resultado=true;
            db.close();
        }catch (Exception e){
            System.out.println("Error "+e);
        }
        return resultado;
    }
    public boolean sincronizarArticulos(ConexionSQLiteHelper conexion, JSONObject datos){

        try {
            String CodEmpresa=datos.get("CodEmpresa").toString();
            String NroArticuloERP=datos.get("NroArticuloERP").toString();
            String DescripcionArticulo=datos.get("DescripcionArticulo").toString();
            String CodMarcaERP=datos.get("CodMarcaERP").toString();
            String CodLineaERP=datos.get("CodLineaERP").toString();
            String CodTipoERP=datos.get("CodTipoERP").toString();
            String CodRegimenERP=datos.get("CodRegimenERP").toString();
            String CodigoBarra=datos.get("CodigoBarra").toString();
            String CodigoLargo=datos.get("CodigoLargo").toString();
            String CodCategoriaERP=datos.get("CodCategoriaERP").toString();
            String CodProveedor=datos.get("CodProveedor").toString();

            //abrimos la base de datos para escribir en ella
            SQLiteDatabase db=conexion.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("CodEmpresa",CodEmpresa);
            values.put("NroArticuloERP",NroArticuloERP);
            values.put("DescripcionArticulo",DescripcionArticulo);
            values.put("CodMarcaERP",CodMarcaERP);
            values.put("CodLineaERP",CodLineaERP);
            values.put("CodTipoERP",CodTipoERP);
            values.put("CodRegimenERP",CodRegimenERP);
            values.put("CodigoBarra",CodigoBarra);
            values.put("CodigoLargo",CodigoLargo);
            values.put("CodCategoriaERP",CodCategoriaERP);
            values.put("CodProveedor",CodProveedor);


            Long idResultado=db.insert("articulos","id", values);
            Log.i("dev","what ->"+idResultado);
            resultado=true;
            db.close();
        }catch (Exception e){
            System.out.println("Error "+e);
        }
        return resultado;
    }

    public boolean insertarDevolucionesJSON(ConexionSQLiteHelper conexion, String codCliente, String descripcion, String json, String migrado){
       resultado=false;
        try {

            //abrimos la base de datos para escribir en ella
            SQLiteDatabase db=conexion.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("codCliente",codCliente);
            values.put("descripcionCli",descripcion);
            values.put("json_request",json);
            values.put("migrado",migrado);
            values.put("fecha",obtenerFecha());

            Long idResultado=db.insert("devoluciones_json","id", values);
            Log.i("dev","what ->"+idResultado);
            resultado=true;
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return resultado;
    }
    public boolean insertarTemporalDetalle(ConexionSQLiteHelper conexion , ArrayList<Cliente> cliente, ArrayList<Articulos> articulos, int codigoClinte, int cantidadDevuelta, int idDetalleTemp){
        try {
                String fecha=obtenerFecha();
            //abrimos la base de datos para escribir en ella
            SQLiteDatabase db=conexion.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("id",idDetalleTemp);
            values.put("KCOO",cliente.get(0).getCodEmpresa());
            values.put("DCTO","CD");
            values.put("DOCO",codigoClinte);
            values.put("LNID",(idDetalleTemp*1000));
            values.put("AN8",cliente.get(0).getNroCuenta());
            values.put("AITM",articulos.get(0).getCodigoBarra());
            values.put("UORG",cantidadDevuelta);
            values.put("LPRC",0);
            values.put("UOM","UN");
            values.put("i55DEPR",0);
            values.put("DRQJ",fecha);
            values.put("UPC3"," ");
            values.put("s55PROMFM"," ");
            values.put("LOCN"," ");
            values.put("CODARTICULO",articulos.get(0).getNroArticuloERP());


            Long idResultado=db.insert("detalle_temp","id", values);
            Log.i("dev",    "what ->"+idResultado);
            resultado=true;
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return resultado;
    }
    public boolean insertarIdDevolucion(ConexionSQLiteHelper conexion, int codigo){
    try {
        //abrimos la base de datos para escribir en ella
        SQLiteDatabase db=conexion.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put("id",codigo);
        Long idResultado=db.insert("devoluciones_id","id", values);
        Log.i("dev","what ->"+idResultado);
        resultado=true;
        db.close();
    }catch (Exception e){
        resultado=false;
        System.out.println(e);
    }
        return resultado;
    }
    public boolean borrarTablaTemporal(ConexionSQLiteHelper conexion){

       try {
           SQLiteDatabase db=conexion.getWritableDatabase();
           int delete=db.delete("detalle_temp", null, null);
           db.close();
           resultado=true;
       }catch (Exception e){
           System.out.println(e);
           resultado=false;
       }
        return  resultado;
    }
    public boolean borrarTablaArticulos(ConexionSQLiteHelper conexion){

        try {
            SQLiteDatabase db=conexion.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS articulos");
            db.execSQL(ConexionSQLiteHelper.CREAR_TABLA_ARTCULOS);
            db.close();
            resultado=true;
        }catch (Exception e){
            System.out.println(e);
            resultado=false;
        }
        return  resultado;
    }
    public boolean borrarTablaClientes(ConexionSQLiteHelper conexion){

        try {
            SQLiteDatabase db=conexion.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS clientes");
            db.execSQL(ConexionSQLiteHelper.CREAR_TABLA_CLIENTES);
            db.close();
            resultado=true;
        }catch (Exception e){
            System.out.println(e);
            resultado=false;
        }
        return  resultado;
    }

    public boolean insertarDetalle(ConexionSQLiteHelper conexion, ArrayList<Detalle>detalle){
      int contador=0;
        for (int i = 0; i < detalle.size(); i++) {
            try {
                String fecha=obtenerFecha();
                //abrimos la base de datos para escribir en ella
                SQLiteDatabase db=conexion.getWritableDatabase();

                ContentValues values= new ContentValues();
                //values.put("id",detalle.get(i).getId());
                values.put("KCOO",detalle.get(i).getKCOO());
                values.put("DCTO","CD");
                values.put("DOCO",detalle.get(i).getDOCO());
                values.put("LNID",detalle.get(i).getLNID());
                values.put("AN8",detalle.get(i).getAN8());
                values.put("AITM",detalle.get(i).getAITM());
                values.put("UORG",detalle.get(i).getUORG());
                values.put("LPRC",0);
                values.put("UOM","UN");
                values.put("i55DEPR",0);
                values.put("DRQJ",fecha);
                values.put("UPC3"," ");
                values.put("s55PROMFM"," ");
                values.put("LOCN"," ");
                values.put("CODARTICULO",detalle.get(i).getCodarticulo());


                Long idResultado=db.insert("devoluciones_detalle","id", values);
                Log.i("dev",    "what ->"+idResultado);
                contador++;
                db.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
        if(contador>0){
            resultado=true;
        }
        return resultado;
    }
    public boolean insertarDevolucionCabecera(ConexionSQLiteHelper conexion, int doco, int cantidadFilas, ArrayList<Cliente>clientes){
        try {
            String fecha=obtenerFecha();
            //abrimos la base de datos para escribir en ella
            SQLiteDatabase db=conexion.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("KCOO",clientes.get(0).getCodEmpresa());
            values.put("DCTO","CD");
            values.put("DOCO",doco);
            values.put("VR02"," ");
            values.put("AN8",clientes.get(0).getNroCuenta());
            values.put("DRQJ",fecha);
            values.put("CRCD","GUA");
            values.put("d55DECL",0);
            values.put("ALPH",clientes.get(0).getRazonSocial());
            values.put("TAX",clientes.get(0).getRUC());
            values.put("ADD1",clientes.get(0).getDireccion());
            values.put("ADD2"," ");
            values.put("CREG",cantidadFilas);
            values.put("s55PROCES",0);
            values.put("migrado","no");
            values.put("updateOn","N");

            Long idResultado=db.insert("devoluciones","id", values);
            Log.i("dev",    "what ->"+idResultado);
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return resultado;
    }
    public ArrayList<Devoluciones> getDevoluciones(String id, ConexionSQLiteHelper conexion){
        Devoluciones devoluciones=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        devolucionesLista= new ArrayList<Devoluciones>();
        Cursor cursor=db.rawQuery("Select * from devoluciones where migrado like '%no%' and DOCO="+id, null);

        while (cursor.moveToNext()){
            devoluciones=new Devoluciones();
            devoluciones.setId(cursor.getInt(0));
            devoluciones.setKCOO(cursor.getString(1));
            devoluciones.setDCTO(cursor.getString(2));
            devoluciones.setDOCO(cursor.getInt(3));
            devoluciones.setVR02(cursor.getString(4));
            devoluciones.setAN8(cursor.getInt(5));
            devoluciones.setDRQJ(cursor.getInt(6));
            devoluciones.setCRCD(cursor.getString(7));
            devoluciones.setD55DECL(cursor.getString(8));
            devoluciones.setALPH(cursor.getString(9));
            devoluciones.setTAX(cursor.getString(10));
            devoluciones.setADD1(cursor.getString(11));
            devoluciones.setADD2(cursor.getString(12));
            devoluciones.setCREG(cursor.getInt(13));
            devoluciones.setS55PROCES(cursor.getInt(14));
            devoluciones.setMigrado(cursor.getString(15));
            devolucionesLista.add(devoluciones);
            db.close();
        }
        return devolucionesLista;
    }
    public ArrayList<Detalle> getFilaDetalle(String id, ConexionSQLiteHelper conexion){
        Detalle devoluciones=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        ListaDetalle= new ArrayList<Detalle>();
        Cursor cursor=db.rawQuery("Select * from devoluciones_detalle where DOCO="+id, null);

        while (cursor.moveToNext()){
            devoluciones=new Detalle();
            devoluciones.setId(cursor.getInt(0));
            devoluciones.setKCOO(cursor.getString(1));
            devoluciones.setDCTO(cursor.getString(2));
            devoluciones.setDOCO(cursor.getInt(3));
            devoluciones.setLNID(cursor.getInt(4));
            devoluciones.setAN8(cursor.getInt(5));
            devoluciones.setAITM(cursor.getString(6));

            ListaDetalle.add(devoluciones);
            db.close();
        }
        return ListaDetalle;
    }
    public int contarFilaDetalle(int id, ConexionSQLiteHelper conexion){
        Detalle devoluciones=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        ListaDetalle= new ArrayList<Detalle>();
        Cursor cursor=db.rawQuery("select count (*) from devoluciones_detalle where doco="+id, null);
        int resultado=0;
        while (cursor.moveToNext()){
            resultado=cursor.getInt(0);

        }
        return resultado;
    }
    public ArrayList<DevolucionesJSON> getDevolucionesJSON(String id, ConexionSQLiteHelper conexion){
        DevolucionesJSON devolucionesJSON=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        devolucionesJSONLista= new ArrayList<DevolucionesJSON>();
        Cursor cursor=db.rawQuery("Select * from devoluciones_json where id like'%"+id+"%'", null);

        while (cursor.moveToNext()){
            devolucionesJSON=new DevolucionesJSON();
            devolucionesJSON.setId(cursor.getInt(0));
            devolucionesJSON.setCodCliente(cursor.getString(1));
            devolucionesJSON.setDescripcion(cursor.getString(2));
            devolucionesJSON.setJson(cursor.getString(3));
            devolucionesJSON.setMigrado(cursor.getString(4));

            devolucionesJSONLista.add(devolucionesJSON);
            db.close();
        }
        return devolucionesJSONLista;
    }

    public static String obtenerFecha(){
    Formatter obj = new Formatter();
    Calendar calendario = Calendar.getInstance();
    String dia = String.valueOf(calendario.get(Calendar.DAY_OF_YEAR));
    //int mes = (calendario.get(Calendar.MONTH) + 1);
    String anio = String.valueOf(calendario.get(Calendar.YEAR));

    String anioJuliano=anio.substring(0,anio.length()-2);
    return "1"+anioJuliano+dia;
    }

    public int getDevolucionClientePorDia(String fechaJuliano, ConexionSQLiteHelper conexion, String cliente){
        int cantidad=0;
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from devoluciones where DRQJ='"+fechaJuliano+"' and AN8='"+cliente+"'", null);

        while (cursor.moveToNext()){
            cantidad=(cursor.getInt(0));
            System.out.println(cantidad);
            db.close();
        }
        return cantidad;
    }
    public boolean actualizarSincronizacion(String id, ConexionSQLiteHelper conexion) {
        boolean resultado=false;
        try {
            SQLiteDatabase db=conexion.getWritableDatabase();

            String [] parametros={id};
            ContentValues values= new ContentValues();
            values.put("migrado","si");

            db.update("devoluciones_json",values,"id=?",parametros);
            System.out.println("actualizado");
            resultado=true;
        }catch (Exception e){
            System.out.println("Exepction actualizarSincronizacion "+e);
        }
        return resultado;
    }

    public boolean actualizarDevolucionCabecera(String id, ConexionSQLiteHelper conexion) {
        boolean resultado=false;
        try {
            SQLiteDatabase db=conexion.getWritableDatabase();

            String [] parametros={id};
            ContentValues values= new ContentValues();
            values.put("migrado","si");

            db.update("devoluciones",values,"id=?",parametros);
            System.out.println("actualizado");
            resultado=true;
        }catch (Exception e){
            System.out.println("Exception ActualizarDevolucionCabecera "+e);
        }
        return resultado;
    }
    public boolean eliminarDetalle(String id,ConexionSQLiteHelper conexion){
        try{
            SQLiteDatabase db=conexion.getWritableDatabase();
            int res=db.delete("devoluciones_detalle","id="+id, null);
            if(res!=0){
                resultado=true;
            }else{
                resultado=false;
            }

        }catch (Exception e){
            System.out.println("Error eliminando: "+e);
        }

        return resultado;
    }
    public boolean eliminarDevolucion(String id,ConexionSQLiteHelper conexion){
        try{
            SQLiteDatabase db=conexion.getWritableDatabase();
            int resDevolucion=db.delete("devoluciones","id="+id, null);
            int resDetalle=db.delete("devoluciones_detalle","DOCO="+id, null);
            int resDevolucionJson=db.delete("devoluciones_json","id="+id, null);
            if(resDevolucion!=0 && resDetalle!=0 && resDevolucionJson !=0){
                resultado=true;
            }else{
                resultado=false;
            }

        }catch (Exception e){
            System.out.println("Error eliminando: "+e);
        }

        return resultado;
    }
}
