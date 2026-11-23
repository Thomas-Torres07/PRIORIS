package negocio.modulo;

import modelo.Cuarto;
import modelo.Paciente;
import modelo.enums.EstadoCuarto;
import negocio.Hospital;
import negocio.Validador;
import java.time.Duration;
import java.time.LocalDateTime;


public class CuartoModulo {

    // --- RF2.1: Gestión CRUD de Cuartos ---
    public boolean registrarCuarto(Cuarto nuevoCuarto) {
        if (nuevoCuarto == null ||
                !Validador.esTextoValido(nuevoCuarto.getCodigo()) ||
                !Validador.esTextoValido(nuevoCuarto.getTipoArea()) ||
                !Validador.esTextoValido(nuevoCuarto.getEquipamiento())) {
            System.out.println("Error: Ningún campo del cuarto puede estar vacío.");
            return false;
        }
        if (!Validador.esPositivo(nuevoCuarto.getTiempoDesinfeccionMinutos())) {
            System.out.println("Error: El tiempo de desinfección debe ser mayor a 0 minutos.");
            return false;
        }
        if (nuevoCuarto == null || nuevoCuarto.getCodigo() == null || nuevoCuarto.getCodigo().trim().isEmpty()) {
            return false;
        }

        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(nuevoCuarto.getCodigo())) {
                return false;
            }
        }

        Hospital.cuartos.add(nuevoCuarto);
        return true;
    }

    public Cuarto buscarCuartoDisponible(String tipoArea) {
        if (tipoArea == null) {
            return null;
        }

        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getEstado() == EstadoCuarto.DISPONIBLE && cuarto.getTipoArea().equalsIgnoreCase(tipoArea)) {
                return cuarto;
            }
        }
        return null;
    }

    public boolean eliminarCuarto(String codigo) {
        if (!Validador.esTextoValido(codigo)) return false;
        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigo) && cuarto.getEstado() != EstadoCuarto.OCUPADO) {
                Hospital.cuartos.remove(cuarto);
                return true;
            }
        }
        return false;
    }

    // --- RF2.2: Control de Estados y Doble Validación de Desinfección ---
    public boolean asignarPacienteACuarto(String codigoCuarto, Paciente paciente) {
        if (!Validador.esTextoValido(codigoCuarto)) return false;
        if (paciente == null || codigoCuarto == null) {
            return false;
        }

        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoCuarto) && cuarto.getEstado() == EstadoCuarto.DISPONIBLE) {
                cuarto.setPacienteAsignado(paciente);
                cuarto.setEstado(EstadoCuarto.OCUPADO);
                return true;
            }
        }
        return false;
    }

    public void iniciarDesinfeccion(String codigoCuarto) {
        if (codigoCuarto == null) {
            return;
        }

        for (Cuarto cuarto : Hospital.cuartos) {
            if (cuarto.getCodigo().equalsIgnoreCase(codigoCuarto) && cuarto.getEstado() == EstadoCuarto.OCUPADO) {
                cuarto.setPacienteAsignado(null);
                cuarto.setEstado(EstadoCuarto.EN_DESINFECCION);
                cuarto.setInicioDesinfeccion(LocalDateTime.now());
                cuarto.setLimpiezaConfirmada(false);
                return;
            }
        }
    }

    public boolean intentarLiberarCuarto(String codigoCuarto, boolean confirmacionManualPersonal) {

        if (codigoCuarto == null || !confirmacionManualPersonal) {
            return false;
        }

        for (Cuarto cuarto : Hospital.cuartos) {

            if (cuarto.getCodigo().equalsIgnoreCase(codigoCuarto)) {

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


