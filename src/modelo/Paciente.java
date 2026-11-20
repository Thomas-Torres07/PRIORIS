package modelo;

import modelo.enums.NivelManchester;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Paciente implements Comparable<Paciente> {
    private String cedula;
    private String nombreCompleto;
    private int edad;
    private String tipoSangre;
    private String antecedentes;
    private List<Alergia> alergias;
    private SignosVitales signosVitales;
    private NivelManchester nivelManchester;
    private LocalDateTime horaIngreso;
    private boolean activo;

    public Paciente(String cedula, String nombreCompleto, int edad, String tipoSangre, String antecedentes) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.tipoSangre = tipoSangre;
        this.antecedentes = antecedentes;
        this.alergias = new ArrayList<>();
        this.horaIngreso = LocalDateTime.now();
        this.activo = true;
    }

    //Fórmula documentada: P(t) = W_base + (t_espera / T_max * 25)
    public double calcularPrioridadDinamica() {
        if (nivelManchester == null) return 0.0;
        long minutosEspera = Duration.between(horaIngreso, LocalDateTime.now()).toMinutes();
        int tMax = nivelManchester.getTiempoMaximoMinutos();

        if (tMax == 0) {
            return nivelManchester.getPesoBase() + minutosEspera;
        }
        return nivelManchester.getPesoBase() + ((double) minutosEspera / tMax) * 25.0;
    }

    @Override
    public int compareTo(Paciente otro) {
        // Orden descendente: mayor puntaje va primero en la PriorityQueue
        return Double.compare(otro.calcularPrioridadDinamica(), this.calcularPrioridadDinamica());
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getTipoSangre() { return tipoSangre; }
    public String getAntecedentes() { return antecedentes; }
    public void setAntecedentes(String antecedentes) { this.antecedentes = antecedentes; }
    public List<Alergia> getAlergias() { return alergias; }
    public void agregarAlergia(Alergia alergia) { this.alergias.add(alergia); }
    public SignosVitales getSignosVitales() { return signosVitales; }
    public void setSignosVitales(SignosVitales signosVitales) { this.signosVitales = signosVitales; }
    public NivelManchester getNivelManchester() { return nivelManchester; }
    public void setNivelManchester(NivelManchester nivelManchester) { this.nivelManchester = nivelManchester; }
    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("[%s] %s (CI: %s) - Triaje: %s | Prioridad: %.2f",
                activo ? "ACTIVO" : "INACTIVO", nombreCompleto, cedula,
                nivelManchester != null ? nivelManchester : "SIN TRIAJE", calcularPrioridadDinamica());
    }
}