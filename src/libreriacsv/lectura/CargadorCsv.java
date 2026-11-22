package libreriacsv.lectura;

import libreriacsv.modelo.FabricaTabla;
import libreriacsv.modelo.FabricaTablaCsv;
import libreriacsv.modelo.Tabla;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Obtiene un CSV y crea el objeto {@link Tabla} que lo representa.
 *
 * No depende de clases concretas sino de tres abstracciones
 * (principio de inversión de dependencias, D):
 * <ul>
 *   <li>{@link OrigenDatos}: de dónde se lee.</li>
 *   <li>{@link ParserCsv}: cómo se interpreta el texto.</li>
 *   <li>{@link FabricaTabla}: qué tabla se crea con los datos.</li>
 * </ul>
 * Se configura con un {@link Builder} (patrón Builder, Creacional).
 *
 * <pre>
 * Tabla tabla = CargadorCsv.builder()
 *         .desdeArchivo("datos/estudiantes.csv")
 *         .separador(';')
 *         .conEncabezado(true)
 *         .construir()
 *         .cargar();
 * </pre>
 */
public class CargadorCsv {

    private final OrigenDatos origen;
    private final ParserCsv parser;
    private final FabricaTabla fabrica;
    private final boolean conEncabezado;
    private final boolean completarFilasCortas;

    private CargadorCsv(Builder builder) {
        this.origen = builder.origen;
        this.parser = builder.parser != null
                ? builder.parser
                : new ParserCsvEstandar(builder.separador, builder.comilla);
        this.fabrica = builder.fabrica;
        this.conEncabezado = builder.conEncabezado;
        this.completarFilasCortas = builder.completarFilasCortas;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Lee el origen y crea la tabla.
     *
     * @throws CsvExcepcion si no se puede leer el origen o el contenido no es válido
     */
    public Tabla cargar() {
        List<List<String>> registros = leerRegistros();

        List<String> encabezados;
        List<List<String>> filas;
        if (registros.isEmpty()) {
            encabezados = new ArrayList<>();
            filas = new ArrayList<>();
        } else if (conEncabezado) {
            encabezados = limpiarEncabezados(registros.get(0));
            filas = registros.subList(1, registros.size());
        } else {
            encabezados = encabezadosGenerados(anchoMaximo(registros));
            filas = registros;
        }

        List<List<String>> filasAjustadas = ajustarFilas(filas, encabezados.size());
        try {
            return fabrica.crear(encabezados, filasAjustadas);
        } catch (IllegalArgumentException e) {
            throw new CsvExcepcion("No se pudo crear la tabla desde " + origen.descripcion() + ": " + e.getMessage(), e);
        }
    }

    private List<List<String>> leerRegistros() {
        try (Reader lector = origen.abrir()) {
            return parser.parsear(lector);
        } catch (IOException e) {
            throw new CsvExcepcion("No se pudo leer " + origen.descripcion() + ": " + e.getMessage(), e);
        }
    }

    private List<String> limpiarEncabezados(List<String> crudos) {
        List<String> encabezados = new ArrayList<>();
        for (String encabezado : crudos) {
            encabezados.add(encabezado.trim());
        }
        return encabezados;
    }

    private List<String> encabezadosGenerados(int cantidad) {
        List<String> encabezados = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            encabezados.add("Columna" + i);
        }
        return encabezados;
    }

    private int anchoMaximo(List<List<String>> registros) {
        int maximo = 0;
        for (List<String> registro : registros) {
            maximo = Math.max(maximo, registro.size());
        }
        return maximo;
    }

    private List<List<String>> ajustarFilas(List<List<String>> filas, int columnas) {
        List<List<String>> resultado = new ArrayList<>();
        int numeroFila = conEncabezado ? 2 : 1; // número de línea lógica en el archivo
        for (List<String> fila : filas) {
            if (fila.size() == columnas) {
                resultado.add(fila);
            } else if (fila.size() < columnas && (completarFilasCortas || !conEncabezado)) {
                List<String> completa = new ArrayList<>(fila);
                while (completa.size() < columnas) {
                    completa.add("");
                }
                resultado.add(completa);
            } else {
                throw new CsvExcepcion("El registro " + numeroFila + " de " + origen.descripcion()
                        + " tiene " + fila.size() + " valores y se esperaban " + columnas);
            }
            numeroFila++;
        }
        return resultado;
    }

    /**
     * Configura un {@link CargadorCsv} paso a paso.
     * Patrón: Builder (Creacional).
     */
    public static class Builder {

        private OrigenDatos origen;
        private ParserCsv parser;
        private FabricaTabla fabrica = new FabricaTablaCsv();
        private char separador = ',';
        private char comilla = '"';
        private boolean conEncabezado = true;
        private boolean completarFilasCortas = false;

        private Builder() {
        }

        /** Origen de datos propio o de la librería. Obligatorio (o usar desdeArchivo / desdeTexto). */
        public Builder desde(OrigenDatos origen) {
            this.origen = origen;
            return this;
        }

        public Builder desdeArchivo(String ruta) {
            return desde(new OrigenArchivo(ruta));
        }

        public Builder desdeTexto(String contenido) {
            return desde(new OrigenTexto(contenido));
        }

        /** Separador de campos para el parser estándar. Por defecto ','. */
        public Builder separador(char separador) {
            this.separador = separador;
            return this;
        }

        /** Carácter de comillas para el parser estándar. Por defecto '"'. */
        public Builder comilla(char comilla) {
            this.comilla = comilla;
            return this;
        }

        /**
         * Parser propio. Si se indica, se ignoran {@link #separador(char)} y {@link #comilla(char)}.
         */
        public Builder parser(ParserCsv parser) {
            this.parser = parser;
            return this;
        }

        /** Fábrica propia para decidir qué tabla se crea. Por defecto {@link FabricaTablaCsv}. */
        public Builder fabrica(FabricaTabla fabrica) {
            this.fabrica = fabrica;
            return this;
        }

        /** Si la primera fila contiene los nombres de las columnas. Por defecto true. */
        public Builder conEncabezado(boolean conEncabezado) {
            this.conEncabezado = conEncabezado;
            return this;
        }

        /**
         * Si es true, las filas con menos valores que columnas se completan con texto vacío.
         * Si es false (por defecto), una fila así produce una {@link CsvExcepcion}.
         */
        public Builder completarFilasCortas(boolean completar) {
            this.completarFilasCortas = completar;
            return this;
        }

        public CargadorCsv construir() {
            if (origen == null) {
                throw new IllegalStateException("Falta indicar el origen de datos (desde, desdeArchivo o desdeTexto)");
            }
            if (fabrica == null) {
                throw new IllegalStateException("La fábrica de tablas no puede ser null");
            }
            return new CargadorCsv(this);
        }
    }
}
