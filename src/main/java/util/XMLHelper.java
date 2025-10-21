package util;

import jakarta.xml.bind.*;
import java.io.File;

/**
 * Utilidad para el manejo de archivos XML mediante JAXB
 * Proporciona métodos para guardar, cargar y gestionar archivos XML con serialización de objetos
 */
public class XMLHelper {

    /**
     * Guarda un objeto en un archivo XML con formato y codificación UTF-8
     * Crea automáticamente los directorios padres si no existen
     * @param <T> Tipo del objeto a guardar
     * @param ruta Ruta completa del archivo XML donde se guardará el objeto
     * @param objeto Objeto a serializar en XML
     * @throws RuntimeException Si ocurre un error durante la serialización o guardado
     */
    public static <T> void guardar(String ruta, T objeto) {
        try {
            // Crear directorio padre si no existe
            File archivo = new File(ruta);
            File directorioPadre = archivo.getParentFile();
            if (directorioPadre != null && !directorioPadre.exists()) {
                directorioPadre.mkdirs();
            }

            JAXBContext context = JAXBContext.newInstance(objeto.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(objeto, archivo);

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar el archivo XML: " + ruta, e);
        }
    }

    /**
     * Carga un objeto desde un archivo XML
     * @param <T> Tipo del objeto a cargar
     * @param ruta Ruta completa del archivo XML a cargar
     * @param clazz Clase del objeto a deserializar
     * @return Objeto deserializado o null si el archivo no existe o hay error
     */
    @SuppressWarnings("unchecked")
    public static <T> T cargar(String ruta, Class<T> clazz) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return null; // Archivo no existe, retornar null
            }

            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(archivo);

        } catch (JAXBException e) {
            System.err.println("Error al cargar el archivo XML: " + ruta + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si un archivo existe en la ruta especificada
     * @param ruta Ruta completa del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     */
    public static boolean existeArchivo(String ruta) {
        return new File(ruta).exists();
    }

    /**
     * Elimina un archivo de la ruta especificada
     * @param ruta Ruta completa del archivo a eliminar
     * @return true si el archivo fue eliminado exitosamente, false si no existía o no se pudo eliminar
     */
    public static boolean eliminarArchivo(String ruta) {
        File archivo = new File(ruta);
        return archivo.exists() && archivo.delete();
    }
}