package negocio.modulo;

import modelo.Paciente;
import modelo.PersonalMedico;

public class MedicoModulo {

    // --- RF3.1: Gestión CRUD de Personal Médico ---
    public boolean registrarMedico(PersonalMedico medico) {
        // (Carlos): Validar cédula única y agregar a HospitalDataStore.medicos.
        return false;
    }

    public PersonalMedico buscarMedicoPorCedula(String cedula) {
        // (Carlos): Buscar profesional en HospitalDataStore.medicos.
        return null;
    }

    public boolean inactivarMedico(String cedula) {
        // (Carlos): Cambiar activo = false y estado = EstadoMedico.FUERA_TURNO.
        return false;
    }

    // --- RF3.2: Asignación Inteligente y Exclusión Mutua ---
    public synchronized PersonalMedico asignarMedicoAutomatico(Paciente paciente) {
        // (Carlos):
        // 1. Filtrar médicos activos con estado DISPONIBLE.
        // 2. Validar ventana de fin de turno (< 20 min restantes: excluir Verde/Azul/Amarillo).
        // 3. Usar polimorfismo: medico.puedeAtenderUrgencia(paciente.getNivelManchester()).
        // 4. Cambiar estado a EstadoMedico.EN_ATENCION y retornar el médico.
        return null;
    }
}