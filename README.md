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
