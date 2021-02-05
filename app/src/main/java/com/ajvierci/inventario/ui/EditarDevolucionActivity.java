package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.adapter.DetalleAdapterModificar;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.entidades.Detalle;
import com.ajvierci.inventario.entidades.DevolucionIndividual;

import java.util.ArrayList;

public class EditarDevolucionActivity extends AppCompatActivity implements View.OnClickListener {
    private ConexionSQLiteHelper conexion;
    private TextView txt_devolucion_id, txt_empresa, empresa_id, cantidad_registros;
    private String parametro;
    private RecyclerView recyclerView;
    private ArrayList<DevolucionIndividual>listDatos;
    private ArrayList<Detalle>ListaDetalle;
    private ArrayList<DevolucionIndividual>ListaDevoluciones;
    private DevolucionIndividual devolucionesDetalles;
    private DetalleAdapterModificar detalleAdapter;

    private Button actualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_devolucion);
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
        this.parametro=getIntent().getExtras().getString("parametro");
        conectarVista();
      //  listenner();
        txt_devolucion_id.setText(parametro);
        setTextosVistas();
    }

    private void conectarVista() {
        this.txt_devolucion_id=findViewById(R.id._editar_devolucion_id_devolucion);
        this.recyclerView=findViewById(R.id._editar_devolucion_recycler_id);
        this.txt_empresa=findViewById(R.id._editar_devolucion_empresa);
        this.empresa_id=findViewById(R.id._editar_devolucion_empresa_id);
        this.cantidad_registros=findViewById(R.id._editar_devolucion_cantidad_detalle);
    }
    private void listenner() {

    }

    private void setTextosVistas(){
        ArrayList<DevolucionIndividual> devoluciones=getDevolucionesCabecera(parametro);
        if(devoluciones.size()>0 && devoluciones!=null){
            txt_empresa.setText(devoluciones.get(0).getEmpresa());
            empresa_id.setText(String.valueOf(devoluciones.get(0).getAN8()));
            cantidad_registros.setText(String.valueOf(devoluciones.size()));
            recyclerView.setAdapter(setLayautManagerRecyclerView(devoluciones));
        }else{
            txt_empresa.setText("**");
        }
    }
    private ArrayList<DevolucionIndividual> getDevolucionesCabecera(String parametro){
        String query="select d.migrado, d.DOCO, d.AN8, d.DRQJ as 'fecha', d.ALPH as 'empresa', a.CodigoBarra as 'codigo_articulo', a.DescripcionArticulo, dtl.UORG as 'cantidad', dtl.id as 'id_detalle', d.id as 'idDevolucion', dj.json_request from devoluciones as d\n" +
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
            devolucionIndividual.setMigrado(cursor.getString(0));
            devolucionIndividual.setDOCO(cursor.getInt(1));
            devolucionIndividual.setAN8(cursor.getInt(2));
            devolucionIndividual.setFecha(cursor.getInt(3));
            devolucionIndividual.setEmpresa(cursor.getString(4));
            devolucionIndividual.setCodigo_aticulo(cursor.getString(5));
            devolucionIndividual.setDescripcionArticulo(cursor.getString(6));
            devolucionIndividual.setCantidad(cursor.getInt(7));
            devolucionIndividual.setIdDetalle(cursor.getInt(8));
            devolucionIndividual.setId_devolucion(cursor.getInt(9));
            devolucionIndividual.setJson(cursor.getString(10));

            ListaDevoluciones.add(devolucionIndividual);
            // db.close();
        }

        return ListaDevoluciones;
    }
    private DetalleAdapterModificar setLayautManagerRecyclerView(ArrayList<DevolucionIndividual>devolucionIndividual) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    devolucionesDetalles.setMigrado(devolucionIndividual.get(i).getMigrado());
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
            detalleAdapter=new DetalleAdapterModificar(listDatos, getApplicationContext());
        }
        return detalleAdapter;
    }

    @Override
    public void onClick(View view) {
        int idView=view.getId();
    }
}