package com.example.supermercado;

public class Producto {

    String nombre;
    int precio;
    String precioString;
    String descripcion;


    public Producto() { }

    public Producto(String nombre, String precioString, String descripcion) {
        this.nombre = nombre;
        //this.precio = Integer.parseInt(precioString);
        this.precioString = precioString;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
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
