package negocio.modulo;

import modelo.Cuarto;
import modelo.Paciente;

public class CuartoModulo {

    // --- RF2.1: Gestión CRUD de Cuartos ---
    public boolean registrarCuarto(Cuarto nuevoCuarto) {
        // (Módulo 2): Validar código único y agregar a HospitalDataStore.cuartos.
        return false;
    }

    public Cuarto buscarCuartoDisponible(String tipoArea) {
        // (Módulo 2): Retornar el primer cuarto con estado DISPONIBLE del tipo solicitado.
        return null;
    }

    public boolean eliminarCuarto(String codigo) {
        // (Módulo 2): Eliminar solo si estado != EstadoCuarto.OCUPADO.
        return false;
    }

    // --- RF2.2: Control de Estados y Doble Validación de Desinfección ---
    public boolean asignarPacienteACuarto(String codigoCuarto, Paciente paciente) {
        // (Módulo 2): Verificar EstadoCuarto.DISPONIBLE, asignar paciente y cambiar a OCUPADO.
        return false;
    }

    public void iniciarDesinfeccion(String codigoCuarto) {
        // (Módulo 2): Liberar paciente, cambiar a EN_DESINFECCION, guardar LocalDateTime.now()
        // y poner limpiezaConfirmada = false.
    }

    public boolean intentarLiberarCuarto(String codigoCuarto, boolean confirmacionManualPersonal) {
        // (Módulo 2): Verificar doble condición:
        // 1. Minutos transcurridos >= cuarto.getTiempoDesinfeccionMinutos()
        // 2. confirmacionManualPersonal == true
        // Si ambas se cumplen, pasar a EstadoCuarto.DISPONIBLE.
        return false;
    }
}
