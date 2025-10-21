package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilidad para el manejo y conversión de fechas
 * Proporciona métodos para parsear, formatear y validar fechas en formato dd/MM/yyyy
 */
public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Convierte una cadena de texto en formato dd/MM/yyyy a un objeto LocalDate
     * @param fecha Cadena de texto con la fecha en formato dd/MM/yyyy
     * @return Objeto LocalDate correspondiente o null si el formato es inválido
     */
    public static LocalDate parse(String fecha) {
        try {
            return LocalDate.parse(fecha, formatter);
        } catch (DateTimeParseException e) {
            return null; // si el formato es inválido
        }
    }

    /**
     * Formatea un objeto LocalDate a una cadena de texto en formato dd/MM/yyyy
     * @param fecha Objeto LocalDate a formatear
     * @return Cadena de texto con la fecha formateada o null si la fecha es null
     */
    public static String format(LocalDate fecha) {
        if (fecha == null) return null;
        return formatter.format(fecha);
    }

    /**
     * Valida si una cadena de texto tiene un formato de fecha válido (dd/MM/yyyy)
     * @param fecha Cadena de texto a validar
     * @return true si la cadena tiene un formato de fecha válido, false en caso contrario
     */
    public static boolean esFechaValida(String fecha) {
        return parse(fecha) != null;
    }
}