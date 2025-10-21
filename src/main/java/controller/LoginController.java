package controller;

import model.*;
import service.*;
import view.*;

import javax.swing.*;

/**
 * Controlador para gestionar el proceso de autenticación de usuarios
 * Maneja el login de médicos, farmacéuticos y administradores
 */
public class LoginController {
    private MedicoService medicoService = new MedicoService();
    private FarmaceutaService farmaceutaService = new FarmaceutaService();
    private AdminService adminService = new AdminService();

    /**
     * Autentica a un usuario en el sistema
     * @param id ID del usuario que intenta autenticarse
     * @param clave Contraseña del usuario
     * @param ventanaPadre Ventana padre para mostrar mensajes de error
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean login(String id, String clave, JFrame ventanaPadre) {
        try {
            // Buscar en médicos
            Medico medico = medicoService.buscarPorId(id);
            if (medico != null && medico.getClave().equals(clave)) {
                abrirVistaMedico(medico);
                return true;
            }

            // Buscar en farmaceutas
            Farmaceuta farmaceuta = farmaceutaService.buscarPorId(id);
            if (farmaceuta != null && farmaceuta.getClave().equals(clave)) {
                abrirVistaFarmaceuta(farmaceuta);
                return true;
            }

            // Buscar en administradores
            Admin admin = adminService.buscarPorId(id);
            if (admin != null && admin.getClave().equals(clave)) {
                abrirVistaAdmin(admin);
                return true;
            }

            // Si llegamos aquí, las credenciales son incorrectas
            JOptionPane.showMessageDialog(ventanaPadre,
                    "⚠️ Usuario o contraseña incorrectos",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            return false;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "Error al intentar conectar con la base de datos: " + e.getMessage(),
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Abre la vista correspondiente para un médico autenticado
     * @param medico Objeto Médico autenticado
     */
    private void abrirVistaMedico(Medico medico) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MedicoView medicoView = new MedicoView(medico);
                medicoView.setVisible(true);
                System.out.println("Acceso exitoso - Médico: " + medico.getNombre());
            }
        });
    }

    /**
     * Abre la vista correspondiente para un farmacéutico autenticado
     * @param farmaceuta Objeto Farmaceuta autenticado
     */
    private void abrirVistaFarmaceuta(Farmaceuta farmaceuta) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FarmaceutaView farmaceutaView = new FarmaceutaView(farmaceuta);
                farmaceutaView.setVisible(true);
                System.out.println("Acceso exitoso - Farmaceuta: " + farmaceuta.getNombre());
            }
        });
    }

    /**
     * Abre la vista correspondiente para un administrador autenticado
     * @param admin Objeto Admin autenticado
     */
    private void abrirVistaAdmin(Admin admin) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminView adminView = new AdminView(admin);
                adminView.setVisible(true);
                System.out.println("Acceso exitoso - Administrador: " + admin.getNombre());
            }
        });
    }
}