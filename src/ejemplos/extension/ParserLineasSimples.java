package ejemplos.extension;

import libreriacsv.lectura.ParserCsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Extensión: parser para archivos simples sin comillas, con un separador de
 * uno o varios caracteres y comentarios que empiezan por "#".
 */
public class ParserLineasSimples implements ParserCsv {

    private final String separador;

    public ParserLineasSimples(String separador) {
        this.separador = separador;
    }

    @Override
    public List<List<String>> parsear(Reader lector) throws IOException {
        List<List<String>> registros = new ArrayList<>();
        BufferedReader entrada = new BufferedReader(lector);
        String linea;
        while ((linea = entrada.readLine()) != null) {
            String limpia = linea.trim();
            if (limpia.isEmpty() || limpia.startsWith("#")) {
                continue;
            }
            List<String> campos = new ArrayList<>();
            for (String campo : Arrays.asList(limpia.split(Pattern.quote(separador), -1))) {
                campos.add(campo.trim());
            }
            registros.add(campos);
        }
        return registros;
    }
}
