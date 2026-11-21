package libreriacsv.conversion;

/**
 * Conversores listos para los tipos más comunes.
 */
public final class Conversores {

    public static final ConversorValor<String> TEXTO = valor -> valor;

    public static final ConversorValor<Integer> ENTERO = valor -> Integer.parseInt(valor.trim());

    // Acepta coma o punto como separador decimal
    public static final ConversorValor<Double> DECIMAL = valor -> Double.parseDouble(valor.trim().replace(',', '.'));

    // "true", "si", "sí", "1" y "x" son verdaderos
    public static final ConversorValor<Boolean> BOOLEANO = valor -> {
        String v = valor.trim().toLowerCase();
        return v.equals("true") || v.equals("si") || v.equals("sí") || v.equals("1") || v.equals("x");
    };

    private Conversores() {
    }
}
