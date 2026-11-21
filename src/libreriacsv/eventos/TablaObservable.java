package libreriacsv.eventos;

/**
 * Tabla a la que se le pueden registrar oyentes.
 * Patrón: Observer (sujeto).
 */
public interface TablaObservable {

    void agregarOyente(OyenteTabla oyente);

    void quitarOyente(OyenteTabla oyente);
}
