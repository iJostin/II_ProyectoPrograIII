package service;

import model.Paciente;
import persistence.PacienteDAO;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de pacientes
 * Actúa como intermediario entre los controladores y el DAO de pacientes
 * Proporciona métodos para CRUD de pacientes
 */
public class PacienteService {
    private PacienteDAO pacienteDAO = new PacienteDAO();

    /**
     * Agrega un nuevo paciente al sistema
     * @param p Objeto Paciente a agregar
     */
    public void agregar(Paciente p) {
        pacienteDAO.agregar(p);
    }

    /**
     * Actualiza la información de un paciente existente
     * @param p Objeto Paciente con la información actualizada
     */
    public void actualizar(Paciente p) {
        pacienteDAO.actualizar(p);
    }

    /**
     * Elimina un paciente del sistema por su ID
     * @param id ID del paciente a eliminar
     */
    public void eliminar(String id) {
        pacienteDAO.eliminar(id);
    }

    /**
     * Busca un paciente por su ID
     * @param id ID del paciente a buscar
     * @return Objeto Paciente encontrado o null si no existe
     */
    public Paciente buscarPorId(String id) {
        return pacienteDAO.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los pacientes del sistema
     * @return Lista de objetos Paciente
     */
    public List<Paciente> listar() {
        return pacienteDAO.getTodos();
    }
}