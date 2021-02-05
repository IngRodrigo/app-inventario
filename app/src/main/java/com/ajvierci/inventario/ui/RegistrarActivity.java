package com.ajvierci.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajvierci.inventario.MainActivity;
import com.ajvierci.inventario.R;
import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.retrofit.api_app.model.request.usuarios.RequestRegistro;
import com.ajvierci.inventario.retrofit.api_app.model.response.usuarios.ResponseRegistro;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesCliente;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean existe;
    private Button btn_registrar;
    private TextView btn_login;
    private EditText txt_user, txt_password, txt_password_confir, txtDocumento, txtNombre, txtApellido, txtCel;
    private ConexionSQLiteHelper conexion;
    private ProgressBar progreso;

    //retrofit
    private ApiDevolucionesService apiDevolucionesService;
    private ApiDevolucionesCliente apiDevolucionesCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        conectarVista();
        instanciarBD();
        listenner();
        retrofit();
    }
    //metodo para instanciar retrofit
    private void retrofit(){
        apiDevolucionesCliente= ApiDevolucionesCliente.getInstance();
        //acceso a todos los servicios http de la instancia
        apiDevolucionesService=apiDevolucionesCliente.getApiDevolucionesService();
    }
    private void instanciarBD() {
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
    }

    private void conectarVista() {
        this.txtDocumento=findViewById(R.id._registrar_txt_documento);
        this.txtNombre=findViewById(R.id._registrar_txt_nombre);
        this.txtApellido=findViewById(R.id._registrar_txt_apellido);
        this.txtCel=findViewById(R.id._registrar_txt_celular);
        this.txt_user=findViewById(R.id._registrar_txt_user);
        this.txt_password=findViewById(R.id._registar_txt_password);
        this.txt_password_confir=findViewById(R.id._registar_txt_password_confir);
        this.btn_registrar=findViewById(R.id._registrar_btn_registrar);
        this.btn_login=findViewById(R.id._registrar_btn_login);
        this.progreso=findViewById(R.id._registrar_btn__progreso);

    }
    private void listenner() {
        this.btn_registrar.setOnClickListener(this);
        this.btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int idView=v.getId();
        if(idView==R.id._registrar_btn_registrar){

            registrarUsuario();
        }
        if(idView==R.id._registrar_btn_login){
            volverAlLogin();
        }
    }

    private void registrarUsuario() {
        String documento=this.txtDocumento.getText().toString();
        String nombre=this.txtNombre.getText().toString();
        String apellido=this.txtApellido.getText().toString();
        String cel=this.txtCel.getText().toString();
        String userName=this.txt_user.getText().toString();
        String userPassword=this.txt_password.getText().toString();
        String userPasswordConfir=this.txt_password_confir.getText().toString();

        if(!documento.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !cel.isEmpty() && !userName.isEmpty() && !userPassword.isEmpty() && !userPasswordConfir.isEmpty())
        {

            if(userPassword.equals(userPasswordConfir)){
                RequestRegistro requestRegistro= new RequestRegistro(nombre, apellido, documento, cel, userName, userPassword);

                Call<ResponseRegistro>call=apiDevolucionesService.registrarUsuario(requestRegistro);
                call.enqueue(new Callback<ResponseRegistro>() {
                    @Override
                    public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response) {
                        if(response.isSuccessful()){
                            ResponseRegistro responseRegistro=response.body();
                            System.out.println(responseRegistro.getStatus());
                            Toast.makeText(getApplicationContext(), "Registro exitoso. Aguarde activavión del administrador", Toast.LENGTH_LONG).show();
                            volverAlLogin();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRegistro> call, Throwable t) {

                    }
                });
            }else{
                System.out.println("No son iguales");
            }

        }
    }


    private void volverAlLogin() {
        Intent i= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean comprobarExisteUsuario(){
        SQLiteDatabase db=conexion.getReadableDatabase();
        String[] parametros={this.txt_user.getText().toString()};
        String[] campos={"nombre"};

        try {
            Cursor cursor=db.query("usuarios",campos,"nombre=?", parametros, null, null, null);
            if(!cursor.moveToFirst()){
                return this.existe=false;
            }else{
                return this.existe=true;
            }

        }   catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error-> "+e,Toast.LENGTH_LONG).show();
        }
        return this.existe;
    }
    private void registro(){


        enviarUsuarioWs();
        /*
        String nombre_user=this.txt_user.getText().toString();
        String password=this.txt_password.getText().toString();
       // Usuario usuario= new Usuario(nombre_user,password);
        //abrimos la base de datos para escribir en ella
        SQLiteDatabase db=conexion.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put("nombre",nombre_user);
        values.put("password",password);

        Long idResultado=db.insert("usuarios","id", values);

        Toast.makeText(getApplicationContext(), "Exito al registrarse: ", Toast.LENGTH_LONG).show();

        Log.i("dev","what ->"+idResultado);
        db.close();
        */
    }

    private void enviarUsuarioWs() {

    }

    public boolean registroUsuarioWs(){
    return  false;
    }

}

