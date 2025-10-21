package controller;

import model.Farmaceuta;
import service.FarmaceutaService;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de farmacéuticos
 * Actúa como intermediario entre la vista y el servicio de farmacéuticos
 */
public class FarmaceutaController {
    private FarmaceutaService farmaceutaService = new FarmaceutaService();

    /**
     * Agrega un nuevo farmacéutico al sistema
     * @param f Objeto Farmaceuta a agregar
     */
    public void agregar(Farmaceuta f) {
        farmaceutaService.agregar(f);
    }

    /**
     * Actualiza la información de un farmacéutico existente
     * @param f Objeto Farmaceuta con la información actualizada
     */
    public void actualizar(Farmaceuta f) {
        farmaceutaService.actualizar(f);
    }

    /**
     * Elimina un farmacéutico del sistema por su ID
     * @param id ID del farmacéutico a eliminar
     */
    public void eliminar(String id) {
        farmaceutaService.eliminar(id);
    }

    /**
     * Busca un farmacéutico por su ID
     * @param id ID del farmacéutico a buscar
     * @return Objeto Farmaceuta encontrado o null si no existe
     */
    public Farmaceuta buscar(String id) {
        return farmaceutaService.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los farmacéuticos del sistema
     * @return Lista de objetos Farmaceuta
     */
    public List<Farmaceuta> listar() {
        return farmaceutaService.listar();
    }
}