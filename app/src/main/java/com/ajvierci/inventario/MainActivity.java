package com.ajvierci.inventario;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ajvierci.inventario.bdsqlite.ConexionSQLiteHelper;
import com.ajvierci.inventario.entidades.Data;
import com.ajvierci.inventario.retrofit.api_app.model.request.usuarios.RequestLogin;
import com.ajvierci.inventario.retrofit.api_app.model.response.usuarios.ResponseLogin;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesCliente;
import com.ajvierci.inventario.retrofit.api_app.net.ApiDevolucionesService;
import com.ajvierci.inventario.ui.MenuActivity;
import com.ajvierci.inventario.ui.RegistrarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences prefrences;
    private Switch recordarUsuario;

    private final int REQUEST_CODE_ASK_PERMISSION=111;
    private Button btn_login;
    private TextView btn_registrarse;
    private EditText txt_user, txt_password;
    private ConexionSQLiteHelper conexion;

    //retrofit
    private ApiDevolucionesService apiDevolucionesService;
    private ApiDevolucionesCliente apiDevolucionesCliente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPreferences();

        conectarVista();
        cargarPreferencias();
        solicitarPemisos();
        instanciarBD();
        listenner();
        retrofit();
    }


    // Creamos la SharedPreferences
    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("recordar","no");
        if(recordar.equals("si")){
            ir_menu_principal();

        }else{
            System.out.println("no");
        }
    }
    private void guardarPreferencias(String id, String usuario, String estado, String rol){

        if(recordarUsuario.isChecked()){
           SharedPreferences.Editor editor= prefrences.edit();
           editor.putString("id", id);
           editor.putString("name_user", usuario);
            editor.putString("estado", estado);
            editor.putString("rol", rol);
            editor.putString("recordar","si");
            editor.apply();
            System.out.println("se guardo");
        }
    }

    //metodo para instanciar retrofit
    private void retrofit(){
        apiDevolucionesCliente=ApiDevolucionesCliente.getInstance();
        //acceso a todos los servicios http de la instancia
        apiDevolucionesService=apiDevolucionesCliente.getApiDevolucionesService();
    }


    private void conectarVista(){
        this.txt_user=findViewById(R.id._login_txt_user);
        this.txt_password=findViewById(R.id._login_txt_password);
        this.btn_login=findViewById(R.id._login_btn_session);
        this.btn_registrarse=findViewById(R.id._login_btn_registrar);
        this.recordarUsuario=findViewById(R.id._login_recordar_usuario);
    }
    private void instanciarBD(){
        conexion=new ConexionSQLiteHelper(this,"bd_inventario", null, 1);
    }
    private void listenner(){
        this.btn_login.setOnClickListener(this);
        this.btn_registrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int idView=v.getId();
        if(idView==R.id._login_btn_session){
            login();
        }
        if(idView==R.id._login_btn_registrar){
            ir_al_registro();
        }
    }



    private void login() {
        String user=txt_user.getText().toString();
        String password=txt_password.getText().toString();

        if(user.isEmpty()){
            txt_user.setError("El campo usuario no puede estar vacio");
        }else{
            if(password.isEmpty()){
                txt_password.setError("El campo password no puede estar vacio");
            }else{
                //iniciarsesion();
                   iniciarSesion(user, password);

            }
        }

    }

    private void iniciarSesion(String usuario, String password) {
        final RequestLogin requestLogin= new RequestLogin(usuario, password);
        Call<ResponseLogin> call= apiDevolucionesService.loginUser(requestLogin);

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()){


                    ResponseLogin responseLogin=response.body();
                    try {
                        JSONObject dataUser= new JSONObject(responseLogin.toString().replace("=",":"));

                        if(dataUser.getString("estado").equals("Activo")){
                            String id=dataUser.getString("id");
                            String nombre=dataUser.getString("nombre")+" "+dataUser.getString("apellido");
                            String estado=dataUser.getString("estado");
                            String rol=dataUser.getString("rol");
                            guardarPreferencias(id, nombre,estado, rol);
                            Toast.makeText(getApplicationContext(),"Bienvenido: "+nombre, Toast.LENGTH_LONG).show();
                            ir_menu_principal();
                        }else
                            if(dataUser.getString("estado").equals("Inactivo")){
                            String nombre=dataUser.getString("userName");
                            Toast.makeText(getApplicationContext(),"El usuario: "+nombre+" se encuentra inactivo o fue dado de baja", Toast.LENGTH_LONG).show();

                            }
                            else
                                if(dataUser.getString("estado").equals("ND")){
                                    Toast.makeText(getApplicationContext(),"El usuario o la contraseña son erroneos, no se encotraron coincidencias", Toast.LENGTH_LONG).show();
                                }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Problemas de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarsesion() {
        SQLiteDatabase db=conexion.getReadableDatabase();
        //   String nombreUser=
        String[] parametros={this.txt_user.getText().toString(), this.txt_password.getText().toString()};
        String[] campos={"nombre"};

        try {
            String sql="Select * from usuarios where nombre=? and password=?";
            //Cursor cursor=db.query("usuarios",campos,"nombre=?", parametros, null, null, null);
            Cursor cursor=db.rawQuery(sql,parametros);
            if(!cursor.moveToFirst()){
                Toast.makeText(this, "Error: Favor verifique sus datos",Toast.LENGTH_LONG).show();
            }else{
                cursor.moveToFirst();
                String nombre=cursor.getString(1);
                cursor.close();
                Toast.makeText(getApplicationContext(),"Bienvenido: "+nombre, Toast.LENGTH_LONG).show();
                ir_menu_principal();

            }
        }   catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error -> "+e,Toast.LENGTH_LONG).show();
//            return false;
        }
    }

    private void ir_menu_principal() {
        Intent i= new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
        finish();
    }
    private void ir_al_registro() {
        Intent i= new Intent(getApplicationContext(), RegistrarActivity.class);
        startActivity(i);
        finish();
    }
    public void solicitarPemisos(){
        int permisosWriteStorage= ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisosReadStorage=ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permisosReadStorage!= PackageManager.PERMISSION_GRANTED || permisosWriteStorage!=PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }
}
