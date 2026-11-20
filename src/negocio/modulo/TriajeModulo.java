package negocio.modulo;

import modelo.Paciente;
import modelo.SignosVitales;
import modelo.enums.NivelManchester;

public class TriajeModulo {

    // --- RF1.1: Gestión CRUD de Pacientes ---
    public boolean registrarPaciente(Paciente nuevoPaciente) {
        // (Joaquín): Validar que no exista la cédula en HospitalDataStore.pacientes y agregarlo.
        return false;
    }

    public Paciente buscarPacientePorCedula(String cedula) {
        // (Joaquín): Buscar y retornar el paciente activo por cédula.
        return null;
    }

    public boolean actualizarPaciente(String cedula, String nuevosAntecedentes) {
        // (Joaquín): Actualizar datos del paciente si existe.
        return false;
    }

    public boolean darDeBajaPaciente(String cedula) {
        // (Joaquín): Marcar paciente.setActivo(false) y remover de colaPrioridadTriaje.
        return false;
    }

    // --- RF1.2: Triaje Automático Manchester (Regla de Negocio) ---
    public NivelManchester evaluarYClasificarTriaje(Paciente paciente, SignosVitales signos) {
        // (Joaquín): Aplicar las reglas clínicas del documento (SpO2, FC, PAS, Temp, EVA),
        // asignar paciente.setSignosVitales(signos), paciente.setNivelManchester(nivel)
        // y encolar en HospitalDataStore.colaPrioridadTriaje.
        return NivelManchester.VERDE;
    }
}