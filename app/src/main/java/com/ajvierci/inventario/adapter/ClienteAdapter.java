package com.ajvierci.inventario.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ajvierci.inventario.ItemClickListener;
import com.ajvierci.inventario.R;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.bdsqlite.MovimientoBD;
import com.ajvierci.inventario.entidades.Cliente;
import com.ajvierci.inventario.entidades.ClienteSeleccionado;
import com.ajvierci.inventario.entidades.DevolucionesJSON;
import com.ajvierci.inventario.ui.DevolucionActivity;
import com.ajvierci.inventario.ui.EditarDevolucionActivity;

import java.util.ArrayList;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolderDatos> implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<DevolucionesJSON> listDatos;
    private View.OnClickListener listener;
    private AdapterView.OnItemClickListener clickListener;
    private ArrayList<Integer> listaSincronizacion=new ArrayList<>();
    public ArrayList<DevolucionesJSON>seleccionadosLista= new ArrayList<>();
    private ConexionSQLiteHelper conexion;
    private Button sincronizar;
    //private ArrayList<String>listDatos;
    private int posicion=0;
    private Context context;
    MovimientoBD bd = new MovimientoBD();
    ArrayList<ClienteSeleccionado>clienteSeleccionadoArrayList;

    public ClienteAdapter(ArrayList<DevolucionesJSON> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //enlaza el adaptador con el itemlist
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
       // view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos holder, final int position) {
        //establece la la comunicacion entre el adatodor y la clase viewHolderDatos
        holder.id.setText(String.valueOf(listDatos.get(position).getId()));
        holder.checkBox_descripcion.setText(listDatos.get(position).getDescripcion());
        holder.codigoCliente.setText(listDatos.get(position).getCodCliente());
        posicion=position;

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int posicion) {
                CheckBox chk=(CheckBox) v;
                //
                if(chk.isChecked()){
                    seleccionadosLista.add(listDatos.get(posicion));
                    System.out.println(posicion);
                }else{
                    if(!chk.isChecked()){
                        seleccionadosLista.remove(listDatos.get(posicion));
                    }
                }
            }
        });

        holder.detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int codigoSincronizar=Integer.parseInt(holder.id.getText().toString());
                Intent intent= new Intent(context, DevolucionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("parametro",String.valueOf(codigoSincronizar));
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClienteSeleccionado seleccion=null;
                System.out.println("Ingreso al card");
                System.out.println("Se imprime el codigo "+holder.id.getText().toString());
                if(holder.checkBox_descripcion.isChecked()){
                    System.out.println("Seleccionado");
                }else{
                    System.out.println("No seleccionado");
                }

            }
        });
        if(listDatos.get(position).getMigrado().equals("si")){
            holder.modificar.setVisibility(View.INVISIBLE);
        }else{
            holder.modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int codigoSincronizar=Integer.parseInt(holder.id.getText().toString());
                    Intent intent= new Intent(context, EditarDevolucionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("parametro",String.valueOf(codigoSincronizar));
                    context.startActivity(intent);
                }
            });
        }
        if(listDatos.get(position).getMigrado().equals("si")){
            holder.eliminar.setVisibility(View.INVISIBLE);
        }else{
            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int codigoEliminarDevolucion=Integer.parseInt(holder.id.getText().toString());
                    System.out.println("codigoEliminarDevolucion = " + codigoEliminarDevolucion);
                    conexion=new ConexionSQLiteHelper(context,"bd_inventario", null, 1);
                    if(bd.eliminarDevolucion(String.valueOf(codigoEliminarDevolucion), conexion)){
                        Toast.makeText(context, "Devolución eliminada", Toast.LENGTH_SHORT).show();
                    }else{
                        System.out.println("Wat");
                    }
                    /*Intent intent= new Intent(context, EditarDevolucionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("parametro",String.valueOf(codigoSincronizar));
                    context.startActivity(intent);*/
                }
            });
        }


    }

    @Override
    public int getItemCount() {
       //retorna el tamaño de esa lista
        return listDatos.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
    this.listener=listener;
    }
    public void setOnClickItemListener(AdapterView.OnItemClickListener listener){
        this.clickListener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
                listener.onClick(v);
        }
    }
    public ArrayList<ClienteSeleccionado> enviarPrueba(ArrayList<ClienteSeleccionado>seleccionados){
        return seleccionados;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener{


        //referenciar datos del itemList view
        TextView chk_cliente;
        private CheckBox checkBox_descripcion;
        private Button detalles, modificar, eliminar;
        TextView migradotxt, json, codigo;
        CardView cardView;
        TextView id,codigoCliente;

        ItemClickListener itemClickListener;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            codigoCliente=itemView.findViewById(R.id._item_list_codigo_cliente);
            id=itemView.findViewById(R.id._item_list_codigo_label);
            modificar=itemView.findViewById(R.id._item_list_btn_modificar);
            cardView=itemView.findViewById(R.id._item_list_card_view_id);
            detalles=itemView.findViewById(R.id._item_list_btn_detalles);
            eliminar=itemView.findViewById(R.id._item_list_btn_eliminar);
            this.checkBox_descripcion=itemView.findViewById(R.id._item_list_chk);
            checkBox_descripcion.setOnClickListener(this);

            //            chk_cliente=itemView.findViewById(R.id.clienteItem);

        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }


        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}
