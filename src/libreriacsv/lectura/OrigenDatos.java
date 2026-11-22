package libreriacsv.lectura;

import java.io.IOException;
import java.io.Reader;

/**
 * De dónde se leen los datos del CSV.
 *
 * El parser solo sabe trabajar con un {@link Reader}. Cada implementación
 * adapta una fuente distinta (archivo, texto, red, base de datos...) a esa
 * interfaz común, así el parser no cambia cuando aparece una fuente nueva.
 *
 * Patrón: Adapter (Estructural).
 */
@FunctionalInterface
public interface OrigenDatos {

    /**
     * Abre un lector nuevo. Quien lo llama es responsable de cerrarlo.
     */
    Reader abrir() throws IOException;

    /** Nombre legible del origen, usado en mensajes de error. */
    default String descripcion() {
        return getClass().getSimpleName();
    }
}
