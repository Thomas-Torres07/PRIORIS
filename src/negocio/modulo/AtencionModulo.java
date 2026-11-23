package negocio.modulo;

import modelo.*;
import modelo.enums.EstadoCuarto;
import modelo.enums.EstadoMedico;
import negocio.Hospital;
import negocio.Validador;

import java.time.LocalDateTime;
import java.util.List;

public class AtencionModulo {

    // 1. Iniciar atención médica (vincula paciente, médico y cuarto)
    public AtencionMedica iniciarAtencion(String id, Paciente p, PersonalMedico m, Cuarto c) {
        if (!Validador.esTextoValido(id) || p == null || m == null || c == null) {
            System.out.println("Error: Datos incompletos para iniciar la atención médica.");
            return null;
        }
        if (!p.isActivo()) {
            System.out.println("Error: El paciente seleccionado se encuentra inactivo.");
            return null;
        }
        for (AtencionMedica a : Hospital.atenciones) {
            if (a.getPaciente() != null
                    && a.getPaciente().getCedula().equals(p.getCedula())
                    && "EN_CURSO".equalsIgnoreCase(a.getEstadoAtencion())) {
                System.out.println("Error: El paciente ya tiene una atención médica EN_CURSO.");
                return null;
            }
        }
        if (buscarAtencionPorId(id) != null) {
            System.out.println("Error: Ya existe una atención registrada con el ID " + id + ".");
            return null;
        }
        if (!m.isActivo() || m.getEstado() != EstadoMedico.DISPONIBLE) {
            System.out.println("Error: El médico seleccionado no se encuentra activo o DISPONIBLE.");
            return null;
        }
        if (c.getEstado() != EstadoCuarto.DISPONIBLE) {
            System.out.println("Error: El cuarto seleccionado no se encuentra DISPONIBLE.");
            return null;
        }

        // Cambiar estados operativos y retirar paciente de la cola de espera
        m.setEstado(EstadoMedico.EN_ATENCION);
        c.setEstado(EstadoCuarto.OCUPADO);
        c.setPacienteAsignado(p);
        Hospital.colaPrioridadTriaje.remove(p);

        AtencionMedica nuevaAtencion = new AtencionMedica(
                id.trim(),
                p,
                m,
                c
        );

        Hospital.atenciones.add(nuevaAtencion);
        System.out.println("Atención [" + id + "] iniciada. Paciente: " + p.getNombreCompleto()
                + " | Médico: Dr. " + m.getNombreCompleto() + " | Cuarto: " + c.getCodigo());
        return nuevaAtencion;
    }

    // 2. Validar contraindicaciones de receta contra las alergias del paciente
    public boolean validarContraindicaciones(Paciente p, Receta r) {
        if (p == null || r == null || r.getMedicamentos() == null) {
            return false;
        }

        List<Alergia> alergiasPaciente = p.getAlergias();
        if (alergiasPaciente == null || alergiasPaciente.isEmpty()) {
            return true;
        }

        for (Medicamento med : r.getMedicamentos()) {
            if (med == null) {
                continue;
            }
            for (Alergia alergia : alergiasPaciente) {
                if (alergia == null) {
                    continue;
                }
                boolean coincideNombre = Validador.esTextoValido(med.getNombreComercial())
                        && Validador.esTextoValido(alergia.getNombreSustancia())
                        && med.getNombreComercial().equalsIgnoreCase(alergia.getNombreSustancia());

                boolean coincidePrincipio = Validador.esTextoValido(med.getCodigoPrincipioActivo())
                        && Validador.esTextoValido(alergia.getCodigoPrincipioActivo())
                        && med.getCodigoPrincipioActivo().equalsIgnoreCase(alergia.getCodigoPrincipioActivo());

                boolean coincideGrupo = Validador.esTextoValido(med.getGrupoFarmacologico())
                        && Validador.esTextoValido(alergia.getGrupoFarmacologico())
                        && med.getGrupoFarmacologico().equalsIgnoreCase(alergia.getGrupoFarmacologico());

                if (coincideNombre || coincidePrincipio || coincideGrupo) {
                    System.out.println("ALERTA CRÍTICA: Prescripción bloqueada. El paciente es alérgico a "
                            + med.getNombreComercial() + " (Sustancia: " + alergia.getNombreSustancia() + ").");
                    return false;
                }
            }
        }
        return true;
    }

    // 3. Emitir y adjuntar receta a una atención activa (previa validación de alergias)
    public boolean emitirReceta(String idAtencion, Receta receta) {
        AtencionMedica atencion = buscarAtencionPorId(idAtencion);
        if (atencion == null) {
            System.out.println("Error: No se encontró ninguna atención con ID " + idAtencion + ".");
            return false;
        }
        if (!"EN_CURSO".equalsIgnoreCase(atencion.getEstadoAtencion())) {
            System.out.println("Error: No se puede emitir una receta en una atención ya finalizada.");
            return false;
        }
        if (receta == null || receta.getMedicamentos() == null || receta.getMedicamentos().isEmpty()) {
            System.out.println("Error: La receta debe contener al menos un medicamento.");
            return false;
        }

        if (!validarContraindicaciones(atencion.getPaciente(), receta)) {
            return false;
        }

        atencion.setReceta(receta);
        System.out.println("Validación de seguridad exitosa. Prescripción generada y adjuntada a la ficha del paciente.");
        return true;
    }

