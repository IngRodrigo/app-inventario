package com.ajvierci.inventario.retrofit.DOCO.model.response;

import org.simpleframework.xml.Element;


public class DOCOResponseBody {

    @Element(name = "ObtenerSiguienteDOCOResponse", required = false)
    private DOCOResponseModel DOCOResponseModel;

    public DOCOResponseModel getDOCOResponseModel() {
        return DOCOResponseModel;
    }

    public void setDOCOResponseModel(DOCOResponseModel DOCOResponseModel) {
        this.DOCOResponseModel = DOCOResponseModel;
    }
}