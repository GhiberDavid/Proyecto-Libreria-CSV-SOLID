package ejemplos.extension;

import libreriacsv.conversion.ConversorValor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Extensión: convierte el texto a LocalDate (formato ISO o uno propio, p. ej. "dd/MM/yyyy").
 */
public class ConversorFecha implements ConversorValor<LocalDate> {

    private final DateTimeFormatter formato;

    public ConversorFecha() {
        this(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public ConversorFecha(String patron) {
        this(DateTimeFormatter.ofPattern(patron));
    }

    public ConversorFecha(DateTimeFormatter formato) {
        this.formato = formato;
    }

    @Override
    public LocalDate convertir(String valor) {
        return LocalDate.parse(valor.trim(), formato);
    }
}
