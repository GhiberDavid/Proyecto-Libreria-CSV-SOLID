package libreriacsv.modelo;

import libreriacsv.eventos.OyenteTabla;
import libreriacsv.eventos.TablaObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación por defecto de Tabla: guarda los datos del CSV en memoria
 * como String y avisa a los oyentes de cada cambio.
 */
public class TablaCsv implements Tabla, TablaObservable {

    private final List<String> encabezados;
    private final List<List<String>> filas;
    private final List<OyenteTabla> oyentes = new ArrayList<>();

    public TablaCsv(List<String> encabezados) {
        this(encabezados, Collections.<List<String>>emptyList());
    }

    public TablaCsv(List<String> encabezados, List<List<String>> filas) {
        this.encabezados = new ArrayList<>();
        for (String encabezado : encabezados) {
            validarNombreNuevo(encabezado);
            this.encabezados.add(encabezado);
        }
        this.filas = new ArrayList<>();
        for (List<String> fila : filas) {
            validarFila(fila);
            this.filas.add(new ArrayList<>(fila));
        }
    }

    // ---------- Lectura ----------

    @Override
    public List<String> obtenerEncabezados() {
        return new ArrayList<>(encabezados);
    }

    @Override
    public int cantidadFilas() {
        return filas.size();
    }

    @Override
    public int cantidadColumnas() {
        return encabezados.size();
    }

    @Override
    public String obtenerValor(int fila, int columna) {
        return filas.get(fila).get(columna);
    }

    @Override
    public List<String> obtenerFila(int fila) {
        return new ArrayList<>(filas.get(fila));
    }

    // ---------- Filas ----------

    @Override
    public void agregarFila(List<String> valores) {
        agregarFila(filas.size(), valores);
    }

    @Override
    public void agregarFila(int posicion, List<String> valores) {
        validarFila(valores);
        if (posicion < 0 || posicion > filas.size()) {
            throw new IndexOutOfBoundsException("Posición de fila no válida: " + posicion);
        }
        List<String> copia = new ArrayList<>(valores);
        filas.add(posicion, copia);
        for (OyenteTabla oyente : copiaOyentes()) {
            oyente.filaAgregada(posicion, new ArrayList<>(copia));
        }
    }

    @Override
    public List<String> quitarFila(int fila) {
        List<String> quitada = filas.remove(fila);
        for (OyenteTabla oyente : copiaOyentes()) {
            oyente.filaQuitada(fila, new ArrayList<>(quitada));
        }
        return new ArrayList<>(quitada);
    }

    // ---------- Columnas ----------

    @Override
    public void agregarColumna(String nombre, String valorPorDefecto) {
        List<String> valores = new ArrayList<>();
        for (int i = 0; i < filas.size(); i++) {
            valores.add(valorPorDefecto == null ? "" : valorPorDefecto);
        }
        agregarColumna(nombre, valores);
    }

    @Override
    public void agregarColumna(String nombre, List<String> valores) {
        validarNombreNuevo(nombre);
        if (valores.size() != filas.size()) {
            throw new IllegalArgumentException("La columna '" + nombre + "' tiene " + valores.size()
                    + " valores, pero la tabla tiene " + filas.size() + " filas");
        }
        encabezados.add(nombre);
        for (int i = 0; i < filas.size(); i++) {
            String valor = valores.get(i);
            filas.get(i).add(valor == null ? "" : valor);
        }
        int posicion = encabezados.size() - 1;
        for (OyenteTabla oyente : copiaOyentes()) {
            oyente.columnaAgregada(posicion, nombre);
        }
    }

    @Override
    public List<String> quitarColumna(String nombre) {
        return quitarColumna(indiceObligatorio(nombre));
    }

    @Override
    public List<String> quitarColumna(int columna) {
        if (columna < 0 || columna >= encabezados.size()) {
            throw new IndexOutOfBoundsException("Posición de columna no válida: " + columna);
        }
        String nombre = encabezados.remove(columna);
        List<String> valores = new ArrayList<>();
        for (List<String> fila : filas) {
            valores.add(fila.remove(columna));
        }
        for (OyenteTabla oyente : copiaOyentes()) {
            oyente.columnaQuitada(columna, nombre);
        }
        return valores;
    }

    // ---------- Oyentes ----------

    @Override
    public void agregarOyente(OyenteTabla oyente) {
        if (oyente == null) {
            throw new IllegalArgumentException("El oyente no puede ser null");
        }
        oyentes.add(oyente);
    }

    @Override
    public void quitarOyente(OyenteTabla oyente) {
        oyentes.remove(oyente);
    }

    // ---------- Validaciones ----------

    // Se recorre una copia por si un oyente se quita a sí mismo durante el aviso
    private List<OyenteTabla> copiaOyentes() {
        return new ArrayList<>(oyentes);
    }

    private void validarFila(List<String> valores) {
        if (valores == null || valores.size() != encabezados.size()) {
            int recibidos = valores == null ? 0 : valores.size();
            throw new IllegalArgumentException("La fila tiene " + recibidos
                    + " valores, pero la tabla tiene " + encabezados.size() + " columnas");
        }
    }

    private void validarNombreNuevo(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la columna no puede ser null");
        }
        if (encabezados.contains(nombre)) {
            throw new IllegalArgumentException("Ya existe la columna: " + nombre);
        }
    }

    @Override
    public String toString() {
        return "TablaCsv{encabezado + " + cantidadFilas() + " filas de datos, " + cantidadColumnas() + " columnas}";
    }
}
