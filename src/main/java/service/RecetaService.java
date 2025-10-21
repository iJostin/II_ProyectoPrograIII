package service;

import model.*;
import persistence.RecetaDAO;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de recetas médicas
 * Proporciona métodos para crear recetas, agregar detalles, cambiar estados y consultar
 * Incluye validaciones de negocio y manejo de excepciones
 */
public class RecetaService {
    private RecetaDAO recetaDAO;

    /**
     * Constructor que inicializa el servicio con un nuevo DAO de recetas
     */
    public RecetaService() {
        this.recetaDAO = new RecetaDAO();
    }

    /**
     * Crea una nueva receta médica con validaciones
     * @param id ID único de la receta
     * @param p Paciente asociado a la receta
     * @param m Médico que emite la receta
     * @param fechaRetiro Fecha límite para retirar los medicamentos
     * @return Receta creada
     * @throws IllegalArgumentException Si los parámetros son inválidos
     * @throws IllegalStateException Si ya existe una receta con el mismo ID
     * @throws RuntimeException Si ocurre un error durante la creación
     */
    public Receta crearReceta(String id, Paciente p, Medico m, LocalDate fechaRetiro) {
        // Validaciones
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la receta no puede ser null o vacío");
        }
        if (p == null) {
            throw new IllegalArgumentException("El paciente no puede ser null");
        }
        if (m == null) {
            throw new IllegalArgumentException("El médico no puede ser null");
        }
        if (fechaRetiro == null) {
            throw new IllegalArgumentException("La fecha de retiro no puede ser null");
        }

        // Verificar que no exista ya una receta con el mismo ID
        Receta existente = recetaDAO.buscarPorId(id.trim());
        if (existente != null) {
            throw new IllegalStateException("Ya existe una receta con el ID: " + id);
        }

        try {
            Receta receta = new Receta(id.trim(), p, m, LocalDate.now(), fechaRetiro, "CONFECCIONADA");
            recetaDAO.agregar(receta);
            System.out.println("Receta creada exitosamente con ID: " + id);
            return receta;
        } catch (Exception e) {
            System.err.println("Error al crear receta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear la receta: " + e.getMessage(), e);
        }
    }

    /**
     * Agrega un detalle (medicamento) a una receta existente
     * @param recetaId ID de la receta a la que se agregará el detalle
     * @param d Detalle de receta (medicamento y cantidad)
     * @throws IllegalArgumentException Si los parámetros son inválidos
     * @throws IllegalStateException Si no se encuentra la receta
     * @throws RuntimeException Si ocurre un error durante la operación
     */
    public void agregarDetalle(String recetaId, DetalleReceta d) {
        if (recetaId == null || recetaId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la receta no puede ser null o vacío");
        }
        if (d == null) {
            throw new IllegalArgumentException("El detalle de receta no puede ser null");
        }

        try {
            Receta receta = recetaDAO.buscarPorId(recetaId.trim());
            if (receta != null) {
                receta.agregarDetalle(d);
                recetaDAO.actualizar(receta);
                System.out.println("Detalle agregado a receta ID: " + recetaId);
            } else {
                throw new IllegalStateException("No se encontró la receta con ID: " + recetaId);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar detalle a receta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo agregar el detalle: " + e.getMessage(), e);
        }
    }

    /**
     * Cambia el estado de una receta (CONFECCIONADA, PROCESO, LISTA, ENTREGADA)
     * @param recetaId ID de la receta a actualizar
     * @param nuevoEstado Nuevo estado de la receta
     * @throws IllegalArgumentException Si los parámetros son inválidos
     * @throws IllegalStateException Si no se encuentra la receta
     * @throws RuntimeException Si ocurre un error durante la operación
     */
    public void cambiarEstado(String recetaId, String nuevoEstado) {
        if (recetaId == null || recetaId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la receta no puede ser null o vacío");
        }
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo estado no puede ser null or vacío");
        }

        try {
            Receta receta = recetaDAO.buscarPorId(recetaId.trim());
            if (receta != null) {
                receta.setEstado(nuevoEstado.trim());
                recetaDAO.actualizar(receta);

                // Forzar la recarga del DAO para asegurar consistencia
                this.recetaDAO = new RecetaDAO();

                System.out.println("Estado cambiado para receta ID: " + recetaId + " a: " + nuevoEstado);
            } else {
                throw new IllegalStateException("No se encontró la receta con ID: " + recetaId);
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar estado de receta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo cambiar el estado: " + e.getMessage(), e);
        }
    }

    /**
     * Busca una receta por su ID
     * @param id ID de la receta a buscar
     * @return Objeto Receta encontrado o null si no existe
     */
    public Receta buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return recetaDAO.buscarPorId(id.trim());
    }

    /**
     * Obtiene la lista de todas las recetas del sistema
     * @return Lista de objetos Receta (datos frescos desde el DAO)
     */
    public List<Receta> listarTodas() {
        // Crear nuevo DAO para asegurar que tenemos los datos más recientes
        RecetaDAO daoFresco = new RecetaDAO();
        return daoFresco.getTodos();
    }
}