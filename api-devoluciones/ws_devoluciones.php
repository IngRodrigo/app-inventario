<?php

require_once 'Conexion.php';

//guardar el json de la peticion en el objeto data
$data = json_decode(file_get_contents('php://input'), true);

//zona horaria por defecto
date_default_timezone_set('America/Asuncion');

$contador = 0;

//comprobar si se hizo una peticion por post
if (isset($_SERVER['REQUEST_METHOD']) && $_SERVER['REQUEST_METHOD'] == 'POST') {



    if ($data['accion'] == 'acceso_usuario') {
        if ($data != null) {
            $usuario = $data['usuario'];
            $password = $data['password'];


            $conexion = Conexion::conectarSqlServer();

            if ($conexion != null) {
                $query = "SELECT
							u.id,
							u.nombre,
							u.apellido,
							u.ci AS 'documento',
							u.cel AS 'celular',
							u.userName,
							u.create_at as 'creado',
							u.update_at as 'actualiado',
							e.id_estado,
							e.descripcion AS 'estado',
							r.id_rol,
							r.descripcion AS 'rol'
						FROM
							usuarios AS u
						INNER JOIN estados AS e ON e.id_estado = u.id_estado
						INNER JOIN roles AS r ON r.id_rol = u.id_rol

						where u.userName='$usuario' and u.userPassword='$password'";


                $stmt = Conexion::conectarSqlServer()->prepare($query);

                $stmt->execute();

                $registros = $stmt->fetch(PDO::FETCH_ASSOC);

                $estado = $registros['estado'];
                if ($registros != null) {

                    if ($estado == 'Activo') {

                        $json = array(
                            "status" => "ok",
                            "comentario_error" => "Ninguno",
                            "data" => $registros
                        );
                    } else {
                        $json = array(
                            "status" => "error",
                            "comentario_error" => "El usuario esta inactivo o fue dado de baja",
                            "data" => $registros
                        );
                    }
                } else {
                    $json = array(
                        "status" => "error",
                        "comentario_error" => "No se encontraron coincidencias",
                        "data" => array(
                            "id" => "0",
                            "nombre" => "ND",
                            "apellido" => "ND",
                            "documento" => "ND",
                            "celular" => "ND",
                            "userName" => "ND",
                            "creado" => "ND",
                            "actualiado" => "ND",
                            "id_estado" => "ND",
                            "estado" => "ND",
                            "id_rol" => "ND",
                            "rol" => "ND"
                        )
                    );
                }
            }

            echo json_encode($json);
        }
    }


    if ($data['accion'] == 'listar_usuarios') {

        $conexion = Conexion::conectarSqlServer();

        if ($conexion != null) {
            $query = "SELECT
								u.id,
								u.nombre,
								u.apellido,
								u.ci AS 'documento',
								u.cel AS 'celular',
								u.userName,
								u.create_at as 'creado',
								u.update_at as 'actualiado',
								e.id_estado,
								e.descripcion AS 'estado',
								r.id_rol,
								r.descripcion AS 'rol'
							FROM
								usuarios AS u
							INNER JOIN estados AS e ON e.id_estado = u.id_estado
							INNER JOIN roles AS r ON r.id_rol = u.id_rol";

            $stmt = Conexion::conectarSqlServer()->prepare($query);

            $stmt->execute();

            $registros = $stmt->fetchAll(PDO::FETCH_ASSOC);


            $json = array(
                "status" => 200,
                "total_registros" => count($registros),
                "detalle" => $registros
            );

            echo json_encode($json, true);
        }
    }

    if ($data['accion'] == 'crear_usuario') {
        //var_dump($_POST);
        $nombre = $data['nombre'];
        $apellido = $data['apellido'];
        $ci = $data['ci'];
        $userName = $data['userName'];
        $userPassword = $data['userPassword'];
        $id_rol = $data['id_rol'];
        $id_estado = $data['id_estado'];
        $cel = $data['cel'];

        $create_at = date('Y-m-d H:i:s');
        $update_at = date('Y-m-d H:i:s');

        $conexion = Conexion::conectarSqlServer();
        if ($conexion != null) {


            $query = "INSERT INTO usuarios (nombre, apellido, ci, cel, userName, userPassword, id_rol, id_estado, create_at, update_at) 
				values
				 ('$nombre', 
				 '$apellido', 
				 '$ci', 
				 '$cel', 
				 '$userName', 
				 '$userPassword', 
				 '$id_rol', 
				 '$id_estado', 
				 '$create_at', 
				 '$update_at')";

            //echo $query;	

            try {
                $stmt = $conexion->prepare($query);
                $stmt->execute();
                $json = array(
                    "status" => "ok",
                    "detalle" => "Registrado correctamente"
                );

                echo json_encode($json, true);

                //print_r($_POST);
            } catch (PDOException $e) {
                $detalle = $e->getMessage();

                $json = array(
                    "status" => "error",
                    "detalle" => "$detalle"
                );

                echo json_encode($json, true);

                //	echo $e->getMessage();
            }
        }
    }

    if ($data['accion'] == 'insertar_devolucion') {
        $conexion = Conexion::conectarSqlServer();




        $status = 'error';

        $id_user = $data['id'];

        $cabecera = $data['json'];

        $detalles = $data['json']['detalle'];

        if($conexion!=null){

            $select="SELECT * FROM [dbo].cab_devoluciones where DOCO=".$cabecera['DOCO'];
            //echo $select;
            $stmt = Conexion::conectarSqlServer()->prepare($select);

            $stmt->execute();

            $registros = $stmt->fetch();
           
            if($registros){
           
                    $json = array(
                    "status" => "ok",
                    "detalle" => "Devolucion ya esta en la base de datos",
                    );
                    echo json_encode($json, true);
                    return;
                         die();
            }

        }


        //armaar insert cabecera

        $KCOO = $cabecera['KCOO'];
        $DCTO = $cabecera['DCTO'];
        $DOCO = $cabecera['DOCO'];
        $VR02 = $cabecera['VR02'];
        $AN8 = $cabecera['AN8'];
        $DRQJ = $cabecera['DRQJ'];
        $CRCD = $cabecera['CRCD'];
        $d55DECL = $cabecera['d55DECL'];
        $ALPH = $cabecera['ALPH'];
        $TAX = $cabecera['TAX'];
        $ADD1 = $cabecera['ADD1'];
        $ADD2 = $cabecera['ADD2'];
        $CREG = $cabecera['CREG'];
        $s55PROCES = $cabecera['s55PROCES'];


        $create_at = date('Y-m-d H:i:s');
        $update_at = date('Y-m-d H:i:s');

        $insert = "INSERT INTO cab_devoluciones (KCOO, DCTO, DOCO, VR02, AN8, DRQJ, CRCD, d55DECL, ALPH, TAX, ADD1, ADD2, CREG, s55PROCES, id_user, create_at, update_at) 
							values (
								'$KCOO', '$DCTO', '$DOCO', '$VR02', '$AN8', '$DRQJ', '$CRCD', '$d55DECL', '$ALPH', '$TAX', '$ADD1', '$ADD2', '$CREG', '$s55PROCES',  '$id_user', '$create_at', '$update_at'
								)";

        if ($conexion != null) {
            try {

                $stmt = $conexion->prepare($insert);
                $status = 'ok';
                $stmt->execute();
            } catch (Exception $e) {
                $status = 'error';
                echo $e;
                return;
            }
        }


        if ($status == 'ok') {
            //ARMAR INSERT DETALLES

            foreach ($detalles as $key => $detalle) {

                $KCOO = $detalle['KCOO'];
                $DCTO = $detalle['DCTO'];
                $DOCO = $detalle['DOCO'];
                $LNID = $detalle['LNID'];
                $AN8 = $detalle['AN8'];
                $AITM = $detalle['AITM'];
                $UORG = $detalle['UORG'];
                $LPRC = $detalle['LPRC'];
                $UOM = $detalle['UOM'];
                $i55DEPR = $detalle['i55DEPR'];
                $DRQJ = $detalle['DRQJ'];
                $UPC3 = $detalle['UPC3'];
                $s55PROMFM = $detalle['s55PROMFM'];
                $LOCN = $detalle['LOCN'];
                $cod_articulo=$detalle['cod_articulo'];


                $create_at = date('Y-m-d H:i:s');
                $update_at = date('Y-m-d H:i:s');

                $insertDetalle = "INSERT INTO dtl_devoluciones (KCOO, DCTO, DOCO, LNID, AN8, AITM, UORG, LPRC, UOM, i55DEPR, DRQJ, UPC3, s55PROMFM, LOCN, create_at, update_at, id_user, cod_articulo) 
								values (
								'$KCOO', '$DCTO', '$DOCO', '$LNID', '$AN8', '$AITM', '$UORG', '$LPRC', '$UOM', '$i55DEPR', '$DRQJ', '$UPC3', '$s55PROMFM', '$LOCN',
								'$create_at','$update_at', '$id_user', '$cod_articulo')";

                if ($conexion != null) {
                    try {

                        $stmt = $conexion->prepare($insertDetalle);
                        $contador++;
                        $stmt->execute();
                    } catch (Exception $e) {
                        $status = 'error';
                        echo $e;
                    }
                }

                if ($contador == count($detalles)) {
                    $json = array(
                    "status" => "ok",
                    "detalle" => "$DOCO",
                    );
                    echo json_encode($json, true);
                }
            }
        }
    }
} else {
    $json = array(
        "status" => "error",
        "detalle" => "No se econtro el recurso solicitado"
    );

    echo json_encode($json, true);
}