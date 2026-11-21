package libreriacsv.eventos;

import java.util.List;

/**
 * Recibe un aviso cada vez que la tabla cambia.
 * Solo hay que sobrescribir los métodos que interesen.
 * Patrón: Observer (observador).
 */
public interface OyenteTabla {

    default void filaAgregada(int posicion, List<String> valores) {
    }

    default void filaQuitada(int posicion, List<String> valores) {
    }

    default void columnaAgregada(int posicion, String nombre) {
    }

    default void columnaQuitada(int posicion, String nombre) {
    }
}
