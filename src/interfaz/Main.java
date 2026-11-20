package interfaz;

import negocio.Hospital;
import negocio.modulo.*;
import java.util.Scanner;

public class Main {
    private static final TriajeModulo triajeService = new TriajeModulo();
    private static final CuartoModulo cuartoService = new CuartoModulo();
    private static final MedicoModulo medicoService = new MedicoModulo();
    private static final AtencionModulo atencionService = new AtencionModulo();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opc;

        do {
            System.out.println("\n=== SISTEMA HOSPITALARIO PRIORIS ===");
            System.out.println("1. [Módulo 1] Gestión de Pacientes y Triaje");
            System.out.println("2. [Módulo 2] Gestión de Cuartos y Desinfección");
            System.out.println("3. [Módulo 3] Gestión de Personal Médico");
            System.out.println("4. [Módulo 5] Atención Médica y Recetas");
            System.out.println("5. Consultar Estado Global del Hospital");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Ingrese un número válido: ");
                scanner.next();
            }
            opc = scanner.nextInt();
            scanner.nextLine();

            switch (opc) {
                case 1 -> menuModulo1();
                case 2 -> menuModulo2();
                case 3 -> menuModulo3();
                case 4 -> menuModulo5();
                case 5 -> mostrarEstadoGlobal();
                case 0 -> System.out.println("Cerrando sistema PRIORIS...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opc != 0);
    }

    private static void menuModulo1() {
        System.out.println("-> Submenú Módulo 1: Registrar / Buscar / Actualizar / Triaje");
        // (Joaquín): Pedir datos por Scanner y llamar a triajeService
    }

    private static void menuModulo2() {
        System.out.println("-> Submenú Módulo 2: Registrar Cuarto / Asignar / Confirmar Limpieza");
        // (Módulo 2): Pedir datos por Scanner y llamar a cuartoService
    }

    private static void menuModulo3() {
        System.out.println("-> Submenú Módulo 3: Registrar Médico / Consultar / Asignar Automático");
        // (Carlos): Pedir datos por Scanner y llamar a medicoService
    }

    private static void menuModulo5() {
        System.out.println("-> Submenú Módulo 5: Iniciar Consulta / Emitir Receta / Dar Alta");
        // (Andrés): Pedir datos por Scanner y llamar a atencionService
    }

    private static void mostrarEstadoGlobal() {
        System.out.println("\n--- PACIENTES (" + Hospital.pacientes.size() + ") ---");
        if (Hospital.pacientes.isEmpty()) System.out.println("Sin registros aún.");
        Hospital.pacientes.forEach(System.out::println);

        System.out.println("\n--- CUARTOS (" + Hospital.cuartos.size() + ") ---");
        if (Hospital.cuartos.isEmpty()) System.out.println("Sin registros aún.");
        Hospital.cuartos.forEach(System.out::println);

        System.out.println("\n--- PERSONAL MÉDICO (" + Hospital.medicos.size() + ") ---");
        if (Hospital.medicos.isEmpty()) System.out.println("Sin registros aún.");
        Hospital.medicos.forEach(System.out::println);
    }
}