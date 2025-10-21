package service;

import model.Medicamento;
import persistence.MedicamentoDAO;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de medicamentos
 * Actúa como intermediario entre los controladores y el DAO de medicamentos
 * Proporciona métodos para CRUD de medicamentos
 */
public class MedicamentoService {
    private MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    /**
     * Agrega un nuevo medicamento al sistema
     * @param m Objeto Medicamento a agregar
     */
    public void agregar(Medicamento m) {
        medicamentoDAO.agregar(m);
    }

    /**
     * Actualiza la información de un medicamento existente
     * @param m Objeto Medicamento con la información actualizada
     */
    public void actualizar(Medicamento m) {
        medicamentoDAO.actualizar(m);
    }

    /**
     * Elimina un medicamento del sistema por su código
     * @param codigo Código del medicamento a eliminar
     */
    public void eliminar(String codigo) {
        medicamentoDAO.eliminar(codigo);
    }

    /**
     * Busca un medicamento por su código
     * @param codigo Código del medicamento a buscar
     * @return Objeto Medicamento encontrado o null si no existe
     */
    public Medicamento buscarPorCodigo(String codigo) {
        return medicamentoDAO.buscarPorId(codigo);
    }

    /**
     * Obtiene la lista de todos los medicamentos del sistema
     * @return Lista de objetos Medicamento
     */
    public List<Medicamento> listar() {
        return medicamentoDAO.getTodos();
    }
}