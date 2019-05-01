package com.example.supermercado;

public class Producto {

    String nombre;
    int precio;
    String precioString;
    String descripcion;
    String tienda;


    public Producto() { }

    public Producto(String nombre, String precioString, String tienda, String descripcion) {
        this.nombre = nombre;
        //this.precio = Integer.parseInt(precioString);
        this.precioString = precioString;
        this.descripcion = descripcion;
        this.tienda = tienda;
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
        this.precioString = precioString;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
