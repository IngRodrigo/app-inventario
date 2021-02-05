package com.ajvierci.inventario.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_USUARIO="CREATE TABLE usuarios (id integer PRIMARY KEY AUTOINCREMENT, nombre TEXT, password TEXT)";
    public static final String CREAR_TABLA_CLIENTES="CREATE TABLE clientes " +
            "(id integer PRIMARY KEY AUTOINCREMENT, " +
            "CodEmpresa TEXT, " +
            "NroCuentaERP integer, " +
            "RazonSocial TEXT, " +
            "Direccion TEXT, " +
            "CodVendedor integer, " +
            "CodZona integer, " +
            "CodRamoERP TEXT, " +
            "CodCanalERP TEXT, " +
            "Telefono TEXT, " +
            "RUC TEXT, " +
            "NroListaPrecioERP TEXT, " +
            "LimiteCredito double," +
            "SaldoGuaranies double, " +
            "SaldoDolares double, " +
            "Estado TEXT, " +
            "Ubicacion TEXT, " +
            "Email TEXT, " +
            "CodCondicionVentaERP TEXT )";

    public static final String CREAR_TABLA_ARTCULOS="CREATE TABLE articulos " +
            "(id integer PRIMARY KEY AUTOINCREMENT, " +
            "CodEmpresa TEXT, " +
            "NroArticuloERP integer, " +
            "DescripcionArticulo TEXT, " +
            "CodMarcaERP TEXT, " +
            "CodLineaERP TEXT, " +
            "CodTipoERP TEXT," +
            "CodRegimenERP TEXT, " +
            "CodigoBarra TEXT, " +
            "CodigoLargo TEXT, " +
            "CodCategoriaERP TEXT, " +
            "CodProveedor integer)";

    final String CREAR_TABLA_DEVOLUCIONES="CREATE TABLE devoluciones " +
            "(id integer PRIMARY KEY AUTOINCREMENT, " +
            "KCOO TEXT, " +
            "DCTO TEXT, " +
            "DOCO TEXT, " +
            "VR02 TEXT, " +
            "AN8 integer, " +
            "DRQJ integer, " +
            "CRCD TEXT, " +
            "d55DECL double, " +
            "ALPH TEXT, " +
            "TAX TEXT, " +
            "ADD1 TEXT, " +
            "ADD2 TEXT, " +
            "CREG integer, " +
            "s55PROCES integer, " +
            "migrado TEXT, " +
            "updateOn TEXT)";
    final String CREAR_TABLA_DEVOLUCIONES_DETALLE="CREATE TABLE devoluciones_detalle " +
            "(id integer PRIMARY KEY AUTOINCREMENT, " +
            "KCOO TEXT, " +
            "DCTO TEXT, " +
            "DOCO integer, " +
            "LNID integer, " +
            "AN8 integer, " +
            "AITM TEXT, " +
            "UORG integer, " +
            "LPRC integer, " +
            "UOM TEXT, " +
            "i55DEPR integer, " +
            "DRQJ text, " +
            "UPC3 TEXT, " +
            "s55PROMFM TEXT, " +
            "LOCN TEXT, " +
            "CODARTICULO TEXT)";
    final String CREAR_TABLA_DEVOLUCIONES_DETALLE_TEMP="CREATE TABLE detalle_temp " +
            "(id integer PRIMARY KEY, " +
            "KCOO TEXT, " +
            "DCTO TEXT, " +
            "DOCO integer," +
            "LNID integer, " +
            "AN8 integer, " +
            "AITM TEXT, " +
            "UORG integer, " +
            "LPRC integer, " +
            "UOM TEXT, " +
            "i55DEPR integer, " +
            "DRQJ TEXT, " +
            "UPC3 TEXT, " +
            "s55PROMFM TEXT, " +
            "LOCN TEXT, " +
            "CODARTICULO TEXT)";
    final String CREAR_TABLA_DEVOLUCIONES_JSON="CREATE TABLE devoluciones_json " +
            "(id integer PRIMARY KEY AUTOINCREMENT, " +
            "codCliente TEXT, " +
            "descripcionCli TEXT, " +
            "json_request TEXT," +
            "migrado TEXT, " +
            "fecha TEXT)";
    final String CREAR_TABLA_DEVOLUCIONES_ID="CREATE TABLE devoluciones_id " +
            "(id integer PRIMARY KEY)";
    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_CLIENTES);
        db.execSQL(CREAR_TABLA_ARTCULOS);
        db.execSQL(CREAR_TABLA_DEVOLUCIONES);
        db.execSQL(CREAR_TABLA_DEVOLUCIONES_DETALLE);
        db.execSQL(CREAR_TABLA_DEVOLUCIONES_DETALLE_TEMP);
        db.execSQL(CREAR_TABLA_DEVOLUCIONES_JSON);
        db.execSQL(CREAR_TABLA_DEVOLUCIONES_ID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS articulos");
        db.execSQL("DROP TABLE IF EXISTS devoluciones");
        db.execSQL("DROP TABLE IF EXISTS devoluciones_detalle");
        db.execSQL("DROP TABLE IF EXISTS detalle_temp");
        db.execSQL("DROP TABLE IF EXISTS devoluciones_json");
        db.execSQL("DROP TABLE IF EXISTS devoluciones_id");
        onCreate(db);
    }
}
