package controller;

import model.Medico;
import service.MedicoService;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de médicos
 * Actúa como intermediario entre la vista y el servicio de médicos
 */
public class MedicoController {
    private MedicoService medicoService = new MedicoService();

    /**
     * Agrega un nuevo médico al sistema
     * @param m Objeto Medico a agregar
     */
    public void agregar(Medico m) {
        medicoService.agregar(m);
    }

    /**
     * Actualiza la información de un médico existente
     * @param m Objeto Medico con la información actualizada
     */
    public void actualizar(Medico m) {
        medicoService.actualizar(m);
    }

    /**
     * Elimina un médico del sistema por su ID
     * @param id ID del médico a eliminar
     */
    public void eliminar(String id) {
        medicoService.eliminar(id);
    }

    /**
     * Busca un médico por su ID
     * @param id ID del médico a buscar
     * @return Objeto Medico encontrado o null si no existe
     */
    public Medico buscar(String id) {
        return medicoService.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los médicos del sistema
     * @return Lista de objetos Medico
     */
    public List<Medico> listar() {
        return medicoService.listar();
    }
}