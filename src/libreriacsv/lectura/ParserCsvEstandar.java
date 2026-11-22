package libreriacsv.lectura;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser que sigue las reglas habituales del formato CSV (RFC 4180):
 * <ul>
 *   <li>Separador configurable (coma por defecto; también punto y coma, tabulador...).</li>
 *   <li>Campos entre comillas que pueden contener el separador o saltos de línea.</li>
 *   <li>Dos comillas seguidas ({@code ""}) dentro de un campo entre comillas = una comilla.</li>
 *   <li>Saltos de línea {@code \n}, {@code \r\n} o {@code \r}.</li>
 *   <li>Se ignoran las líneas vacías y la marca BOM de UTF-8.</li>
 * </ul>
 */
public class ParserCsvEstandar implements ParserCsv {

    private static final int FIN = -1;
    private static final char BOM = '﻿';

    private final char separador;
    private final char comilla;

    /** Parser con coma como separador y comilla doble. */
    public ParserCsvEstandar() {
        this(',', '"');
    }

    public ParserCsvEstandar(char separador) {
        this(separador, '"');
    }

    public ParserCsvEstandar(char separador, char comilla) {
        if (separador == comilla) {
            throw new IllegalArgumentException("El separador y la comilla no pueden ser iguales");
        }
        if (esSaltoDeLinea(separador) || esSaltoDeLinea(comilla)) {
            throw new IllegalArgumentException("El separador y la comilla no pueden ser saltos de línea");
        }
        this.separador = separador;
        this.comilla = comilla;
    }

    @Override
    public List<List<String>> parsear(Reader lector) throws IOException {
        PushbackReader entrada = new PushbackReader(lector, 1);
        saltarBom(entrada);

        List<List<String>> registros = new ArrayList<>();
        List<String> registro = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean campoConComillas = false;
        int linea = 1;

        int c;
        while ((c = entrada.read()) != FIN) {
            if (c == comilla && campo.length() == 0 && !campoConComillas) {
                campoConComillas = true;
                linea = leerCampoEntreComillas(entrada, campo, linea);
            } else if (c == separador) {
                registro.add(campo.toString());
                campo.setLength(0);
                campoConComillas = false;
            } else if (esSaltoDeLinea((char) c)) {
                if (c == '\r') {
                    consumirSiSigue(entrada, '\n');
                }
                cerrarRegistro(registros, registro, campo, campoConComillas);
                registro = new ArrayList<>();
                campo.setLength(0);
                campoConComillas = false;
                linea++;
            } else {
                campo.append((char) c);
            }
        }
        cerrarRegistro(registros, registro, campo, campoConComillas);
        return registros;
    }

    /**
     * Lee el contenido de un campo entre comillas (la comilla de apertura ya se leyó).
     *
     * @return el número de línea actualizado
     */
    private int leerCampoEntreComillas(PushbackReader entrada, StringBuilder campo, int linea) throws IOException {
        int lineaInicio = linea;
        int c;
        while ((c = entrada.read()) != FIN) {
            if (c == comilla) {
                if (consumirSiSigue(entrada, comilla)) {
                    campo.append(comilla); // comilla escapada ""
                } else {
                    return linea; // fin del campo entre comillas
                }
            } else {
                if (c == '\n') {
                    linea++;
                }
                campo.append((char) c);
            }
        }
        throw new CsvExcepcion("Comillas sin cerrar en el campo que empieza en la línea " + lineaInicio);
    }

    /** Si el siguiente carácter es el esperado lo consume y devuelve true; si no, lo devuelve a la entrada. */
    private boolean consumirSiSigue(PushbackReader entrada, char esperado) throws IOException {
        int siguiente = entrada.read();
        if (siguiente == esperado) {
            return true;
        }
        if (siguiente != FIN) {
            entrada.unread(siguiente);
        }
        return false;
    }

    private void saltarBom(PushbackReader entrada) throws IOException {
        consumirSiSigue(entrada, BOM);
    }

    /** Agrega el registro actual, salvo que sea una línea completamente vacía. */
    private void cerrarRegistro(List<List<String>> registros, List<String> registro,
                                StringBuilder campo, boolean campoConComillas) {
        boolean lineaVacia = registro.isEmpty() && campo.length() == 0 && !campoConComillas;
        if (!lineaVacia) {
            registro.add(campo.toString());
            registros.add(registro);
        }
    }

    private static boolean esSaltoDeLinea(char c) {
        return c == '\n' || c == '\r';
    }
}
