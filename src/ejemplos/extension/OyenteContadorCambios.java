package ejemplos.extension;

import libreriacsv.eventos.OyenteTabla;

import java.util.List;

/**
 * Extensión: oyente que cuenta los cambios hechos a la tabla.
 */
public class OyenteContadorCambios implements OyenteTabla {

    private int filasAgregadas;
    private int filasQuitadas;
    private int columnasAgregadas;
    private int columnasQuitadas;

    @Override
    public void filaAgregada(int posicion, List<String> valores) {
        filasAgregadas++;
    }

    @Override
    public void filaQuitada(int posicion, List<String> valores) {
        filasQuitadas++;
    }

    @Override
    public void columnaAgregada(int posicion, String nombre) {
        columnasAgregadas++;
    }

    @Override
    public void columnaQuitada(int posicion, String nombre) {
        columnasQuitadas++;
    }

    public int totalCambios() {
        return filasAgregadas + filasQuitadas + columnasAgregadas + columnasQuitadas;
    }

    @Override
    public String toString() {
        return "Cambios: +" + filasAgregadas + " filas, -" + filasQuitadas + " filas, +"
                + columnasAgregadas + " columnas, -" + columnasQuitadas + " columnas";
    }
}
