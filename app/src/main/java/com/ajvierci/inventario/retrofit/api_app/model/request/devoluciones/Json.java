package com.ajvierci.inventario.retrofit.api_app.model.request.devoluciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Json {

    @SerializedName("KCOO")
    @Expose
    private String kCOO;
    @SerializedName("DCTO")
    @Expose
    private String dCTO;
    @SerializedName("DOCO")
    @Expose
    private Integer dOCO;
    @SerializedName("VR02")
    @Expose
    private String vR02;
    @SerializedName("AN8")
    @Expose
    private Integer aN8;
    @SerializedName("DRQJ")
    @Expose
    private Integer dRQJ;
    @SerializedName("CRCD")
    @Expose
    private String cRCD;
    @SerializedName("d55DECL")
    @Expose
    private Integer d55DECL;
    @SerializedName("ALPH")
    @Expose
    private String aLPH;
    @SerializedName("TAX")
    @Expose
    private String tAX;
    @SerializedName("ADD1")
    @Expose
    private String aDD1;
    @SerializedName("ADD2")
    @Expose
    private String aDD2;
    @SerializedName("CREG")
    @Expose
    private Integer cREG;
    @SerializedName("s55PROCES")
    @Expose
    private Integer s55PROCES;
    @SerializedName("detalle")
    @Expose
    private List<Detalle> detalle = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Json() {
    }

    /**
     *
     * @param d55DECL
     * @param s55PROCES
     * @param vR02
     * @param kCOO
     * @param dRQJ
     * @param aN8
     * @param tAX
     * @param aLPH
     * @param detalle
     * @param dCTO
     * @param dOCO
     * @param aDD2
     * @param aDD1
     * @param cRCD
     * @param cREG
     */
    public Json(String kCOO, String dCTO, Integer dOCO, String vR02, Integer aN8, Integer dRQJ, String cRCD, Integer d55DECL, String aLPH, String tAX, String aDD1, String aDD2, Integer cREG, Integer s55PROCES, List<Detalle> detalle) {
        super();
        this.kCOO = kCOO;
        this.dCTO = dCTO;
        this.dOCO = dOCO;
        this.vR02 = vR02;
        this.aN8 = aN8;
        this.dRQJ = dRQJ;
        this.cRCD = cRCD;
        this.d55DECL = d55DECL;
        this.aLPH = aLPH;
        this.tAX = tAX;
        this.aDD1 = aDD1;
        this.aDD2 = aDD2;
        this.cREG = cREG;
        this.s55PROCES = s55PROCES;
        this.detalle = detalle;
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

    public String getVR02() {
        return vR02;
    }

    public void setVR02(String vR02) {
        this.vR02 = vR02;
    }

    public Integer getAN8() {
        return aN8;
    }

    public void setAN8(Integer aN8) {
        this.aN8 = aN8;
    }

    public Integer getDRQJ() {
        return dRQJ;
    }

    public void setDRQJ(Integer dRQJ) {
        this.dRQJ = dRQJ;
    }

    public String getCRCD() {
        return cRCD;
    }

    public void setCRCD(String cRCD) {
        this.cRCD = cRCD;
    }

    public Integer getD55DECL() {
        return d55DECL;
    }

    public void setD55DECL(Integer d55DECL) {
        this.d55DECL = d55DECL;
    }

    public String getALPH() {
        return aLPH;
    }

    public void setALPH(String aLPH) {
        this.aLPH = aLPH;
    }

    public String getTAX() {
        return tAX;
    }

    public void setTAX(String tAX) {
        this.tAX = tAX;
    }

    public String getADD1() {
        return aDD1;
    }

    public void setADD1(String aDD1) {
        this.aDD1 = aDD1;
    }

    public String getADD2() {
        return aDD2;
    }

    public void setADD2(String aDD2) {
        this.aDD2 = aDD2;
    }

    public Integer getCREG() {
        return cREG;
    }

    public void setCREG(Integer cREG) {
        this.cREG = cREG;
    }

    public Integer getS55PROCES() {
        return s55PROCES;
    }

    public void setS55PROCES(Integer s55PROCES) {
        this.s55PROCES = s55PROCES;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

}
