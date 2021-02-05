package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Articulos;
import com.ajvierci.inventario.entidades.Cliente;
import com.ajvierci.inventario.entidades.Detalle;

import com.ajvierci.inventario.entidades.Devoluciones;
import com.ajvierci.inventario.utilidades.GuardarDevolucionesSincronizadas;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class OperacionesDevolucionesActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner comboClientes, comboArticulos;
    private Button btn_agregar, btn_guardar;
    private EditText txt_cantidad;

    private ConexionSQLiteHelper conexion;
    private ArrayList<Cliente>ClienteLista;
    private ArrayList<String>ListaClientes;
    ArrayList<Cliente>clienteSelect;
    ArrayList<Articulos>articulosSelect;
    private ListView listView;
    private ArrayList<String>lista;
    private ArrayList<Detalle>listaDetalle;
    private ArrayAdapter<String>adapterDetalleTemp=null;

    private ArrayList<String>ListaArticulos;
    private ArrayList<Articulos>ArticulosLista;
    private ArrayList<String>codigoBarraLista= new ArrayList<>();
    private SharedPreferences prefrences;
    int comprobarArticulo=1;
    MovimientoBD insertar= new MovimientoBD();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_devoluciones);

        conectarVistas();
        setPreferences();


        listenner();
        lista=new ArrayList<>();//String para la lista a mostrar
        listaDetalle= new ArrayList<>();//objeto detalle para guardar en la base de datos

        if(adapterDetalleTemp==null){
        adapterDetalleTemp=new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lista
        );
        listView.setAdapter(adapterDetalleTemp);

    }

    }
    // Creamos la SharedPreferences
    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private String cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("id","0");
        return recordar;

    }
    private void instanciarBD(){
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
    }

    private void conectarVistas() {
        instanciarBD();
        this.listView=findViewById(R.id._operaciones_lista_articulos);
        this.btn_agregar=findViewById(R.id._operaciones_btn_agregar);
        this.btn_guardar=findViewById(R.id._operaciones_btn_guardar);
        this.comboClientes=findViewById(R.id._operaciones_spinner);
        this.comboArticulos=findViewById(R.id._operaciones_spinner_articulos);
        this.txt_cantidad=findViewById(R.id._operaciones_editText_cantidad);
        consultarListaClientes();
        consultarArticulos();

        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ListaClientes);
        ArrayAdapter<CharSequence> adaptadorArticulo=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ListaArticulos);
        comboClientes.setAdapter(adaptador);
        comboArticulos.setAdapter(adaptadorArticulo);
        comboClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Display toast message
             if(i==0){
                 Toast.makeText(getApplicationContext(), "Favor seleccione un elemento", Toast.LENGTH_SHORT).show();
             }else{
                 String elemento=adapterView.getItemAtPosition(i).toString();
                 System.out.println(elemento);
             }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        comboArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Toast.makeText(getApplicationContext(), "Favor seleccione un elemento", Toast.LENGTH_SHORT).show();
                }else{
                    String elemento=adapterView.getItemAtPosition(i).toString();
                    System.out.println(elemento);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void listenner(){
        this.btn_guardar.setOnClickListener(this);
        this.btn_agregar.setOnClickListener(this);
    }

    public void consultarListaClientes() {
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cliente cliente=null;
        ClienteLista= getClientes();
        obtenerListaCliente();
    }
    public void consultarArticulos() {
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Articulos articulo=null;
        ArticulosLista=getArticulos();
        obtenerListaArticulo();
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

        }
        db.close();
        return ClienteLista;
    }
    public ArrayList<Cliente> getClientes(String codigo){
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cliente cliente=null;
        ClienteLista= new ArrayList<Cliente>();

        Cursor cursor= db.rawQuery("select * from clientes where NroCuentaERP="+codigo, null);

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

    private void obtenerListaCliente() {
        ListaClientes= new ArrayList<String>();
        ListaClientes.add("Seleccione");

        for (int i=0; i<ClienteLista.size();i++){
            ListaClientes.add(String.valueOf(ClienteLista.get(i).getNroCuenta())+" - "+String.valueOf(ClienteLista.get(i).getRazonSocial()));
        }
    }


    private void obtenerListaArticulo() {
        ListaArticulos= new ArrayList<String>();
        ListaArticulos.add("Seleccione");

        if(ListaArticulos!=null){
            for (int i=0; i<ArticulosLista.size();i++){
                ListaArticulos.add(String.valueOf(ArticulosLista.get(i).getCodigoBarra())+" - "+String.valueOf(ArticulosLista.get(i).getDescripcionArticulo()));
            }
        }
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
            articulo.setDescripcionArticulo(cursor.getString(3));
            articulo.setCodigoBarra(cursor.getString(8));
            ArticulosLista.add(articulo);
        }
        return ArticulosLista;
    }
    public ArrayList<Articulos> getArticulos(String codigo){
        //leer la base de datos
        SQLiteDatabase db=conexion.getReadableDatabase();
        Articulos articulo=null;
        ArticulosLista= new ArrayList<Articulos>();

        Cursor cursor= db.rawQuery("select * from articulos where CodigoBarra='"+codigo+"'", null);

        while (cursor.moveToNext()){
            articulo= new Articulos();
            articulo.setId(cursor.getInt(0));
            articulo.setCodEmpresa(cursor.getString(1));
            articulo.setNroArticuloERP(cursor.getInt(2));
            articulo.setDescripcionArticulo(cursor.getString(3));
            articulo.setCodigoBarra(cursor.getString(8));
            ArticulosLista.add(articulo);
        }
        return ArticulosLista;
    }
    @Override
    public void onClick(View view) {
        int idView=view.getId();
        if(idView==R.id._operaciones_btn_agregar){
            agregarArticuloLista();
        }
        if(idView==R.id._operaciones_btn_guardar){
            guardarDevolucion();
        }
    }

    private void agregarArticuloLista() {

        String cliente=comboClientes.getSelectedItem().toString();
        String articulo=comboArticulos.getSelectedItem().toString();

        if(!cliente.equals("Seleccione") && !articulo.equals("Seleccione") && this.txt_cantidad.getText().length()>0){
            int cantidad=Integer.parseInt(this.txt_cantidad.getText().toString());

            if(cantidad>0){
                String codigoArticulo=articulo.substring(0, articulo.indexOf("-"));
                String codigoCliente=cliente.substring(0, cliente.indexOf("-")-1);
                System.out.println("codigoCliente = " + codigoCliente);
                System.out.println("codigoArticulo = " + codigoArticulo);
                clienteSelect=getClientes(codigoCliente.trim());
                articulosSelect=getArticulos(codigoArticulo.trim());
                if(codigoBarraLista.contains(articulosSelect.get(0).getCodigoBarra())){
                    Toast.makeText(getApplicationContext(), "El Articulo ya se encuentra en la lista", Toast.LENGTH_LONG).show();
                }else{
                    codigoBarraLista.add(articulosSelect.get(0).getCodigoBarra());
                    int doco=getIdDevolucion()+1;
                    this.comboClientes.setEnabled(false);
                    // System.out.println("aquui");
                    Detalle detalleTemp= new Detalle();
                    detalleTemp.setKCOO(clienteSelect.get(0).getCodEmpresa());
                    detalleTemp.setDOCO(doco);
                    detalleTemp.setDCTO("CD");
                    detalleTemp.setLNID((lista.size()+1)*1000);
                    detalleTemp.setAN8(clienteSelect.get(0).getNroCuenta());
                    detalleTemp.setAITM(articulosSelect.get(0).getCodigoBarra());
                    detalleTemp.setUORG(cantidad*100);
                    detalleTemp.setLPRC(0);
                    detalleTemp.setUOM("UN");
                    detalleTemp.setI55DEPR(0);
                    detalleTemp.setUPC3("");
                    detalleTemp.setS55PROMFM("");
                    detalleTemp.setLOCN("");
                    detalleTemp.setDRQJ(Integer.parseInt(MovimientoBD.obtenerFecha()));
                    detalleTemp.setCodarticulo(String.valueOf(articulosSelect.get(0).getNroArticuloERP()));

                    comboArticulos.setSelection(0);
                    this.listaDetalle.add(detalleTemp);
                    this.lista.add(detalleTemp.getAITM()+" - "+articulosSelect.get(0).getDescripcionArticulo()+" - "+cantidad);
                    this.adapterDetalleTemp.notifyDataSetChanged();
                    txt_cantidad.setText("");
                }
            }else{
                Toast.makeText(getApplicationContext(), "Cantidad no puede ser cero", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Error: favor verifique sus datos", Toast.LENGTH_SHORT).show();
        }

    }

    public void guardarDevolucion(){
if(insertar.getDevolucionClientePorDia(MovimientoBD.obtenerFecha(), conexion, String.valueOf(listaDetalle.get(0).getAN8()))>0){
    Toast.makeText(getApplicationContext(),"Ya existe un cliente cargado el dia de hoy", Toast.LENGTH_SHORT).show();
    System.out.println(listaDetalle.get(0).getAN8());
    System.out.println(MovimientoBD.obtenerFecha());
}else{
   if(listaDetalle.size()>0){
        System.out.println("listaDetalle = " + listaDetalle.size());
        System.out.println("lista = " + lista.size());
        String fin_detalle;
        String detalle="";
        StringBuffer cadena = new StringBuffer();
        for (int x=0;x<listaDetalle.size();x++){
            cadena =cadena.append(listaDetalle.get(x).toString().replaceAll("=",":")+", ");
        }
        fin_detalle="["+cadena.substring(0, cadena.length()-2)+"]";//array de objetos detalles
        Devoluciones devoluciones= new Devoluciones();
        devoluciones.setKCOO("00001");
        devoluciones.setDCTO("CD");
        devoluciones.setDOCO(listaDetalle.get(0).getDOCO());
        devoluciones.setVR02("");
        devoluciones.setAN8(listaDetalle.get(0).getAN8());
        devoluciones.setDRQJ(listaDetalle.get(0).getDRQJ());
        devoluciones.setCRCD("GUA");
        devoluciones.setD55DECL("0");
        devoluciones.setALPH(this.clienteSelect.get(0).getRazonSocial());
        devoluciones.setTAX(this.clienteSelect.get(0).getRUC());
        devoluciones.setADD1(this.clienteSelect.get(0).getDireccion());
        devoluciones.setADD2("");
        devoluciones.setCREG((listaDetalle.size()+1)*100);
        devoluciones.setS55PROCES(0);
        devoluciones.setMigrado("no");
        devoluciones.setDetalle(fin_detalle);

        String peticionFinal=devoluciones.toString();
        //System.out.println("peticionFinal = " + peticionFinal);
        //System.out.println("fin_detalle = " + fin_detalle);
        String codCliente=String.valueOf(clienteSelect.get(0).getNroCuenta());
        String descripcionCliente=devoluciones.getALPH();
        String jsonRequest=peticionFinal;
        String migrado="no";

        if(insertar.insertarDevolucionesJSON(conexion, codCliente, descripcionCliente, jsonRequest, migrado)){
            if(insertar.insertarDetalle(conexion, listaDetalle)){
                if(insertar.insertarDevolucionCabecera(conexion,listaDetalle.get(0).getDOCO(),devoluciones.getCREG(),clienteSelect)){
                    if(insertar.insertarIdDevolucion(conexion,getIdDevolucion()+1)){
                        Toast.makeText(getApplicationContext(), "Exito al insertar", Toast.LENGTH_LONG).show();
                        txt_cantidad.setText("");
                        comboArticulos.setSelection(0);
                        btn_guardar.setEnabled(false);
                        btn_agregar.setEnabled(false);

                    }
                }
            }
        }
    }else{
        Toast.makeText(getApplicationContext(), "Favor introduzca datos al detalle. ", Toast.LENGTH_SHORT).show();
    }
}

    }
    public int getIdDevolucion(){
        int idDevolucion=0;
        SQLiteDatabase db=conexion.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select count(id) from devoluciones_id", null);
        while (cursor.moveToNext()){
            idDevolucion=cursor.getInt(0);
        }
        return idDevolucion;
    }
}