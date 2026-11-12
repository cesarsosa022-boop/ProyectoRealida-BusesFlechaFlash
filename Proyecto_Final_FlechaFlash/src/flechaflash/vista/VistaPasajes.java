/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package flechaflash.vista;
import flechaflash.modelo.Lugar;
import flechaflash.modelo.Pasaje;
import flechaflash.modelo.Pasajero;
import flechaflash.modelo.Viaje;
import flechaflash.persistencia.LugarData;
import flechaflash.persistencia.PasajeData;
import flechaflash.persistencia.PasajeroData;
import flechaflash.persistencia.ViajeData;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class VistaPasajes extends javax.swing.JInternalFrame {

    private DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int f, int c) {
            return false;
        }
    };
    
    public VistaPasajes() {
        initComponents();
        cargarCombos();
        cargarCabecera();
        cargarTabla();
    }

    private void cargarCombos() {
        // 1. Cargar Pasajeros activos
        comboBoxPasajero.removeAllItems();
        comboBoxPasajero.addItem("Seleccione un pasajero");
        PasajeroData pasajeroData = new PasajeroData();
        List<Pasajero> pasajeros = pasajeroData.listarPasajeros();
        for (Pasajero p : pasajeros) {
            if (p.isEstado()) {
                String cadena = p.getIdPasajero() + " - " + p.getNombre() + " " + p.getApellido() + " - DNI: " + p.getDni();
                comboBoxPasajero.addItem(cadena);
            }
        }

        // 2. Cargar Viajes activos
        comboBoxViaje.removeAllItems();
        comboBoxViaje.addItem("Seleccione un viaje");
        ViajeData viajeData = new ViajeData();
        List<Viaje> viajes = viajeData.listarViajes();
        for (Viaje v : viajes) {
            if (v.isEstado()) {
                String cadena = v.getIdViaje() + " - " + v.getOrigen() + " -> " + v.getDestino() + " - " + v.getFechaHoraSalida();
                comboBoxViaje.addItem(cadena);
            }
        }

        comboBoxLugar.removeAllItems();
        comboBoxLugar.addItem("Seleccione primero un viaje");
    }

    private void actualizarAsientosDisponibles() {
        comboBoxLugar.removeAllItems();

        if (comboBoxViaje.getSelectedIndex() < 1) {
            comboBoxLugar.addItem("Seleccione primero un viaje");
            txtPrecio.setText("");
            return;
        }

        comboBoxLugar.addItem("Seleccione un asiento");

        try {
            String selectedViaje = comboBoxViaje.getSelectedItem().toString();
            int idViaje = Integer.parseInt(selectedViaje.split(" - ")[0]);

            ViajeData viajeData = new ViajeData();
            Viaje viaje = viajeData.buscarViaje(idViaje);

            if (viaje != null && viaje.getColectivo() != null) {
                txtPrecio.setText(String.valueOf(viaje.getPrecio()));

                LugarData lugarData = new LugarData();
                List<Lugar> todosLosLugares = lugarData.listarLugaresPorColectivo(viaje.getColectivo().getIdColectivo());

                PasajeData pasajeData = new PasajeData();
                List<Pasaje> pasajesDelViaje = pasajeData.listarPasajesPorViaje(idViaje);

                List<Integer> idsLugaresOcupados = new ArrayList<>();
                for (Pasaje p : pasajesDelViaje) {
                    if (p.isEstado()) {
                        idsLugaresOcupados.add(p.getLugar().getIdLugar());
                    }
                }

                for (Lugar lug : todosLosLugares) {
                    if (!idsLugaresOcupados.contains(lug.getIdLugar())) {
                        comboBoxLugar.addItem("Asiento N° " + lug.getNumeroAsiento() + " - ID: " + lug.getIdLugar());
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar asientos: " + e.getMessage());
        }
    }

    private void limpiarTexto() {
        JTFid.setText("");
        cargarCombos();
        txtPrecio.setText("");
        JBguardar.setEnabled(true);
        JBmodificar.setEnabled(false);
        JBeliminar.setEnabled(false);
        JBbajaLogica.setEnabled(false);
        JBaltaLogica.setEnabled(false);
    }

    private void cargarCabecera() {
        modelo.setColumnCount(0);
        modelo.addColumn("ID");
        modelo.addColumn("Pasajero");
        modelo.addColumn("Ruta Viaje");
        modelo.addColumn("Asiento");
        modelo.addColumn("Fecha Venta");
        modelo.addColumn("Hora Venta");
        modelo.addColumn("Precio");
        modelo.addColumn("Estado");
        tablePasaje.setModel(modelo);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        PasajeData pasajeData = new PasajeData();
        List<Pasaje> pasajes = pasajeData.listarPasajes();

        for (Pasaje p : pasajes) {
            String nomPasajero = p.getPasajero().getNombre() + " " + p.getPasajero().getApellido();
            String rutaViaje = p.getViaje().getOrigen() + " -> " + p.getViaje().getDestino();

            modelo.addRow(new Object[]{
                p.getIdPasaje(),
                nomPasajero,
                rutaViaje,
                "N° " + p.getLugar().getNumeroAsiento(),
                p.getFechaVenta(),
                p.getHoraVenta(),
                "$" + p.getPrecio(),
                p.isEstado()
            });
        }
        ajustarColumnas();
    }

    private void ajustarColumnas() {
        tablePasaje.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int col = 0; col < tablePasaje.getColumnCount(); col++) {
            TableColumn columna = tablePasaje.getColumnModel().getColumn(col);

            TableCellRenderer rendEnc = tablePasaje.getTableHeader().getDefaultRenderer();
            Component enc = rendEnc.getTableCellRendererComponent(
                    tablePasaje, columna.getHeaderValue(), false, false, -1, col);
            int ancho = enc.getPreferredSize().width;

            for (int fila = 0; fila < tablePasaje.getRowCount(); fila++) {
                TableCellRenderer rend = tablePasaje.getCellRenderer(fila, col);
                Component c = tablePasaje.prepareRenderer(rend, fila, col);
                ancho = Math.max(ancho, c.getPreferredSize().width);
            }
            columna.setPreferredWidth(ancho + 15);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboBoxPasajero = new javax.swing.JComboBox<>();
        comboBoxViaje = new javax.swing.JComboBox<>();
        comboBoxLugar = new javax.swing.JComboBox<>();
        txtPrecio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JTFid = new javax.swing.JTextField();
        JBbuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        JBguardar = new javax.swing.JButton();
        JBmodificar = new javax.swing.JButton();
        JBlistarPasajes = new javax.swing.JButton();
        JBbajaLogica = new javax.swing.JButton();
        JBaltaLogica = new javax.swing.JButton();
        JBeliminar = new javax.swing.JButton();
        JBlimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePasaje = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 26)); // NOI18N
        jLabel1.setText("Venta de Pasajes");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Pasajero:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Viaje:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Lugar:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Precio:");

        comboBoxPasajero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboBoxViaje.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxViaje.addActionListener(this::comboBoxViajeActionPerformed);

        comboBoxLugar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Id:");

        JBbuscar.setText("Buscar");
        JBbuscar.addActionListener(this::JBbuscarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxViaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxLugar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(57, 57, 57))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxPasajero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JTFid))))
                .addGap(36, 36, 36)
                .addComponent(JBbuscar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(JTFid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxViaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxLugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jPanel2.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        JBguardar.setText("Guardar");
        JBguardar.addActionListener(this::JBguardarActionPerformed);
        jPanel2.add(JBguardar);

        JBmodificar.setText("Modificar");
        JBmodificar.addActionListener(this::JBmodificarActionPerformed);
        jPanel2.add(JBmodificar);

        JBlistarPasajes.setText("Listar Pasajes");
        JBlistarPasajes.addActionListener(this::JBlistarPasajesActionPerformed);
        jPanel2.add(JBlistarPasajes);

        JBbajaLogica.setText("Baja Lógica");
        JBbajaLogica.addActionListener(this::JBbajaLogicaActionPerformed);
        jPanel2.add(JBbajaLogica);

        JBaltaLogica.setText("Alta Lógica");
        JBaltaLogica.addActionListener(this::JBaltaLogicaActionPerformed);
        jPanel2.add(JBaltaLogica);

        JBeliminar.setText("Eliminar");
        JBeliminar.addActionListener(this::JBeliminarActionPerformed);
        jPanel2.add(JBeliminar);

        JBlimpiar.setText("Limpiar");
        JBlimpiar.addActionListener(this::JBlimpiarActionPerformed);
        jPanel2.add(JBlimpiar);

        tablePasaje.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablePasaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePasajeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePasaje);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxViajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxViajeActionPerformed
        actualizarAsientosDisponibles();
    }//GEN-LAST:event_comboBoxViajeActionPerformed

    private void tablePasajeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePasajeMouseClicked
        int fila = tablePasaje.getSelectedRow();
        if (fila == -1) {
            return;
        }

        int idPasaje = (int) tablePasaje.getValueAt(fila, 0);
        JTFid.setText(String.valueOf(idPasaje));
        JBbuscarActionPerformed(null);
    }//GEN-LAST:event_tablePasajeMouseClicked

    private void JBbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarActionPerformed
       try {
            int id = Integer.parseInt(JTFid.getText().trim());
            PasajeData pasajeData = new PasajeData();
            Pasaje pasaje = pasajeData.buscarPasaje(id);

            if (pasaje != null) {
                txtPrecio.setText(Double.toString(pasaje.getPrecio()));

                String comboPas = pasaje.getPasajero().getIdPasajero() + " - " + pasaje.getPasajero().getNombre() + " " + pasaje.getPasajero().getApellido() + " - DNI: " + pasaje.getPasajero().getDni();
                comboBoxPasajero.setSelectedItem(comboPas);

                String comboVia = pasaje.getViaje().getIdViaje() + " - " + pasaje.getViaje().getOrigen() + " -> " + pasaje.getViaje().getDestino() + " - " + pasaje.getViaje().getFechaHoraSalida();
                comboBoxViaje.setSelectedItem(comboVia);

                actualizarAsientosDisponibles();
                String comboLug = "Asiento N° " + pasaje.getLugar().getNumeroAsiento() + " - ID: " + pasaje.getLugar().getIdLugar();
                comboBoxLugar.addItem(comboLug);
                comboBoxLugar.setSelectedItem(comboLug);

                JBguardar.setEnabled(false);
                JBmodificar.setEnabled(true);
                JBeliminar.setEnabled(true);
                if (pasaje.isEstado()) {
                    JBbajaLogica.setEnabled(true);
                    JBaltaLogica.setEnabled(false);
                } else {
                    JBbajaLogica.setEnabled(false);
                    JBaltaLogica.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún pasaje con el ID especificado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido para buscar.");
        }
    }//GEN-LAST:event_JBbuscarActionPerformed

    private void JBguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBguardarActionPerformed
        try {
            if (comboBoxPasajero.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un pasajero.");
                return;
            }

            if (comboBoxViaje.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje.");
                return;
            }

            if (comboBoxLugar.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un asiento disponible.");
                return;
            }

            String textoPrecio = txtPrecio.getText().trim();
            if (textoPrecio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Precio' no puede estar en blanco.");
                return;
            }

            double precio = Double.parseDouble(textoPrecio);
            if (precio < 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo.");
                return;
            }

            int idPasajero = Integer.parseInt(comboBoxPasajero.getSelectedItem().toString().split(" - ")[0]);
            int idViaje = Integer.parseInt(comboBoxViaje.getSelectedItem().toString().split(" - ")[0]);
            
            String itemLugar = comboBoxLugar.getSelectedItem().toString();
            int idLugar = Integer.parseInt(itemLugar.split(" - ID: ")[1]);

            PasajeroData pasData = new PasajeroData();
            Pasajero pasajero = pasData.buscarPasajero(idPasajero);

            ViajeData viajeData = new ViajeData();
            Viaje viaje = viajeData.buscarViaje(idViaje);

            LugarData lugarData = new LugarData();
            Lugar lugar = lugarData.buscarLugar(idLugar);

            Pasaje nuevoPasaje = new Pasaje(pasajero, lugar, viaje, LocalDate.now(), LocalTime.now(), precio, true);

            PasajeData pasajeData = new PasajeData();
            pasajeData.guardarPasaje(nuevoPasaje);

            JOptionPane.showMessageDialog(this, "¡Pasaje registrado correctamente!");

            limpiarTexto();
            cargarTabla();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos. \nError: Ingrese un valor numérico válido.");
        }
    }//GEN-LAST:event_JBguardarActionPerformed

    private void JBmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBmodificarActionPerformed
        try {
            int idPasaje = Integer.parseInt(JTFid.getText().trim());
            
            if (comboBoxPasajero.getSelectedIndex() < 1 || comboBoxViaje.getSelectedIndex() < 1 || comboBoxLugar.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(this, "Todos los campos de selección deben estar completos.");
                return;
            }

            String textoPrecio = txtPrecio.getText().trim();
            if (textoPrecio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Precio' no puede estar en blanco.");
                return;
            }

            double precio = Double.parseDouble(textoPrecio);

            int idPasajero = Integer.parseInt(comboBoxPasajero.getSelectedItem().toString().split(" - ")[0]);
            int idViaje = Integer.parseInt(comboBoxViaje.getSelectedItem().toString().split(" - ")[0]);
            
            String itemLugar = comboBoxLugar.getSelectedItem().toString();
            int idLugar = Integer.parseInt(itemLugar.split(" - ID: ")[1]);

            PasajeroData pasData = new PasajeroData();
            Pasajero pasajero = pasData.buscarPasajero(idPasajero);

            ViajeData viajeData = new ViajeData();
            Viaje viaje = viajeData.buscarViaje(idViaje);

            LugarData lugarData = new LugarData();
            Lugar lugar = lugarData.buscarLugar(idLugar);

            Pasaje pasajeActualizado = new Pasaje(idPasaje, pasajero, lugar, viaje, LocalDate.now(), LocalTime.now(), precio, true);

            PasajeData pasajeData = new PasajeData();
            pasajeData.modificarPasaje(pasajeActualizado);

            JOptionPane.showMessageDialog(this, "Pasaje modificado correctamente!");

            limpiarTexto();
            cargarTabla();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido para modificar.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }//GEN-LAST:event_JBmodificarActionPerformed

    private void JBlistarPasajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBlistarPasajesActionPerformed
        limpiarTexto();
        cargarCabecera();
        cargarTabla();
    }//GEN-LAST:event_JBlistarPasajesActionPerformed

    private void JBbajaLogicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbajaLogicaActionPerformed
        try {
            int idPasaje = Integer.parseInt(JTFid.getText().trim());
            PasajeData pasajeData = new PasajeData();
            pasajeData.bajaLogicaPasaje(idPasaje);

            JOptionPane.showMessageDialog(this, "Se dio de baja el pasaje correctamente.");
            limpiarTexto();
            cargarTabla();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido.");
        }
    }//GEN-LAST:event_JBbajaLogicaActionPerformed

    private void JBaltaLogicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBaltaLogicaActionPerformed
        try {
            int idPasaje = Integer.parseInt(JTFid.getText().trim());
            PasajeData pasajeData = new PasajeData();
            pasajeData.altaLogicaPasaje(idPasaje);

            JOptionPane.showMessageDialog(this, "Se dio de alta el pasaje correctamente.");
            limpiarTexto();
            cargarTabla();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido.");
        }
    }//GEN-LAST:event_JBaltaLogicaActionPerformed

    private void JBeliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBeliminarActionPerformed
        try {
            int idPasaje = Integer.parseInt(JTFid.getText().trim());
            
            int opt = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el pasaje?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                PasajeData pasajeData = new PasajeData();
                pasajeData.borrarPasaje(idPasaje);

                JOptionPane.showMessageDialog(this, "Pasaje eliminado correctamente.");
                limpiarTexto();
                cargarTabla();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido para eliminar.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }//GEN-LAST:event_JBeliminarActionPerformed

    private void JBlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBlimpiarActionPerformed
        limpiarTexto();
    }//GEN-LAST:event_JBlimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBaltaLogica;
    private javax.swing.JButton JBbajaLogica;
    private javax.swing.JButton JBbuscar;
    private javax.swing.JButton JBeliminar;
    private javax.swing.JButton JBguardar;
    private javax.swing.JButton JBlimpiar;
    private javax.swing.JButton JBlistarPasajes;
    private javax.swing.JButton JBmodificar;
    private javax.swing.JTextField JTFid;
    private javax.swing.JComboBox<String> comboBoxLugar;
    private javax.swing.JComboBox<String> comboBoxPasajero;
    private javax.swing.JComboBox<String> comboBoxViaje;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePasaje;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
