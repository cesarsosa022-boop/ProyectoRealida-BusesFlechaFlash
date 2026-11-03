/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pasaje {
    private int idPasaje;
    private Pasajero pasajero;
    private Lugar lugar;
    private Viaje viaje;
    private LocalDate fechaVenta;
    private LocalTime horaVenta;
    private double precio;
    private boolean estado;

    public Pasaje() {
    }

    public Pasaje(int idPasaje, Pasajero pasajero, Lugar lugar, Viaje viaje, LocalDate fechaVenta, LocalTime horaVenta, double precio, boolean estado) {
        this.idPasaje = idPasaje;
        this.pasajero = pasajero;
        this.lugar = lugar;
        this.viaje = viaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public Pasaje(Pasajero pasajero, Lugar lugar, Viaje viaje, LocalDate fechaVenta, LocalTime horaVenta, double precio, boolean estado) {
        this.pasajero = pasajero;
        this.lugar = lugar;
        this.viaje = viaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(int idPasaje) {
        this.idPasaje = idPasaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public LocalTime getHoraVenta() {
        return horaVenta;
    }

    public void setHoraVenta(LocalTime horaVenta) {
        this.horaVenta = horaVenta;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pasaje #" + idPasaje + " - Pasajero: " + (pasajero != null ? pasajero.getApellido() : "N/A") + 
               " - Asiento: " + (lugar != null ? lugar.getNumeroAsiento() : "N/A") + " - Precio: $" + precio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.idPasaje;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pasaje other = (Pasaje) obj;
        return this.idPasaje == other.idPasaje;
    }
}
