package interfaz;

import modelo.Alergia;
import modelo.Paciente;
import modelo.SignosVitales;
import modelo.enums.NivelManchester;
import negocio.Hospital;
import negocio.Validador;
import negocio.modulo.*;

import java.util.PriorityQueue;
import java.util.Scanner;
import modelo.Cuarto;
import modelo.Paciente;


public class Main {
    private static final TriajeModulo triajeModulo = new TriajeModulo();
    private static final CuartoModulo cuartoModulo = new CuartoModulo();
    private static final MedicoModulo medicoModulo = new MedicoModulo();
    private static final AtencionModulo atencionModulo = new AtencionModulo();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            System.out.println("\n=== SISTEMA HOSPITALARIO PRIORIS ===");
            System.out.println("1. [Módulo 1] Ingreso y Triaje de Pacientes");
            System.out.println("2. [Módulo 2] Gestión de Cuartos y Desinfección");
            System.out.println("3. [Módulo 3] Personal Médico y Turnos");
            System.out.println("4. [Módulo 4] Atención Médica y Recetas");
            System.out.println("5. Consultar Estado Global del Hospital");
            System.out.println("0. Salir");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuTriaje();
                case 2 -> menuCuartos();
                case 3 -> menuMedicos();
                case 4 -> menuAtencion();
                case 5 -> mostrarEstadoGlobal();
                case 0 -> System.out.println("Cerrando sistema PRIORIS...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void menuTriaje() {
        int opcTriaje;
        do {
            System.out.println("\n========= INGRESO Y TRIAJE DE PACIENTES =========");
            System.out.println("1. Registrar nuevo paciente");
            System.out.println("2. Buscar paciente por cédula");
            System.out.println("3. Actualizar antecedentes de paciente");
            System.out.println("4. Dar de baja a paciente");
            System.out.println("5. Evaluar signos vitales y Triaje de Manchester");
            System.out.println("6. Ver cola de prioridad actual");
            System.out.println("0. Regresar al menú principal");

            opcTriaje = leerEntero("Seleccione una opción >> ");

            switch (opcTriaje) {
                case 1 -> {
                    System.out.println();
                    String cedula = leerTexto("Ingrese la cédula (10 dígitos): ");
                    while (!Validador.esCedulaValida(cedula)) {
                        cedula = leerTexto("Ingreso inválido, debe tener 10 dígitos numéricos\nIngrese de nuevo: ");
                    }

                    String nombre = leerTexto("Ingrese el nombre completo del paciente: ");
                    while (!Validador.esTextoValido(nombre)) {
                        nombre = leerTexto("Ingreso inválido, por favor, ingrese de nuevo: ");
                    }

                    int edad = leerEntero("Ingrese la edad (0-120): ");
                    while (!Validador.estaEnRango(edad, 0, 120)) {
                        edad = leerEntero("Ingreso inválido, por favor, ingrese un valor entre 0 y 120: ");
                    }

                    String tipoSangre = leerTexto("Ingrese el tipo de sangre del paciente (ej. A+, O-, etc.): ");
                    while (!triajeModulo.esTipoSangreValido(tipoSangre)) {
                        tipoSangre = leerTexto("Ingreso inválido, por favor ingrese de nuevo.\nTipos válidos: (A+, A-, B+, B-, AB+, AB-, O+, O-):");
                    }
                    tipoSangre = tipoSangre.trim().toUpperCase();

                    System.out.print("Antecedentes clínicos (presione Enter si no tiene): ");
                    String antecedentes = scanner.nextLine().trim();

                    Paciente nuevoPaciente = new Paciente(cedula, nombre, edad, tipoSangre, antecedentes);

                    String tieneAlergias = leerTexto("El paciente registra alergias? (S para sí/N para no): ");
                    while (!tieneAlergias.equalsIgnoreCase("S") && !tieneAlergias.equalsIgnoreCase("N")) {
                        tieneAlergias = leerTexto("Opción inválida. Ingrese solo S o N: ");
                    }
                    while (tieneAlergias.equalsIgnoreCase("S")) {
                        String sustancia = leerTexto("  Nombre de la sustancia: ");
                        String principioActivo = leerTexto("  Código de principio activo: ");
                        String grupoFarmacologico = leerTexto("  Grupo farmacológico: ");

                        nuevoPaciente.agregarAlergia(new Alergia(sustancia, principioActivo, grupoFarmacologico));

                        tieneAlergias = leerTexto("Desea agregar otra alergia? (S para sí/N para no): ");
                        while (!tieneAlergias.equalsIgnoreCase("S") && !tieneAlergias.equalsIgnoreCase("N")) {
                            tieneAlergias = leerTexto("Opción inválida. Ingrese solo S o N: ");
                        }
                    }

                    if (triajeModulo.registrarPaciente(nuevoPaciente)) {
                        System.out.println("Paciente registrado exitosamente con cédula " + cedula);
                    } else {
                        System.out.println("ERROR: No se pudo registrar (la cédula ya existe o los datos son inválidos).");
                    }
                }

                case 2 -> {
                    System.out.println();
                    String cedulaBusq = leerTexto("Ingrese la cédula del paciente a buscar: ");
                    Paciente pacienteEncontrado = triajeModulo.buscarPacientePorCedula(cedulaBusq);

                    if (pacienteEncontrado == null) {
                        System.out.println("ERROR: No se encontró ningún paciente activo con esa cédula");
                    } else {
                        System.out.println("\n---- Datos del paciente ----");
                        System.out.println(pacienteEncontrado);
                        System.out.println("Edad: " + pacienteEncontrado.getEdad());
                        System.out.println("Tipo de sangre: " + pacienteEncontrado.getTipoSangre());
                        System.out.println("Antecedentes: " + pacienteEncontrado.getAntecedentes());
                        System.out.println("Alergias: " + (pacienteEncontrado.getAlergias().isEmpty() ? "Ninguna" : pacienteEncontrado.getAlergias()));
                    }
                }

                case 3 -> {
                    System.out.println();
                    String cedulaActualizar = leerTexto("Ingrese la cédula del paciente para actualizar sus antecedentes: ");
                    if (triajeModulo.buscarPacientePorCedula(cedulaActualizar) == null) {
                        System.out.println("ERROR: No se encontró ningún paciente activo con esa cédula");
                        break;
                    }

                    String nuevosAntecedentes = leerTexto("Ingrese los nuevos antecedentes clínicos (o 'Ninguno'): ");
                    if (triajeModulo.actualizarPaciente(cedulaActualizar, nuevosAntecedentes)) {
                        System.out.println("Datos del paciente actualizados correctamente");
                    } else {
                        System.out.println("ERROR: No se pudieron actualizar los antecedentes");
                    }
                }

                case 4 -> {
                    System.out.println();
                    String cedulaBaja = leerTexto("Ingrese la cédula del paciente a dar de baja: ");
                    if (triajeModulo.darDeBajaPaciente(cedulaBaja)) {
                        System.out.println("Paciente dado de baja y retirado de la cola correctamente");
                    } else {
                        System.out.println("ERROR: No se encontró ningún paciente activo con esa cédula");
                    }
                }

                case 5 -> {
                    System.out.println();
                    String cedulaTriaje = leerTexto("Ingrese la cédula del paciente a evaluar: ");
                    Paciente pacienteTriaje = triajeModulo.buscarPacientePorCedula(cedulaTriaje);

                    if (pacienteTriaje == null) {
                        System.out.println("ERROR: No se encontró ningún paciente activo con esa cédula");
                        break;
                    }

                    int presionSistolica = leerEntero("Presión sistólica PAS (mmHg, 40-260): ");
                    int presionDiastolica = leerEntero("Presión diastólica PAD (mmHg, 20-160): ");
                    int frecuenciaCardiaca = leerEntero("Frecuencia cardíaca FC (lpm, 20-250): ");
                    int frecuenciaRespiratoria = leerEntero("Frecuencia respiratoria FR (rpm, 4-60): ");
                    double saturacionOxigeno = leerDouble("Saturación de oxígeno SpO2 (%, 50-100): ");
                    double temperatura = leerDouble("Temperatura corporal (°C, 30-43): ");
                    int dolorEva = leerEntero("Nivel de dolor EVA (1-10): ");

                    SignosVitales signosIngresados = new SignosVitales(
                            presionSistolica, presionDiastolica, frecuenciaCardiaca,
                            frecuenciaRespiratoria, saturacionOxigeno, temperatura, dolorEva
                    );

                    NivelManchester nivelResultado = triajeModulo.evaluarYClasificarTriaje(pacienteTriaje, signosIngresados);

                    if (nivelResultado == null) {
                        System.out.println("Error: Valores de signos vitales fuera del rango biológico permitido (o PAS <= PAD).");
                    } else {
                        System.out.println("Triaje completado. Nivel asignado: " + nivelResultado
                                + " (" + nivelResultado.getDescripcion() + "). Tiempo máximo: "
                                + nivelResultado.getTiempoMaximoMinutos() + " minutos.");
                    }
                }

                case 6 -> {
                    System.out.println("\n---- COLA DE PRIORIDAD DE TRIAJE ----");
                    if (Hospital.colaPrioridadTriaje.isEmpty()) {
                        System.out.println("No hay pacientes en cola de espera");
                    } else {
                        PriorityQueue<Paciente> copiaOrdenada = new PriorityQueue<>(Hospital.colaPrioridadTriaje);
                        int posicion = 1;
                        while (!copiaOrdenada.isEmpty()) {
                            System.out.println(posicion + ". " + copiaOrdenada.poll());
                            posicion++;
                        }
                    }
                }

                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");


            }
        } while (opcTriaje != 0);

    }

