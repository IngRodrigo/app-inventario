package com.ajvierci.inventario.entidades;

public class EntidadClientes {
    //Declarar variables de instancia
    String CodEmpresa;
    int    NroCuentaERP;
    String RazonSocial;
    String Direccion;
    int    CodVendedor;
    int	   CodZona;
    String CodRamoERP;
    String CodCanalERP;
    String Telefono;
    String RUC;
    String NroListaPrecioERP;
    double LimiteCredito;
    double SaldoGuaranies;
    double SaldoDolares;
    String Estado;
    String Ubicacion;
    String Email;
    String CodCondicionVentaERP;


    /*------FUNCIONES------*/


    /*
    //Declarar métodos
    tipo metodo1(parámetros){
        //Cuerpo del método
    }

    tipo metodo2(parámetros){
        //Cuerpo del método
    }*/


    /*------SETTER GETTER Y CONSTRUCTOR------*/
    public EntidadClientes(){

    }

    public EntidadClientes(String CodEmpresa, int NroCuentaERP, String RazonSocial, String Direccion, int CodVendedor, int CodZona, String CodRamoERP, String CodCanalERP, String Telefono, String RUC, String NroListaPrecioERP, double LimiteCredito, double SaldoGuaranies, double SaldoDolares, String Estado, String Ubicacion, String Email, String CodCondicionVentaERP) {
        this.CodEmpresa = CodEmpresa;
        this.NroCuentaERP = NroCuentaERP;
        this.RazonSocial = RazonSocial;
        this.Direccion = Direccion;
        this.CodVendedor = CodVendedor;
        this.CodZona = CodZona;
        this.CodRamoERP = CodRamoERP;
        this.CodCanalERP = CodCanalERP;
        this.Telefono = Telefono;
        this.RUC = RUC;
        this.NroListaPrecioERP = NroListaPrecioERP;
        this.LimiteCredito = LimiteCredito;
        this.SaldoGuaranies = SaldoGuaranies;
        this.SaldoDolares = SaldoDolares;
        this.Estado = Estado;
        this.Ubicacion = Ubicacion;
        this.Email = Email;
        this.CodCondicionVentaERP = CodCondicionVentaERP;
    }

    public String getCodEmpresa() {
        return CodEmpresa;
    }

    public void setCodEmpresa(String CodEmpresa) {
        this.CodEmpresa = CodEmpresa;
    }

    public int getNroCuentaERP() {
        return NroCuentaERP;
    }

    public void setNroCuentaERP(int NroCuentaERP) {
        this.NroCuentaERP = NroCuentaERP;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String RazonSocial) {
        this.RazonSocial = RazonSocial;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public int getCodVendedor() {
        return CodVendedor;
    }

    public void setCodVendedor(int CodVendedor) {
        this.CodVendedor = CodVendedor;
    }

    public int getCodZona() {
        return CodZona;
    }

    public void setCodZona(int CodZona) {
        this.CodZona = CodZona;
    }

    public String getCodRamoERP() {
        return CodRamoERP;
    }

    public void setCodRamoERP(String CodRamoERP) {
        this.CodRamoERP = CodRamoERP;
    }

    public String getCodCanalERP() {
        return CodCanalERP;
    }

    public void setCodCanalERP(String CodCanalERP) {
        this.CodCanalERP = CodCanalERP;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
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

    public void setNroListaPrecioERP(String NroListaPrecioERP) {
        this.NroListaPrecioERP = NroListaPrecioERP;
    }

    public double getLimiteCredito() {
        return LimiteCredito;
    }

    public void setLimiteCredito(double LimiteCredito) {
        this.LimiteCredito = LimiteCredito;
    }

    public double getSaldoGuaranies() {
        return SaldoGuaranies;
    }

    public void setSaldoGuaranies(double SaldoGuaranies) {
        this.SaldoGuaranies = SaldoGuaranies;
    }

    public double getSaldoDolares() {
        return SaldoDolares;
    }

    public void setSaldoDolares(double SaldoDolares) {
        this.SaldoDolares = SaldoDolares;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getCodCondicionVentaERP() {
        return CodCondicionVentaERP;
    }

    public void setCodCondicionVentaERP(String CodCondicionVentaERP) {
        this.CodCondicionVentaERP = CodCondicionVentaERP;
    }
}
