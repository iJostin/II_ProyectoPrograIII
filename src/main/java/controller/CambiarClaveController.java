package controller;

import model.*;
import service.*;
import javax.swing.*;

/**
 * Controlador para gestionar el cambio de contraseña de usuarios
 * Soporta médicos, farmacéuticos y administradores
 */
public class CambiarClaveController {
    private MedicoService medicoService = new MedicoService();
    private FarmaceutaService farmaceutaService = new FarmaceutaService();
    private AdminService adminService = new AdminService();

    /**
     * Cambia la contraseña de un usuario después de validar las credenciales
     * @param usuarioId ID del usuario que desea cambiar la contraseña
     * @param claveActual Contraseña actual del usuario
     * @param claveNueva Nueva contraseña deseada
     * @param confirmarClave Confirmación de la nueva contraseña
     * @param ventanaPadre Ventana padre para mostrar mensajes de diálogo
     * @return true si el cambio fue exitoso, false en caso contrario
     */
    public boolean cambiarClave(String usuarioId, String claveActual, String claveNueva, String confirmarClave, JFrame ventanaPadre) {
        // Validar que todos los campos estén llenos
        if (usuarioId.trim().isEmpty() || claveActual.isEmpty() || claveNueva.isEmpty() || confirmarClave.isEmpty()) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "⚠️ Por favor, complete todos los campos",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar que la nueva contraseña y su confirmación coincidan
        if (!claveNueva.equals(confirmarClave)) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "⚠️ La nueva contraseña y su confirmación no coinciden",
                    "Error de confirmación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar longitud mínima de la nueva contraseña
        if (claveNueva.length() < 4) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "⚠️ La nueva contraseña debe tener al menos 4 caracteres",
                    "Contraseña muy corta",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            // Buscar y validar en médicos
            Medico medico = medicoService.buscarPorId(usuarioId);
            if (medico != null) {
                if (medico.getClave().equals(claveActual)) {
                    return actualizarClaveMedico(medico, claveNueva, ventanaPadre);
                } else {
                    mostrarErrorClaveIncorrecta(ventanaPadre);
                    return false;
                }
            }

            // Buscar y validar en farmaceutas
            Farmaceuta farmaceuta = farmaceutaService.buscarPorId(usuarioId);
            if (farmaceuta != null) {
                if (farmaceuta.getClave().equals(claveActual)) {
                    return actualizarClaveFarmaceuta(farmaceuta, claveNueva, ventanaPadre);
                } else {
                    mostrarErrorClaveIncorrecta(ventanaPadre);
                    return false;
                }
            }

            // Buscar y validar en administradores
            Admin admin = adminService.buscarPorId(usuarioId);
            if (admin != null) {
                if (admin.getClave().equals(claveActual)) {
                    return actualizarClaveAdmin(admin, claveNueva, ventanaPadre);
                } else {
                    mostrarErrorClaveIncorrecta(ventanaPadre);
                    return false;
                }
            }

            // Si llegamos aquí, el usuario no existe
            JOptionPane.showMessageDialog(ventanaPadre,
                    "❌ Usuario no encontrado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "❌ Error al cambiar la contraseña: " + e.getMessage(),
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza la contraseña de un médico
     * @param medico Objeto Médico a actualizar
     * @param nuevaClave Nueva contraseña
     * @param ventanaPadre Ventana para mostrar mensajes
     * @return true si la actualización fue exitosa
     */
    private boolean actualizarClaveMedico(Medico medico, String nuevaClave, JFrame ventanaPadre) {
        try {
            medico.setClave(nuevaClave);
            medicoService.actualizar(medico);
            mostrarExitoActualizacion(ventanaPadre);
            return true;
        } catch (Exception e) {
            mostrarErrorActualizacion(ventanaPadre, e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la contraseña de un farmacéutico
     * @param farmaceuta Objeto Farmaceuta a actualizar
     * @param nuevaClave Nueva contraseña
     * @param ventanaPadre Ventana para mostrar mensajes
     * @return true si la actualización fue exitosa
     */
    private boolean actualizarClaveFarmaceuta(Farmaceuta farmaceuta, String nuevaClave, JFrame ventanaPadre) {
        try {
            farmaceuta.setClave(nuevaClave);
            farmaceutaService.actualizar(farmaceuta);
            mostrarExitoActualizacion(ventanaPadre);
            return true;
        } catch (Exception e) {
            mostrarErrorActualizacion(ventanaPadre, e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la contraseña de un administrador
     * @param admin Objeto Admin a actualizar
     * @param nuevaClave Nueva contraseña
     * @param ventanaPadre Ventana para mostrar mensajes
     * @return true si la actualización fue exitosa
     */
    private boolean actualizarClaveAdmin(Admin admin, String nuevaClave, JFrame ventanaPadre) {
        try {
            admin.setClave(nuevaClave);
            adminService.actualizar(admin);
            mostrarExitoActualizacion(ventanaPadre);
            return true;
        } catch (Exception e) {
            mostrarErrorActualizacion(ventanaPadre, e.getMessage());
            return false;
        }
    }

    /**
     * Muestra mensaje de error cuando la contraseña actual es incorrecta
     * @param ventanaPadre Ventana donde mostrar el mensaje
     */
    private void mostrarErrorClaveIncorrecta(JFrame ventanaPadre) {
        JOptionPane.showMessageDialog(ventanaPadre,
                "⚠️ La contraseña actual es incorrecta",
                "Contraseña incorrecta",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra mensaje de éxito cuando la contraseña se actualiza correctamente
     * @param ventanaPadre Ventana donde mostrar el mensaje
     */
    private void mostrarExitoActualizacion(JFrame ventanaPadre) {
        JOptionPane.showMessageDialog(ventanaPadre,
                "✅ Contraseña actualizada exitosamente",
                "Cambio exitoso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra mensaje de error cuando falla la actualización de contraseña
     * @param ventanaPadre Ventana donde mostrar el mensaje
     * @param mensaje Mensaje de error específico
     */
    private void mostrarErrorActualizacion(JFrame ventanaPadre, String mensaje) {
        JOptionPane.showMessageDialog(ventanaPadre,
                "❌ Error al actualizar la contraseña: " + mensaje,
                "Error de actualización",
                JOptionPane.ERROR_MESSAGE);
    }
}