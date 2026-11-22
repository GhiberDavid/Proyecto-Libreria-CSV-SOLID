package libreriacsv;

import libreriacsv.impresion.FormateadorTabla;
import libreriacsv.impresion.ImpresoraConsola;
import libreriacsv.lectura.CargadorCsv;
import libreriacsv.modelo.Tabla;
import libreriacsv.modelo.TablaLegible;
import libreriacsv.modelo.VistaSoloLectura;

/**
 * Punto de entrada sencillo para los usos más comunes de la librería.
 * Patrón: Facade.
 */
public final class Csv {

    private Csv() {
    }

    /** Lee un CSV separado por comas, con encabezado. */
    public static Tabla leer(String ruta) {
        return cargador().desdeArchivo(ruta).construir().cargar();
    }

    public static Tabla leer(String ruta, char separador) {
        return cargador().desdeArchivo(ruta).separador(separador).construir().cargar();
    }

    public static Tabla desdeTexto(String contenido) {
        return cargador().desdeTexto(contenido).construir().cargar();
    }

    /** Builder con todas las opciones de lectura. */
    public static CargadorCsv.Builder cargador() {
        return CargadorCsv.builder();
    }

    /** Imprime en consola con formato ASCII. */
    public static void imprimir(TablaLegible tabla) {
        new ImpresoraConsola().imprimir(tabla);
    }

    public static void imprimir(TablaLegible tabla, FormateadorTabla formateador) {
        new ImpresoraConsola(formateador).imprimir(tabla);
    }

    public static TablaLegible soloLectura(TablaLegible tabla) {
        return new VistaSoloLectura(tabla);
    }
}
