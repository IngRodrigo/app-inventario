package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ajvierci.inventario.R;

public class FiltrosDevolcionesActivity extends AppCompatActivity {
private Button btn_cliente, btn_fecha, btn_migrado, btn_sin_migrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_devolciones);
        conectarVista();
    }
    public void conectarVista(){
        this.btn_cliente=findViewById(R.id._filtros_btn_Nombre);
        this.btn_fecha=findViewById(R.id._filtos_btn_porFecha);
        this.btn_migrado=findViewById(R.id._filtos_btn_Migrado);
        this.btn_sin_migrar=findViewById(R.id._filtos_btn_sin_migrar);

        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevolucionesActivity.class);
                intent.putExtra("parametro","fecha");

                startActivity(intent);
            }
        });

        btn_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevolucionesActivity.class);
                intent.putExtra("parametro","cliente");

                startActivity(intent);
            }
        });
        btn_migrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevolucionesActivity.class);
                intent.putExtra("parametro","enviado");

                startActivity(intent);
            }
        });
        btn_sin_migrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevolucionesActivity.class);
                intent.putExtra("parametro","pendiente");

                startActivity(intent);
            }
        });

    }
}