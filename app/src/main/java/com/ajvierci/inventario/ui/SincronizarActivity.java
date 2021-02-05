package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Detalle;
import com.ajvierci.inventario.entidades.Devoluciones;
import com.ajvierci.inventario.entidades.DevolucionesJSON;
import com.ajvierci.inventario.retrofit.SincronizarDevoluciones;
import com.ajvierci.inventario.retrofit.articulos.ArticulosGO;
import com.ajvierci.inventario.retrofit.clientes.ClientesGO;
import com.ajvierci.inventario.utilidades.GuardarDevolucionesSincronizadas;
import com.google.gson.Gson;

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
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SincronizarActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Detalle>ListaDetalle;
    private Button btn_sincronizar;
    private int idDevolucion=0;
    private CheckBox chk_clientes, chk_articulos, ch_devoluciones;
    private int codigoDoco=0;
    private boolean clientes= false;
    private boolean articulos=false;
    private boolean devoluciones=false;
    private ConexionSQLiteHelper conexion;
    private ArrayList<DevolucionesJSON>devolucionesJSONLista;
    private ArrayList<Devoluciones>devolucionesLista;
    private ArrayList<Devoluciones>devolucionesNoMigradas;

    private ProgressBar progressBar;
    private SharedPreferences prefrences;
    boolean cargandoCliente=false;
    boolean cargandoArticulo=false;
    boolean cargandoDevoluciones=false;
    TextView cargandoTitulo;
    SincronizarDevoluciones sincronizarDevoluciones;
    MovimientoBD db=new MovimientoBD();
    ArrayList<String>docoLista=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
        conectarVistas();
        setPreferences();
        listenner();

    }


    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private String cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("id","0");
        return recordar;

    }

    private void conectarVistas() {
        this.btn_sincronizar=findViewById(R.id._sincronizar_btn_sincronizar);
        this.chk_clientes=findViewById(R.id._sincronizar_chkClientes);
        this.chk_articulos=findViewById(R.id._sincronizar_chkArticulos);
        this.ch_devoluciones=findViewById(R.id._sincronizar_chkDevoluciones);
        this.progressBar=findViewById(R.id._sincronizar_progreso);
        this.cargandoTitulo=findViewById(R.id._sincronizar_activity_titulo_cargando);
    }
    private void listenner() {
        this.btn_sincronizar.setOnClickListener(this);
    }
    public void itemSeleccionado(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch(v.getId()) {
            case R.id._sincronizar_chkClientes:
                if (checked){
                    this.clientes=true;
                }
                else{
                    this.clientes=false;
                }
                break;
            case R.id._sincronizar_chkArticulos:
                if (checked){
                    this.articulos=true;
                }
                else{
                    this.articulos=false;
                }
                break;
            case R.id._sincronizar_chkDevoluciones:
                if (checked){
                    this.devoluciones=true;
                }
                else{
                    this.devoluciones=false;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int idView=v.getId();
        if(idView==R.id._sincronizar_btn_sincronizar){
            btn_sincronizar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            if(clientes){
                this.cargandoTitulo.setText("Aguarde, sincronizando datos...esto puede tardar.");
                cargandoCliente=true;
                sincroinzarClientes();
            }
            if(articulos){
                cargandoArticulo=true;
                this.cargandoTitulo.setText("Aguarde, sincronizando datos...esto puede tardar.");
                sincronizarArticulos();
            }
            if(devoluciones){
                cargandoDevoluciones=true;
                this.cargandoTitulo.setText("Aguarde, sincronizando datos...esto puede tardar.");
                sincronizarDevoluciones();
            }

        }
    }
    private void comprobar(){
           if(cargandoCliente==false && cargandoArticulo==false &&cargandoDevoluciones==false){
                this.cargandoTitulo.setText("");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Apliación Sincronizada!", Toast.LENGTH_SHORT).show();
                btn_sincronizar.setVisibility(View.VISIBLE);
            }
    }
    private void sincronizarDevoluciones() {
        comprobarExisteDevoluciones();
        //new TaskDoco().execute();
    }

    private void comprobarExisteDevoluciones() {
        devolucionesNoMigradas= getDevoluciones();
        if(devolucionesNoMigradas.size()!=0){
            new TaskDoco().execute();
        }else{
            cargandoDevoluciones=false;
            comprobar();

        }
    }

    class TaskDoco extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int contar=0;
            int resultado=0;
            //devolucionesNoMigradas= getDevoluciones();

            if(devolucionesNoMigradas==null){
                return 0;
            }
            docoLista=getCodigoDOCOSOAP(devolucionesNoMigradas.size());
            if(devolucionesNoMigradas.size()==docoLista.size()){
                for (int i = 0; i < devolucionesNoMigradas.size(); i++) {
                    try {
                        int idDevolucion=devolucionesNoMigradas.get(i).getId();
                        SQLiteDatabase db = conexion.getWritableDatabase();

                        String[] parametros = {String.valueOf(idDevolucion)};
                        ContentValues values = new ContentValues();
                        int nuevoDoco=Integer.parseInt(docoLista.get(i));
                        values.put("DOCO", nuevoDoco);
                        System.out.println("Nuevo Doco"+nuevoDoco);
                        db.update("devoluciones", values, "id=?", parametros);
                        db.update("devoluciones_detalle", values, "DOCO=?", parametros);
                        contar++;
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Error: "+e, Toast.LENGTH_SHORT).show();
                        resultado=0;
                        return resultado;
                    }
                }
                if(contar==devolucionesNoMigradas.size()){
                    resultado=1;
                }
            }else{
                resultado=0;
                return resultado;
            }
        return resultado;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if(s==0){
                progressBar.setVisibility(View.INVISIBLE);
                btn_sincronizar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Ocurrio un problema capturando el codigo DOCO, intentelo nuevamente.", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("Voy a actualizar");
                ActualizarJSON();
            }
        }
    }

    private ArrayList<String> getCodigoDOCOSOAP(int cantidad){
        ArrayList<String>doco=new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
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
                doco.add(String.valueOf(codigoDoco));
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
        }

        return doco;
    }
    public void ActualizarJSON(){
        if(docoLista.size()!=0){
            System.out.println("Entro en actualizar JSON");
            ArrayList<Devoluciones>devoluciones=getDevoluciones();
            String[] idDevolucionJson= new String[devoluciones.size()];
            for (int i = 0; i < devoluciones.size(); i++) {
                String fin_detalle;
                int codigoDoco = devoluciones.get(i).getDOCO();
                int idDevolucion = devoluciones.get(i).getId();
                System.out.println("DOCO :" + codigoDoco);
                System.out.println("ID: " + idDevolucion);
                String detalle = "";
                detalle += getDetalle(String.valueOf(codigoDoco));

                if (detalle.length() > 0) {
                    fin_detalle = "[" + detalle.substring(0, detalle.length() - 2) + "]";
                    ArrayList<Devoluciones> devolucionesParametro = getDevoluciones(String.valueOf(idDevolucion));
                    String jsonPeticion = peticionJSON(fin_detalle, devolucionesParametro, codigoDoco);
                    System.out.println(jsonPeticion);
                    SQLiteDatabase db = conexion.getWritableDatabase();

                    System.out.println(i);
                    String id = devoluciones.get(i).getId().toString();
                    String[] parametros = {id};
                    ContentValues values = new ContentValues();
                    values.put("json_request", jsonPeticion);

                    db.update("devoluciones_json", values, "id=?", parametros);
                    System.out.println("actualizado");
                    idDevolucionJson[i] = id;
                    new TaskEnviarJSON(true).execute(idDevolucionJson[i]);
                }
            }
        }else{
            System.out.println("No");
        }
        //ArrayList<String>idDevolucionJson=new ArrayList<>();

      /* ArrayList<Devoluciones>devoluciones=getDevoluciones();
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
                ArrayList<Devoluciones>devolucionesParametro=getDevoluciones(String.valueOf(idDevolucion));
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
                new TaskEnviarJSON(true).execute(idDevolucionJson[i]);
            }

        }*/

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
    private void sincronizarArticulos() {
        /*ArticulosGO go=new ArticulosGO(getApplicationContext());
        go.getAritculos();*/
        new sincronizarArticulos().execute();
    }

    private void sincroinzarClientes() {
        new sincronizarClientes().execute();
    }

   private void sincronizarClientes() {

   try
   {

       OkHttpClient client = new OkHttpClient().newBuilder()
               .build();
       MediaType mediaType = MediaType.parse("text/xml;charset=\"utf-8\"");
       RequestBody body = RequestBody.create(mediaType, "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n <Body>\r\n <ObtenerClientes xmlns=\"http://tempuri.org/\">\r\n <codCia>00001</codCia>\r\n </ObtenerClientes>\r\n </Body>\r\n</Envelope>");
       Request request = new Request.Builder()
               .url("http://ajvapp-desa.ajvierci.com.py:8088/WSDevoluciones.svc?wsdl")
               .method("POST", body)
               .addHeader("SOAPAction", "http://tempuri.org/IWSDevoluciones/ObtenerClientes")
               .addHeader("Content-Type", "text/xml;charset=\"utf-8\"")
               .build();
       Response response = client.newCall(request).execute();
       if (response.isSuccessful()) {
           String respuesta = response.body().string();
           if(db.borrarTablaClientes(conexion)){
               //System.out.println(respuesta.indexOf("<a:ListaJson>"));
               int inicioRest=respuesta.indexOf("<a:ListaJson>");
               int finRest=respuesta.indexOf("</a:ListaJson>");
                //System.out.println(respuesta.indexOf("</a:ListaJson>")) 3996671;
               String[] json = respuesta.substring((inicioRest+13), finRest).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").split("\\},");
               String elemento = "";
               int contador = 0;
               for (int i = 0; i < json.length; i++) {
                   elemento = json[i].replaceAll("\\}", "");
                //System.out.println("{" + elemento + "}");
                   JSONObject jsonObject = new JSONObject("{" + elemento + "}");
                   conexion= new ConexionSQLiteHelper(getApplicationContext(),"bd_inventario", null, 1);
                   if(db.sincronizarClientes(conexion,jsonObject)){
                       contador++;
                   }
                   //Gson g = new Gson();
                   //ResponseUnRegistro modeloResponse = g.fromJson("{" + elemento + "}", ResponseUnRegistro.class);
               }
                   System.out.println("contador = " + contador);
           }
       }else{
        Toast.makeText(getApplicationContext(), "Error: Conexion con el servidor con problemas.", Toast.LENGTH_SHORT).show();
       }
   }
   catch (Exception e){
       System.out.println("e = " + e);
   }
    }

    private void sincronizaArticulos() {
        try
        {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/xml;charset=\"utf-8\"");
            RequestBody body = RequestBody.create(mediaType, "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n <Body>\r\n <ObtenerArticulos xmlns=\"http://tempuri.org/\">\r\n <codCia>001</codCia>\r\n </ObtenerArticulos>\r\n </Body>\r\n</Envelope>");
            Request request = new Request.Builder()
                    .url("http://ajvapp-desa.ajvierci.com.py:8088/WSDevoluciones.svc?wsdl")
                    .method("POST", body)
                    .addHeader("SOAPAction", "http://tempuri.org/IWSDevoluciones/ObtenerArticulos")
                    .addHeader("Content-Type", "text/xml;charset=\"utf-8\"")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String respuesta = response.body().string();
                if(db.borrarTablaArticulos(conexion)){
                    //System.out.println(respuesta.indexOf("<a:ListaJson>"));
                    int inicioRest=respuesta.indexOf("<a:ListaJson>");
                    int finRest=respuesta.indexOf("</a:ListaJson>");
                    //System.out.println(respuesta.indexOf("</a:ListaJson>")) 3996671;
                    String[] json = respuesta.substring((inicioRest+13), finRest).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").split("\\},");
                    String elemento = "";
                    int contador = 0;
                    for (int i = 0; i < json.length; i++) {
                        elemento = json[i].replaceAll("\\}", "");
                        //System.out.println("{" + elemento + "}");
                        JSONObject jsonObject = new JSONObject("{" + elemento + "}");
                        conexion= new ConexionSQLiteHelper(getApplicationContext(),"bd_inventario", null, 1);
                        if(db.sincronizarArticulos(conexion,jsonObject)){
                            contador++;
                        }
                        //Gson g = new Gson();
                        //ResponseUnRegistro modeloResponse = g.fromJson("{" + elemento + "}", ResponseUnRegistro.class);
                    }
                    System.out.println("contador = " + contador);


                }
            }
        }
        catch (Exception e){
            System.out.println("e = " + e);
        }
    }


    class sincronizarClientes extends AsyncTask<String , Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            cargandoCliente=true;
            sincronizarClientes();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cargandoCliente=false;
            comprobar();
        }
    }
   class sincronizarArticulos extends AsyncTask<String, Void, String>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           cargandoArticulo=true;
       }

       @Override
       protected String doInBackground(String... strings) {
           sincronizaArticulos();
           return null;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           cargandoArticulo=false;
           comprobar();
       }
   }

    private class TaskEnviarJSON extends AsyncTask<String, Void, String>{
        public TaskEnviarJSON(boolean showLoading){
            super();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {

            System.out.println("ejecutando...");
            int contador=0;
                System.out.println(strings[0]);
              //  System.out.println(0);
                System.out.println("cantidad "+strings.length);;
                ArrayList<DevolucionesJSON>devolucionesEnviar=getDevolucionesJSON(strings[0]);
                System.out.println("tamaño consulta "+devolucionesEnviar.size());
                String json=devolucionesEnviar.get(0).getJson();
                sincronizarDevoluciones=new SincronizarDevoluciones(json);
                if(sincronizarDevoluciones.setNewDevolucion()){
                    System.out.println("Entre");
                    actualizarDevolucionCabecera(strings[0]);
                    actualizarSincronizacion(strings[0]);
                }else{
                    System.out.println("error al sincronizar datos");
                }

            System.out.println("Fin ");
           return String.valueOf(contador);

        }

        @Override
        protected void onPostExecute(String s) {
            if(!cargarPreferencias().equals("0")){
                cargandoDevoluciones=false;
                comprobar();
                String id=cargarPreferencias();
                GuardarDevolucionesSincronizadas go= new GuardarDevolucionesSincronizadas(getApplicationContext(), id);
                if(go.iniciarOperaciones()){
                    System.out.println("Fin_actualizar migracion");
                    System.out.println(s);
                }
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "ERRO: Usuario no reconocido", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    private void actualizarSincronizacion(String id) {
       try {
           SQLiteDatabase db=conexion.getWritableDatabase();

           String [] parametros={id};
           ContentValues values= new ContentValues();
           values.put("migrado","si");

           db.update("devoluciones_json",values,"id=?",parametros);
           System.out.println("actualizado");
           db.close();
       }catch (Exception e){
           System.out.println("Exepction actualizarSincronizacion "+e);
       }
    }

    private void actualizarDevolucionCabecera(String id) {
        try {
            SQLiteDatabase db=conexion.getWritableDatabase();

            String [] parametros={id};
            ContentValues values= new ContentValues();
            values.put("migrado","si");

            db.update("devoluciones",values,"id=?",parametros);
            System.out.println("actualizado");
            db.close();
        }catch (Exception e){
            System.out.println("Exception ActualizarDevolucionCabecera "+e);
        }
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
        }
        return devolucionesLista;
    }

    public ArrayList<Devoluciones> getDevoluciones(String parametro){
        Devoluciones devoluciones=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        devolucionesLista= new ArrayList<Devoluciones>();
        Cursor cursor=db.rawQuery("Select * from devoluciones where id like'%"+parametro+"%'", null);

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
    public ArrayList<DevolucionesJSON> getDevolucionesJSON(){
        DevolucionesJSON devolucionesJSON=null;
        SQLiteDatabase db=conexion.getReadableDatabase();
        devolucionesJSONLista= new ArrayList<DevolucionesJSON>();
        Cursor cursor=db.rawQuery("Select * from devoluciones_json where migrado like'%no%'", null);

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
}