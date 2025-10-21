package controller;

import model.Paciente;
import service.PacienteService;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de pacientes
 * Actúa como intermediario entre la vista y el servicio de pacientes
 */
public class PacienteController {
    private PacienteService pacienteService = new PacienteService();

    /**
     * Agrega un nuevo paciente al sistema
     * @param p Objeto Paciente a agregar
     */
    public void agregar(Paciente p) {
        pacienteService.agregar(p);
    }

    /**
     * Actualiza la información de un paciente existente
     * @param p Objeto Paciente con la información actualizada
     */
    public void actualizar(Paciente p) {
        pacienteService.actualizar(p);
    }

    /**
     * Elimina un paciente del sistema por su ID
     * @param id ID del paciente a eliminar
     */
    public void eliminar(String id) {
        pacienteService.eliminar(id);
    }

    /**
     * Busca un paciente por su ID
     * @param id ID del paciente a buscar
     * @return Objeto Paciente encontrado o null si no existe
     */
    public Paciente buscar(String id) {
        return pacienteService.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los pacientes del sistema
     * @return Lista de objetos Paciente
     */
    public List<Paciente> listar() {
        return pacienteService.listar();
    }
}