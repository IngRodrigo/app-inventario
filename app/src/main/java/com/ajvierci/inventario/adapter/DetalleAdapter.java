package com.ajvierci.inventario.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajvierci.inventario.R;
import com.ajvierci.inventario.entidades.DevolucionIndividual;

import java.util.ArrayList;

public class DetalleAdapter extends RecyclerView.Adapter<DetalleAdapter.ViewHolderDatos> implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<DevolucionIndividual> listDatos;
    private View.OnClickListener listener;
    private AdapterView.OnItemClickListener clickListener;
    //private ArrayList<String>listDatos;
    private int posicion=0;
  /*  public ClienteAdapter(ArrayList<DevolucionesJSON> listDatos) {
        this.listDatos = listDatos;
    }*/

    public DetalleAdapter(ArrayList<DevolucionIndividual> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //enlaza el adaptador con el itemlist
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_detalle
                        , null
                        , false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, final int position) {
        //establece la la comunicacion entre el adatodor y la clase viewHolderDatos
        holder.DOCO.setText(String.valueOf(listDatos.get(position).getCodigo_aticulo()));
        holder.id.setText(String.valueOf(listDatos.get(position).getId_devolucion()));
        holder.descripcion.setText(String.valueOf(listDatos.get(position).getDescripcionArticulo()));
        holder.cantidad.setText(String.valueOf((listDatos.get(position).getCantidad()/100)));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {


        TextView DOCO, id,  descripcion, cantidad;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            DOCO=itemView.findViewById(R.id._item_list_detalle_DOCO);
            id=itemView.findViewById(R.id._item_list_detalle_DOCO);
            descripcion=itemView.findViewById(R.id._item_list_detalle_articulo);
            cantidad=itemView.findViewById(R.id._item_list_detalle_cantidad);
            id=itemView.findViewById(R.id._item_list_detalle_id_devolucion);

        }

    }
}
