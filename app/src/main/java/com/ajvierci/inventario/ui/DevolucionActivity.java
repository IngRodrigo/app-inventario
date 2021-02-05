package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
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
import com.ajvierci.inventario.adapter.DetalleAdapter;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Detalle;
import com.ajvierci.inventario.entidades.DevolucionIndividual;
import com.ajvierci.inventario.entidades.Devoluciones;
import com.ajvierci.inventario.entidades.DevolucionesJSON;
import com.ajvierci.inventario.retrofit.SincronizarDevoluciones;
import com.ajvierci.inventario.utilidades.Constantes;
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

public class DevolucionActivity extends AppCompatActivity implements View.OnClickListener {
private String parametro, aitm;
private TextView txt_devolucion_id, txt_empresa, txt_doco;
private RecyclerView recyclerView;
private ArrayList<DevolucionIndividual>listDatos;
private ArrayList<Detalle>ListaDetalle;
private ArrayList<DevolucionIndividual>ListaDevoluciones;
private DevolucionIndividual devolucionesDetalles;
private DetalleAdapter detalleAdapter;
private Button _btn_sincronizar;
private ProgressBar _progreso;

private ConexionSQLiteHelper conexion;
private ArrayList<DevolucionesJSON>devolucion;
private int codigoDoco=0;

private SharedPreferences prefrences;
MovimientoBD movimientoBD=new MovimientoBD();




@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
        this.parametro=getIntent().getExtras().getString("parametro");
        conectarVista();
        setPreferences();
        txt_devolucion_id.setText(parametro);
        setTextosVistas();
        listener();

    }
    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private String cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("id","0");
        return recordar;

    }
    private void listener() {
    this._btn_sincronizar.setOnClickListener(this);
    }

    private void setTextosVistas(){
        ArrayList<DevolucionIndividual>devoluciones=getDevolucionesCabecera(parametro);
        if(devoluciones.size()>0 && devoluciones!=null){
            txt_empresa.setText("CODIGO: "+devoluciones.get(0).getAN8()+" \n"+"CLIENTE: "+devoluciones.get(0).getEmpresa());
            txt_doco.setText(String.valueOf(devoluciones.get(0).getDOCO()));

            recyclerView.setAdapter(setLayautManagerRecyclerView(devoluciones));
        }else{
            txt_empresa.setText("**");
        }
    }
    private void conectarVista() {
        this.txt_devolucion_id=findViewById(R.id._devolucion_txt_id_devolucion);
        this.recyclerView=findViewById(R.id._devolucion_rercycler_detalle);
        this.txt_doco=findViewById(R.id._devolucion_txt_doco_devolucion);
        this.txt_empresa=findViewById(R.id._devolucion_txt_empresa);
        this._btn_sincronizar=findViewById(R.id._devoluciones_btn_sincronizar);
        this._progreso=findViewById(R.id._devolucion_progeso);
        this._btn_sincronizar.setVisibility(View.INVISIBLE);
}
    private DetalleAdapter setLayautManagerRecyclerView(ArrayList<DevolucionIndividual>devolucionIndividual) {
       // this.parametro=getIntent().getExtras().getString("parametro");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
     //   ArrayList<Devol> listaDevoluciones=getDetalle(doco);
        if(devolucionIndividual!=null){
            if(devolucionIndividual.size()!=0){
                listDatos=new ArrayList<>();
                for (int i = 0; i < devolucionIndividual.size(); i++) {

                    //devoluciones=new DevolucionesJSON(i,"00001","Test Rodrgo", "{json↓}","si","120266");
                    devolucionesDetalles= new DevolucionIndividual();
                    devolucionesDetalles.setDOCO(devolucionIndividual.get(i).getDOCO());
                    devolucionesDetalles.setAN8(devolucionIndividual.get(i).getAN8());
                    devolucionesDetalles.setFecha(devolucionIndividual.get(i).getFecha());
                    devolucionesDetalles.setEmpresa(devolucionIndividual.get(i).getEmpresa());
                    devolucionesDetalles.setCodigo_aticulo(devolucionIndividual.get(i).getCodigo_aticulo());
                    devolucionesDetalles.setDescripcionArticulo(devolucionIndividual.get(i).getDescripcionArticulo());
                    devolucionesDetalles.setCantidad(devolucionIndividual.get(i).getCantidad());
                    devolucionesDetalles.setIdDetalle(devolucionIndividual.get(i).getIdDetalle());
                    devolucionesDetalles.setId_devolucion(devolucionIndividual.get(i).getId_devolucion());
                    devolucionesDetalles.setJson(devolucionIndividual.get(i).getJson());
                    listDatos.add(devolucionesDetalles);
                    //  System.out.println(listDatos.get(0).getDescripcion());
                }


            }else{
                Toast.makeText(getApplicationContext(), "Nada que mostrar", Toast.LENGTH_LONG).show();
            }
        }else{
            System.out.println("Nada que mostrar");
        }
        if(listDatos!=null && listDatos.size()!=0){
            detalleAdapter=new DetalleAdapter(listDatos);
        }
        return detalleAdapter;
    }

    private ArrayList<Detalle> getDetalle(int parametro) {
            SQLiteDatabase db=conexion.getReadableDatabase();
            Detalle cliente=null;
            ListaDetalle= new ArrayList<Detalle>();
            Cursor cursor= db.rawQuery("select * from devoluciones_detalle where DOCO = '"+parametro+"'", null);

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
    private ArrayList<DevolucionIndividual> getDevolucionesCabecera(String parametro){
    System.out.println("parametro = " + parametro);
    String query="select d.DOCO, d.AN8, d.DRQJ as 'fecha', d.ALPH as 'empresa', a.CodigoBarra as 'codigo_articulo', a.DescripcionArticulo, dtl.UORG as 'cantidad', dtl.id as 'id_detalle', d.id as 'idDevolucion', dj.json_request from devoluciones as d\n" +
                "INNER JOIN devoluciones_detalle as dtl on dtl.DOCO=d.DOCO\n" +
                "inner join articulos as a on a.NroArticuloERP=dtl.CODARTICULO\n" +
                "inner join devoluciones_json as dj on d.id=dj.id\n" +
                "where d.id="+parametro;

            conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
            SQLiteDatabase db=conexion.getReadableDatabase();
            DevolucionIndividual devolucionIndividual=null;
            ListaDevoluciones = new ArrayList<>();

            Cursor cursor= db.rawQuery(query, null);

            while (cursor.moveToNext()){
                devolucionIndividual= new DevolucionIndividual();
                devolucionIndividual.setDOCO(cursor.getInt(0));
                devolucionIndividual.setAN8(cursor.getInt(1));
                devolucionIndividual.setFecha(cursor.getInt(2));
                devolucionIndividual.setEmpresa(cursor.getString(3));
                devolucionIndividual.setCodigo_aticulo(cursor.getString(4));
                devolucionIndividual.setDescripcionArticulo(cursor.getString(5));
                devolucionIndividual.setCantidad(cursor.getInt(6));
                devolucionIndividual.setIdDetalle(cursor.getInt(7));
                devolucionIndividual.setId_devolucion(cursor.getInt(8));
                devolucionIndividual.setJson(cursor.getString(9));
                ListaDevoluciones.add(devolucionIndividual);
                // db.close();
            }

        return ListaDevoluciones;
    }

    @Override
    public void onClick(View view) {
        int idView=view.getId();

        if(idView==R.id._devoluciones_btn_sincronizar){
            devolucion=movimientoBD.getDevolucionesJSON(txt_devolucion_id.getText().toString(), conexion);
            if(devolucion.size()>0){
                if(devolucion.get(0).getMigrado().equals("no")){
                    new TaskDOCO(true).execute(txt_devolucion_id.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Esta devolución ya se encuentra sincronizada.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    class TaskDOCO extends AsyncTask<String, Void, String>{

    public TaskDOCO(boolean showLoading){
            super();
        }

        @Override
        protected void onPreExecute() {
            _progreso.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("id para buscar DOCO: "+strings[0]);

           if(getCodigoDOCOSOAP()>0){
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
               System.out.println("DOCO actualizado: "+codigoDoco);
               db.close();
           }else{
               Toast.makeText(getApplicationContext(),"Error al generar codigo DOCO",Toast.LENGTH_SHORT).show();
           }

            return String.valueOf(codigoDoco);
        }

        @Override
        protected void onPostExecute(String s) {
            _progreso.setVisibility(View.INVISIBLE);
            int codigo=Integer.parseInt(s);
            if(codigo>0){
                ActualizarJSON(codigoDoco);
            }else{
                System.out.println("Error al recibir doco");
            }
            super.onPostExecute(s);
        }
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
    private void ActualizarJSON(int doco) {
        //ArrayList<String>idDevolucionJson=new ArrayList<>();
        ArrayList<Devoluciones>devoluciones=movimientoBD.getDevoluciones(String.valueOf(doco), conexion);
        if(devoluciones.size()>0){

            String[] idDevolucionJson= new String[devoluciones.size()];
            for (int i = 0; i < devoluciones.size(); i++) {
                String fin_detalle;
                int codigoDoco=devoluciones.get(i).getDOCO();
                int idDevolucion=devoluciones.get(i).getId();
                System.out.println("DOCO :"+codigoDoco);
                System.out.println("ID: "+idDevolucion);
                String detalle = "";
                detalle+=getDetalle(codigoDoco);
                if(detalle.length()>0){
                    fin_detalle="["+detalle.substring(0, detalle.length()-2)+"]";
                    ArrayList<Devoluciones>devolucionesParametro=devoluciones;
                    String jsonPeticion= Constantes.peticionJSON(fin_detalle,devolucionesParametro,codigoDoco);
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
    class EnviarDevoluciones extends AsyncTask<String, Void, String>{

        public EnviarDevoluciones(boolean showLoading){
            super();

        }
        @Override
        protected void onPreExecute() {
            _progreso.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("exito")){
                if(!cargarPreferencias().equals("0")){
                    String id=cargarPreferencias();
                    GuardarDevolucionesSincronizadas go= new GuardarDevolucionesSincronizadas(getApplicationContext(), id);
                    if(go.iniciarOperaciones()){
                        _progreso.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Sincronizacion exitosa", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    _progreso.setVisibility(View.INVISIBLE);
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
            ArrayList<DevolucionesJSON>jsonEnviar=movimientoBD.getDevolucionesJSON(strings[0], conexion);
            String jsonRequest=jsonEnviar.get(0).getJson();
            System.out.println("Se enviara: "+jsonRequest);
            SincronizarDevoluciones sincronizarDevoluciones=new SincronizarDevoluciones(jsonRequest);
            if(sincronizarDevoluciones.setNewDevolucion()){
                if(movimientoBD.actualizarDevolucionCabecera(strings[0], conexion)){
                    System.out.println("cabecera actualizada");
                }
                if(movimientoBD.actualizarSincronizacion(strings[0], conexion)){
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
}