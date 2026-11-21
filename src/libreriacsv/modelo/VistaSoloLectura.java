package libreriacsv.modelo;

import java.util.List;

/**
 * Vista de una tabla que no permite modificarla.
 * Implementa solo TablaLegible (no Tabla) para no tener métodos de edición
 * que lancen excepciones.
 */
public class VistaSoloLectura implements TablaLegible {

    private final TablaLegible original;

    public VistaSoloLectura(TablaLegible original) {
        if (original == null) {
            throw new IllegalArgumentException("La tabla no puede ser null");
        }
        this.original = original;
    }

    @Override
    public List<String> obtenerEncabezados() {
        return original.obtenerEncabezados();
    }

    @Override
    public int cantidadFilas() {
        return original.cantidadFilas();
    }

    @Override
    public int cantidadColumnas() {
        return original.cantidadColumnas();
    }

    @Override
    public String obtenerValor(int fila, int columna) {
        return original.obtenerValor(fila, columna);
    }

    @Override
    public List<String> obtenerFila(int fila) {
        return original.obtenerFila(fila);
    }
}
