package com.ajvierci.inventario.entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detalle {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    String codarticulo;

    public String getCodarticulo() {
        return codarticulo;
    }

    public void setCodarticulo(String codarticulo) {
        this.codarticulo = codarticulo;
    }

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
    private Integer uORG;
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

    public Integer getUORG() {
        return uORG;
    }

    public void setUORG(Integer uORG) {
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

    @Override
    public String toString() {
        return "{" +
                "\"kCOO\"='" + kCOO + '\'' +
                ", \"dCTO\"='" + dCTO + '\'' +
                ", \"dOCO\"=" + dOCO +
                ", \"lNID\"=" + lNID +
                ", \"aN8\"=" + aN8 +
                ", \"aITM\"='" + aITM + '\'' +
                ", \"uORG\"=" + uORG +
                ", \"lPRC\"=" + lPRC +
                ", \"uOM\"='" + uOM + '\'' +
                ", \"i55DEPR\"=" + i55DEPR +
                ", \"dRQJ\"=" + dRQJ +
                ", \"uPC3\"='" + uPC3 + '\'' +
                ", \"s55PROMFM\"='" + s55PROMFM + '\'' +
                ", \"lOCN\"='" + lOCN + '\'' +
                '}';
    }
}