package negocio.modulo;

import modelo.*;

public class AtencionModulo {

    private final CuartoModulo cuartoService = new CuartoModulo();

    // --- RF5.1: Gestión CRUD de Atención Médica y Recetas ---
    public AtencionMedica iniciarAtencion(String idAtencion, Paciente paciente, PersonalMedico medico, Cuarto cuarto) {
        // (Andrés): Crear AtencionMedica, vincular recursos y guardar en HospitalDataStore.atenciones.
        return null;
    }

    public boolean cerrarAtencionConAlta(String idAtencion, String diagnostico, Receta receta) {
        // (Andrés):
        // 1. Validar que diagnóstico no esté vacío.
        // 2. Si hay receta, ejecutar validarContraindicaciones(paciente, receta).
        // 3. Marcar estadoAtencion = "ALTA_MEDICA", liberar médico a DISPONIBLE
        //    y llamar a cuartoService.iniciarDesinfeccion(cuarto.getCodigo()).
        return false;
    }

    // --- RF5.2: Validación de Seguridad en Recetas (Cruce de Alergias) ---
    public boolean validarContraindicaciones(Paciente paciente, Receta receta) {
        // (Andrés): Comparar cada Medicamento de la receta contra paciente.getAlergias():
        // - Si coincide codigoPrincipioActivo o grupoFarmacologico -> BLOQUEAR (retornar false).
        // - Si solo hay similitud en nombre -> mostrar advertencia en consola.
        return true;
    }
}
