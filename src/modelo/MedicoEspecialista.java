package modelo;

import modelo.enums.NivelManchester;
import java.time.LocalTime;

public class MedicoEspecialista extends PersonalMedico {
    private String especialidad;

    public MedicoEspecialista(String cedula, String nombreCompleto, String especialidad,
                              LocalTime horaInicioTurno, LocalTime horaFinTurno) {
        super(cedula, nombreCompleto, horaInicioTurno, horaFinTurno);
        this.especialidad = especialidad;
    }

    @Override
    public String getEspecialidad() {
        return especialidad;
    }

    @Override
    public boolean puedeAtenderUrgencia(NivelManchester nivel) {
        return true;
    }
}
