/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import flechaflash.modelo.Conexion;
import flechaflash.modelo.Pasajero;

public class PasajeroData {
    private Connection con = null;

    public PasajeroData() {
        con = Conexion.getConexion();
    }

    public void guardarPasajero(Pasajero pasajero) {
        String sql = "INSERT INTO pasajeros (dni, nombre, apellido, correo, telefono, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pasajero.getDni());
            ps.setString(2, pasajero.getNombre());
            ps.setString(3, pasajero.getApellido());
            ps.setString(4, pasajero.getCorreo());
            ps.setString(5, pasajero.getTelefono());
            ps.setBoolean(6, pasajero.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pasajero.setIdPasajero(rs.getInt(1));
                System.out.println("-> Pasajero guardado exitosamente con ID: " + pasajero.getIdPasajero());
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al guardar pasajero: " + ex.getMessage());
        }
    }

    public Pasajero buscarPasajero(int id) {
        String sql = "SELECT * FROM pasajeros WHERE idPasajero = ?";
        Pasajero pasajero = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pasajero = new Pasajero();
                pasajero.setIdPasajero(rs.getInt("idPasajero"));
                pasajero.setDni(rs.getString("dni"));
                pasajero.setNombre(rs.getString("nombre"));
                pasajero.setApellido(rs.getString("apellido"));
                pasajero.setCorreo(rs.getString("correo"));
                pasajero.setTelefono(rs.getString("telefono"));
                pasajero.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar pasajero: " + ex.getMessage());
        }
        return pasajero;
    }

    public List<Pasajero> listarPasajeros() {
        List<Pasajero> pasajeros = new ArrayList<>();
        String sql = "SELECT * FROM pasajeros";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pasajero p = new Pasajero();
                p.setIdPasajero(rs.getInt("idPasajero"));
                p.setDni(rs.getString("dni"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setTelefono(rs.getString("telefono"));
                p.setEstado(rs.getBoolean("estado"));
                pasajeros.add(p);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar pasajeros: " + ex.getMessage());
        }
        return pasajeros;
    }

    public void modificarPasajero(Pasajero pasajero) {
        String sql = "UPDATE pasajeros SET dni = ?, nombre = ?, apellido = ?, correo = ?, telefono = ? WHERE idPasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pasajero.getDni());
            ps.setString(2, pasajero.getNombre());
            ps.setString(3, pasajero.getApellido());
            ps.setString(4, pasajero.getCorreo());
            ps.setString(5, pasajero.getTelefono());
            ps.setInt(6, pasajero.getIdPasajero());
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasajero actualizado correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar pasajero: " + ex.getMessage());
        }
    }

    public void bajaLogicaPasajero(int id) {
        String sql = "UPDATE pasajeros SET estado = 0 WHERE idPasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasajero dado de baja logica correctamente (estado = 0).");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de baja al pasajero: " + ex.getMessage());
        }
    }

    public void altaLogicaPasajero(int id) {
        String sql = "UPDATE pasajeros SET estado = 1 WHERE idPasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasajero dado de alta logica correctamente (estado = 1).");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de alta al pasajero: " + ex.getMessage());
        }
    }

    public void borrarPasajero(int id) {
        String sql = "DELETE FROM pasajeros WHERE idPasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasajero borrado fisicamente de la base de datos.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al borrar pasajero: " + ex.getMessage());
        }
    }
}