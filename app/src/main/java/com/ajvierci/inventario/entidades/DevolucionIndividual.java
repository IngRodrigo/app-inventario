package com.ajvierci.inventario.entidades;

public class DevolucionIndividual {
private int DOCO, AN8, fecha, cantidad, id_devolucion, idDetalle;

private String empresa, descripcionArticulo, json, migrado, codigo_aticulo;

    public String getMigrado() {
        return migrado;
    }

    public void setMigrado(String migrado) {
        this.migrado = migrado;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getDOCO() {
        return DOCO;
    }

    public void setDOCO(int DOCO) {
        this.DOCO = DOCO;
    }

    public int getAN8() {
        return AN8;
    }

    public void setAN8(int AN8) {
        this.AN8 = AN8;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public String getCodigo_aticulo() {
        return codigo_aticulo;
    }

    public void setCodigo_aticulo(String codigo_aticulo) {
        this.codigo_aticulo = codigo_aticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_devolucion() {
        return id_devolucion;
    }

    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public DevolucionIndividual(){

}

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdDetalle() {
        return idDetalle;
    }
}
