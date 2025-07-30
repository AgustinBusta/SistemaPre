package es.proyecto.sistema.SistemaPresupuesto.Ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;

import  javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.proyecto.sistema.SistemaPresupuesto.Servicios.AuthService;
import es.proyecto.sistema.SistemaPresupuesto.model.Vendedor;

@Component // ¡Importante! Para que Spring la gestione e inyecte dependencias
public class LoginScreen extends JFrame {

    private final AuthService authService; // Inyectado por Spring
    private final PresupuestoPanel presupuestoPanel; // Inyectado por Spring, para abrir después del login

    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton;

    @Autowired // Spring inyectará las dependencias necesarias
    public LoginScreen(AuthService authService, PresupuestoPanel presupuestoPanel) {
        this.authService = authService;
        this.presupuestoPanel = presupuestoPanel; // Inyectamos el panel principal aquí

        setTitle("Login - Sistema de Presupuestos");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 2, 10, 10)); // Filas, columnas, hgap, vgap
        ((GridLayout) getLayout()).setVgap(15); // Espacio vertical

        add(new JLabel("Usuario:"));
        userField = new JTextField(15);
        add(userField);

        add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(15);
        add(passwordField);

        add(new JLabel("")); // Espacio en blanco para alinear el botón
        loginButton = new JButton("Ingresar");
        add(loginButton);
    }

    private void addListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());

                Vendedor vendedor = authService.autenticarVendedor(username, password).orElse(null);

                if (vendedor != null) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "¡Bienvenido, " + vendedor.getNombre() + "!");
                    // Cerrar la ventana de login
                    LoginScreen.this.dispose();
                    // Abrir la ventana principal del presupuesto
                    presupuestoPanel.setVendedorConectado(vendedor); // Pasar el vendedor autenticado
                    presupuestoPanel.setVisible(true); // Mostrar el PresupuestoPanel
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}