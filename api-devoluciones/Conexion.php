<?php
class Conexion{
    
    
    static public function conectarMySql(){
        
        $link= new PDO("mysql:host=localhost;dbname=apirest",
                        "root",
                        "");

        $link->exec("set names utf8");
        
        return $link;
    }



    /**=================================
	*
	*Conexion para SQL SERVER PHP 7
	*
    ====================================**/

     static public  function conectarSqlServer(){
            $conn=null;
               try {
                     $servidor= "ajvsqlse-desa\DEVSQL2016";
                     $bd = "app_devoluciones";
                     $usuario_bd = "usr_desarrollo";
                     $password_bd = "e123456e";
               
                       //para produccion usar dblib php 5
                       //$conn = new PDO ("dblib:charset=UTF-8;host=".$serverName_GBK.";dbname=".$dbName_GBK.";",$username_GBK, $password_GBK);
                       //para desarrollo en php7 usar

                       $conn= new PDO("sqlsrv:server=$servidor;Database=$bd",  "$usuario_bd", "$password_bd");
                       $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                      // $conn->exec("set names utf8");

           } catch (PDOException $e) {
                   $respuesta = "Error conexion con la Bases de Datos del servidor" .$servidor." <br /> ". $e->getMessage() . "\n";
                    echo $respuesta;
           }
           return $conn; 
        }

}