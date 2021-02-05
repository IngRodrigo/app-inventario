package com.ajvierci.inventario.entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Articulos {
    private  int id;
    @SerializedName("CodEmpresa")
    @Expose
    private String codEmpresa;
    @SerializedName("NroArticuloERP")
    @Expose
    private Integer nroArticuloERP;
    @SerializedName("DescripcionArticulo")
    @Expose
    private String descripcionArticulo;
    @SerializedName("CodMarcaERP")
    @Expose
    private String codMarcaERP;
    @SerializedName("CodLineaERP")
    @Expose
    private String codLineaERP;
    @SerializedName("CodTipoERP")
    @Expose
    private String codTipoERP;
    @SerializedName("CodRegimenERP")
    @Expose
    private String codRegimenERP;
    @SerializedName("CodigoBarra")
    @Expose
    private String codigoBarra;
    @SerializedName("CodigoLargo")
    @Expose
    private String codigoLargo;
    @SerializedName("CodCategoriaERP")
    @Expose
    private String codCategoriaERP;

    public int getId() {
        return id;
    }

    public Articulos() {
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("CodProveedor")
    @Expose
    private Integer codProveedor;

    public Articulos(int id, String codEmpresa, Integer nroArticuloERP, String descripcionArticulo, String codMarcaERP, String codLineaERP, String codTipoERP, String codRegimenERP, String codigoBarra, String codigoLargo, String codCategoriaERP, Integer codProveedor) {
        this.id = id;
        this.codEmpresa = codEmpresa;
        this.nroArticuloERP = nroArticuloERP;
        this.descripcionArticulo = descripcionArticulo;
        this.codMarcaERP = codMarcaERP;
        this.codLineaERP = codLineaERP;
        this.codTipoERP = codTipoERP;
        this.codRegimenERP = codRegimenERP;
        this.codigoBarra = codigoBarra;
        this.codigoLargo = codigoLargo;
        this.codCategoriaERP = codCategoriaERP;
        this.codProveedor = codProveedor;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Integer getNroArticuloERP() {
        return nroArticuloERP;
    }

    public void setNroArticuloERP(Integer nroArticuloERP) {
        this.nroArticuloERP = nroArticuloERP;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public String getCodMarcaERP() {
        return codMarcaERP;
    }

    public void setCodMarcaERP(String codMarcaERP) {
        this.codMarcaERP = codMarcaERP;
    }

    public String getCodLineaERP() {
        return codLineaERP;
    }

    public void setCodLineaERP(String codLineaERP) {
        this.codLineaERP = codLineaERP;
    }

    public String getCodTipoERP() {
        return codTipoERP;
    }

    public void setCodTipoERP(String codTipoERP) {
        this.codTipoERP = codTipoERP;
    }

    public String getCodRegimenERP() {
        return codRegimenERP;
    }

    public void setCodRegimenERP(String codRegimenERP) {
        this.codRegimenERP = codRegimenERP;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoLargo() {
        return codigoLargo;
    }

    public void setCodigoLargo(String codigoLargo) {
        this.codigoLargo = codigoLargo;
    }

    public String getCodCategoriaERP() {
        return codCategoriaERP;
    }

    public void setCodCategoriaERP(String codCategoriaERP) {
        this.codCategoriaERP = codCategoriaERP;
    }

    public Integer getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(Integer codProveedor) {
        this.codProveedor = codProveedor;
    }

}