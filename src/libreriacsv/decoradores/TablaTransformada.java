package libreriacsv.decoradores;

import libreriacsv.modelo.Tabla;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Decorador que aplica una transformación a cada valor al leerlo
 * (por ejemplo, quitar espacios o pasar a mayúsculas).
 *
 * Los datos guardados no cambian: solo cambia lo que se obtiene al leer.
 *
 * <pre>
 * Tabla limpia = new TablaTransformada(tabla, String::trim);
 * Tabla mayusculas = new TablaTransformada(tabla, String::toUpperCase);
 * </pre>
 */
public class TablaTransformada extends DecoradorTabla {

    private final UnaryOperator<String> transformacion;

    public TablaTransformada(Tabla original, UnaryOperator<String> transformacion) {
        super(original);
        if (transformacion == null) {
            throw new IllegalArgumentException("La transformación no puede ser null");
        }
        this.transformacion = transformacion;
    }

    @Override
    public String obtenerValor(int fila, int columna) {
        return transformacion.apply(original.obtenerValor(fila, columna));
    }

    @Override
    public List<String> obtenerFila(int fila) {
        List<String> transformada = new ArrayList<>();
        for (String valor : original.obtenerFila(fila)) {
            transformada.add(transformacion.apply(valor));
        }
        return transformada;
    }
}
