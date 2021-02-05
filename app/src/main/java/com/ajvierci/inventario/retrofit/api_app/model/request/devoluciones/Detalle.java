package com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detalle {

    @SerializedName("KCOO")
    @Expose
    private String kCOO;
    @SerializedName("DCTO")
    @Expose
    private String dCTO;
    @SerializedName("DOCO")
    @Expose
    private Integer dOCO;
    @SerializedName("LNID")
    @Expose
    private Integer lNID;
    @SerializedName("AN8")
    @Expose
    private Integer aN8;
    @SerializedName("AITM")
    @Expose
    private String aITM;
    @SerializedName("UORG")
    @Expose
    private String uORG;
    @SerializedName("LPRC")
    @Expose
    private Integer lPRC;
    @SerializedName("UOM")
    @Expose
    private String uOM;
    @SerializedName("i55DEPR")
    @Expose
    private Integer i55DEPR;
    @SerializedName("DRQJ")
    @Expose
    private Integer dRQJ;
    @SerializedName("UPC3")
    @Expose
    private String uPC3;
    @SerializedName("s55PROMFM")
    @Expose
    private String s55PROMFM;
    @SerializedName("LOCN")
    @Expose
    private String lOCN;
    @SerializedName("cod_articulo")
    @Expose
    private String codArticulo;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    /**
     * No args constructor for use in serialization
     *
     */
    public Detalle() {
    }

    /**
     *
     * @param i55DEPR
     * @param lPRC
     * @param s55PROMFM
     * @param kCOO
     * @param dRQJ
     * @param aN8
     * @param aITM
     * @param codArticulo
     * @param lNID
     * @param uOM
     * @param dCTO
     * @param dOCO
     * @param uORG
     * @param lOCN
     * @param uPC3
     */
    public Detalle(String kCOO, String dCTO, Integer dOCO, Integer lNID, Integer aN8, String aITM, String uORG, Integer lPRC, String uOM, Integer i55DEPR, Integer dRQJ, String uPC3, String s55PROMFM, String lOCN, String codArticulo, String descripcion) {
        super();
        this.kCOO = kCOO;
        this.dCTO = dCTO;
        this.dOCO = dOCO;
        this.lNID = lNID;
        this.aN8 = aN8;
        this.aITM = aITM;
        this.uORG = uORG;
        this.lPRC = lPRC;
        this.uOM = uOM;
        this.i55DEPR = i55DEPR;
        this.dRQJ = dRQJ;
        this.uPC3 = uPC3;
        this.s55PROMFM = s55PROMFM;
        this.lOCN = lOCN;
        this.codArticulo = codArticulo;
        this.descripcion=descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getKCOO() {
        return kCOO;
    }

    public void setKCOO(String kCOO) {
        this.kCOO = kCOO;
    }

    public String getDCTO() {
        return dCTO;
    }

    public void setDCTO(String dCTO) {
        this.dCTO = dCTO;
    }

    public Integer getDOCO() {
        return dOCO;
    }

    public void setDOCO(Integer dOCO) {
        this.dOCO = dOCO;
    }

    public Integer getLNID() {
        return lNID;
    }

    public void setLNID(Integer lNID) {
        this.lNID = lNID;
    }

    public Integer getAN8() {
        return aN8;
    }

    public void setAN8(Integer aN8) {
        this.aN8 = aN8;
    }

    public String getAITM() {
        return aITM;
    }

    public void setAITM(String aITM) {
        this.aITM = aITM;
    }

    public String getUORG() {
        return uORG;
    }

    public void setUORG(String uORG) {
        this.uORG = uORG;
    }

    public Integer getLPRC() {
        return lPRC;
    }

    public void setLPRC(Integer lPRC) {
        this.lPRC = lPRC;
    }

    public String getUOM() {
        return uOM;
    }

    public void setUOM(String uOM) {
        this.uOM = uOM;
    }

    public Integer getI55DEPR() {
        return i55DEPR;
    }

    public void setI55DEPR(Integer i55DEPR) {
        this.i55DEPR = i55DEPR;
    }

    public Integer getDRQJ() {
        return dRQJ;
    }

    public void setDRQJ(Integer dRQJ) {
        this.dRQJ = dRQJ;
    }

    public String getUPC3() {
        return uPC3;
    }

    public void setUPC3(String uPC3) {
        this.uPC3 = uPC3;
    }

    public String getS55PROMFM() {
        return s55PROMFM;
    }

    public void setS55PROMFM(String s55PROMFM) {
        this.s55PROMFM = s55PROMFM;
    }

    public String getLOCN() {
        return lOCN;
    }

    public void setLOCN(String lOCN) {
        this.lOCN = lOCN;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

}