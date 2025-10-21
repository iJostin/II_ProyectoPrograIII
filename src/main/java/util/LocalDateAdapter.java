package util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Adaptador JAXB para la serialización/deserialización de objetos LocalDate en XML
 * Convierte entre LocalDate (objeto Java) y String (formato ISO en XML)
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    /**
     * Convierte una cadena XML en formato ISO (yyyy-MM-dd) a un objeto LocalDate
     * @param v Cadena de texto con la fecha en formato ISO
     * @return Objeto LocalDate correspondiente o null si la cadena es null
     * @throws Exception Si ocurre un error durante la conversión
     */
    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return (v == null) ? null : LocalDate.parse(v);
    }

    /**
     * Convierte un objeto LocalDate a una cadena XML en formato ISO (yyyy-MM-dd)
     * @param v Objeto LocalDate a convertir
     * @return Cadena de texto en formato ISO o null si el objeto es null
     * @throws Exception Si ocurre un error durante la conversión
     */
    @Override
    public String marshal(LocalDate v) throws Exception {
        return (v == null) ? null : v.toString();
    }
}