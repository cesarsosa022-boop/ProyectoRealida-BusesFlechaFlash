/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.modelo;

/**
 *
 * @author UPrO
 */
public class Pasajero {
     private int idPasajero;
    private String dni;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private boolean estado;

    public Pasajero(int idPasajero, String dni, String nombre, String apellido, String correo, String telefono, boolean estado) {
        this.idPasajero = idPasajero;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pasajero{" + "idPasajero=" + idPasajero + ", dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", telefono=" + telefono + ", estado=" + estado + '}';
    }
    
}
