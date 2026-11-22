package libreriacsv.lectura;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Convierte el texto de un CSV en una lista de registros.
 *
 * La librería trae {@link ParserCsvEstandar}, pero se puede usar cualquier
 * otra implementación (formato propio, columnas de ancho fijo, etc.)
 * sin modificar el cargador.
 *
 * Patrón: Strategy (Comportamiento).
 */
@FunctionalInterface
public interface ParserCsv {

    /**
     * @param lector texto a interpretar (no hay que cerrarlo aquí)
     * @return una lista por registro; cada registro es la lista de sus campos
     * @throws CsvExcepcion si el formato no es válido
     */
    List<List<String>> parsear(Reader lector) throws IOException;
}
