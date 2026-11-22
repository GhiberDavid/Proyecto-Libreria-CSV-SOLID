package ejemplos.extension;

import libreriacsv.impresion.FormateadorBase;

import java.util.List;

/**
 * Extensión: imprime la tabla como HTML reutilizando la plantilla FormateadorBase.
 */
public class FormateadorHtml extends FormateadorBase {

    @Override
    protected String lineaSuperior(List<Integer> anchos) {
        return "<table>";
    }

    @Override
    protected String formatearFila(List<String> valores, List<Integer> anchos) {
        StringBuilder fila = new StringBuilder("  <tr>");
        for (String valor : valores) {
            fila.append("<td>").append(escapar(valor)).append("</td>");
        }
        return fila.append("</tr>").toString();
    }

    @Override
    protected String lineaInferior(List<Integer> anchos) {
        return "</table>";
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
