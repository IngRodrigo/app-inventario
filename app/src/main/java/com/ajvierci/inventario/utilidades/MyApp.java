package com.ajvierci.inventario.utilidades;
/*==========================================================
*
*capturar el contexto de la aplicacion desde cualquier clase
* se debe agregar esta linea al manifest
* android:name=".utilidades.MyApp"
*
* ==========================================================*/

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp instance;

    //devolver myapp
    public static MyApp getInstance(){
        return instance;
    }

    //obtener el contexto de la app
    public static Context getContext(){
        return instance;
    }


    @Override
    public void onCreate() {
        instance=this;
        super.onCreate();

    }
}

