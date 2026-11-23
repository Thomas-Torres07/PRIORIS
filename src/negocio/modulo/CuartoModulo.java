package negocio.modulo;

import modelo.AtencionMedica;
import modelo.Cuarto;
import modelo.Paciente;
import modelo.enums.EstadoCuarto;
import negocio.Hospital;
import negocio.Validador;

import java.time.Duration;
import java.time.LocalDateTime;

public class CuartoModulo {

    public boolean esTipoAreaValido(String tipoArea) {
        if (!Validador.esTextoValido(tipoArea)) {
            return false;
        }
        String limpio = tipoArea.trim().toUpperCase();
        return limpio.equals("UCI")
                || limpio.equals("BOX")
                || limpio.equals("OBSERVACIÓN")
                || limpio.equals("OBSERVACION");
    }

    // --- RF2.1: Gestión CRUD de Cuartos ---
    public boolean registrarCuarto(Cuarto nuevoCuarto) {
        if (nuevoCuarto == null ||
                !Validador.esTextoValido(nuevoCuarto.getCodigo()) ||
                !Validador.esTextoValido(nuevoCuarto.getTipoArea()) ||
                !Validador.esTextoValido(nuevoCuarto.getEquipamiento())) {
            System.out.println("Error: Ningún campo del cuarto puede estar vacío.");
            return false;
        }
        if (!esTipoAreaValido(nuevoCuarto.getTipoArea())) {
            System.out.println("Error: Tipo de área inválido. Debe ser UCI, Box u Observación.");
            return false;
        }
        if (!Validador.esPositivo(nuevoCuarto.getTiempoDesinfeccionMinutos())) {
            System.out.println("Error: El tiempo de desinfección debe ser mayor a 0 minutos.");
            return false;
        }

        String codigoLimpio = nuevoCuarto.getCodigo().trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoLimpio)) {
                return false;
            }
        }

        Hospital.cuartos.add(nuevoCuarto);
        return true;
    }

    public Cuarto buscarCuartoDisponible(String tipoArea) {
        if (!esTipoAreaValido(tipoArea)) {
            return null;
        }

        String areaLimpia = tipoArea.trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getEstado() == EstadoCuarto.DISPONIBLE && cuarto.getTipoArea().equalsIgnoreCase(areaLimpia)) {
                return cuarto;
            }
        }
        return null;
    }

    public boolean eliminarCuarto(String codigo) {
        if (!Validador.esTextoValido(codigo)) {
            return false;
        }
        String codigoLimpio = codigo.trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoLimpio) && cuarto.getEstado() != EstadoCuarto.OCUPADO) {
                Hospital.cuartos.remove(cuarto);
                return true;
            }
        }
        return false;
    }

    // --- RF2.2: Control de Estados y Doble Validación de Desinfección ---
    public boolean asignarPacienteACuarto(String codigoCuarto, Paciente paciente) {
        if (!Validador.esTextoValido(codigoCuarto) || paciente == null || !paciente.isActivo()) {
            return false;
        }

        for (Cuarto c : Hospital.cuartos) {
            if (c.getPacienteAsignado() != null
                    && c.getPacienteAsignado().getCedula().equals(paciente.getCedula())) {
                System.out.println("Error: El paciente ya se encuentra asignado al cuarto " + c.getCodigo() + ".");
                return false;
            }
        }

        String codigoLimpio = codigoCuarto.trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoLimpio) && cuarto.getEstado() == EstadoCuarto.DISPONIBLE) {
                cuarto.setPacienteAsignado(paciente);
                cuarto.setEstado(EstadoCuarto.OCUPADO);
                return true;
            }
        }
        return false;
    }

    public void iniciarDesinfeccion(String codigoCuarto) {
        if (!Validador.esTextoValido(codigoCuarto)) {
            System.out.println("Error: El código del cuarto no puede estar vacío.");
            return;
        }

        String codigoLimpio = codigoCuarto.trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoLimpio)) {
                if (cuarto.getEstado() != EstadoCuarto.OCUPADO) {
                    System.out.println("Error: Solo se puede iniciar desinfección en un cuarto OCUPADO.");
                    return;
                }
                if (cuarto.getPacienteAsignado() != null) {
                    for (AtencionMedica a : Hospital.atenciones) {
                        if (a.getPaciente() != null
                                && a.getPaciente().getCedula().equals(cuarto.getPacienteAsignado().getCedula())
                                && "EN_CURSO".equalsIgnoreCase(a.getEstadoAtencion())) {
                            System.out.println("Error: El paciente en este cuarto tiene una atención médica EN_CURSO. Debe cerrarse con Alta Médica primero.");
                            return;
                        }
                    }
                }
                cuarto.setPacienteAsignado(null);
                cuarto.setEstado(EstadoCuarto.EN_DESINFECCION);
                cuarto.setInicioDesinfeccion(LocalDateTime.now());
                cuarto.setLimpiezaConfirmada(false);
                System.out.println("Desinfección iniciada para el cuarto " + cuarto.getCodigo() + ".");
                return;
            }
        }
        System.out.println("Error: No se encontró un cuarto con el código " + codigoLimpio + ".");
    }

    public boolean intentarLiberarCuarto(String codigoCuarto, boolean confirmacionManualPersonal) {
        if (!Validador.esTextoValido(codigoCuarto) || !confirmacionManualPersonal) {
            return false;
        }

        String codigoLimpio = codigoCuarto.trim();
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoLimpio)) {
                if (cuarto.getEstado() != EstadoCuarto.EN_DESINFECCION) {
                    System.out.println("El cuarto no está en desinfección.");
                    return false;
                }

                if (cuarto.getInicioDesinfeccion() == null) {
                    System.out.println("No se ha registrado el inicio de la desinfección.");
                    return false;
                }

                long minutosTranscurridos = Duration.between(
                        cuarto.getInicioDesinfeccion(),
                        LocalDateTime.now()
                ).toMinutes();

                System.out.println("Minutos transcurridos: " + minutosTranscurridos);
                System.out.println("Minutos necesarios: " + cuarto.getTiempoDesinfeccionMinutos());

                if (minutosTranscurridos >= cuarto.getTiempoDesinfeccionMinutos()) {
                    cuarto.setEstado(EstadoCuarto.DISPONIBLE);
                    cuarto.setLimpiezaConfirmada(true);
                    cuarto.setInicioDesinfeccion(null);
                    return true;
                } else {
                    System.out.println("Todavía no termina el tiempo de desinfección.");
                    return false;
                }
            }
        }
        System.out.println("No se encontró el cuarto.");
        return false;
    }
}