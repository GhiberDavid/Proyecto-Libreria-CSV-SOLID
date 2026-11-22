package libreriacsv.lectura;

/**
 * Error al leer o interpretar un CSV.
 *
 * Es una excepción no comprobada para que el código de uso sea sencillo;
 * la causa original (por ejemplo, un IOException) se conserva.
 */
public class CsvExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CsvExcepcion(String mensaje) {
        super(mensaje);
    }

    public CsvExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
