package libreriacsv.decoradores;

import libreriacsv.impresion.FormateadorTabla;
import libreriacsv.modelo.TablaLegible;

/**
 * Base para modificar el texto que genera un {@link FormateadorTabla}
 * sin tocar su clase: se envuelve el formateador y se añade algo antes
 * o después de su resultado.
 *
 * Patrón: Decorator (Estructural).
 */
public abstract class DecoradorFormateador implements FormateadorTabla {

    protected final FormateadorTabla original;

    protected DecoradorFormateador(FormateadorTabla original) {
        if (original == null) {
            throw new IllegalArgumentException("El formateador a decorar no puede ser null");
        }
        this.original = original;
    }

    /** Por defecto devuelve el mismo texto que el formateador envuelto. */
    @Override
    public String formatear(TablaLegible tabla) {
        return original.formatear(tabla);
    }
}
