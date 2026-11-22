package libreriacsv.impresion;

import libreriacsv.modelo.TablaLegible;

/**
 * Convierte una tabla en texto.
 *
 * Solo genera el texto; mostrarlo es responsabilidad de una
 * {@link ImpresoraTabla} (principio de responsabilidad única, S).
 * Depende solo de {@link TablaLegible}, no de los métodos de edición (principio I).
 *
 * Patrón: Strategy (Comportamiento). Cada implementación es una forma
 * distinta de mostrar la tabla.
 */
@FunctionalInterface
public interface FormateadorTabla {

    String formatear(TablaLegible tabla);
}
