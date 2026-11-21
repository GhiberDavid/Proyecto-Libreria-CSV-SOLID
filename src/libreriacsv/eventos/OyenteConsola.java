package libreriacsv.eventos;

import java.io.PrintStream;
import java.util.List;

/**
 * Oyente que muestra cada cambio de la tabla en consola.
 */
public class OyenteConsola implements OyenteTabla {

    private final PrintStream salida;

    public OyenteConsola() {
        this(System.out);
    }

    public OyenteConsola(PrintStream salida) {
        this.salida = salida;
    }

    @Override
    public void filaAgregada(int posicion, List<String> valores) {
        salida.println("[tabla] Fila agregada " + describirFila(posicion) + ": " + valores);
    }

    @Override
    public void filaQuitada(int posicion, List<String> valores) {
        salida.println("[tabla] Fila quitada " + describirFila(posicion) + ": " + valores);
    }

    @Override
    public void columnaAgregada(int posicion, String nombre) {
        salida.println("[tabla] Columna agregada '" + nombre + "' " + describirColumna(posicion));
    }

    @Override
    public void columnaQuitada(int posicion, String nombre) {
        salida.println("[tabla] Columna quitada '" + nombre + "' " + describirColumna(posicion));
    }

    // Muestra el número contando desde 1, la línea con encabezado y el índice del código
    private String describirFila(int indice) {
        return "(fila de datos " + (indice + 1) + ", línea " + (indice + 2)
                + " contando el encabezado, índice " + indice + ")";
    }

    private String describirColumna(int indice) {
        return "(columna " + (indice + 1) + ", índice " + indice + ")";
    }
}