    private static void menuCuartos() {
        int opcion;

        do {
            System.out.println("\n--- MÓDULO 2: GESTIÓN DE CUARTOS ---");
            System.out.println("1. Registrar cuarto");
            System.out.println("2. Mostrar todos los cuartos");
            System.out.println("3. Buscar cuarto disponible");
            System.out.println("4. Asignar paciente a un cuarto");
            System.out.println("5. Iniciar desinfección");
            System.out.println("6. Confirmar limpieza y liberar cuarto");
            System.out.println("7. Eliminar cuarto");
            System.out.println("0. Volver al menú principal");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarCuarto();
                    break;
                case 2:
                    System.out.println("\n--- LISTA DE CUARTOS ---");
                    if (Hospital.cuartos.isEmpty()) {
                        System.out.println("No existen cuartos registrados.");
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
                    System.out.println("De vuelta al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarCuarto() {
        System.out.println("\n--- REGISTRAR CUARTO ---");
        String codigo = leerTexto("Código del cuarto: ");
        String tipoArea = leerTexto("Tipo de área (UCI, Box, Observación): ");
        String equipamiento = leerTexto("Equipamiento: ");
        int tiempo = leerEntero("Tiempo de desinfección en minutos: ");

        Cuarto cuarto = new Cuarto(codigo, tipoArea, equipamiento, tiempo);
        if (cuartoModulo.registrarCuarto(cuarto)) {
            System.out.println("Cuarto registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar. Verifique que el código no esté duplicado y el tiempo sea válido.");
        }
    }

    private static void buscarCuarto() {
        String tipoArea = leerTexto("Ingrese el tipo de área que desea buscar: ");

        Cuarto cuarto = cuartoModulo.buscarCuartoDisponible(tipoArea);
        if (cuarto != null) {
            System.out.println("Cuarto disponible encontrado:");
            System.out.println(cuarto);
        } else {
            System.out.println("No hay cuartos disponibles de ese tipo.");
        }
    }

    private static void asignarPaciente() {
        String codigo = leerTexto("Ingrese el código del cuarto: ");
        String cedula = leerTexto("Ingrese la cédula del paciente: ");

        Paciente paciente = triajeModulo.buscarPacientePorCedula(cedula);
        if (paciente == null || !paciente.isActivo()) {
            System.out.println("Paciente no encontrado o inactivo.");
            return;
        }

        if (cuartoModulo.asignarPacienteACuarto(codigo, paciente)) {
            System.out.println("Paciente asignado correctamente.");
        } else {
            System.out.println("No se pudo asignar. Verifique que el cuarto exista y esté disponible.");
        }
    }

    private static void iniciarDesinfeccion() {
        String codigo = leerTexto("Ingrese el código del cuarto: ");
        cuartoModulo.iniciarDesinfeccion(codigo);
        System.out.println("Solicitud de desinfección procesada. Consulte el estado del cuarto para verificar.");
    }

    private static void confirmarLimpieza() {
        String codigo = leerTexto("Ingrese el código del cuarto: ");
        String respuesta = leerTexto("¿El personal confirma que la limpieza está realizada? (s/n): ");
        boolean confirmacion = respuesta.equalsIgnoreCase("s");

        if (cuartoModulo.intentarLiberarCuarto(codigo, confirmacion)) {
            System.out.println("Cuarto liberado y disponible.");
        } else {
            System.out.println("No se pudo liberar el cuarto. Puede que falte tiempo de desinfección o confirmación.");
        }
    }

    private static void eliminarCuarto() {
        String codigo = leerTexto("Ingrese el código del cuarto que desea eliminar: ");
        if (cuartoModulo.eliminarCuarto(codigo)) {
            System.out.println("Cuarto eliminado correctamente.");
        } else {
            System.out.println("No se puede eliminar. Verifique el código y que el cuarto no esté ocupado.");
        }
    }

    private static void menuMedicos() {
        System.out.println("-> Submenú Médicos: Registrar Médico / Consultar / Asignar Automático");
    }

    private static void menuAtencion() {
        int subOpcion;
        do {
            System.out.println("\n--- SUBMENÚ ATENCIÓN MÉDICA Y RECETAS ---");
            System.out.println("1. Iniciar nueva atención médica");
            System.out.println("2. Consultar fichas clínicas / atenciones");
            System.out.println("3. Actualizar notas de evolución");
            System.out.println("4. Emitir receta médica (con validación de alergias)");
            System.out.println("5. Anular receta emitida por error");
            System.out.println("6. Cerrar atención y dar Alta Médica");
            System.out.println("0. Volver al menú principal");

            subOpcion = leerEntero("Seleccione una opción: ");

            switch (subOpcion) {
                case 1 -> {
                    String idAtencion = leerTexto("Ingrese ID de la atención (ej. AT-01): ");
                    String cedulaPac = leerTexto("Ingrese cédula del paciente: ");
                    modelo.Paciente paciente = atencionModulo.buscarPacientePorCedula(cedulaPac);

                    if (paciente == null) {
                        System.out.println("Error: No se encontró ningún paciente registrado con esa cédula.");
                        break;
                    }

                    modelo.PersonalMedico medico = atencionModulo.obtenerPrimerMedicoDisponible();
                    modelo.Cuarto cuarto = atencionModulo.obtenerPrimerCuartoDisponible();

                    if (medico == null || cuarto == null) {
                        System.out.println("Alerta: No hay médicos o cuartos disponibles en este momento.");
                        break;
                    }

                    atencionModulo.iniciarAtencion(idAtencion, paciente, medico, cuarto);
                }
                case 2 -> {
                    if (Hospital.atenciones.isEmpty()) {
                        System.out.println("No existen atenciones médicas registradas.");
                    } else {
                        for (modelo.AtencionMedica a : Hospital.atenciones) {
                            System.out.println("ID: " + a.getIdAtencion()
                                    + " | Paciente: " + a.getPaciente().getNombreCompleto()
                                    + " | Médico: " + a.getMedico().getNombreCompleto()
                                    + " | Estado: " + a.getEstadoAtencion()
                                    + " | Diagnóstico: " + (a.getDiagnostico() != null ? a.getDiagnostico() : "Pendiente"));
                        }
                    }
                }
                case 3 -> {
                    String idAtencion = leerTexto("Ingrese ID de la atención: ");
                    String notas = leerTexto("Ingrese las notas de evolución médica: ");
                    atencionModulo.actualizarNotasEvolucion(idAtencion, notas);
                }
                case 4 -> {
                    String idAtencion = leerTexto("Ingrese ID de la atención: ");
                    String idReceta = leerTexto("Ingrese ID de la receta (ej. REC-01): ");
                    String indicaciones = leerTexto("Ingrese indicaciones generales: ");

                    modelo.Receta nuevaReceta = new modelo.Receta(idReceta, indicaciones);

                    int cantidadMeds = leerEntero("¿Cuántos medicamentos desea agregar a la receta?: ");
                    for (int i = 1; i <= cantidadMeds; i++) {
                        System.out.println("Medicamento #" + i + ":");
                        String nombre = leerTexto("  Nombre comercial: ");
                        String principio = leerTexto("  Código principio activo: ");
                        String grupo = leerTexto("  Grupo farmacológico: ");
                        String dosis = leerTexto("  Dosis (ej. 500 mg): ");
                        String frecuencia = leerTexto("  Frecuencia (ej. Cada 8 horas): ");

                        modelo.Medicamento med = new modelo.Medicamento(nombre, principio, grupo, dosis, frecuencia);
                        nuevaReceta.agregarMedicamento(med);
                    }

                    atencionModulo.emitirReceta(idAtencion, nuevaReceta);
                }
                case 5 -> {
                    String idAtencion = leerTexto("Ingrese ID de la atención cuya receta desea anular: ");
                    atencionModulo.anularReceta(idAtencion);
                }
                case 6 -> {
                    String idAtencion = leerTexto("Ingrese ID de la atención a cerrar: ");
                    String diagnostico = leerTexto("Ingrese el diagnóstico final obligatorio: ");
                    atencionModulo.cerrarAtencionConAlta(idAtencion, diagnostico, null);
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (subOpcion != 0);
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

    // --- Validaciones para el ingreso de datos ---

    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.print("Dato inválido. Ingrese un número entero: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.print("Dato inválido. Ingrese un valor numérico: ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        while (entrada.isEmpty()) {
            System.out.print("El campo no puede estar vacío. Intente de nuevo: ");
            entrada = scanner.nextLine().trim();
        }
        return entrada;
    }
}