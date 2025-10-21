package controller;

import model.*;
import service.RecetaService;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de recetas médicas
 * Actúa como intermediario entre la vista y el servicio de recetas
 */
public class RecetaController {
    private RecetaService recetaService = new RecetaService();

    /**
     * Crea una nueva receta médica
     * @param id ID de la receta
     * @param p Paciente asociado a la receta
     * @param m Médico que emite la receta
     * @param fechaRetiro Fecha límite para retirar los medicamentos
     * @return Objeto Receta creado
     */
    public static Receta crearReceta(String id, Paciente p, Medico m, LocalDate fechaRetiro) {
        RecetaService service = new RecetaService();
        return service.crearReceta(id, p, m, fechaRetiro);
    }

    /**
     * Agrega un detalle (medicamento) a una receta existente
     * @param recetaId ID de la receta a la que se agregará el detalle
     * @param d Detalle de receta (medicamento y cantidad)
     */
    public static void agregarDetalle(String recetaId, DetalleReceta d) {
        RecetaService service = new RecetaService();
        service.agregarDetalle(recetaId, d);
    }

    /**
     * Cambia el estado de una receta (pendiente, dispensada, etc.)
     * @param recetaId ID de la receta a actualizar
     * @param nuevoEstado Nuevo estado de la receta
     */
    public void cambiarEstado(String recetaId, String nuevoEstado) {
        recetaService.cambiarEstado(recetaId, nuevoEstado);
    }

    /**
     * Busca una receta por su ID
     * @param id ID de la receta a buscar
     * @return Objeto Receta encontrado o null si no existe
     */
    public Receta buscarPorId(String id) {
        return recetaService.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todas las recetas del sistema
     * @return Lista de objetos Receta
     */
    public List<Receta> listarTodas() {
        return recetaService.listarTodas();
    }

    /**
     * Busca una receta por su ID (metodo estático para compatibilidad)
     * @param id ID de la receta a buscar
     * @return Objeto Receta encontrado o null si no existe
     */
    public static Receta buscar(String id) {
        RecetaService service = new RecetaService();
        return service.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todas las recetas (metodo estático para compatibilidad)
     * @return Lista de objetos Receta
     */
    public static List<Receta> listar() {
        RecetaService service = new RecetaService();
        return service.listarTodas();
    }

    /**
     * Cambia el estado de una receta (metodo estático para compatibilidad)
     * @param recetaId ID de la receta a actualizar
     * @param nuevoEstado Nuevo estado de la receta
     */
    public static void cambiarEstadoReceta(String recetaId, String nuevoEstado) {
        RecetaService service = new RecetaService();
        service.cambiarEstado(recetaId, nuevoEstado);
    }
}