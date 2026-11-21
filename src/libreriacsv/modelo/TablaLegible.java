package libreriacsv.modelo;

import libreriacsv.conversion.ConversorValor;

import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones de solo lectura de una tabla.
 * Las filas y columnas se cuentan desde 0 y el encabezado no cuenta como fila.
 */
public interface TablaLegible {

    List<String> obtenerEncabezados();

    int cantidadFilas();

    int cantidadColumnas();

    String obtenerValor(int fila, int columna);

    List<String> obtenerFila(int fila);

    /** Devuelve -1 si la columna no existe. */
    default int indiceDeColumna(String nombreColumna) {
        return obtenerEncabezados().indexOf(nombreColumna);
    }

    default String obtenerValor(int fila, String nombreColumna) {
        return obtenerValor(fila, indiceObligatorio(nombreColumna));
    }

    default List<String> obtenerColumna(String nombreColumna) {
        int indice = indiceObligatorio(nombreColumna);
        List<String> valores = new ArrayList<>();
        for (int i = 0; i < cantidadFilas(); i++) {
            valores.add(obtenerValor(i, indice));
        }
        return valores;
    }

    default <T> T obtenerValor(int fila, String nombreColumna, ConversorValor<T> conversor) {
        return conversor.convertir(obtenerValor(fila, nombreColumna));
    }

    default <T> List<T> obtenerColumna(String nombreColumna, ConversorValor<T> conversor) {
        List<T> convertidos = new ArrayList<>();
        for (String valor : obtenerColumna(nombreColumna)) {
            convertidos.add(conversor.convertir(valor));
        }
        return convertidos;
    }

    /** Igual que indiceDeColumna, pero lanza una excepción si la columna no existe. */
    default int indiceObligatorio(String nombreColumna) {
        int indice = indiceDeColumna(nombreColumna);
        if (indice < 0) {
            throw new IllegalArgumentException("No existe la columna: " + nombreColumna);
        }
        return indice;
    }
}
