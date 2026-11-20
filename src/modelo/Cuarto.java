package modelo;

import modelo.enums.EstadoCuarto;
import java.time.LocalDateTime;

public class Cuarto {
    private String codigo;
    private String tipoArea;
    private String equipamiento;
    private EstadoCuarto estado;
    private int tiempoDesinfeccionMinutos;
    private LocalDateTime inicioDesinfeccion;
    private boolean limpiezaConfirmada;
    private Paciente pacienteAsignado;

    public Cuarto(String codigo, String tipoArea, String equipamiento, int tiempoDesinfeccionMinutos) {
        this.codigo = codigo;
        this.tipoArea = tipoArea;
        this.equipamiento = equipamiento;
        this.tiempoDesinfeccionMinutos = tiempoDesinfeccionMinutos;
        this.estado = EstadoCuarto.DISPONIBLE;
        this.limpiezaConfirmada = true;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public String getTipoArea() { return tipoArea; }
    public void setTipoArea(String tipoArea) { this.tipoArea = tipoArea; }
    public String getEquipamiento() { return equipamiento; }
    public void setEquipamiento(String equipamiento) { this.equipamiento = equipamiento; }
    public EstadoCuarto getEstado() { return estado; }
    public void setEstado(EstadoCuarto estado) { this.estado = estado; }
    public int getTiempoDesinfeccionMinutos() { return tiempoDesinfeccionMinutos; }
    public void setTiempoDesinfeccionMinutos(int tiempoDesinfeccionMinutos) { this.tiempoDesinfeccionMinutos = tiempoDesinfeccionMinutos; }
    public LocalDateTime getInicioDesinfeccion() { return inicioDesinfeccion; }
    public void setInicioDesinfeccion(LocalDateTime inicioDesinfeccion) { this.inicioDesinfeccion = inicioDesinfeccion; }
    public boolean isLimpiezaConfirmada() { return limpiezaConfirmada; }
    public void setLimpiezaConfirmada(boolean limpiezaConfirmada) { this.limpiezaConfirmada = limpiezaConfirmada; }
    public Paciente getPacienteAsignado() { return pacienteAsignado; }
    public void setPacienteAsignado(Paciente pacienteAsignado) { this.pacienteAsignado = pacienteAsignado; }

    @Override
    public String toString() {
        return String.format("Cuarto %s (%s) - Estado: %s", codigo, tipoArea, estado);
    }
}