package modelo;

import modelo.enums.NivelManchester;
import java.time.LocalTime;

public class MedicoGeneral extends PersonalMedico {

    public MedicoGeneral(String cedula, String nombreCompleto, LocalTime horaInicioTurno, LocalTime horaFinTurno) {
        super(cedula, nombreCompleto, horaInicioTurno, horaFinTurno);
    }

    @Override
    public String getEspecialidad() {
        return "Medicina General";
    }

    @Override
    public boolean puedeAtenderUrgencia(NivelManchester nivel) {
        return nivel == NivelManchester.AMARILLO || nivel == NivelManchester.VERDE || nivel == NivelManchester.AZUL;
    }
}
