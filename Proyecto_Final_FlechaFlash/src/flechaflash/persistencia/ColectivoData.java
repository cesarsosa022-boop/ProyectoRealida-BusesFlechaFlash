/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flechaflash.persistencia;

import flechaflash.modelo.Colectivo;
import flechaflash.modelo.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColectivoData {
    private Connection con = null;

    public ColectivoData() {
        con = Conexion.getConexion();
    }

    public void guardarColectivo(Colectivo colectivo) {
        String sql = "INSERT INTO colectivo (matricula, marca, modelo, capacidad, chofer, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, colectivo.getMatricula());
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setBoolean(6, colectivo.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                colectivo.setIdColectivo(rs.getInt(1));
                System.out.println("-> Colectivo guardado exitosamente con ID: " + colectivo.getIdColectivo());
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al guardar colectivo: " + ex.getMessage());
        }
    }

    public Colectivo buscarColectivo(int id) {
        String sql = "SELECT * FROM colectivo WHERE idColectivo = ?";
        Colectivo colectivo = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                colectivo = new Colectivo();
                colectivo.setIdColectivo(rs.getInt("idColectivo"));
                colectivo.setMatricula(rs.getString("matricula"));
                colectivo.setMarca(rs.getString("marca"));
                colectivo.setModelo(rs.getString("modelo"));
                colectivo.setCapacidad(rs.getInt("capacidad"));
                colectivo.setChofer(rs.getString("chofer"));
                colectivo.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar colectivo: " + ex.getMessage());
        }
        return colectivo;
    }

    public List<Colectivo> listarColectivos() {
        List<Colectivo> colectivos = new ArrayList<>();
        String sql = "SELECT * FROM colectivo";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Colectivo c = new Colectivo();
                c.setIdColectivo(rs.getInt("idColectivo"));
                c.setMatricula(rs.getString("matricula"));
                c.setMarca(rs.getString("marca"));
                c.setModelo(rs.getString("modelo"));
                c.setCapacidad(rs.getInt("capacidad"));
                c.setChofer(rs.getString("chofer"));
                c.setEstado(rs.getBoolean("estado"));
                colectivos.add(c);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar colectivos: " + ex.getMessage());
        }
        return colectivos;
    }

    public void modificarColectivo(Colectivo colectivo) {
        String sql = "UPDATE colectivo SET matricula = ?, marca = ?, modelo = ?, capacidad = ?, chofer = ? WHERE idColectivo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, colectivo.getMatricula());
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setInt(6, colectivo.getIdColectivo());
            
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Colectivo actualizado correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar colectivo: " + ex.getMessage());
        }
    }

    public void bajaLogicaColectivo(int id) {
        String sql = "UPDATE colectivo SET estado = 0 WHERE idColectivo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Colectivo dado de baja lógica correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de baja el colectivo: " + ex.getMessage());
        }
    }
    
    public void altaLogicaColectivo(int id) {
        String sql = "UPDATE colectivo SET estado = 1 WHERE idColectivo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Colectivo dado de alta lógica correctamente (estado = 1).");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de alta el colectivo: " + ex.getMessage());
        }
    }

    public void borrarColectivo(int id) {
        String sql = "DELETE FROM colectivo WHERE idColectivo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Colectivo borrado físicamente de la base de datos.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al borrar colectivo: " + ex.getMessage());
        }
    }
}