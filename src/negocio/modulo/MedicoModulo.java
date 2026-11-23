package negocio.modulo;

import modelo.MedicoEspecialista;
import modelo.Paciente;
import modelo.PersonalMedico;
import modelo.enums.EstadoMedico;
import modelo.enums.NivelManchester;
import negocio.Hospital;
import negocio.Validador;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoModulo {

    public List<PersonalMedico> listarMedicos() {
        return new ArrayList<>(Hospital.medicos);
    }

    public boolean registrarMedico(PersonalMedico medico) {
        if (medico == null
                || !Validador.esCedulaValida(medico.getCedula())
                || !Validador.esTextoValido(medico.getNombreCompleto())
                || medico.getHoraInicioTurno() == null
                || medico.getHoraFinTurno() == null
                || !medico.getHoraInicioTurno().isBefore(medico.getHoraFinTurno())) {
            System.out.println("Error: Datos del médico inválidos o horario inconsistente.");
            return false;
        }

        if (medico instanceof MedicoEspecialista especialista
                && !Validador.esTextoValido(especialista.getEspecialidad())) {
            System.out.println("Error: La especialidad del médico no puede estar vacía.");
            return false;
        }

        String cedulaCorregida = medico.getCedula().trim();

        for (PersonalMedico m : Hospital.medicos) {
            if (m.getCedula().trim().equals(cedulaCorregida)) {
                System.out.println("Error: Ya existe un médico registrado con la cédula " + cedulaCorregida + ".");
                return false;
            }
        }

        Hospital.medicos.add(medico);
        return true;
    }

    public PersonalMedico buscarMedicoPorCedula(String cedula) {
        if (!Validador.esCedulaValida(cedula)) {
            return null;
        }

        String cedulaCorregida = cedula.trim();

        for (PersonalMedico m : Hospital.medicos) {
            if (cedulaCorregida.equals(m.getCedula().trim())) {
                return m;
            }
        }
        return null;
    }

    public boolean actualizarMedico(String cedula, String nuevoNombre, LocalTime nuevoInicio, LocalTime nuevoFin) {
        PersonalMedico medico = buscarMedicoPorCedula(cedula);

        if (medico == null || !medico.isActivo() || medico.getEstado() == EstadoMedico.EN_ATENCION) {
            return false;
        }
        if (!Validador.esTextoValido(nuevoNombre)) {
            return false;
        }
        if (nuevoInicio == null || nuevoFin == null || !nuevoInicio.isBefore(nuevoFin)) {
            return false;
        }

        medico.setNombreCompleto(nuevoNombre.trim());
        medico.setHoraInicioTurno(nuevoInicio);
        medico.setHoraFinTurno(nuevoFin);
        return true;
    }

    public boolean inactivarMedico(String cedula) {
        PersonalMedico medico = buscarMedicoPorCedula(cedula);

        if (medico == null || medico.getEstado() == EstadoMedico.EN_ATENCION) {
            return false;
        }

        if (medico.isActivo() && medico.getEstado() == EstadoMedico.DISPONIBLE) {
            medico.setActivo(false);
            medico.setEstado(EstadoMedico.FUERA_TURNO);
            return true;
        }

        return false;
    }

    public synchronized PersonalMedico asignarMedicoAutomatico(Paciente paciente) {
        if (paciente == null || !paciente.isActivo() || paciente.getNivelManchester() == null) {
            return null;
        }

        LocalTime horaActual = LocalTime.now();

        for (PersonalMedico medico : Hospital.medicos) {
            if (medico.isActivo() && medico.getEstado() == EstadoMedico.DISPONIBLE) {

                if (horaActual.isBefore(medico.getHoraInicioTurno()) || !horaActual.isBefore(medico.getHoraFinTurno())) {
                    continue;
                }

                long minutosRestantes = Duration.between(horaActual, medico.getHoraFinTurno()).toMinutes();

                if (minutosRestantes < 20) {
                    if (paciente.getNivelManchester() == NivelManchester.VERDE
                            || paciente.getNivelManchester() == NivelManchester.AZUL
                            || paciente.getNivelManchester() == NivelManchester.AMARILLO) {
                        continue;
                    }
                }

                if (!medico.puedeAtenderUrgencia(paciente.getNivelManchester())) {
                    continue;
                }

                medico.setEstado(EstadoMedico.EN_ATENCION);
                return medico;
            }
        }
        return null;
    }
}