package ejemplos;

import ejemplos.extension.ConversorFecha;
import ejemplos.extension.FabricaTablaVigilada;
import ejemplos.extension.FormateadorHtml;
import ejemplos.extension.FormateadorJson;
import ejemplos.extension.OyenteContadorCambios;
import ejemplos.extension.ParserLineasSimples;
import ejemplos.extension.TablaConValorPorDefecto;
import libreriacsv.Csv;
import libreriacsv.decoradores.FormateadorConTitulo;
import libreriacsv.modelo.Tabla;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * Demostración de cómo extender la librería con clases propias
 * (paquete ejemplos.extension) sin modificar el paquete libreriacsv.
 */
public class DemoExtension {

    public static void main(String[] args) {
        Tabla tabla = Csv.leer("datos/estudiantes.csv");

        System.out.println("1) Conversor propio a LocalDate");
        LocalDate ingreso = tabla.obtenerValor(0, "fecha_ingreso", new ConversorFecha());
        long dias = ChronoUnit.DAYS.between(ingreso, LocalDate.of(2026, 1, 1));
        System.out.println("Ana ingresó el " + ingreso + " (" + dias + " días antes del 01/01/2026)");

        System.out.println();
        System.out.println("2) Formateador propio: HTML");
        Csv.imprimir(tabla, new FormateadorHtml());

        System.out.println();
        System.out.println("3) Formateador propio: JSON, decorado con un decorador de la librería");
        Csv.imprimir(tabla, new FormateadorConTitulo(new FormateadorJson(), "estudiantes.json"));

        System.out.println();
        System.out.println("4) Decorador propio: celdas vacías como N/A");
        Csv.imprimir(new TablaConValorPorDefecto(tabla, "N/A"));

        System.out.println();
        System.out.println("5) Fábrica propia que registra un oyente propio");
        OyenteContadorCambios contador = new OyenteContadorCambios();
        Tabla vigilada = Csv.cargador()
                .desdeArchivo("datos/estudiantes.csv")
                .fabrica(new FabricaTablaVigilada(contador))
                .construir()
                .cargar();
        vigilada.agregarFila(Arrays.asList("6", "Pedro Silva", "Sistemas", "16", "2024-01-15", "si"));
        vigilada.quitarColumna("activo");
        vigilada.quitarFila(0);
        System.out.println(contador);

        System.out.println();
        System.out.println("6) Parser propio con separador '|' y comentarios");
        String texto = "# inventario\n"
                + "pieza | cantidad\n"
                + "tornillo | 120\n"
                + "tuerca | 80\n";
        Tabla inventario = Csv.cargador()
                .desdeTexto(texto)
                .parser(new ParserLineasSimples("|"))
                .construir()
                .cargar();
        Csv.imprimir(inventario);
    }
}
