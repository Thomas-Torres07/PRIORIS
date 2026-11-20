package modelo;

import java.time.LocalDateTime;

public class AtencionMedica {
    private String idAtencion;
    private Paciente paciente;
    private PersonalMedico medico;
    private Cuarto cuarto;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String diagnostico;
    private String notasEvolucion;
    private Receta receta;
    private String estadoAtencion; // "EN_CURSO", "ALTA_MEDICA", "DERIVADO"

    public AtencionMedica(String idAtencion, Paciente paciente, PersonalMedico medico, Cuarto cuarto) {
        this.idAtencion = idAtencion;
        this.paciente = paciente;
        this.medico = medico;
        this.cuarto = cuarto;
        this.fechaInicio = LocalDateTime.now();
        this.estadoAtencion = "EN_CURSO";
    }

    // Getters y Setters
    public String getIdAtencion() { return idAtencion; }
    public Paciente getPaciente() { return paciente; }
    public PersonalMedico getMedico() { return medico; }
    public Cuarto getCuarto() { return cuarto; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getNotasEvolucion() { return notasEvolucion; }
    public void setNotasEvolucion(String notasEvolucion) { this.notasEvolucion = notasEvolucion; }
    public Receta getReceta() { return receta; }
    public void setReceta(Receta receta) { this.receta = receta; }
    public String getEstadoAtencion() { return estadoAtencion; }
    public void setEstadoAtencion(String estadoAtencion) { this.estadoAtencion = estadoAtencion; }
}