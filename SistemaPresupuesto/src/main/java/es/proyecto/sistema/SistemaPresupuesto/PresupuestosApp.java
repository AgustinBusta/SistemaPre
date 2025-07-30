package es.proyecto.sistema.SistemaPresupuesto;

import java.awt.EventQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext; // ← Import corregido

import es.proyecto.sistema.SistemaPresupuesto.Ui.LoginScreen;

@SpringBootApplication(scanBasePackages = "es.proyecto.sistema.SistemaPresupuesto")
public class PresupuestosApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PresupuestosApp.class, args);

        EventQueue.invokeLater(() -> {
            LoginScreen loginScreen = context.getBean(LoginScreen.class);
            loginScreen.setVisible(true);
        });
    }
}
