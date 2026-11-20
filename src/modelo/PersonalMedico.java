package modelo;

import modelo.enums.EstadoMedico;
import modelo.enums.NivelManchester;
import java.time.LocalTime;

public class PersonalMedico {
    protected String cedula;
    protected String nombreCompleto;
    protected LocalTime horaInicioTurno;
    protected LocalTime horaFinTurno;
    protected EstadoMedico estado;
    protected boolean activo;

    public PersonalMedico(String cedula, String nombreCompleto, LocalTime horaInicioTurno, LocalTime horaFinTurno) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.horaInicioTurno = horaInicioTurno;
        this.horaFinTurno = horaFinTurno;
        this.estado = EstadoMedico.DISPONIBLE;
        this.activo = true;
    }

    // Métodos normales que las clases hijas sobrescriben con @Override
    public String getEspecialidad() {
        return "Medicina General";
    }

    public boolean puedeAtenderUrgencia(NivelManchester nivel) {
        return true;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public LocalTime getHoraInicioTurno() { return horaInicioTurno; }
    public void setHoraInicioTurno(LocalTime horaInicioTurno) { this.horaInicioTurno = horaInicioTurno; }
    public LocalTime getHoraFinTurno() { return horaFinTurno; }
    public void setHoraFinTurno(LocalTime horaFinTurno) { this.horaFinTurno = horaFinTurno; }
    public EstadoMedico getEstado() { return estado; }
    public void setEstado(EstadoMedico estado) { this.estado = estado; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("Dr(a). %s | Especialidad: %s | Turno: %s-%s | Estado: %s",
                nombreCompleto, getEspecialidad(), horaInicioTurno, horaFinTurno, estado);
    }
}