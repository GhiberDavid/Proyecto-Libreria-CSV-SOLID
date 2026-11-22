package ejemplos;

import libreriacsv.Csv;
import libreriacsv.conversion.Conversores;
import libreriacsv.decoradores.FormateadorConResumen;
import libreriacsv.decoradores.FormateadorConTitulo;
import libreriacsv.decoradores.TablaTransformada;
import libreriacsv.eventos.OyenteConsola;
import libreriacsv.eventos.TablaObservable;
import libreriacsv.impresion.FormateadorAscii;
import libreriacsv.impresion.FormateadorMarkdown;
import libreriacsv.modelo.Tabla;

import java.util.Arrays;
import java.util.List;

/**
 * Demostración de las funciones básicas.
 * Ejecutar desde la raíz del proyecto para que encuentre la carpeta "datos".
 */
public class DemoBasico {

    public static void main(String[] args) {
        Tabla tabla = Csv.leer("datos/estudiantes.csv");
        System.out.println("1) Tabla leída: " + tabla);
        Csv.imprimir(tabla);

        if (tabla instanceof TablaObservable) {
            ((TablaObservable) tabla).agregarOyente(new OyenteConsola());
        }

        // Las posiciones empiezan en 0: quitarFila(3) quita la 4.ª fila de datos (José)
        System.out.println();
        System.out.println("2) Agregar y quitar filas");
        tabla.agregarFila(Arrays.asList("6", "Pedro Silva", "Sistemas", "16", "2024-01-15", "si"));
        tabla.quitarFila(3);
        Csv.imprimir(tabla);

        System.out.println();
        System.out.println("3) Agregar y quitar columnas");
        tabla.agregarColumna("seccion", "A");
        tabla.quitarColumna("fecha_ingreso");
        Csv.imprimir(tabla);

        System.out.println();
        System.out.println("4) Valores convertidos");
        int id = tabla.obtenerValor(0, "id", Conversores.ENTERO);
        double nota = tabla.obtenerValor(0, "nota", Conversores.DECIMAL);
        boolean activo = tabla.obtenerValor(0, "activo", Conversores.BOOLEANO);
        System.out.println("id=" + id + ", nota=" + nota + ", activo=" + activo);

        System.out.println();
        System.out.println("5) Formato Markdown");
        Csv.imprimir(tabla, new FormateadorMarkdown());

        System.out.println();
        System.out.println("6) Formato ASCII decorado con título y resumen");
        Csv.imprimir(tabla, new FormateadorConResumen(
                new FormateadorConTitulo(new FormateadorAscii(), "Estudiantes")));

        System.out.println();
        System.out.println("7) Tabla decorada: valores en mayúsculas");
        Csv.imprimir(new TablaTransformada(tabla, String::toUpperCase));

        System.out.println();
        System.out.println("8) CSV separado por punto y coma");
        Tabla productos = Csv.cargador()
                .desdeArchivo("datos/productos_punto_y_coma.csv")
                .separador(';')
                .construir()
                .cargar();
        Csv.imprimir(productos);
        List<Double> precios = productos.obtenerColumna("precio", Conversores.DECIMAL);
        double total = 0;
        for (double precio : precios) {
            total += precio;
        }
        System.out.printf("Suma de precios: %.2f%n", total);
    }
}
