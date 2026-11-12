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
        efectoBotones(panelBotones);
        efectoBuscar(JBbuscar);
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
        tablePasaje.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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
        java.awt.GridBagConstraints gridBagConstraints;

        panelPasajes = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelFormulario = new javax.swing.JPanel();
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
        panelBotones = new javax.swing.JPanel();
        JBguardar = new javax.swing.JButton();
        JBmodificar = new javax.swing.JButton();
        JBlistarPasajes = new javax.swing.JButton();
        JBbajaLogica = new javax.swing.JButton();
        JBaltaLogica = new javax.swing.JButton();
        JBeliminar = new javax.swing.JButton();
        JBlimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePasaje = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 247, 250));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        panelPasajes.setBackground(new java.awt.Color(245, 247, 250));
        panelPasajes.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(31, 41, 55));
        jLabel1.setText("Venta de Pasajes");

        panelFormulario.setBackground(new java.awt.Color(245, 247, 250));
        panelFormulario.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(31, 41, 55));
        jLabel2.setText("Pasajero:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(31, 41, 55));
        jLabel3.setText("Viaje:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(31, 41, 55));
        jLabel4.setText("Lugar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(31, 41, 55));
        jLabel5.setText("Precio:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(jLabel5, gridBagConstraints);

        comboBoxPasajero.setBackground(new java.awt.Color(255, 255, 255));
        comboBoxPasajero.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        comboBoxPasajero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxPasajero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(comboBoxPasajero, gridBagConstraints);

        comboBoxViaje.setBackground(new java.awt.Color(255, 255, 255));
        comboBoxViaje.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        comboBoxViaje.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxViaje.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        comboBoxViaje.addActionListener(this::comboBoxViajeActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(comboBoxViaje, gridBagConstraints);

        comboBoxLugar.setBackground(new java.awt.Color(255, 255, 255));
        comboBoxLugar.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        comboBoxLugar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxLugar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(comboBoxLugar, gridBagConstraints);

        txtPrecio.setBackground(new java.awt.Color(255, 255, 255));
        txtPrecio.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        txtPrecio.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(txtPrecio, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(31, 41, 55));
        jLabel6.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(jLabel6, gridBagConstraints);

        JTFid.setBackground(new java.awt.Color(255, 255, 255));
        JTFid.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        JTFid.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(JTFid, gridBagConstraints);

        JBbuscar.setBackground(new java.awt.Color(37, 99, 235));
        JBbuscar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBbuscar.setForeground(new java.awt.Color(255, 255, 255));
        JBbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/buscar.png"))); // NOI18N
        JBbuscar.setText("Buscar");
        JBbuscar.setBorderPainted(false);
        JBbuscar.setFocusPainted(false);
        JBbuscar.addActionListener(this::JBbuscarActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFormulario.add(JBbuscar, gridBagConstraints);

        panelBotones.setBackground(new java.awt.Color(245, 247, 250));
        panelBotones.setLayout(new java.awt.GridLayout(1, 7, 10, 0));

        JBguardar.setBackground(new java.awt.Color(255, 255, 255));
        JBguardar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBguardar.setForeground(new java.awt.Color(31, 41, 55));
        JBguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/guardar.png"))); // NOI18N
        JBguardar.setText("Guardar");
        JBguardar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBguardar.setContentAreaFilled(false);
        JBguardar.setFocusPainted(false);
        JBguardar.addActionListener(this::JBguardarActionPerformed);
        panelBotones.add(JBguardar);

        JBmodificar.setBackground(new java.awt.Color(255, 255, 255));
        JBmodificar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBmodificar.setForeground(new java.awt.Color(31, 41, 55));
        JBmodificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/editar.png"))); // NOI18N
        JBmodificar.setText("Modificar");
        JBmodificar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBmodificar.setContentAreaFilled(false);
        JBmodificar.setFocusPainted(false);
        JBmodificar.addActionListener(this::JBmodificarActionPerformed);
        panelBotones.add(JBmodificar);

        JBlistarPasajes.setBackground(new java.awt.Color(255, 255, 255));
        JBlistarPasajes.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBlistarPasajes.setForeground(new java.awt.Color(31, 41, 55));
        JBlistarPasajes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/portapapeles.png"))); // NOI18N
        JBlistarPasajes.setText("Listar Pasajes");
        JBlistarPasajes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBlistarPasajes.setContentAreaFilled(false);
        JBlistarPasajes.setFocusPainted(false);
        JBlistarPasajes.addActionListener(this::JBlistarPasajesActionPerformed);
        panelBotones.add(JBlistarPasajes);

        JBbajaLogica.setBackground(new java.awt.Color(255, 255, 255));
        JBbajaLogica.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBbajaLogica.setForeground(new java.awt.Color(31, 41, 55));
        JBbajaLogica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/bajaLogica.png"))); // NOI18N
        JBbajaLogica.setText("Baja Lógica");
        JBbajaLogica.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBbajaLogica.setContentAreaFilled(false);
        JBbajaLogica.setFocusPainted(false);
        JBbajaLogica.addActionListener(this::JBbajaLogicaActionPerformed);
        panelBotones.add(JBbajaLogica);

        JBaltaLogica.setBackground(new java.awt.Color(255, 255, 255));
        JBaltaLogica.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBaltaLogica.setForeground(new java.awt.Color(31, 41, 55));
        JBaltaLogica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/altaLogica.png"))); // NOI18N
        JBaltaLogica.setText("Alta Lógica");
        JBaltaLogica.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBaltaLogica.setContentAreaFilled(false);
        JBaltaLogica.setFocusPainted(false);
        JBaltaLogica.addActionListener(this::JBaltaLogicaActionPerformed);
        panelBotones.add(JBaltaLogica);

        JBeliminar.setBackground(new java.awt.Color(255, 255, 255));
        JBeliminar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBeliminar.setForeground(new java.awt.Color(31, 41, 55));
        JBeliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/eliminar.png"))); // NOI18N
        JBeliminar.setText("Eliminar");
        JBeliminar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBeliminar.setContentAreaFilled(false);
        JBeliminar.setFocusPainted(false);
        JBeliminar.addActionListener(this::JBeliminarActionPerformed);
        panelBotones.add(JBeliminar);

        JBlimpiar.setBackground(new java.awt.Color(255, 255, 255));
        JBlimpiar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        JBlimpiar.setForeground(new java.awt.Color(31, 41, 55));
        JBlimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/limpiar.png"))); // NOI18N
        JBlimpiar.setText("Limpiar");
        JBlimpiar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        JBlimpiar.setContentAreaFilled(false);
        JBlimpiar.setFocusPainted(false);
        JBlimpiar.addActionListener(this::JBlimpiarActionPerformed);
        panelBotones.add(JBlimpiar);

        tablePasaje.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
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
        tablePasaje.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablePasaje.setFillsViewportHeight(true);
        tablePasaje.setGridColor(new java.awt.Color(209, 213, 219));
        tablePasaje.setRowHeight(30);
        tablePasaje.setSelectionBackground(new java.awt.Color(37, 99, 235));
        tablePasaje.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tablePasaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePasajeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePasaje);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flechaflash/icon/boleto.png"))); // NOI18N

        javax.swing.GroupLayout panelPasajesLayout = new javax.swing.GroupLayout(panelPasajes);
        panelPasajes.setLayout(panelPasajesLayout);
        panelPasajesLayout.setHorizontalGroup(
            panelPasajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPasajesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPasajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, 1099, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(panelPasajesLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPasajesLayout.setVerticalGroup(
            panelPasajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPasajesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPasajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPasajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPasajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    
    private void efectoBotones(java.awt.Container panel) {
        final java.awt.Color azul = new java.awt.Color(37, 99, 235);
        final java.awt.Color gris = new java.awt.Color(243, 244, 246);

        for (java.awt.Component c : panel.getComponents()) {
            if (!(c instanceof javax.swing.JButton)) continue;
            javax.swing.JButton b = (javax.swing.JButton) c;

            final boolean principal = b.getBackground().equals(azul);
            final java.awt.Color base       = b.getBackground();
            final java.awt.Color hover      = principal ? new java.awt.Color(59, 130, 246) : new java.awt.Color(239, 246, 255);
            final java.awt.Color presionado = principal ? new java.awt.Color(29, 78, 216)  : new java.awt.Color(219, 234, 254);
            final javax.swing.border.Border bordeNormal = javax.swing.BorderFactory.createCompoundBorder(
                    b.getBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            final javax.swing.border.Border bordeHover  = javax.swing.BorderFactory.createLineBorder(azul, 2);

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (b.isEnabled()) { b.setBackground(hover); b.setBorder(bordeHover); }
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    if (b.isEnabled()) { b.setBackground(base); b.setBorder(bordeNormal); }
                }
                @Override public void mousePressed(java.awt.event.MouseEvent e) {
                    if (b.isEnabled()) b.setBackground(presionado);
                }
                @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                    if (b.isEnabled()) b.setBackground(b.contains(e.getPoint()) ? hover : base);
                }
            });

            // Al habilitar/deshabilitar: gris claro si está deshabilitado
            b.addPropertyChangeListener("enabled", e -> {
                b.setBorder(bordeNormal);
                b.setBackground(b.isEnabled() ? base : gris);
            });

            if (!b.isEnabled()) b.setBackground(gris);
        }
    }
    
    private void efectoBuscar(javax.swing.JButton b) {
        final java.awt.Color base       = b.getBackground();                 // 37, 99, 235
        final java.awt.Color hover      = new java.awt.Color(59, 130, 246);  // azul más claro
        final java.awt.Color presionado = new java.awt.Color(29, 78, 216);   // azul más oscuro
        final java.awt.Color aro        = new java.awt.Color(147, 197, 253); // celeste del borde

        // Borde de 3 px con los mismos insets que el original, así el botón no cambia de tamaño
        java.awt.Insets ins = b.getInsets();
        final javax.swing.border.Border bordeNormal = b.getBorder();
        final javax.swing.border.Border bordeClick = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(aro, 3),
                javax.swing.BorderFactory.createEmptyBorder(
                        Math.max(0, ins.top - 3), Math.max(0, ins.left - 3),
                        Math.max(0, ins.bottom - 3), Math.max(0, ins.right - 3)));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) b.setBackground(hover);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) { b.setBackground(base); b.setBorder(bordeNormal); b.setBorderPainted(false); }
            }
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) { b.setBackground(presionado); b.setBorder(bordeClick); b.setBorderPainted(true); }
            }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBorder(bordeNormal);
                    b.setBorderPainted(false);
                    b.setBackground(b.contains(e.getPoint()) ? hover : base);
                }
            }
        });
    }
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelPasajes;
    private javax.swing.JTable tablePasaje;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
