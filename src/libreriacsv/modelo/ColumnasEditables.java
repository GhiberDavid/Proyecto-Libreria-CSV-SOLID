package libreriacsv.modelo;

import java.util.List;

/**
 * Agregar y quitar columnas.
 */
public interface ColumnasEditables {

    /** Agrega una columna con el mismo valor en todas las filas. */
    void agregarColumna(String nombre, String valorPorDefecto);

    /** Agrega una columna con un valor por fila. */
    void agregarColumna(String nombre, List<String> valores);

    /** Devuelve los valores que tenía la columna. */
    List<String> quitarColumna(String nombre);

    List<String> quitarColumna(int columna);
}
