package libreriacsv.impresion;

import libreriacsv.modelo.TablaLegible;

import java.util.ArrayList;
import java.util.List;

/**
 * Esqueleto común para los formateadores con columnas alineadas.
 *
 * El método {@link #formatear(TablaLegible)} es final y fija el orden de
 * los pasos: calcular anchos, línea superior, encabezado, separador,
 * filas y línea inferior. Las subclases solo definen cómo se dibuja cada
 * paso.
 *
 * Patrón: Template Method (Comportamiento).
 */
public abstract class FormateadorBase implements FormateadorTabla {

    protected static final String SALTO = System.lineSeparator();

    /** Método plantilla: el algoritmo no cambia, solo sus pasos. */
    @Override
    public final String formatear(TablaLegible tabla) {
        if (tabla.cantidadColumnas() == 0) {
            return textoTablaVacia() + SALTO;
        }
        List<Integer> anchos = calcularAnchos(tabla);
        StringBuilder texto = new StringBuilder();

        agregarLinea(texto, lineaSuperior(anchos));
        agregarLinea(texto, formatearFila(limpiar(tabla.obtenerEncabezados()), anchos));
        agregarLinea(texto, lineaBajoEncabezado(anchos));
        for (int i = 0; i < tabla.cantidadFilas(); i++) {
            agregarLinea(texto, formatearFila(limpiar(tabla.obtenerFila(i)), anchos));
        }
        agregarLinea(texto, lineaInferior(anchos));
        return texto.toString();
    }

    // ---------- Paso obligatorio ----------

    /** Dibuja una fila (también se usa para el encabezado). */
    protected abstract String formatearFila(List<String> valores, List<Integer> anchos);

    // ---------- Pasos opcionales (hooks): por defecto no dibujan nada ----------

    protected String lineaSuperior(List<Integer> anchos) {
        return null;
    }

    protected String lineaBajoEncabezado(List<Integer> anchos) {
        return null;
    }

    protected String lineaInferior(List<Integer> anchos) {
        return null;
    }

    protected String textoTablaVacia() {
        return "(tabla vacía)";
    }

    /** Ancho de cada columna: el texto más largo entre el encabezado y sus valores. */
    protected List<Integer> calcularAnchos(TablaLegible tabla) {
        List<Integer> anchos = new ArrayList<>();
        for (String encabezado : limpiar(tabla.obtenerEncabezados())) {
            anchos.add(encabezado.length());
        }
        for (int fila = 0; fila < tabla.cantidadFilas(); fila++) {
            List<String> valores = limpiar(tabla.obtenerFila(fila));
            for (int col = 0; col < anchos.size(); col++) {
                anchos.set(col, Math.max(anchos.get(col), valores.get(col).length()));
            }
        }
        return anchos;
    }

    /** Texto de una celda tal como se mostrará. Por defecto los saltos de línea se cambian por espacios. */
    protected String textoCelda(String valor) {
        return valor == null ? "" : valor.replace("\r\n", " ").replace('\n', ' ').replace('\r', ' ');
    }

    // ---------- Utilidades para las subclases ----------

    /** Rellena el texto con espacios a la derecha hasta el ancho indicado. */
    protected static String rellenar(String texto, int ancho) {
        StringBuilder resultado = new StringBuilder(texto);
        while (resultado.length() < ancho) {
            resultado.append(' ');
        }
        return resultado.toString();
    }

    /** Repite un carácter n veces. */
    protected static String repetir(char caracter, int veces) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            resultado.append(caracter);
        }
        return resultado.toString();
    }

    private List<String> limpiar(List<String> valores) {
        List<String> limpios = new ArrayList<>();
        for (String valor : valores) {
            limpios.add(textoCelda(valor));
        }
        return limpios;
    }

    private static void agregarLinea(StringBuilder texto, String linea) {
        if (linea != null) {
            texto.append(linea).append(SALTO);
        }
    }
}
