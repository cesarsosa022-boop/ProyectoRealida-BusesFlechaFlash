package flechaflash.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Viaje {
    private int idViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaHoraSalida;
    private LocalDateTime fechaHoraLlegada;
    private Colectivo colectivo;
    private double precio;
    private boolean estado;

    public Viaje() {
    }

    public Viaje(int idViaje, String origen, String destino, LocalDateTime fechaHoraSalida, LocalDateTime fechaHoraLlegada, Colectivo colectivo, double precio, boolean estado) {
        this.idViaje = idViaje;
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = fechaHoraLlegada;
        this.colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public Viaje(String origen, String destino, LocalDateTime fechaHoraSalida, LocalDateTime fechaHoraLlegada, Colectivo colectivo, double precio, boolean estado) {
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = fechaHoraLlegada;
        this.colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public Colectivo getColectivo() {
        return colectivo;
    }

    public void setColectivo(Colectivo colectivo) {
        this.colectivo = colectivo;
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
        return "Viaje{" + "idViaje=" + idViaje + ", origen=" + origen + ", destino=" + destino + 
               ", salida=" + fechaHoraSalida + ", llegada=" + fechaHoraLlegada + 
               ", colectivo=" + (colectivo != null ? colectivo.getMatricula() : "N/A") + 
               ", precio=" + precio + ", estado=" + estado + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Viaje other = (Viaje) obj;
        return Objects.equals(this.origen, other.origen) && Objects.equals(this.destino, other.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origen, destino);
    }
}