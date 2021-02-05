package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Articulos;
import com.ajvierci.inventario.entidades.Cliente;
import com.ajvierci.inventario.entidades.DetalleTemp;
import com.ajvierci.inventario.retrofit.DOCO.DocoGO;

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

public class CrearDevolucionesActivity extends AppCompatActivity implements View.OnClickListener {
private Spinner comboClientes, comboArticulos;
private Button btn_agregar_detalle, btn_guarda_devolucion;
private EditText editText_cantidad;
private ProgressBar progressBar;
private ArrayList<Cliente>ClienteLista;
private ArrayList<String>ListaClientes;
private ArrayList<Articulos>ArticulosLista;
private ArrayList<String>ListaArticulos;
private ArrayList<DetalleTemp>ListaDetalleTemp;
private ArrayList<Cliente>clientes;
private int idDevolucion;
private  int codigoDoco=0;

private boolean guardar=false;

private ConexionSQLiteHelper conexion;
MovimientoBD db=new MovimientoBD();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_devoluciones);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        conectarVista();
        instanciarBD();
        listenner();
        consultarListaClientes();
        consultarArticulos();
        obtenerListaArticulo();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ListaClientes);
        ArrayAdapter<CharSequence>adaptadorArticulo=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ListaArticulos);
        comboClientes.setAdapter(adaptador);
        comboArticulos.setAdapter(adaptadorArticulo);
    }



    private void conectarVista() {
        this.comboClientes=findViewById(R.id._crear_devoluciones_combo_clientes);
        this.comboArticulos=findViewById(R.id._crear_devoluciones_combo_Articulos);
        this.editText_cantidad=findViewById(R.id._crear_devoluciones_editText_cantidad);
        this.btn_agregar_detalle=findViewById(R.id._crear_devoluciones_btn_agregar);
        this.btn_guarda_devolucion=findViewById(R.id._crea_devoluciones_btn_guardar);
        this.progressBar=findViewById(R.id._crear_devoluciones_progreso);


    }
    private void listenner() {
        this.btn_agregar_detalle.setOnClickListener(this);
        this.btn_guarda_devolucion.setOnClickListener(this);
    }

    private void instanciarBD(){
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
    }
    public void consultarListaClientes() {
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cliente cliente=null;
        ClienteLista= getClientes();
        obtenerListaCliente();
    }
    private void consultarArticulos() {
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Articulos articulo=null;
        ArticulosLista=getArticulos();
        obtenerListaArticulo();
    }

    private void obtenerListaCliente() {
        ListaClientes= new ArrayList<String>();
        ListaClientes.add("Seleccione");

        for (int i=0; i<ClienteLista.size();i++){
            ListaClientes.add(String.valueOf(ClienteLista.get(i).getNroCuenta()));
        }
    }
    private void obtenerListaArticulo() {
        ListaArticulos= new ArrayList<String>();
        ListaArticulos.add("Seleccione");

        if(ListaArticulos!=null){
            for (int i=0; i<ArticulosLista.size();i++){
                ListaArticulos.add(String.valueOf(ArticulosLista.get(i).getNroArticuloERP()));
            }
        }
    }
    public ArrayList<Cliente> getClientes(){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cliente cliente=null;
        ClienteLista= new ArrayList<Cliente>();

        Cursor cursor= db.rawQuery("select * from clientes order by NroCuentaERP asc", null);

        while (cursor.moveToNext()){
            cliente= new Cliente();
            cliente.setId(cursor.getInt(0));
            cliente.setCodEmpresa(cursor.getString(1));
            cliente.setNroCuenta(cursor.getInt(2));
            cliente.setRazonSocial(cursor.getString(3));
            cliente.setDireccion(cursor.getString(4));
            cliente.setCodVendedor(cursor.getInt(5));
            cliente.setCodZona(cursor.getInt(6));
            cliente.setCodRamoERP(cursor.getString(7));
            cliente.setCodCanalERP(cursor.getString(8));
            cliente.setTelefono(cursor.getString(9));
            cliente.setRUC(cursor.getString(10));
            cliente.setNroListaPrecioERP(cursor.getString(11));
            cliente.setLimiteCredito(cursor.getDouble(12));
            cliente.setSaldoGuaranies(cursor.getDouble(13));
            cliente.setSaldoDolares(cursor.getDouble(14));
            cliente.setEstado(cursor.getString(15));
            cliente.setUbicacion(cursor.getString(16));
            cliente.setEmail(cursor.getString(17));
            cliente.setCodCondicionVentaERP(cursor.getString(18));

            ClienteLista.add(cliente);
            db.close();
        }
        return ClienteLista;
    }
    public ArrayList<Articulos> getArticulos(){
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Articulos articulo=null;
        ArticulosLista= new ArrayList<Articulos>();

        Cursor cursor= db.rawQuery("select * from articulos order by NroArticuloERP asc", null);

        while (cursor.moveToNext()){
            articulo= new Articulos();
            articulo.setId(cursor.getInt(0));
            articulo.setCodEmpresa(cursor.getString(1));
            articulo.setNroArticuloERP(cursor.getInt(2));
            ArticulosLista.add(articulo);
        }
        return ArticulosLista;
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

    public ArrayList<Cliente> getClientes(String Parametro){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cliente cliente=null;
        ClienteLista= new ArrayList<Cliente>();

        Cursor cursor= db.rawQuery("select * from clientes where NroCuentaERP = "+Parametro+" order by NroCuentaERP desc", null);

        while (cursor.moveToNext()){
            cliente= new Cliente();
            cliente.setId(cursor.getInt(0));
            cliente.setCodEmpresa(cursor.getString(1));
            cliente.setNroCuenta(cursor.getInt(2));
            cliente.setRazonSocial(cursor.getString(3));
            cliente.setDireccion(cursor.getString(4));
            cliente.setCodVendedor(cursor.getInt(5));
            cliente.setCodZona(cursor.getInt(6));
            cliente.setCodRamoERP(cursor.getString(7));
            cliente.setCodCanalERP(cursor.getString(8));
            cliente.setTelefono(cursor.getString(9));
            cliente.setRUC(cursor.getString(10));
            cliente.setNroListaPrecioERP(cursor.getString(11));
            cliente.setLimiteCredito(cursor.getDouble(12));
            cliente.setSaldoGuaranies(cursor.getDouble(13));
            cliente.setSaldoDolares(cursor.getDouble(14));
            cliente.setEstado(cursor.getString(15));
            cliente.setUbicacion(cursor.getString(16));
            cliente.setEmail(cursor.getString(17));
            cliente.setCodCondicionVentaERP(cursor.getString(18));

            ClienteLista.add(cliente);
            db.close();
        }
        return ClienteLista;

    }
    public ArrayList<Articulos> getArticulos(String Parametro){
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Articulos articulo=null;
        ArticulosLista= new ArrayList<Articulos>();

        Cursor cursor= db.rawQuery("select * from articulos where NroArticuloERP = "+Parametro+" order by NroArticuloERP desc", null);

        while (cursor.moveToNext()){
            articulo= new Articulos();
            articulo.setId(cursor.getInt(0));
            articulo.setCodEmpresa(cursor.getString(1));
            articulo.setNroArticuloERP(cursor.getInt(2));
            articulo.setDescripcionArticulo(cursor.getString(3));
            articulo.setCodMarcaERP(cursor.getString(4));
            articulo.setCodLineaERP(cursor.getString(5));
            articulo.setCodTipoERP(cursor.getString(6));
            articulo.setCodRegimenERP(cursor.getString(7));
            articulo.setCodigoBarra(cursor.getString(8));
            articulo.setCodigoLargo(cursor.getString(9));
            articulo.setCodCategoriaERP(cursor.getString(10));
            articulo.setCodProveedor(cursor.getInt(11));
            ArticulosLista.add(articulo);
            db.close();
        }
        return ArticulosLista;
    }

    @Override
    public void onClick(View v) {
        int idView=v.getId();
        guardar=true;
        if(idView==R.id._crear_devoluciones_btn_agregar){
            ArrayList<DetalleTemp>detalleTemps=getClientesTemp(comboClientes.getSelectedItem().toString());
            if(detalleTemps.size()>0){
                for (int i = 0; i < detalleTemps.size(); i++) {
                    int DOCO=detalleTemps.get(i).getDOCO();
                    if(comprobarDocoArticuloExiste(DOCO, comboArticulos.getSelectedItem().toString())){
                        Toast.makeText(getApplicationContext(),"El articulo ya fue cargado al detalle", Toast.LENGTH_SHORT).show();
                    }else{
                        System.out.println("agregar");
                        crearDevolucionDetalle();
                    }
                }
            }else{
                System.out.println("Agregar");
                crearDevolucionDetalle();
            }

        }
        if(idView==R.id._crea_devoluciones_btn_guardar){
         //   new Task().execute();
            guardarDevolucion();
        }

    }

    private boolean comprobarDocoArticuloExiste(int doco, String articulo) {

        boolean resultado=false;
        ArrayList<DetalleTemp>detalle_doco=getDetallePorDOCOTemp(doco);
        for (int i = 0; i < detalle_doco.size(); i++) {
            if(detalle_doco.get(i).getGetNroArticuloERP().equals(articulo)){
                resultado=true;
            }
        }
        return resultado;
    }

    private void crearDevolucionDetalle(){

        if(editText_cantidad.length()>0){
            int cantidadDevuelta=Integer.parseInt(editText_cantidad.getText().toString());
            if(cantidadDevuelta!=0){
                String parametroCod=comboClientes.getSelectedItem().toString();
                String parametroCodArticulo=comboArticulos.getSelectedItem().toString();

                if(parametroCod.equals("Seleccione")||parametroCodArticulo.equals("Seleccione")){
                    Toast.makeText(getApplicationContext(),"Los parametros de cliente y articulo no pueden estar vacios", Toast.LENGTH_LONG).show();
                }else{
                    ArrayList<Cliente>clientes=getClientes(parametroCod);
                    System.out.println("cliente insertado: "+clientes.get(0).getNroCuenta());
                    ArrayList<Articulos>articulos=getArticulos(parametroCodArticulo);
                    int clienteCod=getIdDevolucion()+1;
                    int idDetalleTemp=getCantidadFilasDetalleTemp()+1;
                    if( db.insertarTemporalDetalle(conexion, clientes, articulos,clienteCod, (cantidadDevuelta)*100, idDetalleTemp)){
                        Toast.makeText(getApplicationContext(), "Se agrego el articulo al detalle", Toast.LENGTH_SHORT).show();
                    }
                    editText_cantidad.setText("0");
                }

            }else{
                Toast.makeText(getApplicationContext(), "La cantidad no puede ser nula.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "La cantidad no puede ser nula.", Toast.LENGTH_SHORT).show();
        }
    }
    private void guardarDevolucion() {
        clientes=getClientes(comboClientes.getSelectedItem().toString());
        String fin_detalle;
        String detalle = "";

            detalle += getDetalle();
            fin_detalle="["+detalle.substring(0, detalle.length()-2)+"]";
            String peticionFinal=peticionJSON(fin_detalle, clientes);
            System.out.println(peticionFinal);
            String codCliente=String.valueOf(clientes.get(0).getNroCuenta());
            String descripcionCliente=clientes.get(0).getRazonSocial();
            String jsonRequest=peticionFinal;
            String migrado="no";
            //String fecha=MovimientoBD.obtenerFecha();
            System.out.println("codigo devolucion: "+getIdDevolucion());
            int cantidadFilas=getCantidadFilasDetalleTemp();
            System.out.println("Cantidad de lineas: "+getCantidadFilasDetalleTemp());
            MovimientoBD insertar= new MovimientoBD();

          /*  if(insertar.insertarDevolucionesJSON(conexion, codCliente, descripcionCliente, jsonRequest, migrado)){
                if(insertar.insertarDetalle(conexion, getClientesTemp(comboClientes.getSelectedItem().toString()))){
                    if(insertar.insertarDevolucionCabecera(conexion,getIdDevolucion()+1,(cantidadFilas*100),clientes)){
                        if(insertar.insertarIdDevolucion(conexion,getIdDevolucion()+1)){
                            if(insertar.borrarTablaTemporal(conexion)){
                                Toast.makeText(getApplicationContext(), "Exito al insertar", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

            }*/
    }

    public String peticionJSON(String json,ArrayList<Cliente>clientes){
        int idDevolucion=1;
        int cantidadFilas=0;
        String jsonObject="";
        String parametro=comboClientes.getSelectedItem().toString();
        if(!parametro.equals("Seleccione")){
            //cantidad de filas del detalle
            //DRQJ es ka fecha de devolucion, tiene que convertirse en juliano JDE
            jsonObject= "[{\"KCOO\":" + "\"" + clientes.get(0).getCodEmpresa() + "\", " +
                    "\"DCTO\":\"CD\"," +
                    "\"DOCO\":" + (getIdDevolucion()+1)  + ", " +
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

        }
        return jsonObject;
    }
    public String getDetalle(){
        StringBuffer  cadena;
        String detalleCadena="";
        String clienteCod=comboClientes.getSelectedItem().toString();
        ListaDetalleTemp =getClientesTemp(clienteCod);
            String [] detalle= new String[ListaDetalleTemp.size()];
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
            cadena = new StringBuffer();
            for (int x=0;x<detalle.length;x++){
                cadena =cadena.append(detalle[x]+", ");
            }
            detalleCadena=cadena.toString();


        return detalleCadena;


    }
    public ArrayList<DetalleTemp> getClientesTemp(String codigoClienteTemp){
        SQLiteDatabase db=conexion.getReadableDatabase();
        DetalleTemp cliente=null;
        ListaDetalleTemp= new ArrayList<DetalleTemp>();
        System.out.println(codigoClienteTemp);
        Cursor cursor= db.rawQuery("select * from detalle_temp where AN8 = "+codigoClienteTemp, null);

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
            cliente.setGetNroArticuloERP(cursor.getString(15));


            ListaDetalleTemp.add(cliente);
            db.close();
        }
        return ListaDetalleTemp;
    }
    public ArrayList<DetalleTemp> getDetallePorDOCOTemp(int doco){
        SQLiteDatabase db=conexion.getReadableDatabase();
        DetalleTemp cliente=null;
        ListaDetalleTemp= new ArrayList<DetalleTemp>();
        Cursor cursor= db.rawQuery("select * from detalle_temp where DOCO = '"+doco+"'", null);

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
            cliente.setGetNroArticuloERP(cursor.getString(15));


            ListaDetalleTemp.add(cliente);
            db.close();
        }
        return ListaDetalleTemp;
    }

    private int getCodigoDoco() {

        int codigoDoco=0;
        DocoGO go= new DocoGO(getApplicationContext());
        JSONObject jsonObject=go.getDOCO();
        if(jsonObject!=null){
            try {
                codigoDoco=Integer.parseInt(jsonObject.get("SiguienteDOCO").toString());
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(),"Error al recuperar codigoDOCO "+e,Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Error al parsear JSON getCodigoDOCO ", Toast.LENGTH_LONG).show();
            return codigoDoco;
        }
        return codigoDoco;
    }


    class Task extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            codigoDoco= getCodigoDOCOSOAP();
            System.out.println("Codigo doco es: "+codigoDoco);
            return codigoDoco;
        }

        @Override
        protected void onPostExecute(Integer s) {
            progressBar.setVisibility(View.INVISIBLE);
            guardarDevolucion();
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
}