package ejemplos.extension;

import libreriacsv.decoradores.DecoradorTabla;
import libreriacsv.modelo.Tabla;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensión: decorador que muestra un texto por defecto (p. ej. "N/A") en las celdas vacías.
 */
public class TablaConValorPorDefecto extends DecoradorTabla {

    private final String valorPorDefecto;

    public TablaConValorPorDefecto(Tabla original, String valorPorDefecto) {
        super(original);
        this.valorPorDefecto = valorPorDefecto;
    }

    @Override
    public String obtenerValor(int fila, int columna) {
        return reemplazarSiVacio(super.obtenerValor(fila, columna));
    }

    @Override
    public List<String> obtenerFila(int fila) {
        List<String> resultado = new ArrayList<>();
        for (String valor : super.obtenerFila(fila)) {
            resultado.add(reemplazarSiVacio(valor));
        }
        return resultado;
    }

    private String reemplazarSiVacio(String valor) {
        return valor == null || valor.trim().isEmpty() ? valorPorDefecto : valor;
    }
}
