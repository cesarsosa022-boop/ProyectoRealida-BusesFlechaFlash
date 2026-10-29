/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.modelo;

import java.time.Year;
import java.util.Objects;

/**
 *
 * @author UPrO
 */
public class Colectivo {
    private int idColectivo;
    private String matricula;
    private String marca;
    private Year modelo;
    private Lugar capacidad;
    private String chofer;
    private boolean estado;

    public Colectivo(int idColectivo, String matricula, String marca, Year modelo, Lugar capacidad, String chofer, boolean estado) {
        this.idColectivo = idColectivo;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.chofer = chofer;
        this.estado = estado;
    }

    public int getIdColectivo() {
        return idColectivo;
    }

    public void setIdColectivo(int idColectivo) {
        this.idColectivo = idColectivo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Year getModelo() {
        return modelo;
    }

    public void setModelo(Year modelo) {
        this.modelo = modelo;
    }

    public Lugar getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Lugar capacidad) {
        this.capacidad = capacidad;
    }

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Colectivo{" + "idColectivo=" + idColectivo + ", matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", capacidad=" + capacidad + ", chofer=" + chofer + ", estado=" + estado + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.idColectivo;
        hash = 67 * hash + Objects.hashCode(this.matricula);
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
        final Colectivo other = (Colectivo) obj;
        if (this.idColectivo != other.idColectivo) {
            return false;
        }
        return Objects.equals(this.matricula, other.matricula);
    }
    
    
    
}
