package flechaflash.persistencia;

import flechaflash.modelo.Colectivo;
import flechaflash.modelo.Conexion;
import flechaflash.modelo.Lugar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LugarData {
    private Connection con = null;
    private ColectivoData colectivoData;

    public LugarData() {
        con = Conexion.getConexion();
        colectivoData = new ColectivoData();
    }

    // Método para generar los lugares de un colectivo automáticamente según su capacidad
    public void generarLugaresParaColectivo(Colectivo colectivo) {
        for (int i = 1; i <= colectivo.getCapacidad(); i++) {
            String sql = "INSERT INTO lugar (idColectivo, numeroAsiento, estado) VALUES (?, ?, ?)";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, colectivo.getIdColectivo());
                ps.setInt(2, i);
                ps.setBoolean(3, true); // true = disponible
                ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                System.out.println("Error al generar lugar N° " + i + ": " + ex.getMessage());
            }
        }
        System.out.println("-> Se generaron " + colectivo.getCapacidad() + " lugares para el colectivo ID: " + colectivo.getIdColectivo());
    }

    // Listar todos los lugares/asientos de un colectivo específico
    public List<Lugar> listarLugaresPorColectivo(int idColectivo) {
        List<Lugar> lugares = new ArrayList<>();
        String sql = "SELECT * FROM lugar WHERE idColectivo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idColectivo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lugar lugar = new Lugar();
                lugar.setIdLugar(rs.getInt("idLugar"));
                
                Colectivo colectivo = colectivoData.buscarColectivo(idColectivo);
                lugar.setColectivo(colectivo);
                
                lugar.setNumeroAsiento(rs.getInt("numeroAsiento"));
                lugar.setEstado(rs.getBoolean("estado"));
                
                lugares.add(lugar);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar lugares: " + ex.getMessage());
        }
        return lugares;
    }

    // Buscar un lugar por ID
    public Lugar buscarLugar(int idLugar) {
        String sql = "SELECT * FROM lugar WHERE idLugar = ?";
        Lugar lugar = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idLugar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lugar = new Lugar();
                lugar.setIdLugar(rs.getInt("idLugar"));
                
                int idColectivo = rs.getInt("idColectivo");
                Colectivo colectivo = colectivoData.buscarColectivo(idColectivo);
                lugar.setColectivo(colectivo);
                
                lugar.setNumeroAsiento(rs.getInt("numeroAsiento"));
                lugar.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar lugar: " + ex.getMessage());
        }
        return lugar;
    }
}