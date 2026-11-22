package libreriacsv.impresion;

import libreriacsv.modelo.TablaLegible;

import java.util.List;

/**
 * Formato de tabla Markdown, útil para pegar en un README o en GitHub.
 *
 * <pre>
 * | Nombre | Edad |
 * |--------|------|
 * | Ana    | 20   |
 * </pre>
 */
public class FormateadorMarkdown extends FormateadorBase {

    @Override
    protected String formatearFila(List<String> valores, List<Integer> anchos) {
        StringBuilder linea = new StringBuilder("|");
        for (int i = 0; i < anchos.size(); i++) {
            String valor = valores.get(i).replace("|", "\\|");
            linea.append(' ').append(rellenar(valor, anchos.get(i))).append(" |");
        }
        return linea.toString();
    }

    @Override
    protected String lineaBajoEncabezado(List<Integer> anchos) {
        StringBuilder linea = new StringBuilder("|");
        for (int ancho : anchos) {
            linea.append(repetir('-', ancho + 2)).append('|');
        }
        return linea.toString();
    }

    @Override
    protected List<Integer> calcularAnchos(TablaLegible tabla) {
        List<Integer> anchos = super.calcularAnchos(tabla);
        // Markdown necesita al menos tres guiones por columna.
        for (int i = 0; i < anchos.size(); i++) {
            anchos.set(i, Math.max(3, anchos.get(i)));
        }
        return anchos;
    }
}
