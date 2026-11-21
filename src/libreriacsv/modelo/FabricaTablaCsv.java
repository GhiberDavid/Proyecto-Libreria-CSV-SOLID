package libreriacsv.modelo;

import java.util.List;

/**
 * Fábrica por defecto: crea una TablaCsv.
 */
public class FabricaTablaCsv implements FabricaTabla {

    @Override
    public Tabla crear(List<String> encabezados, List<List<String>> filas) {
        return new TablaCsv(encabezados, filas);
    }
}
