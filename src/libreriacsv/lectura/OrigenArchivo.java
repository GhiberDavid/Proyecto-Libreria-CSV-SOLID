package libreriacsv.lectura;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Adapta un archivo del disco a {@link OrigenDatos}.
 */
public class OrigenArchivo implements OrigenDatos {

    private final Path ruta;
    private final Charset codificacion;

    public OrigenArchivo(String ruta) {
        this(Paths.get(ruta), StandardCharsets.UTF_8);
    }

    public OrigenArchivo(Path ruta) {
        this(ruta, StandardCharsets.UTF_8);
    }

    public OrigenArchivo(Path ruta, Charset codificacion) {
        if (ruta == null || codificacion == null) {
            throw new IllegalArgumentException("La ruta y la codificación no pueden ser null");
        }
        this.ruta = ruta;
        this.codificacion = codificacion;
    }

    @Override
    public Reader abrir() throws IOException {
        return Files.newBufferedReader(ruta, codificacion);
    }

    @Override
    public String descripcion() {
        return "archivo " + ruta.toAbsolutePath();
    }
}
