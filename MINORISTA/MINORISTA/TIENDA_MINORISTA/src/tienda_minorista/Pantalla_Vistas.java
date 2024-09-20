/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Pantalla_Vistas extends JFrame {

    private JTabbedPane tabbedPane;

    public Pantalla_Vistas() {

        setTitle("Vistas Base de Datos Minorista");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        tabbedPane.addTab("Compras por Cliente", Vista2());
        tabbedPane.addTab("Historial de ventas por tienda", Vista3());
        tabbedPane.addTab("Los 20 productos más vendidos en cada tienda", Vista4());
        tabbedPane.addTab("Los 20 productos más vendidos en cada país.", Vista5());
        tabbedPane.addTab("Las 5 tiendas con más ventas en lo que va de año", Vista6());
        tabbedPane.addTab("Tiendas que se vende Coca-Cola más que Pepsi", Vista7());
        tabbedPane.addTab("Los 3 principales tipos de productos que los clientes compran además de la leche", Vista8());
    }

    private JPanel Vista2() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn(" Cliente");
        model.addColumn("Numero de Factura");
        model.addColumn("Fecha");
        model.addColumn("Cantidad");
        model.addColumn("Producto");
        model.addColumn("Precio");
        model.addColumn("Subtotal");
        model.addColumn("ISV");
        model.addColumn("Total");

        actualizarTablaVista2(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista2(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarTablaVista2(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM vista_compras_cliente";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("cliente"), rs.getString("factura_numero"), rs.getString("fecha"), rs.getString("cantidad"), rs.getString("producto"), rs.getString("precio"), rs.getString("subtotal"), rs.getString("isv"), rs.getString("total")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }
    }

    private JPanel Vista3() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn(" ID de la Tienda");
        model.addColumn("Nombre de la Tienda");
        model.addColumn("Numero de Factura");
        model.addColumn("Fecha de Venta");
        model.addColumn("Cliente");
        model.addColumn("Total de Venta");

        actualizarTablaVista3(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista3(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;

    }

      private void actualizarTablaVista3(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM HistorialVentasTienda";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("tienda_id"), rs.getString("nombre_tienda"), rs.getString("numero_factura"), rs.getString("fecha_venta"), rs.getString("cliente"), rs.getString("total_venta")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }
    }

    private JPanel Vista4() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn(" ID de la Tienda");
        model.addColumn("Nombre de la Tienda");
        model.addColumn("Producto");
        model.addColumn("Total Vendido");

        actualizarTablaVista4(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista4(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    
    private void actualizarTablaVista4(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM ProductosMasVendidosTienda";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("tienda_id"), rs.getString("nombre_tienda"), rs.getString("producto"), rs.getString("total_vendido")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }

    }

    private JPanel Vista5() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("Pais");
        model.addColumn("Producto");
        model.addColumn("Total Vendido");
        // model.addColumn("Total Vendido");

        actualizarTablaVista5(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista5(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
     private void actualizarTablaVista5(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM ProductosMasVendidosPais";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("pais"), rs.getString("producto"), rs.getString("total_vendido")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }

    }
    

    private JPanel Vista6() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
          DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("ID de la Tienda");
        model.addColumn("Nombre de la Tienda");
        model.addColumn("Total Vendido");
        // model.addColumn("Total Vendido");

        actualizarTablaVista6(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista6(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }

     private void actualizarTablaVista6(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM TiendasMasVentasAnio";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("tienda_id"), rs.getString("nombre_tienda"), rs.getString("total_vendido")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }

    }

    
    private JPanel Vista7() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("ID de la Tienda");
        model.addColumn("Nombre de la Tienda");
        model.addColumn("Coca-Cola Vendida");
        model.addColumn("Pepsi Vendida");
        // model.addColumn("Total Vendido");

        actualizarTablaVista7(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista7(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
        private void actualizarTablaVista7(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM TiendasCocaColaVsPepsi";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("tienda_id"), rs.getString("nombre_tienda"), rs.getString("coca_cola_vendida"), rs.getString("pepsi_vendida")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }

    }
    
    
     private JPanel Vista8() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
         DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("Tipo de Producto");
        model.addColumn("Total Vendido");
        //model.addColumn("Coca-Cola Vendida");

        actualizarTablaVista8(model);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Vista");

        btnActualizar.addActionListener(e -> actualizarTablaVista8(model));

        panelBotones.add(btnActualizar);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
     
        private void actualizarTablaVista8(DefaultTableModel model) {
        try {

            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM ProductosMasCompradosSinLeche";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {

                Object[] Row = {rs.getString("tipo_producto"), rs.getString("total_vendido")};
                model.addRow(Row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar vistas: " + ex.getMessage());
        }
    }
}
