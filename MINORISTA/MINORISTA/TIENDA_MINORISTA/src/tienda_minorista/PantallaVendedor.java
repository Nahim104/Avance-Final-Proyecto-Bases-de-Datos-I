package tienda_minorista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PantallaVendedor extends JFrame {
    private JButton btnVerVentas;
    private JButton btnVerVendedores;
    private JButton btnCerrarSesion;

    public PantallaVendedor() {
        setTitle("Vendedor - Sistema Minorista");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Título
        JLabel lblTitulo = new JLabel("Pantalla de Vendedor");
        lblTitulo.setBounds(100, 20, 200, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.BLUE);
        add(lblTitulo);

        // Botón Ver Vendedores
        btnVerVendedores = new JButton("Ver Vendedores Asignados");
        btnVerVendedores.setBounds(100, 60, 200, 30);
        btnVerVendedores.setBackground(Color.CYAN);
        btnVerVendedores.setForeground(Color.BLACK);
        add(btnVerVendedores);

        // Botón Ver Ventas
        btnVerVentas = new JButton("Ver Ventas Realizadas");
        btnVerVentas.setBounds(100, 100, 200, 30);
        btnVerVentas.setBackground(Color.LIGHT_GRAY);
        btnVerVentas.setForeground(Color.BLACK);
        add(btnVerVentas);

        // Botón Cerrar Sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(100, 140, 200, 30);
        btnCerrarSesion.setBackground(Color.RED);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> {
            this.dispose();
            new Login().setVisible(true);
        });
        add(btnCerrarSesion);

        // Agregar funcionalidad a los botones
        btnVerVendedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verVendedoresAsignados();
            }
        });

        btnVerVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verVentasRealizadas();
            }
        });
    }

    private void verVendedoresAsignados() {
        // Implementar la funcionalidad para ver los vendedores asignados a una tienda
        try (Connection conn = new ConexionDB().conectar()) {
            String query = "SELECT * FROM Tienda_Vendedor";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            JFrame frame = new JFrame("Vendedores Asignados");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            String[] columnNames = {"ID Tienda", "Nombre Tienda", "ID Vendedor", "Nombre Vendedor"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(model);

            while (rs.next()) {
                int id = rs.getInt("tienda_id");
                String nombre = rs.getString("nombre_tienda");
                int vendedorId = rs.getInt("vendedor_id");
                String nombre2 = rs.getString("nombre_vendedor");
                model.addRow(new Object[]{id,nombre, vendedorId, nombre2});
            }

            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);
            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar vendedores asignados: " + ex.getMessage());
        }
    }

    private void verVentasRealizadas() {
        // Implementar la funcionalidad para ver las ventas realizadas por el vendedor
        try (Connection conn = new ConexionDB().conectar()) {
            String query = "SELECT * FROM Factura_Vendedor";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JFrame frame = new JFrame("Ventas Realizadas");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            String[] columnNames = {"Factura Número", "Vendedor ID"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(model);

            while (rs.next()) {
                int facturaNumero = rs.getInt("factura_numero");
                int vendedorId = rs.getInt("vendedor_id");
                model.addRow(new Object[]{facturaNumero, vendedorId});
            }

            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);
            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar ventas realizadas: " + ex.getMessage());
        }
    }

    
}



