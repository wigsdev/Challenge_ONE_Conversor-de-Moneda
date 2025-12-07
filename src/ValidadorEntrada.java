import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase para validar entradas del usuario de forma robusta.
 * Previene crashes por entradas inválidas y proporciona mensajes claros.
 */
public class ValidadorEntrada {
    private static final int MAX_INTENTOS = 3;

    /**
     * Lee una opción del menú validando el rango
     * @param scanner Scanner para leer la entrada
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return La opción válida seleccionada
     */
    public static int leerOpcion(Scanner scanner, int min, int max) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                if (opcion >= min && opcion <= max) {
                    return opcion;
                }
                
                System.out.println("⚠️  Opción fuera de rango. Ingrese un valor entre " + min + " y " + max);
                System.out.print(">>> ");
                intentos++;
                
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número.");
                scanner.nextLine(); // Limpiar buffer
                System.out.print(">>> ");
                intentos++;
            }
        }
        
        System.out.println("❌ Máximo de intentos alcanzado. Saliendo...");
        return max; // Retornar opción de salir
    }

    /**
     * Lee un monto validando que sea positivo
     * @param scanner Scanner para leer la entrada
     * @return El monto válido ingresado
     */
    public static double leerMonto(Scanner scanner) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            try {
                double monto = scanner.nextDouble();
                scanner.nextLine(); // Limpiar buffer
                
                if (monto > 0) {
                    return monto;
                }
                
                System.out.println("⚠️  El monto debe ser mayor a cero.");
                System.out.print("Ingrese el monto: ");
                intentos++;
                
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número válido.");
                scanner.nextLine(); // Limpiar buffer
                System.out.print("Ingrese el monto: ");
                intentos++;
            }
        }
        
        throw new RuntimeException("No se pudo obtener un monto válido después de " + MAX_INTENTOS + " intentos.");
    }

    /**
     * Lee un código de moneda validando el formato
     * @param scanner Scanner para leer la entrada
     * @param mensaje Mensaje a mostrar al usuario
     * @return El código de moneda válido (3 letras mayúsculas)
     */
    public static String leerCodigoMoneda(Scanner scanner, String mensaje) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            System.out.print(mensaje);
            String codigo = scanner.nextLine().trim().toUpperCase();
            
            if (codigo.matches("[A-Z]{3}")) {
                return codigo;
            }
            
            System.out.println("⚠️  Código inválido. Debe ser de 3 letras (ej: USD, ARS, BRL)");
            intentos++;
        }
        
        throw new RuntimeException("No se pudo obtener un código de moneda válido.");
    }

    /**
     * Confirma una acción con el usuario
     * @param scanner Scanner para leer la entrada
     * @param mensaje Mensaje de confirmación
     * @return true si el usuario confirma, false en caso contrario
     */
    public static boolean confirmar(Scanner scanner, String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("Y") || respuesta.equals("YES");
    }
}
