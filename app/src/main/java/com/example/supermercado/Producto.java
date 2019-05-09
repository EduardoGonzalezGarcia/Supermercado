package com.example.supermercado;

public class Producto {

    String nombre;
    String precioString;
    String descripcion;
    String tienda;
    int cantidad;


    public Producto() { }

    public Producto(String nombre, String precioString, String tienda, String descripcion) {
        this.nombre = nombre;
        this.cantidad=1;
        boolean decimal = false;
        for(int i=0; i<precioString.length();i++){
            if(precioString.charAt(i)==','){
                decimal = true;
                if(precioString.length() == i+2) {
                    precioString = precioString + "0";
                    break;
                }
            }
        }
        if(!decimal) precioString = precioString + ",00";
        this.precioString = precioString;
        this.descripcion = descripcion;
        this.tienda = tienda;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTienda() {
        return tienda;
    }

    public String getPrecioString() {
        return precioString;
    }

    public void setPrecioString(String precioString) {
        boolean decimal = false;
        for(int i=0; i<precioString.length();i++){
            if(precioString.charAt(i)==','){
                decimal = true;
                if(precioString.length() == i+2) {
                    precioString = precioString + "0";
                    break;
                }
            }
        }
        if(!decimal) precioString = precioString + ",00";
        this.precioString = precioString;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
