package tienda_minorista;
import tienda_minorista.ConexionDB;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboRol;
    private JButton btnLogin;

    public Login() {
         
        // Configuración del JFrame
        setTitle("Login - Sistema Minorista");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        // Componentes
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(10, 20, 80, 25);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(100, 20, 160, 25);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(10, 60, 80, 25);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(100, 60, 160, 25);
        add(txtContrasena);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(10, 100, 80, 25);
        add(lblRol);

        String[] roles = {"Administrador", "Vendedor", "Cliente"};
        comboRol = new JComboBox<>(roles);
        comboRol.setBounds(100, 100, 160, 25);
        add(comboRol);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(100, 140, 160, 25);
        add(btnLogin);

        // Evento del botón Login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String contrasena = new String(txtContrasena.getPassword());
                String rol = comboRol.getSelectedItem().toString();

                verificarLogin(usuario, contrasena, rol);
            }
        });
    }

    private void verificarLogin(String usuario, String contrasena, String rol) {
        if (rol.equals("Administrador")) {
            // Usuario fijo para el administrador
            if (usuario.equals("Admin") && contrasena.equals("12345")) {
                JOptionPane.showMessageDialog(this, "¡Login exitoso como Administrador!");
                abrirPantallaPorRol("Administrador");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos para Administrador");
            }
            return;
        }

        // Para Cliente y Vendedor, consulta la base de datos
        try {
            Connection conn = new ConexionDB().conectar();
            String query = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ? AND rol = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            stmt.setString(3, rol);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rolDB = rs.getString("rol"); // Obtiene el rol desde la base de datos
                if (rolDB.equals(rol)) { // Verifica si coincide con el rol seleccionado en el login
                    JOptionPane.showMessageDialog(this, "¡Login exitoso como " + rol + "!");
                    abrirPantallaPorRol(rol);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: El rol seleccionado no coincide con el rol registrado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void abrirPantallaPorRol(String rol) {
        switch (rol) {
            case "Administrador":
                new PantallaAdministrador().setVisible(true);
                break;
            case "Vendedor":
                new PantallaVendedor().setVisible(true);
                break;
            case "Cliente":
                new PantallaCliente().setVisible(true);
                break;
        }
        this.dispose(); // Cierra la ventana actual del login
    }

    public static void main(String[] args) {
        ConexionDB conexionDB = new ConexionDB();
        conexionDB.conectar();
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        
    }
}

