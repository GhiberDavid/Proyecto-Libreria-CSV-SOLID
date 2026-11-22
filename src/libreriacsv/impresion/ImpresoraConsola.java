package libreriacsv.impresion;

import libreriacsv.modelo.TablaLegible;

import java.io.PrintStream;

/**
 * Imprime la tabla en pantalla usando el formateador que se le pase.
 *
 * Recibe el formateador por el constructor (inyección de dependencias):
 * depende de la abstracción {@link FormateadorTabla}, no de una clase
 * concreta (principio D). Para cambiar cómo se ve la tabla se cambia el
 * formateador, no esta clase (principio O).
 */
public class ImpresoraConsola implements ImpresoraTabla {

    private final FormateadorTabla formateador;
    private final PrintStream salida;

    /** Imprime en System.out con el formato ASCII. */
    public ImpresoraConsola() {
        this(new FormateadorAscii());
    }

    public ImpresoraConsola(FormateadorTabla formateador) {
        this(formateador, System.out);
    }

    public ImpresoraConsola(FormateadorTabla formateador, PrintStream salida) {
        if (formateador == null || salida == null) {
            throw new IllegalArgumentException("El formateador y la salida no pueden ser null");
        }
        this.formateador = formateador;
        this.salida = salida;
    }

    @Override
    public void imprimir(TablaLegible tabla) {
        salida.print(formateador.formatear(tabla));
        salida.flush();
    }
}
