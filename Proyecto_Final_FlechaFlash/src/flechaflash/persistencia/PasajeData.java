package flechaflash.persistencia;

import flechaflash.modelo.Conexion;
import flechaflash.modelo.Lugar;
import flechaflash.modelo.Pasaje;
import flechaflash.modelo.Pasajero;
import flechaflash.modelo.Viaje;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PasajeData {
    private Connection con = null;
    private PasajeroData pasajeroData;
    private LugarData lugarData;
    private ViajeData viajeData;

    public PasajeData() {
        con = Conexion.getConexion();
        pasajeroData = new PasajeroData();
        lugarData = new LugarData();
        viajeData = new ViajeData();
    }

    // Guardar / Vender un pasaje
    public void guardarPasaje(Pasaje pasaje) {
        String sql = "INSERT INTO pasaje (idPasajero, idLugar, idViaje, fechaVenta, horaVenta, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pasaje.getPasajero().getIdPasajero());
            ps.setInt(2, pasaje.getLugar().getIdLugar());
            ps.setInt(3, pasaje.getViaje().getIdViaje());
            ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
            ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
            ps.setDouble(6, pasaje.getPrecio());
            ps.setBoolean(7, pasaje.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pasaje.setIdPasaje(rs.getInt(1));
                System.out.println("-> Pasaje guardado exitosamente con ID: " + pasaje.getIdPasaje());
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al guardar pasaje: " + ex.getMessage());
        }
    }

    // Buscar un pasaje por ID
    public Pasaje buscarPasaje(int idPasaje) {
        String sql = "SELECT * FROM pasaje WHERE idPasaje = ?";
        Pasaje pasaje = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPasaje);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pasaje = new Pasaje();
                pasaje.setIdPasaje(rs.getInt("idPasaje"));
                
                int idPasajero = rs.getInt("idPasajero");
                Pasajero pasajero = pasajeroData.buscarPasajero(idPasajero);
                pasaje.setPasajero(pasajero);
                
                int idLugar = rs.getInt("idLugar");
                Lugar lugar = lugarData.buscarLugar(idLugar);
                pasaje.setLugar(lugar);
                
                int idViaje = rs.getInt("idViaje");
                Viaje viaje = viajeData.buscarViaje(idViaje);
                pasaje.setViaje(viaje);
                
                pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
                pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
                pasaje.setPrecio(rs.getDouble("precio"));
                pasaje.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar pasaje: " + ex.getMessage());
        }
        return pasaje;
    }

    // Listar todos los pasajes
    public List<Pasaje> listarPasajes() {
        List<Pasaje> pasajes = new ArrayList<>();
        String sql = "SELECT * FROM pasaje";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pasaje pasaje = new Pasaje();
                pasaje.setIdPasaje(rs.getInt("idPasaje"));
                
                int idPasajero = rs.getInt("idPasajero");
                pasaje.setPasajero(pasajeroData.buscarPasajero(idPasajero));
                
                int idLugar = rs.getInt("idLugar");
                pasaje.setLugar(lugarData.buscarLugar(idLugar));
                
                int idViaje = rs.getInt("idViaje");
                pasaje.setViaje(viajeData.buscarViaje(idViaje));
                
                pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
                pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
                pasaje.setPrecio(rs.getDouble("precio"));
                pasaje.setEstado(rs.getBoolean("estado"));
                
                pasajes.add(pasaje);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar pasajes: " + ex.getMessage());
        }
        return pasajes;
    }

    // Listar pasajes emitidos para un viaje específico
    public List<Pasaje> listarPasajesPorViaje(int idViaje) {
        List<Pasaje> pasajes = new ArrayList<>();
        String sql = "SELECT * FROM pasaje WHERE idViaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pasaje pasaje = new Pasaje();
                pasaje.setIdPasaje(rs.getInt("idPasaje"));
                pasaje.setPasajero(pasajeroData.buscarPasajero(rs.getInt("idPasajero")));
                pasaje.setLugar(lugarData.buscarLugar(rs.getInt("idLugar")));
                pasaje.setViaje(viajeData.buscarViaje(idViaje));
                pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
                pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
                pasaje.setPrecio(rs.getDouble("precio"));
                pasaje.setEstado(rs.getBoolean("estado"));
                
                pasajes.add(pasaje);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al listar pasajes por viaje: " + ex.getMessage());
        }
        return pasajes;
    }

    // Baja lógica de un pasaje (anulación)
    public void bajaLogicaPasaje(int idPasaje) {
        String sql = "UPDATE pasaje SET estado = 0 WHERE idPasaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPasaje);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasaje dado de baja lógica correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al dar de baja el pasaje: " + ex.getMessage());
        }
    }

    // Borrado físico de un pasaje
    public void borrarPasaje(int idPasaje) {
        String sql = "DELETE FROM pasaje WHERE idPasaje = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPasaje);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("-> Pasaje borrado físicamente de la base de datos.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al borrar pasaje: " + ex.getMessage());
        }
    }
}
