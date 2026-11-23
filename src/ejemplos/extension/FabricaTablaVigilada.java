package ejemplos.extension;

import libreriacsv.eventos.OyenteTabla;
import libreriacsv.modelo.FabricaTabla;
import libreriacsv.modelo.Tabla;
import libreriacsv.modelo.TablaCsv;

import java.util.List;

/**
 * Extensión: fábrica que entrega cada tabla con un oyente ya registrado.
 */
public class FabricaTablaVigilada implements FabricaTabla {

    private final OyenteTabla oyente;

    public FabricaTablaVigilada(OyenteTabla oyente) {
        this.oyente = oyente;
    }

    @Override
    public Tabla crear(List<String> encabezados, List<List<String>> filas) {
        TablaCsv tabla = new TablaCsv(encabezados, filas);
        tabla.agregarOyente(oyente);
        return tabla;
    }
}
