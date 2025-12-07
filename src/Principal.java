import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConversorDeMoneda conversor = new ConversorDeMoneda();

        int opcion;

        do {
            mostrarMenu();
            opcion = ValidadorEntrada.leerOpcion(scanner, 1, 8);

            if (opcion == 8) {
                System.out.println("👋 Saliendo del conversor. ¡Hasta luego!");
                break;
            }

            if (opcion == 7) {
                conversor.getHistorial().mostrarHistorial();
                continue;
            }

            // Procesar conversión
            procesarConversion(scanner, conversor, opcion);

        } while (true);

        scanner.close();
    }

    /**
     * Muestra el menú principal de opciones
     */
    private static void mostrarMenu() {
        System.out.println("\n*****************************************************");
        System.out.println("¡Sea bienvenido/a al conversor de Moneda =]");
        System.out.println("");
        System.out.println("1) Dólar =>> Peso argentino");
        System.out.println("2) Peso argentino =>> Dólar");
        System.out.println("3) Dólar =>> Real brasileño");
        System.out.println("4) Real brasileño =>> Dólar");
        System.out.println("5) Dólar =>> Sol peruano");
        System.out.println("6) Sol peruano =>> Dólar");
        System.out.println("7) Mostrar historial de conversiones");
        System.out.println("8) Salir");
        System.out.println("Elija una opción válida:");
        System.out.println("*****************************************************");
        System.out.print(">>> ");
    }

    /**
     * Procesa la conversión de moneda según la opción seleccionada
     */
    private static void procesarConversion(Scanner scanner, ConversorDeMoneda conversor, int opcion) {
        String monedaOrigen, monedaDestino, nombreOrigen, nombreDestino;

        switch (opcion) {
            case 1:
                monedaOrigen = "USD"; monedaDestino = "ARS";
                nombreOrigen = "Dólares"; nombreDestino = "Pesos argentinos";
                break;
            case 2:
                monedaOrigen = "ARS"; monedaDestino = "USD";
                nombreOrigen = "Pesos argentinos"; nombreDestino = "Dólares";
                break;
            case 3:
                monedaOrigen = "USD"; monedaDestino = "BRL";
                nombreOrigen = "Dólares"; nombreDestino = "Reales brasileños";
                break;
            case 4:
                monedaOrigen = "BRL"; monedaDestino = "USD";
                nombreOrigen = "Reales brasileños"; nombreDestino = "Dólares";
                break;
            case 5:
                monedaOrigen = "USD"; monedaDestino = "PEN";
                nombreOrigen = "Dólares"; nombreDestino = "Soles peruanos";
                break;
            case 6:
                monedaOrigen = "PEN"; monedaDestino = "USD";
                nombreOrigen = "Soles peruanos"; nombreDestino = "Dólares";
                break;
            default:
                System.out.println("❌ Opción inválida.");
                return;
        }

        try {
            System.out.print("Ingrese el monto en " + nombreOrigen + ": ");
            double monto = ValidadorEntrada.leerMonto(scanner);
            double resultado = conversor.convertir(monedaOrigen, monedaDestino, monto);
            System.out.printf("✅ %.2f %s son equivalentes a %.2f %s.%n", 
                monto, nombreOrigen, resultado, nombreDestino);
        } catch (Exception e) {
            System.out.println("❌ Error en la conversión: " + e.getMessage());
        }
    }
}
