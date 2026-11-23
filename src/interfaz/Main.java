package interfaz;

import negocio.Hospital;
import negocio.modulo.*;
import java.util.Scanner;
import modelo.PersonalMedico;
import modelo.MedicoGeneral;
import modelo.MedicoEspecialista;
import modelo.Paciente;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

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

        int opcion;

        do {
            System.out.println("\n=== MÓDULO 3: PERSONAL MÉDICO ===");
            System.out.println("1. Registrar médico");
            System.out.println("2. Buscar médico por cédula");
            System.out.println("3. Actualizar datos y horarios");
            System.out.println("4. Inactivar médico");
            System.out.println("5. Listar médicos y turnos");
            System.out.println("6. Asignar médico automáticamente");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Ingrese un número válido: ");
                scanner.next();
            }

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> registrarMedicoMenu();
                case 2 -> buscarMedicoMenu();
                case 3 -> actualizarMedicoMenu();
                case 4 -> inactivarMedicoMenu();
                case 5 -> listarMedicosMenu();
                case 6 -> asignarMedicoMenu();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
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


    private static void registrarMedicoMenu() {

        System.out.println("\n=== REGISTRAR MÉDICO ===");

        System.out.println("Seleccione el tipo de médico:");
        System.out.println("1. Médico General");
        System.out.println("2. Médico Especialista");

        int opc = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Ingrese la cédula: ");
        String cedula = scanner.nextLine();

        System.out.print("Ingrese el nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese la hora de entrada (HH:mm): ");
        String horaEntrada = scanner.nextLine();

        System.out.print("Ingrese la hora de salida (HH:mm): ");
        String horaSalida = scanner.nextLine();


        LocalTime inicioTurno;
        LocalTime finTurno;

        try {
            inicioTurno = LocalTime.parse(horaEntrada);
            finTurno = LocalTime.parse(horaSalida);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Ingrese las horas en formato HH:mm.");
            return;
        }


        if (!inicioTurno.isBefore(finTurno)) {
            System.out.println("Error: La hora de entrada debe ser anterior a la salida.");
            return;
        }

        switch (opc) {

            case 1 -> {
                MedicoGeneral medico = new MedicoGeneral(cedula, nombre, inicioTurno, finTurno);
                boolean registrado = medicoService.registrarMedico(medico);

                if (registrado) {
                    System.out.println("Médico registrado correctamente.");
                }else {
                    System.out.println("Error: No se pudo registrar al médico.");
                }

            }

            case 2 -> {
                System.out.print("Ingrese la especialidad: ");
                String especialidad = scanner.nextLine();

                while (especialidad.isBlank()){
                    System.out.println("Datos ingresados incorrectos. Intente nuevamente: ");
                    especialidad = scanner.nextLine();
                }

                especialidad = especialidad.trim();

                MedicoEspecialista medico = new MedicoEspecialista(cedula, nombre, especialidad, inicioTurno, finTurno);
                boolean registrado = medicoService.registrarMedico(medico);

                if (registrado) {
                    System.out.println("Médico registrado correctamente.");
                }else {
                    System.out.println("Error: No se pudo registrar al médico.");
                }

            }

            default -> {
                System.out.println("Opción no válida.");
            }



        }
    }


    private static void buscarMedicoMenu() {

        System.out.println("\n=== BUSCAR MÉDICO ===");

        System.out.print("Ingrese la cédula del médico: ");
        String cedula = scanner.nextLine();

        PersonalMedico medico = medicoService.buscarMedicoPorCedula(cedula);

        if (medico == null){
            System.out.println("No se encontró el médico con esa cédula.");
            return;
        }else {
            System.out.println("Médico encontrado:");
            System.out.println(medico);
        }
    }



    private static void actualizarMedicoMenu() {

        System.out.println("\n=== ACTUALIZAR MÉDICO ===");

        System.out.print("Ingrese la cédula del médico: ");
        String cedula = scanner.nextLine();

        PersonalMedico medico = medicoService.buscarMedicoPorCedula(cedula);

        if (medico == null) {
            System.out.println("No se encontró un médico con esa cédula.");
            return;
        }

        System.out.println("\nMédico encontrado:");
        System.out.println(medico);

        System.out.print("\nIngrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        if (nuevoNombre.isBlank()) {
            System.out.println("Error: El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Ingrese la nueva hora de entrada (HH:mm): ");
        String horaEntrada = scanner.nextLine();

        System.out.print("Ingrese la nueva hora de salida (HH:mm): ");
        String horaSalida = scanner.nextLine();

        LocalTime nuevaEntrada;
        LocalTime nuevaSalida;

        try {
            nuevaEntrada = LocalTime.parse(horaEntrada);
            nuevaSalida = LocalTime.parse(horaSalida);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Ingrese las horas en formato HH:mm.");
            return;
        }

        if (!nuevaEntrada.isBefore(nuevaSalida)) {
            System.out.println("Error: La hora de entrada debe ser anterior a la salida.");
            return;
        }

        boolean actualizado = medicoService.actualizarMedico(
                cedula,
                nuevoNombre,
                nuevaEntrada,
                nuevaSalida
        );

        if (actualizado) {
            System.out.println("\nMédico actualizado correctamente.");
            System.out.println("Datos actualizados:");
            System.out.println(medico);
        } else {
            System.out.println("Error: No se pudo actualizar el médico.");
        }
    }


    private static void inactivarMedicoMenu() {

        System.out.println("\n=== INACTIVAR MÉDICO ===");

        System.out.print("Ingrese la cédula del médico: ");
        String cedula = scanner.nextLine();

        PersonalMedico medico = medicoService.buscarMedicoPorCedula(cedula);

        if (medico == null) {
            System.out.println("No se encontró el médico con esa cédula.");
            return;
        }

        if (!medico.isActivo()) {
            System.out.println("El médico ya se encuentra inactivo.");
            return;
        }

        System.out.println("\nMédico encontrado:");
        System.out.println(medico);

        boolean inactivado = medicoService.inactivarMedico(cedula);

        if (inactivado) {
            System.out.println("\nMédico inactivado correctamente.");
        } else {
            System.out.println("Error: No se pudo inactivar al médico.");
            System.out.println("Verifique que no esté atendiendo a un paciente.");
        }
    }


    private static void listarMedicosMenu() {

        System.out.println("\n=== LISTADO DE MÉDICOS Y TURNOS ===");

        List<PersonalMedico> medicos = medicoService.listarMedicos();

        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }

        for (PersonalMedico medico : medicos) {

            System.out.println("------------------------------");
            System.out.println("Cédula: " + medico.getCedula());
            System.out.println("Nombre: " + medico.getNombreCompleto());
            System.out.println("Especialidad: " + medico.getEspecialidad());
            System.out.println("Inicio del turno: " + medico.getHoraInicioTurno());
            System.out.println("Fin del turno: " + medico.getHoraFinTurno());
            System.out.println("Estado: " + medico.getEstado());

            if (medico.isActivo()) {
                System.out.println("Activo: Sí");
            } else {
                System.out.println("Activo: No");
            }
        }

        System.out.println("------------------------------");
        System.out.println("Total de médicos registrados: " + medicos.size());
    }


    private static void asignarMedicoMenu() {

        System.out.println("\n=== ASIGNAR MÉDICO AUTOMÁTICAMENTE ===");

        System.out.print("Ingrese la cédula del paciente: ");
        String cedula = scanner.nextLine().trim();

        Paciente pacienteEncontrado = null;

        for (Paciente paciente : Hospital.pacientes) {
            if (paciente.getCedula().equals(cedula)) {
                pacienteEncontrado = paciente;
                break;
            }
        }

        if (pacienteEncontrado == null) {
            System.out.println("No se encontró un paciente con esa cédula.");
            return;
        }

        if (pacienteEncontrado.getNivelManchester() == null) {
            System.out.println("Error: El paciente no tiene un nivel Manchester asignado.");
            return;
        }

        System.out.println("\nPaciente encontrado:");
        System.out.println(pacienteEncontrado);

        PersonalMedico medicoAsignado =
                medicoService.asignarMedicoAutomatico(pacienteEncontrado);

        if (medicoAsignado == null) {
            System.out.println("\nNo hay médicos disponibles para atender al paciente.");
        } else {
            System.out.println("\nMédico asignado correctamente.");
            System.out.println("Datos del médico:");
            System.out.println(medicoAsignado);
        }
    }


}