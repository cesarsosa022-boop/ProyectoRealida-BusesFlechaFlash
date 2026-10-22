
package flechaflash.modelo;

import java.util.Objects;

public class Lugar {
    private int idLugar;
    private Colectivo colectivo;
    private int numeroAsiento;
    private boolean estado;

    public Lugar() {
    }

    public Lugar(int idLugar, Colectivo colectivo, int numeroAsiento, boolean estado) {
        this.idLugar = idLugar;
        this.colectivo = colectivo;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public int getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(int idLugar) {
        this.idLugar = idLugar;
    }

    public Colectivo getColectivo() {
        return colectivo;
    }

    public void setColectivo(Colectivo colectivo) {
        this.colectivo = colectivo;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Lugar{" + "idLugar=" + idLugar + ", colectivo=" + colectivo + ", numeroAsiento=" + numeroAsiento + ", estado=" + (estado ? "Disponible" : "No Disponible")+ '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lugar lugar = (Lugar) obj;
        return idLugar == lugar.idLugar;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idLugar);
    }
    

}
