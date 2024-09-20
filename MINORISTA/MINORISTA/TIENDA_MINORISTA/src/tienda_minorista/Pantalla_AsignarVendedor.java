package tienda_minorista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenes
 */
public class Pantalla_AsignarVendedor extends JFrame {

    private JTable vendedoresTable;
    private JTable tiendasTable;
    private DefaultTableModel vendedoresModel;
    private DefaultTableModel tiendasModel;
    private JTabbedPane tabbedPane;
    private DefaultTableModel tableModel;
    private JTable tiendaVendedorTable;


    public Pantalla_AsignarVendedor() {

        setTitle("Datos de Vendedores y Tiendas con Formularios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        tabbedPane.addTab("Lista de Vendedores y Tiendas", listar_tiendas_y_vendedores());
        tabbedPane.addTab("Asignar Vendedores", AsigarVendedores());
        tabbedPane.addTab("Lista de tiendas con Vendedores", Listar_tiendas_con_vendedores());
        tabbedPane.setSelectedIndex(0);
    }

    private JPanel listar_tiendas_y_vendedores() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10)); // Usamos GridLayout para que las tablas estén una sobre la otra

        // Crear el modelo de la tabla de vendedores
        String[] vendedoresColumnNames = {"ID", "Nombre", "Usuario", "Contraseña", "Rol"};
        vendedoresModel = new DefaultTableModel(vendedoresColumnNames, 0);
        vendedoresTable = new JTable(vendedoresModel);
        JScrollPane vendedoresScrollPane = new JScrollPane(vendedoresTable);

        // Crear el modelo de la tabla de tiendas
        String[] tiendasColumnNames = {"ID", "Nombre", "Ubicación", "Horario", "País"};
        tiendasModel = new DefaultTableModel(tiendasColumnNames, 0);
        tiendasTable = new JTable(tiendasModel);
        JScrollPane tiendasScrollPane = new JScrollPane(tiendasTable);

        // Agregar los JScrollPane (con las tablas) al panel
        panel.add(vendedoresScrollPane); // Añadir la tabla de vendedores al panel
        panel.add(tiendasScrollPane);    // Añadir la tabla de tiendas al panel

        // Actualizar ambas tablas con datos de la base de datos
        actualizarTablaVendedores(vendedoresModel);
        actualizarTablaTiendas(tiendasModel);

        return panel;
    }

    private JPanel Listar_tiendas_con_vendedores() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        String[] columnNames = {"ID Tienda", "Nombre Tienda", "ID Vendedor", "Nombre Vendedor"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tiendaVendedorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tiendaVendedorTable);

        // Crear el botón de actualizar
        JButton actualizarButton = new JButton("Actualizar Tabla");
        actualizarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actualizarButton.setMaximumSize(new Dimension(150, 30));

        // Acción del botón para actualizar la tabla
        actualizarButton.addActionListener(e -> {

            actualizarTablaTiendaConVendedores(tableModel);

        });

        // Añadir la tabla y el botón al panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actualizarButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel AsigarVendedores() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nombretiendaLabel = new JLabel("Ingresar Nombre de la Tienda:");
        nombretiendaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nombretiendaTextField = new JTextField(10);
        nombretiendaTextField.setMaximumSize(new Dimension(200, 30));
        nombretiendaTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tiendaLabel = new JLabel("Ingresar ID de Tienda:");
        tiendaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField tiendaTextField = new JTextField(10);
        tiendaTextField.setMaximumSize(new Dimension(200, 30));
        tiendaTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombrevendedorLabel = new JLabel("Ingresar Nombre de Vendedor:");
        nombrevendedorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nombrevendedorTextField = new JTextField(10);
        nombrevendedorTextField.setMaximumSize(new Dimension(200, 30));
        nombrevendedorTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vendedorLabel = new JLabel("Ingresar ID de Vendedor:");
        vendedorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField vendedorTextField = new JTextField(10);
        vendedorTextField.setMaximumSize(new Dimension(200, 30));
        vendedorTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton AsignarVendedor = new JButton("Asignar");
        AsignarVendedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        AsignarVendedor.setMaximumSize(new Dimension(100, 30));

        AsignarVendedor.addActionListener(e -> {
            try {
                // Conexión a la base de datos
                Connection conn = new ConexionDB().conectar();

                // Consulta para llamar al procedimiento almacenado
                String query = "CALL CrearTiendaVendedor(?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);

                // Obtener los valores de los campos de texto
                int tiendaIdInt = Integer.parseInt(tiendaTextField.getText());
                String nombreTienda = nombretiendaTextField.getText();
                int vendedorIdInt = Integer.parseInt(vendedorTextField.getText());
                String nombreVendedor = nombrevendedorTextField.getText();

                // Asignar los valores al PreparedStatement
                stmt.setInt(1, tiendaIdInt);
                stmt.setString(2, nombreTienda);
                stmt.setInt(3, vendedorIdInt);
                stmt.setString(4, nombreVendedor);

                // Ejecutar la consulta
                stmt.executeUpdate();

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(null, "Vendedor asignado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al asignar vendedor: " + ex.getMessage());
            }

        });

        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Añadir espacio superior
        panel.add(nombretiendaLabel);
        panel.add(nombretiendaTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Añadir espacio entre componentes
        panel.add(tiendaLabel);
        panel.add(tiendaTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Añadir espacio entre componentes
        panel.add(nombrevendedorLabel);
        panel.add(nombrevendedorTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Añadir espacio entre componentes
        panel.add(vendedorLabel);
        panel.add(vendedorTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Añadir espacio antes del botón
        panel.add(AsignarVendedor);
        return panel;
    }

    private void actualizarTablaVendedores(DefaultTableModel model) {
        try {
            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM Vendedor";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("contrasena"),
                    rs.getString("rol")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vendedores: " + ex.getMessage());
        }
    }

    private void actualizarTablaTiendas(DefaultTableModel model) {
        try {
            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM Tienda";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("ubicacion"),
                    rs.getString("horario"),
                    rs.getString("pais")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar tiendas: " + ex.getMessage());
        }
    }

    private void actualizarTablaTiendaConVendedores(DefaultTableModel model){
         try {
            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM Tienda_Vendedor";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("tienda_id"),
                    rs.getString("nombre_tienda"),
                    rs.getString("vendedor_id"),
                    rs.getString("nombre_vendedor"),
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar tiendas: " + ex.getMessage());
        }
    }
    
}
