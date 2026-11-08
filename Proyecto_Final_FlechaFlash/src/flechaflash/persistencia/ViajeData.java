/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.persistencia;

import flechaflash.modelo.Colectivo;
import flechaflash.modelo.Conexion;
import flechaflash.modelo.Viaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViajeData {
    private Connection con = null;
    private ColectivoData colectivoData;

    public ViajeData() {
        con = Conexion.getConexion();
        colectivoData = new ColectivoData(); 
    }

    public void guardarViaje(Viaje viaje) {
        String sql = "INSERT INTO viaje (origen, destino, horaSalida, horaLlegada, idColectivo, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, viaje.getOrigen());
            ps.setString(2, viaje.getDestino());
            ps.setTimestamp(3, Timestamp.valueOf(viaje.getFechaHoraSalida()));
            ps.setTimestamp(4, Timestamp.valueOf(viaje.getFechaHoraLlegada()));
            ps.setInt(5, viaje.getColectivo().getIdColectivo());
            ps.setDouble(6, viaje.getPrecio());
            ps.setBoolean(7, viaje.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                viaje.setIdViaje(rs.getInt(1));
                System.out.println("-> Viaje guardado exitosamente con ID: " + viaje.getIdViaje());
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al guardar viaje: " + ex.getMessage());
        }
    }

    public Viaje buscarViaje(int id) {
        String sql = "SELECT * FROM viaje WHERE idViaje = ?";
        Viaje viaje = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                viaje = new Viaje();
                viaje.setIdViaje(rs.getInt("idViaje"));
                viaje.setOrigen(rs.getString("origen"));
                viaje.setDestino(rs.getString("destino"));
                
                Timestamp tsSalida = rs.getTimestamp("horaSalida");
                if (tsSalida != null) {
                    viaje.setFechaHoraSalida(tsSalida.toLocalDateTime());
                }
                
                Timestamp tsLlegada = rs.getTimestamp("horaLlegada");
                if (tsLlegada != null) {
                    viaje.setFechaHoraLlegada(tsLlegada.toLocalDateTime());
                }
                
                int idColectivo = rs.getInt("idColectivo");
                Colectivo colectivo = colectivoData != null ? colectivoData.buscarColectivo(idColectivo) : null;
                viaje.setColectivo(colectivo);
                
                viaje.setPrecio(rs.getDouble("precio"));
                viaje.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar viaje: " + ex.getMessage());
        }
        return viaje;
    }

    public List<Viaje> listarViajes() {
        List<Viaje> viajes = new ArrayList<>();
        String sql = "SELECT * FROM viaje";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Viaje viaje = new Viaje();
                viaje.setIdViaje(rs.getInt("idViaje"));
                viaje.setOrigen(rs.getString("origen"));
                viaje.setDestino(rs.getString("destino"));
                
                Timestamp tsSalida = rs.getTimestamp("horaSalida");
                if (tsSalida != null) {
                    viaje.setFechaHoraSalida(tsSalida.toLocalDateTime());
                }
                
                Timestamp tsLlegada = rs.getTimestamp("horaLlegada");
                if (tsLlegada != null) {
                    viaje.setFechaHoraLlegada(tsLlegada.toLocalDateTime());
                }
                
                int idColectivo = rs.getInt("idColectivo");
                Colectivo colectivo = colectivoData != null ? colectivoData.buscarColectivo(idColectivo) : null;
                viaje.setColectivo(colectivo);
                
                viaje.setPrecio(rs.getDouble("precio"));
                viaje.setEstado(rs.getBoolean("estado"));
                
                viajes.add(viaje);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar viajes: " + ex.getMessage());
        }
        return viajes;
    }

    public void modificarViaje(Viaje viaje) {
        String sql = "UPDATE viaje SET origen = ?, destino = ?, horaSalida = ?, horaLlegada = ?, idColectivo = ?, precio = ? WHERE idViaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, viaje.getOrigen());
            ps.setString(2, viaje.getDestino());
            ps.setTimestamp(3, Timestamp.valueOf(viaje.getFechaHoraSalida()));
            ps.setTimestamp(4, Timestamp.valueOf(viaje.getFechaHoraLlegada()));
            ps.setInt(5, viaje.getColectivo().getIdColectivo());
            ps.setDouble(6, viaje.getPrecio());
            ps.setInt(7, viaje.getIdViaje());
            
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Viaje actualizado correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar viaje: " + ex.getMessage());
        }
    }

    public void bajaLogicaViaje(int id) {
        String sql = "UPDATE viaje SET estado = 0 WHERE idViaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Viaje dado de baja lógica correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de baja el viaje: " + ex.getMessage());
        }
    }
    
    public void altaLogicaViaje(int id) {
        String sql = "UPDATE viaje SET estado = 1 WHERE idViaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Viaje dado de alta lógica correctamente (estado = 1).");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de alta el viaje: " + ex.getMessage());
        }
    }

    public void borrarViaje(int id) {
        String sql = "DELETE FROM viaje WHERE idViaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Viaje borrado físicamente de la base de datos.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al borrar viaje: " + ex.getMessage());
        }
    }
}