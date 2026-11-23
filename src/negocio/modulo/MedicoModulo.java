package negocio.modulo;

import modelo.Paciente;
import modelo.PersonalMedico;
import modelo.enums.EstadoMedico;
import modelo.enums.NivelManchester;
import negocio.Hospital;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class MedicoModulo {

    public List<PersonalMedico> listarMedicos() {

        List<PersonalMedico> copia = new ArrayList<>(Hospital.medicos);

        return copia;
    }

    public boolean registrarMedico(PersonalMedico medico) {

        if (medico == null || medico.getCedula() == null || medico.getCedula().isBlank() ||
                medico.getNombreCompleto() == null || medico.getNombreCompleto().isBlank()){
            return false;
        }

        String cedulaCorregida = medico.getCedula().trim();

        for (PersonalMedico m: Hospital.medicos){
            if (m.getCedula().trim().equals(cedulaCorregida)){
                return false;
            }
        }

        Hospital.medicos.add(medico);

        return true;
    }

    public PersonalMedico buscarMedicoPorCedula(String cedula) {

        if (cedula == null || cedula.isBlank()) {
            return null;
        }

        String cedulaCorregida = cedula.trim();

        for (PersonalMedico m: Hospital.medicos){
            if (cedulaCorregida.equals(m.getCedula().trim())){
                return m;
            }
        }
        return null;
    }


    public boolean actualizarMedico(String cedula, String nuevoNombre, LocalTime nuevoInicio, LocalTime nuevoFin) {

        PersonalMedico medico = buscarMedicoPorCedula(cedula);

        if (medico == null){
            return false;
        }
        if (nuevoNombre == null || nuevoNombre.isBlank()){
            return false;
        }
        if (nuevoInicio == null || nuevoFin == null){
            return false;
        }

        if (!nuevoInicio.isBefore(nuevoFin)){
            return false;
        }
        medico.setNombreCompleto(nuevoNombre.trim());
        medico.setHoraInicioTurno(nuevoInicio);
        medico.setHoraFinTurno(nuevoFin);
        return true;
    }


    public boolean inactivarMedico(String cedula) {

        PersonalMedico medico = buscarMedicoPorCedula(cedula);

        if (medico == null || medico.getEstado() == EstadoMedico.EN_ATENCION){
            return false;
        }

        if (medico.isActivo() && medico.getEstado() == EstadoMedico.DISPONIBLE){
            medico.setActivo(false);
            medico.setEstado(EstadoMedico.FUERA_TURNO);
            return true;
        }

        return false;
    }

    public synchronized PersonalMedico asignarMedicoAutomatico(Paciente paciente) {

        if (paciente == null || paciente.getNivelManchester() == null){
            return null;
        }

        LocalTime horaActual = LocalTime.now();

        for (PersonalMedico medico: Hospital.medicos){
            if (medico.isActivo() && medico.getEstado() == EstadoMedico.DISPONIBLE){

                long minutosRestantes = Duration.between(horaActual, medico.getHoraFinTurno()).toMinutes();

                if (horaActual.isBefore(medico.getHoraInicioTurno()) || !horaActual.isBefore(medico.getHoraFinTurno())){
                    continue;
                }

                if (minutosRestantes < 20){
                    if (paciente.getNivelManchester() == NivelManchester.VERDE ||
                            paciente.getNivelManchester() == NivelManchester.AZUL ||
                    paciente.getNivelManchester() == NivelManchester.AMARILLO){
                        continue;
                    }
                }

                if (!medico.puedeAtenderUrgencia(paciente.getNivelManchester())){
                    continue;
                }

                medico.setEstado(EstadoMedico.EN_ATENCION);
                return medico;

            }
        }
        return null;
    }
}