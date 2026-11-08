/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flechaflash.vista;

import flechaflash.modelo.Colectivo;
import flechaflash.modelo.Pasajero;
import flechaflash.modelo.Viaje;
import flechaflash.persistencia.ColectivoData;
import flechaflash.persistencia.PasajeroData;
import flechaflash.persistencia.ViajeData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        // =====================================================================
        // CARÁTULA: PRESENTACIÓN DE LOS INTEGRANTES DEL GRUPO (Requerimiento)
        // =====================================================================
        System.out.println("=================================================");
        System.out.println("   PROYECTO REALIDAD FLECHAFLASH - 1er ENTREGA");
        System.out.println("   GRUPO: 2");
        System.out.println("   INTEGRANTES:");
        System.out.println("   - Abba Genaro");
        System.out.println("   - Fernandez Jorge Federico");
        System.out.println("   - Sosa Ojeda Cesar Alejandro");
        System.out.println("   - Godoy Aranda Ignacio Agustin");
        System.out.println("=================================================\n");

        PasajeroData pasData = new PasajeroData();

        // ---------------------------------------------------------------------
        // 1. INSERCIÓN DE DATOS DE PRUEBA EN LA BD
        // ---------------------------------------------------------------------
        System.out.println("=== 1. INSERTANDO PASAJEROS DE PRUEBA EN LA BD ===");
        Pasajero pas1 = new Pasajero("32456789", "Mariana", "Rios", "mariana.rios@email.com", "2664551122", true);
        Pasajero pas2 = new Pasajero("36789123", "Esteban", "Quito", "esteban.quito@email.com", "2664883344", true);
        Pasajero pas3 = new Pasajero("41555666", "Valeria", "Suarez", "valeria.suarez@email.com", "2664779900", true);
        Pasajero pas4 = new Pasajero("39222333", "Gonzalo", "Peralta", "gonzalo.peralta@email.com", "2664223344", true);

        pasData.guardarPasajero(pas1);
        pasData.guardarPasajero(pas2);
        pasData.guardarPasajero(pas3);
        pasData.guardarPasajero(pas4);

        // ---------------------------------------------------------------------
        // 2. MOSTRAR PASAJEROS DESDE LA BD
        // ---------------------------------------------------------------------
        System.out.println("\n=== 2. LISTADO DE PASAJEROS REGISTRADOS ===");
        listarYMostrar(pasData);

        // ---------------------------------------------------------------------
        // 3. PRUEBAS OPCIONALES (Buscar, Actualizar, Baja/Alta Lógica, Borrar)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 3. PRUEBAS OPCIONALES ===");
        
        // ---------------------------------------------------------------------
        // 3. OPCIONAL: Método BUSCAR
        // ---------------------------------------------------------------------
        System.out.println("\n=== 3. PRUEBA OPCIONAL: BUSCAR PASAJERO ===");
        Pasajero buscado = pasData.buscarPasajero(pas1.getIdPasajero());
        if (buscado != null) {
            System.out.println("Pasajero encontrado por ID (" + pas1.getIdPasajero() + "): " + buscado);
        }

        // ---------------------------------------------------------------------
        // 4. OPCIONAL: Método ACTUALIZAR (Modificar)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 4. PRUEBA OPCIONAL: ACTUALIZAR PASAJERO ===");
        if (buscado != null) {
            buscado.setNombre("Carlos Alberto");
            pasData.modificarPasajero(buscado); // Actualiza en la BD
            System.out.println("Nombre actualizado a 'Carlos Alberto'.");
        }

        // ---------------------------------------------------------------------
        // 5. OPCIONAL: Método BAJA LÓGICA
        // ---------------------------------------------------------------------
        System.out.println("\n=== 5. PRUEBA OPCIONAL: BAJA LOGICA ===");
        pasData.bajaLogicaPasajero(pas2.getIdPasajero()); // Desactiva al pasajero (estado = 0)
        System.out.println("Pasajeros activos tras la baja logica de ID " + pas2.getIdPasajero() + ":");
        listarYMostrar(pasData);

        // ---------------------------------------------------------------------
        // 6. OPCIONAL: Método ALTA LÓGICA
        // ---------------------------------------------------------------------
        System.out.println("\n=== 6. PRUEBA OPCIONAL: ALTA LOGICA ===");
        pasData.altaLogicaPasajero(pas2.getIdPasajero()); // Reactiva al pasajero (estado = 1)
        System.out.println("Pasajeros activos tras reactivar (Alta Logica) al ID " + pas2.getIdPasajero() + ":");
        listarYMostrar(pasData);

        // ---------------------------------------------------------------------
        // 7. OPCIONAL: Método BORRAR (Baja Física)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 7. PRUEBA OPCIONAL: BORRAR PASAJERO (BAJA FISICA) ===");
        pasData.borrarPasajero(pas3.getIdPasajero()); // Elimina al pasajero de la BD
        System.out.println("Pasajeros activos tras eliminar definitivamente al ID " + pas3.getIdPasajero() + ":");
        listarYMostrar(pasData);
    

        // =====================================================================
        // 4. PRUEBAS PARA VIAJE / VIAJEDATA (Implementación Nueva)
        // =====================================================================
        System.out.println("\n=================================================");
        System.out.println("=== 8. PRUEBAS DE VIAJES Y COLECTIVOS EN CONSOLA ===");
        System.out.println("=================================================");

        ColectivoData colData = new ColectivoData();
        ViajeData viajeData = new ViajeData();

        // Verificar o buscar un colectivo previo para asociarlo al viaje
        Colectivo colectivoPrueba = colData.buscarColectivo(1);
        if (colectivoPrueba == null) {
            colectivoPrueba = new Colectivo("AA123BB", "Mercedes Benz", "Marcopolo", 40, "Juan Perez", true);
            colData.guardarColectivo(colectivoPrueba);
            System.out.println("Colectivo de prueba creado con ID: " + colectivoPrueba.getIdColectivo());
        } else {
            System.out.println("Se reutiliza el colectivo existente con ID: " + colectivoPrueba.getIdColectivo());
        }

        // Inserción de Viajes usando LocalDateTime combinado con la fecha actual
        System.out.println("\n=== 9. INSERTANDO VIAJES DE PRUEBA ===");
        LocalDate hoy = LocalDate.now();
        Viaje viaje1 = new Viaje("San Luis", "Mendoza", LocalDateTime.of(hoy, LocalTime.of(8, 0)), LocalDateTime.of(hoy, LocalTime.of(12, 30)), colectivoPrueba, 15000.00, true);
        Viaje viaje2 = new Viaje("San Luis", "Córdoba", LocalDateTime.of(hoy, LocalTime.of(14, 0)), LocalDateTime.of(hoy, LocalTime.of(18, 0)), colectivoPrueba, 18000.00, true);

        viajeData.guardarViaje(viaje1);
        viajeData.guardarViaje(viaje2);

        // Listar Viajes
        System.out.println("\n=== 10. LISTADO DE VIAJES REGISTRADOS ===");
        listarYMostrarViajes(viajeData);

        // Pruebas opcionales de Viaje
        System.out.println("\n=== 11. PRUEBA OPCIONAL: BUSCAR VIAJE ===");
        Viaje viajeBuscado = viajeData.buscarViaje(viaje1.getIdViaje());
        if (viajeBuscado != null) {
            System.out.println("Viaje encontrado por ID (" + viaje1.getIdViaje() + "): " + viajeBuscado);
        }

        System.out.println("\n=== 12. PRUEBA OPCIONAL: ACTUALIZAR VIAJE ===");
        if (viajeBuscado != null) {
            viajeBuscado.setPrecio(17500.00);
            viajeData.modificarViaje(viajeBuscado);
            System.out.println("Precio del viaje actualizado a $17500.00");
        }

        System.out.println("\n=== 13. PRUEBA OPCIONAL: BAJA LOGICA DE VIAJE ===");
        viajeData.bajaLogicaViaje(viaje2.getIdViaje());
        System.out.println("Viajes tras la baja logica del ID " + viaje2.getIdViaje() + ":");
        listarYMostrarViajes(viajeData);

        System.out.println("\n=== 14. PRUEBA OPCIONAL: ALTA LOGICA DE VIAJE ===");
        viajeData.altaLogicaViaje(viaje2.getIdViaje());
        System.out.println("Viajes tras la alta logica del ID " + viaje2.getIdViaje() + ":");
        listarYMostrarViajes(viajeData);

        System.out.println("\n=== 15. PRUEBA OPCIONAL: BORRAR VIAJE (BAJA FISICA) ===");
        // viajeData.borrarViaje(viaje2.getIdViaje());
    }

    // Método auxiliar para no repetir el bucle de impresión en consola de pasajeros
    private static void listarYMostrar(PasajeroData pasData) {
        List<Pasajero> lista = pasData.listarPasajeros();
        for (Pasajero p : lista) {
            System.out.println(p);
        }
    }

    // Método auxiliar para listar viajes
    private static void listarYMostrarViajes(ViajeData viajeData) {
        List<Viaje> lista = viajeData.listarViajes();
        for (Viaje v : lista) {
            System.out.println(v);
        }
    }
}