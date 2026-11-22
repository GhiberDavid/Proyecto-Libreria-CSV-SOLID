package libreriacsv.lectura;

import java.io.Reader;
import java.io.StringReader;

/**
 * Adapta un {@code String} con contenido CSV a {@link OrigenDatos}.
 * Útil para pruebas o para datos que ya están en memoria.
 */
public class OrigenTexto implements OrigenDatos {

    private final String contenido;

    public OrigenTexto(String contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException("El contenido no puede ser null");
        }
        this.contenido = contenido;
    }

    @Override
    public Reader abrir() {
        return new StringReader(contenido);
    }

    @Override
    public String descripcion() {
        return "texto en memoria";
    }
}
