package service;

import model.Farmaceuta;
import persistence.FarmaceutaDAO;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de farmacéuticos
 * Actúa como intermediario entre los controladores y el DAO de farmacéuticos
 * Proporciona métodos para CRUD de farmacéuticos
 */
public class FarmaceutaService {
    private FarmaceutaDAO farmaceutaDAO = new FarmaceutaDAO();

    /**
     * Agrega un nuevo farmacéutico al sistema
     * @param f Objeto Farmaceuta a agregar
     */
    public void agregar(Farmaceuta f) {
        farmaceutaDAO.agregar(f);
    }

    /**
     * Actualiza la información de un farmacéutico existente
     * @param f Objeto Farmaceuta con la información actualizada
     */
    public void actualizar(Farmaceuta f) {
        farmaceutaDAO.actualizar(f);
    }

    /**
     * Elimina un farmacéutico del sistema por su ID
     * @param id ID del farmacéutico a eliminar
     */
    public void eliminar(String id) {
        farmaceutaDAO.eliminar(id);
    }

    /**
     * Busca un farmacéutico por su ID
     * @param id ID del farmacéutico a buscar
     * @return Objeto Farmaceuta encontrado o null si no existe
     */
    public Farmaceuta buscarPorId(String id) {
        return farmaceutaDAO.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los farmacéuticos del sistema
     * @return Lista de objetos Farmaceuta
     */
    public List<Farmaceuta> listar() {
        return farmaceutaDAO.getTodos();
    }
}