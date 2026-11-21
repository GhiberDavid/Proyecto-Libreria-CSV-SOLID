package libreriacsv.modelo;

import java.util.List;

/**
 * Decide qué tabla se crea al cargar un CSV.
 * Patrón: Factory Method.
 */
@FunctionalInterface
public interface FabricaTabla {

    Tabla crear(List<String> encabezados, List<List<String>> filas);
}
