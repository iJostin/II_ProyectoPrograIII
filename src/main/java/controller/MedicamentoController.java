package controller;

import model.Medicamento;
import service.MedicamentoService;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de medicamentos
 * Actúa como intermediario entre la vista y el servicio de medicamentos
 */
public class MedicamentoController {
    private static MedicamentoService medicamentoService = new MedicamentoService();

    /**
     * Agrega un nuevo medicamento al sistema
     * @param m Objeto Medicamento a agregar
     */
    public void agregar(Medicamento m) {
        medicamentoService.agregar(m);
    }

    /**
     * Actualiza la información de un medicamento existente
     * @param m Objeto Medicamento con la información actualizada
     */
    public void actualizar(Medicamento m) {
        medicamentoService.actualizar(m);
    }

    /**
     * Elimina un medicamento del sistema por su código
     * @param codigo Código del medicamento a eliminar
     */
    public void eliminar(String codigo) {
        medicamentoService.eliminar(codigo);
    }

    /**
     * Busca un medicamento por su código
     * @param codigo Código del medicamento a buscar
     * @return Objeto Medicamento encontrado o null si no existe
     */
    public static Medicamento buscar(String codigo) {
        return medicamentoService.buscarPorCodigo(codigo);
    }

    /**
     * Obtiene la lista de todos los medicamentos del sistema
     * @return Lista de objetos Medicamento
     */
    public List<Medicamento> listar() {
        return medicamentoService.listar();
    }
}