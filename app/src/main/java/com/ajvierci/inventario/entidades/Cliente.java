package com.ajvierci.inventario.entidades;

public class Cliente {

    private String CodEmpresa, RazonSocial, Direccion, CodRamoERP, CodCanalERP, Telefono, RUC,
    NroListaPrecioERP, Estado, Ubicacion, Email, CodCondicionVentaERP;
    private int NroCuenta, CodVendedor, CodZona, id;
    private double LimiteCredito, SaldoGuaranies, SaldoDolares;

    public Cliente(int id, String codEmpresa, String razonSocial, String direccion, String codRamoERP, String codCanalERP, String telefono, String RUC, String nroListaPrecioERP, String estado, String ubicacion, String email, String codCondicionVentaERP, int nroCuenta, int codVendedor, int codZona, double limiteCredito, double saldoGuaranies, double saldoDolares) {
        CodEmpresa = codEmpresa;
        RazonSocial = razonSocial;
        Direccion = direccion;
        CodRamoERP = codRamoERP;
        CodCanalERP = codCanalERP;
        Telefono = telefono;
        this.RUC = RUC;
        NroListaPrecioERP = nroListaPrecioERP;
        Estado = estado;
        Ubicacion = ubicacion;
        Email = email;
        CodCondicionVentaERP = codCondicionVentaERP;
        NroCuenta = nroCuenta;
        CodVendedor = codVendedor;
        CodZona = codZona;
        LimiteCredito = limiteCredito;
        SaldoGuaranies = saldoGuaranies;
        SaldoDolares = saldoDolares;
        id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente() {

    }

    public String getCodEmpresa() {
        return CodEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        CodEmpresa = codEmpresa;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCodRamoERP() {
        return CodRamoERP;
    }

    public void setCodRamoERP(String codRamoERP) {
        CodRamoERP = codRamoERP;
    }

    public String getCodCanalERP() {
        return CodCanalERP;
    }

    public void setCodCanalERP(String codCanalERP) {
        CodCanalERP = codCanalERP;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getNroListaPrecioERP() {
        return NroListaPrecioERP;
    }

    public void setNroListaPrecioERP(String nroListaPrecioERP) {
        NroListaPrecioERP = nroListaPrecioERP;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCodCondicionVentaERP() {
        return CodCondicionVentaERP;
    }

    public void setCodCondicionVentaERP(String codCondicionVentaERP) {
        CodCondicionVentaERP = codCondicionVentaERP;
    }

    public int getNroCuenta() {
        return NroCuenta;
    }

    public void setNroCuenta(int nroCuenta) {
        NroCuenta = nroCuenta;
    }

    public int getCodVendedor() {
        return CodVendedor;
    }

    public void setCodVendedor(int codVendedor) {
        CodVendedor = codVendedor;
    }

    public int getCodZona() {
        return CodZona;
    }

    public void setCodZona(int codZona) {
        CodZona = codZona;
    }

    public double getLimiteCredito() {
        return LimiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        LimiteCredito = limiteCredito;
    }

    public double getSaldoGuaranies() {
        return SaldoGuaranies;
    }

    public void setSaldoGuaranies(double saldoGuaranies) {
        SaldoGuaranies = saldoGuaranies;
    }

    public double getSaldoDolares() {
        return SaldoDolares;
    }

    public void setSaldoDolares(double saldoDolares) {
        SaldoDolares = saldoDolares;
    }
}
