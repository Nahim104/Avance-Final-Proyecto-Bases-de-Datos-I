package tienda_minorista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class PantallaCliente extends JFrame {
     private JTextField tiendaIdField;
    private JTextArea productosArea;
    private JTextField upcField;
    private JButton verProductosButton;
    private JButton comprarButton;
    private JButton cerrarSesionButton;
    private JTable productosTable;
    private DefaultTableModel productosTableModel;
    
    public PantallaCliente() {

    setTitle("Pantalla Cliente - Tienda Minorista");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);
    
    JPanel panelSuperior = new JPanel(new FlowLayout());
    JLabel tiendaIdLabel = new JLabel("ID de la tienda:");
    tiendaIdField = new JTextField(10);
    verProductosButton = new JButton("Ver productos");
    panelSuperior.add(tiendaIdLabel);
    panelSuperior.add(tiendaIdField);
    panelSuperior.add(verProductosButton);
    
    String[] columnNames = {"Producto", "Marca", "Tipo", "Precio", "Cantidad", "Tamaño", "Embalaje"};
    productosTableModel = new DefaultTableModel(columnNames, 0); // 0 es el número de filas iniciales
    productosTable = new JTable(productosTableModel);
    JScrollPane scrollPane = new JScrollPane(productosTable);
    
    JPanel panelInferior = new JPanel(new FlowLayout());
    JLabel upcLabel = new JLabel("UPC del producto:");
    upcField = new JTextField(10);
    comprarButton = new JButton("Realizar compra");
    cerrarSesionButton = new JButton("Cerrar sesión");
    panelInferior.add(upcLabel);
    panelInferior.add(upcField);
    panelInferior.add(comprarButton);
    panelInferior.add(cerrarSesionButton);
    
    verProductosButton.setBackground(Color.CYAN);
    comprarButton.setBackground(Color.GREEN);
    cerrarSesionButton.setBackground(Color.RED);
    
    add(panelSuperior, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(panelInferior, BorderLayout.SOUTH);
    
    verProductosButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            verProductos();
        }
    });
    
    comprarButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            realizarCompra();
        }
    });
    
    cerrarSesionButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            cerrarSesion();
        }
    });
    
    setVisible(true);
}

 private void verProductos() {
    int tiendaId = Integer.parseInt(tiendaIdField.getText());
    productosTableModel.setRowCount(0); // Limpiar las filas de la tabla antes de añadir nuevos productos
    
    try (Connection con = DriverManager.getConnection("jdbc:mysql://tiendadb1.c3zkqgxrrnas.us-east-1.rds.amazonaws.com:3306/MINORISTA", "admin", "Kokunsuper140401")) {
        String query = "CALL VerProductosPorTienda(?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, tiendaId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String producto = rs.getString("producto");
            String marca = rs.getString("marca");
            String tipo = rs.getString("tipo");
            double precio = rs.getDouble("precio");
            int cantidad = rs.getInt("cantidad");
            String tamaño = rs.getString("tamaño");
            String embalaje = rs.getString("embalaje");
            
            productosTableModel.addRow(new Object[]{producto, marca, tipo, precio, cantidad, tamaño, embalaje});
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al obtener productos: " + e.getMessage());
    }
}

   private void realizarCompra() {
    int tiendaId = Integer.parseInt(tiendaIdField.getText());
    int productoUPC = Integer.parseInt(upcField.getText());
    int clienteId = 1;
    int vendedorId = 201; // Ejemplo, este dato se puede obtener dinámicamente
    int cantidadComprada = 1; // Cantidad fija como mencionaste
    int facturaNumero = 0; // Inicializar la variable para el número de factura
    
    try (Connection con = DriverManager.getConnection("jdbc:mysql://tiendadb1.c3zkqgxrrnas.us-east-1.rds.amazonaws.com:3306/MINORISTA", "admin", "Kokunsuper140401")) {
        String query = "CALL RegistrarCompra2(?, ?, ?, ?, ?, ?)"; // Incluir parámetro de salida
        CallableStatement stmt = con.prepareCall(query);
        stmt.setInt(1, clienteId);
        stmt.setInt(2, tiendaId);
        stmt.setInt(3, productoUPC);
        stmt.setInt(4, vendedorId);
        stmt.setInt(5, cantidadComprada);
        stmt.registerOutParameter(6, java.sql.Types.INTEGER); // Parámetro de salida para el número de factura
        
        // Ejecutar la consulta
        stmt.execute();
        
        // Obtener el número de factura generado
        facturaNumero = stmt.getInt(6);
        
        // Mostrar la factura con el número de factura generado
        mostrarFactura(facturaNumero);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al realizar la compra: " + e.getMessage());
    }
}

// Cambiar el método mostrarFactura() para recibir el número de factura como parámetro
private void mostrarFactura(int facturaNumero) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://tiendadb1.c3zkqgxrrnas.us-east-1.rds.amazonaws.com:3306/MINORISTA", "admin", "Kokunsuper140401")) {
        String query = "CALL MostrarReciboFactura(?)";
        CallableStatement stmt = con.prepareCall(query);
        stmt.setInt(1, facturaNumero); // Pasar el número de factura correcto
        
        // Primer conjunto de resultados: Información general de la factura
        ResultSet rs = stmt.executeQuery();
        StringBuilder factura = new StringBuilder();
        
        // Procesar la información general de la factura
        if (rs.next()) {
            factura.append("Factura Número: ").append(rs.getInt("Número de Factura")).append("\n");
            factura.append("Cliente: ").append(rs.getString("Cliente")).append("\n");
            factura.append("Fecha: ").append(rs.getDate("Fecha")).append("\n");
            factura.append("Subtotal: ").append(rs.getDouble("Subtotal")).append("\n");
            factura.append("ISV: ").append(rs.getDouble("Impuesto")).append("\n");
            factura.append("Total: ").append(rs.getDouble("Total")).append("\n");
        }

        // Pasar al segundo conjunto de resultados: Productos de la factura
        if (stmt.getMoreResults()) {
            rs = stmt.getResultSet(); // Obtener el nuevo ResultSet para los productos
            factura.append("\n--- Productos ---\n");
            while (rs.next()) {
                factura.append("Producto: ").append(rs.getString("Producto")).append("\n");
                factura.append("Cantidad: ").append(rs.getInt("Cantidad")).append("\n");
                factura.append("Precio Unitario: ").append(rs.getDouble("Precio Unitario")).append("\n");
                factura.append("Total Producto: ").append(rs.getDouble("Total Producto")).append("\n\n");
            }
        }
        
        // Mostrar la factura en un diálogo
        JOptionPane.showMessageDialog(this, factura.toString(), "Factura", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al mostrar la factura: " + e.getMessage());
    }
}

    // Método para cerrar sesión
    private void cerrarSesion() {
        dispose();
        new Login().setVisible(true);
    }

    
}
