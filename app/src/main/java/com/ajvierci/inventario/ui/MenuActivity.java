package com.ajvierci.inventario.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ajvierci.inventario.MainActivity;
import com.ajvierci.inventario.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
Button btn_sincronizar, btn_devoluciones;
private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        conectarVista();
        listenner();
        preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE );

    }

    private void conectarVista() {
        this.btn_sincronizar=findViewById(R.id.menu_sincronizar_btn);
        this.btn_devoluciones=findViewById(R.id.menu_devoluciones_btn);
    }

    private void listenner() {
        this.btn_sincronizar.setOnClickListener(this);
        this.btn_devoluciones.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int idView=v.getId();
        if(idView==R.id.menu_sincronizar_btn){
            ir_sincronizar();
        }
        if(idView==R.id.menu_devoluciones_btn){
            ir_devoluciones();
        }

    }


    private void ir_sincronizar() {
        Intent i=new Intent(getApplicationContext(),SincronizarActivity.class);
        startActivity(i);

    }

    private void ir_devoluciones() {
        Intent i=new Intent(getApplicationContext(),FiltrosDevolcionesActivity.class);
        startActivity(i);
    }

//Creacion de menu y sus opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id._menu_cerra_sesion:
                preferences.edit().clear().apply();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }
}