    // 4. Consultar atención médica por ID
    public AtencionMedica buscarAtencionPorId(String idAtencion) {
        if (!Validador.esTextoValido(idAtencion)) {
            return null;
        }
        for (AtencionMedica a : Hospital.atenciones) {
            if (a.getIdAtencion().equalsIgnoreCase(idAtencion.trim())) {
                return a;
            }
        }
        return null;
    }

    // 5. Actualizar notas médicas de evolución durante la consulta
    public boolean actualizarNotasEvolucion(String idAtencion, String nuevasNotas) {
        AtencionMedica atencion = buscarAtencionPorId(idAtencion);
        if (atencion == null) {
            System.out.println("Error: No existe una atención con ID " + idAtencion + ".");
            return false;
        }
        if (!"EN_CURSO".equalsIgnoreCase(atencion.getEstadoAtencion())) {
            System.out.println("Error: La atención ya está cerrada y no admite modificaciones.");
            return false;
        }
        if (!Validador.esTextoValido(nuevasNotas)) {
            System.out.println("Error: Las notas de evolución no pueden estar vacías.");
            return false;
        }

        atencion.setNotasEvolucion(nuevasNotas.trim());
        System.out.println("Notas de evolución actualizadas correctamente.");
        return true;
    }

    // 6. Anular receta emitida por error antes de cerrar el alta
    public boolean anularReceta(String idAtencion) {
        AtencionMedica atencion = buscarAtencionPorId(idAtencion);
        if (atencion == null || atencion.getReceta() == null) {
            System.out.println("Error: No existe una receta activa asociada a esa atención.");
            return false;
        }
        if (!"EN_CURSO".equalsIgnoreCase(atencion.getEstadoAtencion())) {
            System.out.println("Error: No se puede anular la receta de una atención ya cerrada.");
            return false;
        }

        atencion.getReceta().setActiva(false);
        atencion.setReceta(null);
        System.out.println("Receta anulada correctamente antes del cierre de alta.");
        return true;
    }

    // 7. Cerrar atención médica con alta y liberar recursos (médico y cuarto)
    public boolean cerrarAtencionConAlta(String id, String diagnostico, Receta r) {
        AtencionMedica atencion = buscarAtencionPorId(id);
        if (atencion == null) {
            System.out.println("Error: No se encontró la atención médica con ID " + id + ".");
            return false;
        }
        if (!"EN_CURSO".equalsIgnoreCase(atencion.getEstadoAtencion())) {
            System.out.println("Error: Esta atención médica ya fue cerrada anteriormente.");
            return false;
        }
        if (!Validador.esTextoValido(diagnostico)) {
            System.out.println("Error: No se puede cerrar la atención médica sin ingresar un diagnóstico.");
            return false;
        }

        if (r != null) {
            if (!validarContraindicaciones(atencion.getPaciente(), r)) {
                return false;
            }
            atencion.setReceta(r);
        }

        atencion.setDiagnostico(diagnostico.trim());
        atencion.setFechaFin(LocalDateTime.now());
        atencion.setEstadoAtencion("ALTA_MEDICA");

        // Liberar médico a DISPONIBLE
        if (atencion.getMedico() != null) {
            atencion.getMedico().setEstado(EstadoMedico.DISPONIBLE);
        }

        // Pasar cuarto a EN_DESINFECCION
        if (atencion.getCuarto() != null) {
            atencion.getCuarto().setPacienteAsignado(null);
            atencion.getCuarto().setEstado(EstadoCuarto.EN_DESINFECCION);
            atencion.getCuarto().setInicioDesinfeccion(LocalDateTime.now());
        }

        System.out.println("Diagnóstico guardado. Atención cerrada exitosamente con Alta Médica.");
        return true;
    }

    // --- Métodos auxiliares para buscar entidades en Hospital ---
    public Paciente buscarPacientePorCedula(String cedula) {
        if (!Validador.esTextoValido(cedula)) return null;
        for (Paciente p : Hospital.pacientes) {
            if (p.getCedula().equals(cedula.trim()) && p.isActivo()) {
                return p;
            }
        }
        return null;
    }

    public PersonalMedico obtenerPrimerMedicoDisponible() {
        for (PersonalMedico m : Hospital.medicos) {
            if (m.isActivo() && m.getEstado() == EstadoMedico.DISPONIBLE) {
                return m;
            }
        }
        return null;
    }

    public Cuarto obtenerPrimerCuartoDisponible() {
        for (Cuarto c : Hospital.cuartos) {
            if (c.getEstado() == EstadoCuarto.DISPONIBLE) {
                return c;
            }
        }
        return null;
    }
}