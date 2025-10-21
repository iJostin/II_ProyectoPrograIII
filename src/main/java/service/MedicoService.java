package service;

import model.Medico;
import persistence.MedicoDAO;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de médicos
 * Actúa como intermediario entre los controladores y el DAO de médicos
 * Proporciona métodos para CRUD de médicos
 */
public class MedicoService {
    private MedicoDAO medicoDAO = new MedicoDAO();

    /**
     * Agrega un nuevo médico al sistema
     * @param m Objeto Medico a agregar
     */
    public void agregar(Medico m) {
        medicoDAO.agregar(m);
    }

    /**
     * Actualiza la información de un médico existente
     * @param m Objeto Medico con la información actualizada
     */
    public void actualizar(Medico m) {
        medicoDAO.actualizar(m);
    }

    /**
     * Elimina un médico del sistema por su ID
     * @param id ID del médico a eliminar
     */
    public void eliminar(String id) {
        medicoDAO.eliminar(id);
    }

    /**
     * Busca un médico por su ID
     * @param id ID del médico a buscar
     * @return Objeto Medico encontrado o null si no existe
     */
    public Medico buscarPorId(String id) {
        return medicoDAO.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los médicos del sistema
     * @return Lista de objetos Medico
     */
    public List<Medico> listar() {
        return medicoDAO.getTodos();
    }
}