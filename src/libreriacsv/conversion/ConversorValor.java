package libreriacsv.conversion;

/**
 * Convierte el texto de una celda a otro tipo (Integer, Double, LocalDate...).
 * Patrón: Strategy.
 */
@FunctionalInterface
public interface ConversorValor<T> {

    T convertir(String valor);
}
