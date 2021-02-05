package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.adapter.ClienteAdapter;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.ClienteSeleccionado;
import com.ajvierci.inventario.entidades.Detalle;
import com.ajvierci.inventario.entidades.Devoluciones;
import com.ajvierci.inventario.entidades.DevolucionesJSON;
import com.ajvierci.inventario.retrofit.SincronizarDevoluciones;
import com.ajvierci.inventario.utilidades.GuardarDevolucionesSincronizadas;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class DevolucionesActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<DevolucionesJSON>devolucionesJSONLista;

    private StringBuilder sb=null;
    private ArrayList<Detalle>ListaDetalle;
    private int codigoDoco=0;
    private RecyclerView recyclerView;
    private TextView _titulo_orden_lista;
    private TextView _codigo_cliente;
    private Button _btn_sincronizar, btn_crear_devolucion;


    private ArrayList<DevolucionesJSON> listDatos;
    public ArrayList<DevolucionesJSON>ListaDevoluciones;
    public ArrayList<DevolucionesJSON>datosLista= new ArrayList<>();


    private ProgressBar _progreso_lista;
    private String parametro;
    private ConexionSQLiteHelper conexion;
    private DevolucionesJSON devoluciones;
    private ClienteAdapter clienteAdapter;

    private ArrayList<Devoluciones>devolucionesLista;



    private String migrado;
    private String descripcion;
    private String json;
    private int idDev;
    private  SharedPreferences prefrences;
    private SwipeRefreshLayout swipeRefreshLayout;


    SincronizarDevoluciones sincronizarDevoluciones;

    public DevolucionesActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devoluciones);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<ClienteSeleccionado> arrayListClienteSeleccionado=new ArrayList<>();
        conectarVista();
        setPreferences();
        listenner();
        this.parametro=getIntent().getExtras().getString("parametro");
        //cargarDatosALista(getClientes(parametro));
        this.datosLista=getClientes(parametro);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        clienteAdapter=new ClienteAdapter(this.datosLista, getApplicationContext());

        recyclerView.setAdapter(clienteAdapter);
        this._titulo_orden_lista.setText("LISTA ORDENADA :  \""+parametro.toUpperCase()+"\"");

        //setLayautManagerRecyclerView();
        swipeRefreshLayout = findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                    actualiarDatos();

                    clienteAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Lista actualizada", Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void actualiarDatos() {
        this.datosLista.clear();
        //System.out.println("parametro = " + this.parametro);
        //System.out.println(getClientes(this.parametro).size());
        for (int i = 0; i <getClientes(this.parametro).size() ; i++) {
            this.datosLista.add(getClientes(this.parametro).get(i));
        }

    }


    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private String cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("id","0");
        return recordar;

    }

    private void listenner() {
        _btn_sincronizar.setOnClickListener(this);
        btn_crear_devolucion.setOnClickListener(this);
    }

    private ClienteAdapter setLayautManagerRecyclerView() {
        //cargarDatosALista();

        clienteAdapter=new ClienteAdapter(listDatos, getApplicationContext());
        return clienteAdapter;
    }

    private void cargarDatosALista(ArrayList<DevolucionesJSON>listaDevoluciones) {

        if(listaDevoluciones!=null){
            if(listaDevoluciones.size()!=0){
                System.out.println(ListaDevoluciones.size());
                listDatos= new ArrayList<>();
                for (int i = 0; i < listaDevoluciones.size(); i++) {
                    this.migrado=listaDevoluciones.get(i).getMigrado();
                    this.descripcion=listaDevoluciones.get(i).getDescripcion();
                    this.json=listaDevoluciones.get(i).getJson();
                    this.idDev=listaDevoluciones.get(i).getId();
                    devoluciones= new DevolucionesJSON();
                    devoluciones.setDescripcion(descripcion);
                    devoluciones.setMigrado(migrado);
                    devoluciones.setJson(json);
                    devoluciones.setId(idDev);
                    devoluciones.setCodCliente(listaDevoluciones.get(i).getCodCliente());
                    listDatos.add(devoluciones);
                    //  System.out.println(listDatos.get(0).getDescripcion());
                }
            }else{
                Toast.makeText(getApplicationContext(), "Nada que mostrar", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void conectarVista() {
        this.recyclerView=findViewById(R.id.recyclerId);
        this._btn_sincronizar=findViewById(R.id._devoluciones_btn_sincronizar);
        this.btn_crear_devolucion=findViewById(R.id._devoluciones_btn_crear);
        this._titulo_orden_lista=findViewById(R.id._devoluciones_titulo_lista_orden);
        this._progreso_lista=findViewById(R.id._devoluciones_progreso);
    }

    public ArrayList<DevolucionesJSON> getClientes(String parametro){
        if(parametro.equals("cliente")){
            ListaDevoluciones=new ArrayList<>();
            conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
            SQLiteDatabase db=conexion.getReadableDatabase();
            DevolucionesJSON devoluciones=null;

            Cursor cursor= db.rawQuery("select * from devoluciones_json order by descripcionCli", null);

            while (cursor.moveToNext()){
                devoluciones= new DevolucionesJSON();
                devoluciones.setId(cursor.getInt(0));
                devoluciones.setCodCliente(cursor.getString(1));
                devoluciones.setDescripcion(cursor.getString(2));
                devoluciones.setJson(cursor.getString(3));
                devoluciones.setMigrado(cursor.getString(4));


                ListaDevoluciones.add(devoluciones);
                // db.close();
            }

        }
        if(parametro.equals("enviado")){
            _btn_sincronizar.setVisibility(View.INVISIBLE);
            ListaDevoluciones=new ArrayList<>();
            conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
            SQLiteDatabase db=conexion.getReadableDatabase();
            DevolucionesJSON devoluciones=null;


            Cursor cursor= db.rawQuery("select * from devoluciones_json where migrado like'%si%'", null);

            while (cursor.moveToNext()){
                devoluciones= new DevolucionesJSON();
                devoluciones.setId(cursor.getInt(0));
                devoluciones.setCodCliente(cursor.getString(1));
                devoluciones.setDescripcion(cursor.getString(2));
                devoluciones.setJson(cursor.getString(3));
                devoluciones.setMigrado(cursor.getString(4));


                ListaDevoluciones.add(devoluciones);
                db.close();
            }

        }
        if(parametro.equals("pendiente")){
            ListaDevoluciones=new ArrayList<>();
            conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
            SQLiteDatabase db=conexion.getReadableDatabase();
            DevolucionesJSON devoluciones=null;


            Cursor cursor= db.rawQuery("select * from devoluciones_json where migrado like '%no%'", null);

            while (cursor.moveToNext()){
                devoluciones= new DevolucionesJSON();
                devoluciones.setId(cursor.getInt(0));
                devoluciones.setCodCliente(cursor.getString(1));
                devoluciones.setDescripcion(cursor.getString(2));
                devoluciones.setJson(cursor.getString(3));
                devoluciones.setMigrado(cursor.getString(4));


                ListaDevoluciones.add(devoluciones);
                db.close();
            }

        }
        if(parametro.equals("fecha")){
            ListaDevoluciones=new ArrayList<>();
            conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
            SQLiteDatabase db=conexion.getReadableDatabase();
            DevolucionesJSON devoluciones=null;

            Cursor cursor= db.rawQuery("select * from devoluciones_json order by fecha", null);

            while (cursor.moveToNext()){
                devoluciones= new DevolucionesJSON();
                devoluciones.setId(cursor.getInt(0));
                devoluciones.setCodCliente(cursor.getString(1));
                devoluciones.setDescripcion(cursor.getString(2));
                devoluciones.setJson(cursor.getString(3));
                devoluciones.setMigrado(cursor.getString(4));


                ListaDevoluciones.add(devoluciones);
                db.close();
            }

        }
        return ListaDevoluciones;
    }

    @Override
    public void onClick(View v) {
        int idView=v.getId();
        if(idView==R.id._devoluciones_btn_crear){
            irCrearDevoluciones();
        }
        if(idView==R.id._devoluciones_btn_sincronizar){
            sb=new StringBuilder();

            ArrayList<DevolucionesJSON>jsonArrayList=clienteAdapter.seleccionadosLista;
            ArrayList<DevolucionesJSON>jsonEnviar=new ArrayList<>();
            for (DevolucionesJSON devolucion:jsonArrayList
            ) {
                // System.out.println(devolucion.);
                if(!devolucion.getMigrado().equals("si")){

                    // sb.append(devolucion.getJson());
                    jsonEnviar.add(devolucion);
                    //jsonEnviar=sb.toString();
                    //sb.append("\n");
                }
            }
            if(clienteAdapter.seleccionadosLista.size()>0){
                SincronizarDevoluciones go=null;
                for (int i = 0; i < jsonEnviar.size(); i++) {
                    // go=new SincronizarDevoluciones(jsonEnviar.get(i));
                    System.out.println(i+": "+jsonEnviar.get(i).getDescripcion());
                    new TaskDoco(true).execute(String.valueOf(jsonEnviar.get(i).getId()));

                }
            }else{
                System.out.println("Seleccione una devolucion");
            }
        }
    }

    private void capturarSelectRecyclerViewJSON(){
        sb=new StringBuilder();

        ArrayList<DevolucionesJSON>jsonArrayList=clienteAdapter.seleccionadosLista;
        ArrayList<DevolucionesJSON>jsonEnviar=new ArrayList<>();
        for (DevolucionesJSON devolucion:jsonArrayList
        ) {
            if(!devolucion.getMigrado().equals("si")){
                // sb.append(devolucion.getJson());
                jsonEnviar.add(devolucion);
                //jsonEnviar=sb.toString();
                //sb.append("\n");
            }
        }
        if(clienteAdapter.seleccionadosLista.size()>0){
            SincronizarDevoluciones go=null;
            for (int i = 0; i < jsonEnviar.size(); i++) {
                // go=new SincronizarDevoluciones(jsonEnviar.get(i));

            }
        }else{
            System.out.println("Seleccione una devolucion");
        }
    }
    private void irCrearDevoluciones() {
        /*Intent intent= new Intent(getApplicationContext(), CrearDevolucionesActivity.class);
        startActivity(intent);*/
        Intent intent= new Intent(getApplicationContext(), OperacionesDevolucionesActivity.class);
        startActivity(intent);
    }
    class EnviarDevoluciones extends AsyncTask<String, Void, String>{

        public EnviarDevoluciones(boolean showLoading){
            super();

        }
        @Override
        protected void onPreExecute() {
            _progreso_lista.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("exito")){
                if(!cargarPreferencias().equals("0")){
                    String id=cargarPreferencias();
                    GuardarDevolucionesSincronizadas go= new GuardarDevolucionesSincronizadas(getApplicationContext(), id );
                    if(go.iniciarOperaciones()){
                        _progreso_lista.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Sincronizacion exitosa", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    _progreso_lista.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ERRO: Usuario no reconocido", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Error durante la sincronizacion", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado="error";
            System.out.println("Se actualizara: "+strings[0]);
            ArrayList<DevolucionesJSON>jsonEnviar= getDevolucionesJSON(strings[0]);
            String jsonRequest=jsonEnviar.get(0).getJson();
            System.out.println("Se enviara: "+jsonRequest);
            SincronizarDevoluciones sincronizarDevoluciones=new SincronizarDevoluciones(jsonRequest);
            if(sincronizarDevoluciones.setNewDevolucion()){
                if(actualizarDevolucionCabecera(strings[0])){
                    System.out.println("cabecera actualizada");
                }
                if(actualizarSincronizacion(strings[0])){
                    System.out.println("json actualizado");
                }

                resultado="exito";
            }else{
                Toast.makeText(getApplicationContext(),"Error al insertar la devoolucion", Toast.LENGTH_SHORT).show();
            }

            //sincronizarDevoluciones=new SincronizarDevoluciones(strings[0]);

            return resultado;
        }


    }
    class TaskDoco extends AsyncTask<String, Integer, Integer> {
        public TaskDoco(boolean showLoading){
            super();
        }


        @Override
        protected void onPreExecute() {
            _progreso_lista.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            //ArrayList<Devoluciones>devoluciones= getDevoluciones(strings[0]);
            System.out.println("id para buscar DOCO: "+strings[0]);
            codigoDoco = getCodigoDOCOSOAP();
            SQLiteDatabase db = conexion.getWritableDatabase();

                String[] parametros = {String.valueOf(strings[0])};
              //  String[]parametros2={"128"};
                ContentValues values = new ContentValues();
               values.put("DOCO", codigoDoco);
                System.out.println(codigoDoco+"___actualizar");
               //values.put("DOCO", 129);
                db.update("devoluciones", values, "id=?", parametros);
                db.update("devoluciones_detalle", values, "DOCO=?", parametros);
                System.out.println("actualizado");

                db.close();

            return codigoDoco;
        }

        @Override
        protected void onPostExecute(Integer s) {
            _progreso_lista.setVisibility(View.INVISIBLE);

            if(s>0){
                ActualizarJSON(s);
                System.out.println("exito");
               // recyclerView.setAdapter(setLayautManagerRecyclerView());
            }else{
                System.out.println("Error al ejecutar DOCO");
            }
            super.onPostExecute(s);
        }
    }

    private void ActualizarJSON(int doco) {
        //ArrayList<String>idDevolucionJson=new ArrayList<>();
        ArrayList<Devoluciones>devoluciones=getDevoluciones(String.valueOf(doco));
        if(devoluciones.size()>0){

        String[] idDevolucionJson= new String[devoluciones.size()];
        for (int i = 0; i < devoluciones.size(); i++) {
            String fin_detalle;
            int codigoDoco=devoluciones.get(i).getDOCO();
            int idDevolucion=devoluciones.get(i).getId();
            System.out.println("DOCO :"+codigoDoco);
            System.out.println("ID: "+idDevolucion);
            String detalle = "";
            detalle+=getDetalle(String.valueOf(codigoDoco));
            if(detalle.length()>0){
                fin_detalle="["+detalle.substring(0, detalle.length()-2)+"]";
                ArrayList<Devoluciones>devolucionesParametro=getDevoluciones(String.valueOf(doco));
                String jsonPeticion=peticionJSON(fin_detalle,devolucionesParametro,codigoDoco);
                System.out.println(jsonPeticion);
                SQLiteDatabase db=conexion.getWritableDatabase();
                System.out.println(i);
                String id=devoluciones.get(i).getId().toString();
                String [] parametros={id};
                ContentValues values= new ContentValues();
                values.put("json_request",jsonPeticion);

                db.update("devoluciones_json",values,"id=?",parametros);
                System.out.println("actualizado");
                idDevolucionJson[i]=id;

                new EnviarDevoluciones(true).execute(idDevolucionJson[i]);
            }

        }
    }else{
        Toast.makeText(getApplicationContext(),"Error inesperado", Toast.LENGTH_SHORT).show();
    }
    }
    private boolean actualizarSincronizacion(String id) {
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

    private boolean actualizarDevolucionCabecera(String id) {
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
    public ArrayList<DevolucionesJSON> getDevolucionesJSON(String id){
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
    public ArrayList<Devoluciones> getDevoluciones(String id){
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

        }
        return devolucionesLista;
    }
    public ArrayList<Devoluciones> getDevoluciones(){
        Devoluciones devoluciones=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        devolucionesLista= new ArrayList<Devoluciones>();
        Cursor cursor=db.rawQuery("Select * from devoluciones where migrado like'%no%'", null);

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
    public String peticionJSON(String json,ArrayList<Devoluciones>devoluciones,int codigoDoco){
        int idDevolucion=1;
        int cantidadFilas=0;
        String jsonObject="";
        // String parametro=comboClientes.getSelectedItem().toString();

        //cantidad de filas del detalle
        //DRQJ es ka fecha de devolucion, tiene que convertirse en juliano JDE
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
    public String getDetalle(String id){
        StringBuffer  cadena;
        String detalleCadena="";

        ListaDetalle =getDetalleDatos(id);
        String [] detalle= new String[ListaDetalle.size()];
        for (int i = 0; i <ListaDetalle.size() ; i++) {
            detalle[i]="{\"KCOO\":\""+ListaDetalle.get(i).getKCOO()+"\", " +
                    "\"DCTO\":\"CD\", " +
                    "\"DOCO\":" + ListaDetalle.get(i).getDOCO() + ", " +
                    "\"LNID\":"+(ListaDetalle.get(i).getLNID())+", " +
                    "\"AN8\":" + ListaDetalle.get(i).getAN8() + ", " +
                    "\"AITM\":\""+ListaDetalle.get(i).getAITM()+"\", " +
                    "\"UORG\":-"+ListaDetalle.get(i).getUORG()+", " +
                    "\"LPRC\":0, " +
                    "\"UOM\":\"UN\", " +
                    "\"i55DEPR\":0, " +
                    "\"DRQJ\":"+ListaDetalle.get(i).getDRQJ()+", " +
                    "\"UPC3\":\" \", " +
                    "\"s55PROMFM\":\" \", " +
                    "\"LOCN\":\" \"}";
        }
        cadena = new StringBuffer();
        for (int x=0;x<detalle.length;x++){
            cadena =cadena.append(detalle[x]+", ");
        }
        detalleCadena=cadena.toString();


        return detalleCadena;
    }
    private ArrayList<Detalle> getDetalleDatos(String parametro) {
        SQLiteDatabase db=conexion.getReadableDatabase();
        Detalle cliente=null;
        ListaDetalle= new ArrayList<Detalle>();
        Cursor cursor= db.rawQuery("select * from devoluciones_detalle where DOCO like '%"+parametro+"%'", null);

        while (cursor.moveToNext()){
            cliente= new Detalle();
            cliente.setId(cursor.getInt(0));
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
            cliente.setCodarticulo(cursor.getString(15));


            ListaDetalle.add(cliente);
            db.close();
        }
        return ListaDetalle;

    }
    private int getCodigoDOCOSOAP(){
        final String NAMESPACE = "http://tempuri.org/";
        String URL="http://ajvapp-desa.ajvierci.com.py:8088/WSDevoluciones.svc?wsdl";
        final String METHOD_NAME = "ObtenerSiguienteDOCO";
        final String SOAP_ACTION ="http://tempuri.org/IWSDevoluciones/ObtenerSiguienteDOCO";
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapObject  resultsRequestSOAP=null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //   Se crea un objeto SoapObject para poder realizar la peticion
        //    para consumir el ws SOAP. El constructor recibe
        //    el namespace. Por lo regular el namespace es el dominio
        //    donde se encuentra el web service
        request = new SoapObject(NAMESPACE, METHOD_NAME);

        //  request.addProperty("codCia", "00019");

        //    Se crea un objeto SoapSerializationEnvelope para serealizar la
        //    peticion SOAP y permitir viajar el mensaje por la nube
        //    el constructor recibe la version de SOAP
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        //Se envuelve la peticion soap
        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        HttpTransportSE transporte = new HttpTransportSE(URL,300000);

        try {
            //Hace la llamada al ws
            transporte.call(SOAP_ACTION, envelope);

            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //resultsRequestSOAP = (SoapObject)envelope.getResponse();

            resultsRequestSOAP = (SoapObject) envelope.getResponse();
            String json=resultsRequestSOAP.getProperty(2).toString().replaceAll("\\[", "").replaceAll("\\]","");
            JSONObject objeto= new JSONObject(json);
            codigoDoco=Integer.parseInt(objeto.get("SiguienteDOCO").toString());
            /// System.out.println(resultsRequestSOAP.getProperty(2).toString());
            //Thread.sleep(7);
            //JSONObject json= new JSONObject(resultsRequestSOAP.getProperty(2).toString());

        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codigoDoco;
    }
}