package libreriacsv.impresion;

import libreriacsv.modelo.TablaLegible;

/**
 * Muestra una tabla en algún destino (consola, archivo, ventana...).
 */
@FunctionalInterface
public interface ImpresoraTabla {

    void imprimir(TablaLegible tabla);
}
