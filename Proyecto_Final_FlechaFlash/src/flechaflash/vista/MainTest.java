/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flechaflash.vista;

import flechaflash.modelo.Pasajero;
import flechaflash.persistencia.PasajeroData;
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
        pasData.bajaLogicaPasajero(pas2.getIdPasajero()); // Desactiva a Ana Mendoza (estado = 0)
        System.out.println("Pasajeros activos tras la baja logica de ID " + pas2.getIdPasajero() + ":");
        listarYMostrar(pasData);

        // ---------------------------------------------------------------------
        // 6. OPCIONAL: Método ALTA LÓGICA
        // ---------------------------------------------------------------------
        System.out.println("\n=== 6. PRUEBA OPCIONAL: ALTA LOGICA ===");
        pasData.altaLogicaPasajero(pas2.getIdPasajero()); // Reactiva a Ana Mendoza (estado = 1)
        System.out.println("Pasajeros activos tras reactivar (Alta Logica) al ID " + pas2.getIdPasajero() + ":");
        listarYMostrar(pasData);

        // ---------------------------------------------------------------------
        // 7. OPCIONAL: Método BORRAR (Baja Física)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 7. PRUEBA OPCIONAL: BORRAR PASAJERO (BAJA FISICA) ===");
        pasData.borrarPasajero(pas3.getIdPasajero()); // Elimina a Lucas Gómez de la BD
        System.out.println("Pasajeros activos tras eliminar definitivamente al ID " + pas3.getIdPasajero() + ":");
        listarYMostrar(pasData);
    }

    // Método auxiliar para no repetir el bucle de impresión en consola
    private static void listarYMostrar(PasajeroData pasData) {
        List<Pasajero> lista = pasData.listarPasajeros();
        for (Pasajero p : lista) {
            System.out.println(p);
        }
    }
}
