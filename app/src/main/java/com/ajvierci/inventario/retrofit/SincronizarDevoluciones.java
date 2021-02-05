package com.ajvierci.inventario.retrofit;

import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SincronizarDevoluciones {
    private String json;
    private String erro="Oracle.DataAccess.Client.OracleException ORA-00001: restricción única (CRPDTA.F554711T_PK) violada    en WSDevoluciones.ConexionOracle.EjecutarComando(String comando)\n" +
            "       en WSDevoluciones.WSDevoluciones.GenerarEDIDevoluciones(String listaDevoluciones";
    public SincronizarDevoluciones(String json){
        this.json=json;
    }

    public boolean setNewDevolucion(){
        boolean resultado=false;
        final String NAMESPACE = "http://tempuri.org/";
        String URL="http://ajvapp-desa.ajvierci.com.py:8088/WSDevoluciones.svc?wsdl";
        final String METHOD_NAME = "GenerarEDIDevoluciones";
        final String SOAP_ACTION ="http://tempuri.org/IWSDevoluciones/GenerarEDIDevoluciones";
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapObject  resultsRequestSOAP=null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //   Se crea un objeto SoapObject para poder realizar la peticion
        //    para consumir el ws SOAP. El constructor recibe
        //    el namespace. Por lo regular el namespace es el dominio
        //    donde se encuentra el web service
        request = new SoapObject(NAMESPACE, METHOD_NAME);

         request.addProperty("lisDevol", json);

        //    Se crea un objeto SoapSerializationEnvelope para serealizar la
        //    peticion SOAP y permitir viajar el mensaje por la nube
        //    el constructor recibe la version de SOAP
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        //Se envuelve la peticion soap
        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        HttpTransportSE transporte = new HttpTransportSE(URL,300000);

        try {
            //Hace la llamada al ws
            transporte.call(SOAP_ACTION, envelope);


            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //resultsRequestSOAP = (SoapObject)envelope.getResponse();

            resultsRequestSOAP = (SoapObject) envelope.getResponse();
            if(resultsRequestSOAP!=null){
                String json=resultsRequestSOAP.getProperty(0).toString().replaceAll("\\[", "").replaceAll("\\]","");
                if(json.equals(erro)){
                    resultado=false;
                }else{
                    resultado=true;
                }
                System.out.println("resultado: "+json);

            }



        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
