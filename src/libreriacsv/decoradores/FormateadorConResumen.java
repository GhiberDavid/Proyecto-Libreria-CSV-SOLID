package libreriacsv.decoradores;

import libreriacsv.impresion.FormateadorTabla;
import libreriacsv.modelo.TablaLegible;

/**
 * Añade debajo de la tabla una línea con la cantidad de filas y columnas.
 */
public class FormateadorConResumen extends DecoradorFormateador {

    public FormateadorConResumen(FormateadorTabla original) {
        super(original);
    }

    @Override
    public String formatear(TablaLegible tabla) {
        return super.formatear(tabla)
                + "Encabezado + " + tabla.cantidadFilas() + " fila(s) de datos, " + tabla.cantidadColumnas() + " columna(s)"
                + System.lineSeparator();
    }
}
