# Librería CSV SOLID

Librería en Java para leer archivos `.csv`, representarlos como un objeto
`Tabla`, agregar o quitar filas y columnas, e imprimir la tabla en pantalla.

Está diseñada siguiendo los principios **SOLID** y se puede **extender con
código propio sin modificar el código de la librería**.

- Lenguaje: Java 11 o superior, sin dependencias externas.
- Entorno: IntelliJ IDEA (compilador propio de IntelliJ, sin Maven ni Gradle).

---

## Índice

1. [Abrir el proyecto](#1-abrir-el-proyecto)
2. [Estructura](#2-estructura)
3. [Uso básico](#3-uso-básico)
4. [Opciones de lectura (Builder)](#4-opciones-de-lectura-builder)
5. [Cómo extender la librería sin modificarla](#5-cómo-extender-la-librería-sin-modificarla)
6. [Patrones de diseño usados](#6-patrones-de-diseño-usados)
7. [Principios SOLID](#7-principios-solid)

---

## 1. Abrir el proyecto

1. En IntelliJ: **File → New → Project from Version Control** y pegar la URL del repositorio
   (o **File → Open** sobre la carpeta si ya está clonada).
2. Si IntelliJ avisa que falta el SDK: **File → Project Structure → Project → SDK** y elegir un JDK 11 o superior.
   Si la carpeta `src` no aparece en azul: clic derecho sobre `src` → **Mark Directory as → Sources Root**.
3. Ejecutar `src/ejemplos/DemoBasico.java` (clic derecho → **Run**).
   La carpeta de trabajo debe ser la raíz del proyecto (es lo que usa IntelliJ por defecto)
   para que encuentre la carpeta `datos/`.

Para usar la librería en otro proyecto se puede copiar la carpeta `src/libreriacsv`
o generar un `.jar` (**File → Project Structure → Artifacts → + → JAR**).

---

## 2. Estructura

```
src/
├── libreriacsv/              ← CÓDIGO BASE DE LA LIBRERÍA (no se modifica para extenderla)
│   ├── Csv.java              Fachada: punto de entrada sencillo
│   ├── modelo/               Tabla, sus interfaces, la implementación y la fábrica
│   ├── lectura/              Origen de datos, parser y cargador (Builder)
│   ├── conversion/           Conversores de String a otros tipos
│   ├── impresion/            Formateadores e impresora
│   ├── decoradores/          Decoradores de tabla y de formateador
│   └── eventos/              Oyentes de cambios (Observer)
└── ejemplos/                 ← CÓDIGO DE USO Y EXTENSIONES PROPIAS
    ├── DemoBasico.java
    ├── DemoExtension.java
    └── extension/            Clases propias que extienden la librería
datos/                        Archivos CSV de ejemplo
```

Todo lo que está en `ejemplos/extension` extiende la librería **sin tocar** nada de `libreriacsv/`.

---

## 3. Uso básico

### Leer un CSV

Por defecto se espera coma como separador y la primera fila como encabezado.
Todos los valores se guardan como `String`.

```java
Tabla tabla = Csv.leer("datos/estudiantes.csv");
Tabla otra  = Csv.leer("datos/productos_punto_y_coma.csv", ';');
Tabla desdeTexto = Csv.desdeTexto("nombre,edad\nAna,20\nLuis,22");
```

### Posiciones de filas y columnas

Igual que en las listas de Java, **las posiciones empiezan en 0** y **el
encabezado no es una fila de datos**:

| Línea en Excel / archivo | Qué es                    | Índice en la librería |
|--------------------------|---------------------------|-----------------------|
| 1                        | encabezado (`id,nombre…`) | `obtenerEncabezados()`|
| 2                        | primera fila de datos     | fila `0`              |
| 3                        | segunda fila de datos     | fila `1`              |

`cantidadFilas()` devuelve solo las filas de datos. Lo mismo con las columnas:
la primera columna es la `0`. Los mensajes de `OyenteConsola` muestran ambos
números para evitar confusiones.

### Consultar datos

```java
tabla.obtenerEncabezados();          // [id, nombre, carrera, ...]
tabla.cantidadFilas();
tabla.cantidadColumnas();
tabla.obtenerValor(0, 1);            // por posición
tabla.obtenerValor(0, "nombre");     // por nombre de columna
tabla.obtenerFila(2);
tabla.obtenerColumna("carrera");
```

### Obtener valores con otro tipo

```java
int id        = tabla.obtenerValor(0, "id", Conversores.ENTERO);
double nota   = tabla.obtenerValor(0, "nota", Conversores.DECIMAL);
boolean activo = tabla.obtenerValor(0, "activo", Conversores.BOOLEANO);
List<Double> notas = tabla.obtenerColumna("nota", Conversores.DECIMAL);
```

### Agregar y quitar filas

```java
tabla.agregarFila(Arrays.asList("6", "Pedro", "Sistemas", "16", "2024-01-15", "si"));
tabla.agregarFila(0, Arrays.asList(/* ... */));   // en una posición
tabla.quitarFila(3);                               // devuelve la fila quitada
```

### Agregar y quitar columnas

```java
tabla.agregarColumna("seccion", "A");                      // mismo valor para todas las filas
tabla.agregarColumna("puesto", Arrays.asList("1","2","3")); // un valor por fila
tabla.quitarColumna("fecha_ingreso");
tabla.quitarColumna(0);
```

### Imprimir en pantalla

```java
Csv.imprimir(tabla);                               // formato ASCII por defecto
Csv.imprimir(tabla, new FormateadorMarkdown());    // otro formato
```

Salida por defecto:

```
+----+-----------------+-------------+-------+
| id | nombre          | carrera     | nota  |
+----+-----------------+-------------+-------+
| 1  | Ana Pérez       | Informática | 18.5  |
| 3  | Martínez, Carla | Informática | 19.25 |
+----+-----------------+-------------+-------+
```

### Errores

- Problemas al leer o interpretar el CSV → `CsvExcepcion` (con el registro o línea del problema).
- Filas o columnas con tamaño incorrecto, nombres repetidos o columnas inexistentes → `IllegalArgumentException`.
- Posiciones fuera de rango → `IndexOutOfBoundsException`.

---

## 4. Opciones de lectura (Builder)

```java
Tabla tabla = Csv.cargador()                 // o CargadorCsv.builder()
        .desdeArchivo("datos/archivo.csv")   // o .desdeTexto(...) o .desde(origenPropio)
        .separador(';')                      // por defecto ','
        .comilla('\'')                       // por defecto '"'
        .conEncabezado(false)                // por defecto true; si es false se generan Columna1, Columna2...
        .completarFilasCortas(true)          // rellena con "" las filas a las que les faltan valores
        .parser(parserPropio)                // reemplaza al parser estándar
        .fabrica(fabricaPropia)              // decide qué Tabla se crea
        .construir()
        .cargar();
```

El parser estándar admite campos entre comillas con comas, saltos de línea y
comillas dobles escapadas (`""`), saltos de línea de Windows o Linux, BOM de
UTF-8 y líneas vacías.

---

## 5. Cómo extender la librería sin modificarla

Cada punto de extensión es una **interfaz** o **clase base** de la librería.
Se crea una clase propia fuera del paquete `libreriacsv` y se le pasa a la
librería. Todos los ejemplos siguientes están en `src/ejemplos/extension` y
se ejecutan en `src/ejemplos/DemoExtension.java`.

| Quiero…                                   | Implemento / heredo de            | Se conecta con                          |
|-------------------------------------------|-----------------------------------|-----------------------------------------|
| Convertir valores a otro tipo             | `ConversorValor<T>`               | `tabla.obtenerValor(fila, col, conv)`   |
| Imprimir con otro formato                 | `FormateadorTabla` o `FormateadorBase` | `Csv.imprimir(tabla, formateador)` |
| Añadir algo a la impresión existente      | `DecoradorFormateador`            | envolver otro formateador               |
| Cambiar lo que devuelve la tabla al leer  | `DecoradorTabla`                  | envolver la tabla                       |
| Reaccionar a los cambios de la tabla      | `OyenteTabla`                     | `((TablaObservable) tabla).agregarOyente(...)` |
| Leer otro formato de texto                | `ParserCsv`                       | `.parser(...)` en el Builder            |
| Leer desde otra fuente                    | `OrigenDatos`                     | `.desde(...)` en el Builder             |
| Crear otro tipo de tabla al cargar        | `FabricaTabla`                    | `.fabrica(...)` en el Builder           |
| Mostrar la tabla en otro destino          | `ImpresoraTabla`                  | usarla directamente                     |

### 5.1 Conversor propio (Strategy)

```java
public class ConversorFecha implements ConversorValor<LocalDate> {
    @Override
    public LocalDate convertir(String valor) {
        return LocalDate.parse(valor.trim());
    }
}

LocalDate ingreso = tabla.obtenerValor(0, "fecha_ingreso", new ConversorFecha());
```

Como es una interfaz funcional, también vale una lambda:

```java
ConversorValor<String> iniciales = v -> v.substring(0, 1);
```

### 5.2 Formateador propio heredando la plantilla (Template Method)

`FormateadorBase` ya calcula los anchos de columna y fija el orden de los pasos.
Solo hay que decir cómo se dibuja cada fila y, si se quiere, las líneas superior,
bajo el encabezado e inferior.

```java
public class FormateadorHtml extends FormateadorBase {
    @Override
    protected String lineaSuperior(List<Integer> anchos) { return "<table>"; }

    @Override
    protected String formatearFila(List<String> valores, List<Integer> anchos) {
        StringBuilder fila = new StringBuilder("  <tr>");
        for (String v : valores) fila.append("<td>").append(v).append("</td>");
        return fila.append("</tr>").toString();
    }

    @Override
    protected String lineaInferior(List<Integer> anchos) { return "</table>"; }
}

Csv.imprimir(tabla, new FormateadorHtml());
```

### 5.3 Formateador propio desde cero (Strategy)

```java
public class FormateadorJson implements FormateadorTabla {
    @Override
    public String formatear(TablaLegible tabla) { /* construir el JSON */ }
}
```

### 5.4 Modificar el resultado de la impresión (Decorator)

Los decoradores se pueden combinar:

```java
FormateadorTabla formato =
        new FormateadorConResumen(
            new FormateadorConTitulo(new FormateadorAscii(), "Estudiantes"));
Csv.imprimir(tabla, formato);
```

Decorador propio:

```java
public class FormateadorConFecha extends DecoradorFormateador {
    public FormateadorConFecha(FormateadorTabla original) { super(original); }

    @Override
    public String formatear(TablaLegible tabla) {
        return "Generado el " + LocalDate.now() + System.lineSeparator() + super.formatear(tabla);
    }
}
```

### 5.5 Modificar lo que devuelve la tabla (Decorator)

```java
Tabla mayusculas = new TablaTransformada(tabla, String::toUpperCase);   // de la librería
Tabla sinVacios  = new TablaConValorPorDefecto(tabla, "N/A");          // propio
```

Decorador propio: heredar de `DecoradorTabla` y sobrescribir solo los métodos que cambian.

```java
public class TablaConValorPorDefecto extends DecoradorTabla {
    private final String porDefecto;

    public TablaConValorPorDefecto(Tabla original, String porDefecto) {
        super(original);
        this.porDefecto = porDefecto;
    }

    @Override
    public String obtenerValor(int fila, int columna) {
        String v = super.obtenerValor(fila, columna);
        return v.trim().isEmpty() ? porDefecto : v;
    }
    // obtenerFila(...) se sobrescribe igual
}
```

### 5.6 Reaccionar a los cambios (Observer)

```java
public class OyenteContadorCambios implements OyenteTabla {
    private int cambios;

    @Override
    public void filaAgregada(int posicion, List<String> valores) { cambios++; }

    @Override
    public void columnaQuitada(int posicion, String nombre) { cambios++; }
    // los demás avisos tienen implementación vacía por defecto
}

((TablaObservable) tabla).agregarOyente(new OyenteContadorCambios());
```

### 5.7 Parser propio (Strategy)

```java
public class ParserLineasSimples implements ParserCsv {
    @Override
    public List<List<String>> parsear(Reader lector) throws IOException { /* ... */ }
}

Tabla t = Csv.cargador().desdeTexto(texto).parser(new ParserLineasSimples("|")).construir().cargar();
```

### 5.8 Origen de datos propio (Adapter)

```java
public class OrigenRecurso implements OrigenDatos {
    private final String nombre;
    public OrigenRecurso(String nombre) { this.nombre = nombre; }

    @Override
    public Reader abrir() {
        return new InputStreamReader(getClass().getResourceAsStream(nombre), StandardCharsets.UTF_8);
    }
}

Tabla t = Csv.cargador().desde(new OrigenRecurso("/datos.csv")).construir().cargar();
```

### 5.9 Fábrica propia (Factory Method)

```java
public class FabricaTablaVigilada implements FabricaTabla {
    private final OyenteTabla oyente;
    public FabricaTablaVigilada(OyenteTabla oyente) { this.oyente = oyente; }

    @Override
    public Tabla crear(List<String> encabezados, List<List<String>> filas) {
        TablaCsv tabla = new TablaCsv(encabezados, filas);
        tabla.agregarOyente(oyente);
        return tabla;
    }
}

Tabla t = Csv.cargador().desdeArchivo("datos/estudiantes.csv")
        .fabrica(new FabricaTablaVigilada(new OyenteConsola()))
        .construir().cargar();
```

---

## 6. Patrones de diseño usados

| Tipo           | Patrón          | Dónde                                                        | Para qué |
|----------------|-----------------|--------------------------------------------------------------|----------|
| Creacional     | Builder         | `CargadorCsv.Builder`                                        | Configurar la lectura con muchas opciones opcionales de forma legible |
| Creacional     | Factory Method  | `FabricaTabla`, `FabricaTablaCsv`                            | Que el cargador no dependa de la clase concreta de tabla |
| Estructural    | Facade          | `Csv`                                                        | Uso sencillo para los casos comunes |
| Estructural    | Adapter         | `OrigenDatos`, `OrigenArchivo`, `OrigenTexto`                | Leer de fuentes distintas con una misma interfaz (`Reader`) |
| Estructural    | Decorator       | `DecoradorTabla`, `TablaTransformada`, `DecoradorFormateador`, `FormateadorConTitulo`, `FormateadorConResumen` | Modificar resultados sin tocar las clases originales |
| Comportamiento | Strategy        | `ConversorValor`, `ParserCsv`, `FormateadorTabla`            | Intercambiar algoritmos de conversión, lectura e impresión |
| Comportamiento | Template Method | `FormateadorBase`, `FormateadorAscii`, `FormateadorMarkdown` | Reutilizar el algoritmo de impresión cambiando solo sus pasos |
| Comportamiento | Observer        | `TablaObservable`, `OyenteTabla`, `OyenteConsola`, `TablaCsv` | Ejecutar código propio cuando la tabla cambia |

---

## 7. Principios SOLID

- **S – Responsabilidad única:** cada clase hace una sola cosa. `TablaCsv` guarda datos,
  `ParserCsvEstandar` interpreta texto, `OrigenArchivo` abre el archivo, `CargadorCsv`
  coordina la lectura, `FormateadorAscii` genera texto e `ImpresoraConsola` lo muestra.
- **O – Abierto/cerrado:** se añaden formatos, conversores, parsers, orígenes, fábricas,
  oyentes y decoradores nuevos implementando interfaces, sin modificar la librería
  (ver `ejemplos/extension`).
- **L – Sustitución de Liskov:** cualquier `Tabla` (`TablaCsv` o un decorador) y cualquier
  `FormateadorTabla` se pueden intercambiar sin romper el código que los usa. La vista de
  solo lectura implementa `TablaLegible` y no `Tabla`, para no tener métodos de edición
  que lancen excepciones.
- **I – Segregación de interfaces:** la tabla se divide en `TablaLegible`, `FilasEditables`
  y `ColumnasEditables`; ser observable es otra interfaz (`TablaObservable`). Los
  formateadores e impresoras solo dependen de `TablaLegible`.
- **D – Inversión de dependencias:** `CargadorCsv` depende de `OrigenDatos`, `ParserCsv` y
  `FabricaTabla`; `ImpresoraConsola` depende de `FormateadorTabla`. Las implementaciones
  concretas se inyectan desde fuera (Builder o constructor).
