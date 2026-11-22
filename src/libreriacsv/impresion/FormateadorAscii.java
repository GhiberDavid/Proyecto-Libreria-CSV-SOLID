package libreriacsv.impresion;

import java.util.List;

/**
 * Formato por defecto: tabla con bordes dibujados con caracteres ASCII.
 *
 * <pre>
 * +--------+------+
 * | Nombre | Edad |
 * +--------+------+
 * | Ana    | 20   |
 * +--------+------+
 * </pre>
 */
public class FormateadorAscii extends FormateadorBase {

    @Override
    protected String formatearFila(List<String> valores, List<Integer> anchos) {
        StringBuilder linea = new StringBuilder("|");
        for (int i = 0; i < anchos.size(); i++) {
            linea.append(' ').append(rellenar(valores.get(i), anchos.get(i))).append(" |");
        }
        return linea.toString();
    }

    @Override
    protected String lineaSuperior(List<Integer> anchos) {
        return borde(anchos);
    }

    @Override
    protected String lineaBajoEncabezado(List<Integer> anchos) {
        return borde(anchos);
    }

    @Override
    protected String lineaInferior(List<Integer> anchos) {
        return borde(anchos);
    }

    private String borde(List<Integer> anchos) {
        StringBuilder linea = new StringBuilder("+");
        for (int ancho : anchos) {
            linea.append(repetir('-', ancho + 2)).append('+');
        }
        return linea.toString();
    }
}
