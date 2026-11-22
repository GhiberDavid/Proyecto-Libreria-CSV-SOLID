package ejemplos.extension;

import libreriacsv.impresion.FormateadorTabla;
import libreriacsv.modelo.TablaLegible;

import java.util.List;

/**
 * Extensión: imprime la tabla como JSON implementando FormateadorTabla desde cero.
 */
public class FormateadorJson implements FormateadorTabla {

    @Override
    public String formatear(TablaLegible tabla) {
        List<String> encabezados = tabla.obtenerEncabezados();
        StringBuilder json = new StringBuilder("[");
        for (int fila = 0; fila < tabla.cantidadFilas(); fila++) {
            json.append(fila == 0 ? "\n" : ",\n").append("  {");
            for (int col = 0; col < encabezados.size(); col++) {
                if (col > 0) {
                    json.append(", ");
                }
                json.append(texto(encabezados.get(col))).append(": ").append(texto(tabla.obtenerValor(fila, col)));
            }
            json.append("}");
        }
        return json.append(tabla.cantidadFilas() > 0 ? "\n]" : "]").append(System.lineSeparator()).toString();
    }

    private String texto(String valor) {
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}
