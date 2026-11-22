package interfaz;

import negocio.Hospital;
import negocio.modulo.*;

import java.sql.SQLOutput;
import java.util.Scanner;
import modelo.Cuarto;
import modelo.Paciente;

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
        int opcion;

        do{
            System.out.println("\n--- MÓDULO 2: GESTIÓN DE CUARTOS ---");
            System.out.println("1. Registrar cuarto");
            System.out.println("2. Mostrar todos los cuartos");
            System.out.println("3. Buscar cuarto disponible");
            System.out.println("4. Asignar paciente a un cuarto");
            System.out.println("5. Iniciar desinfección");
            System.out.println("6. Confirmar limpieza y liberar cuarto");
            System.out.println("7. Eliminar cuarto");
            System.out.println("0. Volver al menú principal");
            System.out.println("Seleccione una opcion: ");

            while (!scanner.hasNextInt()){
                System.out.println("Ingrese un número válido: ");
                scanner.next();
            }

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch(opcion){
                case 1:
                    registrarCuarto();
                    break;
                case 2:
                    System.out.println("\n--- LISTA DE CUARTOS ---");
                    if (Hospital.cuartos.isEmpty()) {
                        System.out.println("No existen cuartos registrados");
                    } else {
                        Hospital.cuartos.forEach(System.out::println);
                    }
                    break;

                case 3:
                    buscarCuarto();
                    break;
                case 4:
                    asignarPaciente();
                    break;
                case 5:
                    iniciarDesinfeccion();
                    break;
                case 6:
                    confirmarLimpieza();
                    break;
                case 7:
                    eliminarCuarto();
                    break;
                case 0:
                    System.out.println("De vuelta al menú principal");
                    break;
                default:
                    System.out.println("Opcion no válida");
            }
        }while(opcion != 0);
    }

    private static void registrarCuarto(){
        System.out.println("\n--- REGISTRAR CUARTO ---");
        System.out.println("Código del caurto: ");
        String codigo = scanner.nextLine();
        System.out.println("Tipo de área: ");
        String tipoArea = scanner.nextLine();
        System.out.println("Equipamiento: ");
        String equipamiento = scanner.nextLine();
        System.out.println("Tiempo de desinfección en minutos: ");
        int tiempo = scanner.nextInt();
        scanner.nextLine();

        Cuarto cuarto = new Cuarto(codigo, tipoArea, equipamiento, tiempo);
        if(cuartoService.registrarCuarto(cuarto)){
            System.out.println("Cuarto registrado");
        } else {
            System.out.println("No se pudo registrar. Verifique que el código exista");
        }
    }

    private static void buscarCuarto(){
        System.out.println("Ingrese el tipo de área que buscar: ");
        String tipoArea = scanner.nextLine();

        Cuarto cuarto = cuartoService.buscarCuartoDisponible(tipoArea);
        if(cuarto != null){
            System.out.println("Cuarto disponible encontrado: ");
            System.out.println(cuarto);
        } else {
            System.out.println("No hay cuartos disponibles de ese tipo");
        }
    }

    private static void asignarPaciente(){
        System.out.println("Ingrese el código del cuarto: ");
        String codigo = scanner.nextLine();
        System.out.println("Ingrese la cédula del paciente: ");
        String cedula = scanner.nextLine();
        Paciente paciente = null;
        for(Paciente p : Hospital.pacientes){
            if(p.getCedula().equals(cedula) && p.isActivo()){
                paciente = p;
                break;
            }
        }

        if (paciente == null){
            System.out.println("Paciente no encontrado o inactivo");
            return;
        }

        if(cuartoService.asignarPacienteACuarto(codigo, paciente)){
            System.out.println("Paciente asignado correctamente");
        } else {
            System.out.println("No se pudo asignar. Verifique que el cuarto esté disponible");
        }
    }

    private static void iniciarDesinfeccion(){
        System.out.println("Ingrese el código del cuarto: ");
        String codigo = scanner.nextLine();
        cuartoService.iniciarDesinfeccion(codigo);
        System.out.println("Se solicito iniciar con la desinfección");
        System.out.println("Consulte el estado del cuarto para verificar el cambio");
    }

    private static void confirmarLimpieza(){
        System.out.println("Ingrese el código del cuarto: ");
        String codigo = scanner.nextLine();
        System.out.println("¿El personal confirma que la limpieza está realizada? (s/n): ");
        String respuesta = scanner.nextLine();
        boolean confirmacion = respuesta.equalsIgnoreCase("s");
        if(cuartoService.intentarLiberarCuarto(codigo, confirmacion)){
            System.out.println("Cuarto liberado y disponible");
        } else {
            System.out.println("No se pudo liberar el cuarto. Puede que falte tiempo de desinfección o confirmación");
        }
    }

    private static void eliminarCuarto(){
        System.out.println("Ingrese el código del cuarto que desea eliminar: ");
        String codigo = scanner.nextLine();
        if(cuartoService.eliminarCuarto(codigo)){
            System.out.println("Cuarto eliminado correctamente");
        } else {
            System.out.println("No se puede eliminar. Verifique el cpodigo y el estado del cuarto");
        }
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