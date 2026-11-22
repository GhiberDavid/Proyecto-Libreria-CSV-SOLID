package libreriacsv.decoradores;

import libreriacsv.modelo.Tabla;

import java.util.List;

/**
 * Base para cambiar el comportamiento de una {@link Tabla} existente sin
 * modificar su clase.
 *
 * Envuelve otra tabla y le delega todas las llamadas. Una subclase solo
 * sobrescribe los métodos cuyo resultado quiere cambiar. Como también es
 * una {@link Tabla}, se puede usar en cualquier sitio donde se use la
 * original (principio L) y se pueden apilar varios decoradores.
 *
 * Patrón: Decorator (Estructural).
 */
public abstract class DecoradorTabla implements Tabla {

    protected final Tabla original;

    protected DecoradorTabla(Tabla original) {
        if (original == null) {
            throw new IllegalArgumentException("La tabla a decorar no puede ser null");
        }
        this.original = original;
    }

    /** @return la tabla envuelta. */
    public Tabla getOriginal() {
        return original;
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

    @Override
    public void agregarFila(List<String> valores) {
        original.agregarFila(valores);
    }

    @Override
    public void agregarFila(int posicion, List<String> valores) {
        original.agregarFila(posicion, valores);
    }

    @Override
    public List<String> quitarFila(int fila) {
        return original.quitarFila(fila);
    }

    @Override
    public void agregarColumna(String nombre, String valorPorDefecto) {
        original.agregarColumna(nombre, valorPorDefecto);
    }

    @Override
    public void agregarColumna(String nombre, List<String> valores) {
        original.agregarColumna(nombre, valores);
    }

    @Override
    public List<String> quitarColumna(String nombre) {
        return original.quitarColumna(nombre);
    }

    @Override
    public List<String> quitarColumna(int columna) {
        return original.quitarColumna(columna);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + original + ")";
    }
}
