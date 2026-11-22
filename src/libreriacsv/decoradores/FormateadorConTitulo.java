package libreriacsv.decoradores;

import libreriacsv.impresion.FormateadorTabla;
import libreriacsv.modelo.TablaLegible;

/**
 * Añade un título encima de la tabla.
 */
public class FormateadorConTitulo extends DecoradorFormateador {

    private final String titulo;

    public FormateadorConTitulo(FormateadorTabla original, String titulo) {
        super(original);
        this.titulo = titulo == null ? "" : titulo;
    }

    @Override
    public String formatear(TablaLegible tabla) {
        String salto = System.lineSeparator();
        StringBuilder subrayado = new StringBuilder();
        for (int i = 0; i < titulo.length(); i++) {
            subrayado.append('=');
        }
        return titulo + salto + subrayado + salto + super.formatear(tabla);
    }
}
