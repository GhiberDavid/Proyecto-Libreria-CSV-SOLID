package libreriacsv.modelo;

import java.util.List;

/**
 * Agregar y quitar filas.
 */
public interface FilasEditables {

    void agregarFila(List<String> valores);

    void agregarFila(int posicion, List<String> valores);

    /** Devuelve los valores de la fila quitada. */
    List<String> quitarFila(int fila);
}
