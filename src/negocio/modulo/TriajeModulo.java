package negocio.modulo;

import modelo.Alergia;
import modelo.Paciente;
import modelo.SignosVitales;
import modelo.enums.NivelManchester;
import negocio.Hospital;
import negocio.Validador;

import java.util.HashSet;
import java.util.Set;

public class TriajeModulo {

    // Validación de tipos de sangre
    public boolean esTipoSangreValido(String tipoSangre) {
        if (!Validador.esTextoValido(tipoSangre)) {
            return false;
        }
        String tipo = tipoSangre.trim().toUpperCase();
        return tipo.equals("A+") || tipo.equals("A-")
                || tipo.equals("B+") || tipo.equals("B-")
                || tipo.equals("AB+") || tipo.equals("AB-")
                || tipo.equals("O+") || tipo.equals("O-");
    }

    // --- RF1.1: Gestión CRUD de Pacientes ---
    public boolean registrarPaciente(Paciente nuevoPaciente) {
        if (nuevoPaciente == null) {
            return false;
        }

        if (!Validador.esCedulaValida(nuevoPaciente.getCedula())
                || !Validador.esTextoValido(nuevoPaciente.getNombreCompleto())
                || !Validador.estaEnRango(nuevoPaciente.getEdad(), 0, 120)
                || !esTipoSangreValido(nuevoPaciente.getTipoSangre())) {
            return false;
        }

        for (Paciente pacienteExistente : Hospital.pacientes) {
            if (pacienteExistente.getCedula().equals(nuevoPaciente.getCedula())) {
                return false;
            }
        }

        // Validar que las alergias registradas no tengan campos vacíos ni principios activos duplicados
        if (nuevoPaciente.getAlergias() != null && !nuevoPaciente.getAlergias().isEmpty()) {
            Set<String> principiosRegistrados = new HashSet<>();
            for (Alergia alergia : nuevoPaciente.getAlergias()) {
                if (alergia == null
                        || !Validador.esTextoValido(alergia.getNombreSustancia())
                        || !Validador.esTextoValido(alergia.getCodigoPrincipioActivo())
                        || !Validador.esTextoValido(alergia.getGrupoFarmacologico())) {
                    return false;
                }
                String codigoNormalizado = alergia.getCodigoPrincipioActivo().trim().toUpperCase();
                if (!principiosRegistrados.add(codigoNormalizado)) {
                    return false;
                }
            }
        }

        if (!Validador.esTextoValido(nuevoPaciente.getAntecedentes())) {
            nuevoPaciente.setAntecedentes("Ninguno");
        }

        Hospital.pacientes.add(nuevoPaciente);
        return true;
    }

    public Paciente buscarPacientePorCedula(String cedula) {
        if (Hospital.pacientes.isEmpty() || !Validador.esCedulaValida(cedula)) {
            return null;
        }

        String cedulaLimpia = cedula.trim();
        for (Paciente pacienteActual : Hospital.pacientes) {
            if (pacienteActual.getCedula().equals(cedulaLimpia) && pacienteActual.isActivo()) {
                return pacienteActual;
            }
        }

        return null;
    }

    public boolean actualizarPaciente(String cedula, String nuevosAntecedentes) {
        if (Hospital.pacientes.isEmpty() || !Validador.esTextoValido(nuevosAntecedentes)) {
            return false;
        }

        Paciente pacienteActualizar = buscarPacientePorCedula(cedula);
        if (pacienteActualizar == null) {
            return false;
        }

        pacienteActualizar.setAntecedentes(nuevosAntecedentes.trim());
        return true;
    }

    public boolean darDeBajaPaciente(String cedula) {
        if (Hospital.pacientes.isEmpty()) {
            return false;
        }

        Paciente pacienteBaja = buscarPacientePorCedula(cedula);

        if (pacienteBaja == null) {
            return false;
        }

        pacienteBaja.setActivo(false);
        Hospital.colaPrioridadTriaje.remove(pacienteBaja);
        return true;
    }

    // Validación de signos vitales dentro de rangos biológicos
    public boolean sonSignosValidos(SignosVitales signos) {
        if (signos == null) {
            return false;
        }

        return Validador.estaEnRango(signos.getSaturacionOxigeno(), 50.0, 100.0) &&
                Validador.estaEnRango(signos.getTemperatura(), 30.0, 43.0) &&
                Validador.estaEnRango(signos.getFrecuenciaCardiaca(), 20, 250) &&
                Validador.estaEnRango(signos.getFrecuenciaRespiratoria(), 4, 60) &&
                Validador.estaEnRango(signos.getPresionSistolica(), 40, 260) &&
                Validador.estaEnRango(signos.getPresionDiastolica(), 20, 160) &&
                signos.getPresionSistolica() > signos.getPresionDiastolica() &&
                Validador.estaEnRango(signos.getNivelDolorEva(), 1, 10);
    }

    // --- RF1.2: Triaje Automático Manchester (Regla de Negocio) ---
    public NivelManchester evaluarYClasificarTriaje(Paciente paciente, SignosVitales signos) {
        if (Hospital.pacientes.isEmpty() || paciente == null || !sonSignosValidos(signos) || !paciente.isActivo()) {
            return null;
        }

        double spO2 = signos.getSaturacionOxigeno();
        double temp = signos.getTemperatura();
        int fC = signos.getFrecuenciaCardiaca();
        int fR = signos.getFrecuenciaRespiratoria();
        int paS = signos.getPresionSistolica();
        int paD = signos.getPresionDiastolica();
        int eva = signos.getNivelDolorEva();

        NivelManchester nivelAsignado;

        if (spO2 < 90.0 || temp < 34.0 || temp > 41.0 || fC <= 40 || fC >= 140 || fR <= 8 || fR >= 30
                || paS <= 80 || paS >= 220 || paD <= 40 || paD >= 130) {
            nivelAsignado = NivelManchester.ROJO;

        } else if (spO2 <= 91.0 || temp <= 35.0 || temp >= 39.5 || fC <= 50 || fC >= 131 || fR <= 10 || fR >= 25
                || paS <= 90 || paS >= 180 || paD <= 50 || paD >= 120 || eva >= 8) {
            nivelAsignado = NivelManchester.NARANJA;

        } else if (spO2 <= 93.0 || temp >= 38.5 || fC >= 111 || fR >= 21
                || paS <= 100 || paS >= 150 || paD >= 100 || eva >= 5) {
            nivelAsignado = NivelManchester.AMARILLO;

        } else if (spO2 <= 95.0 || temp >= 37.5 || temp <= 36.0 || fC >= 91
                || paS <= 110 || paD >= 90 || eva >= 3) {
            nivelAsignado = NivelManchester.VERDE;

        } else {
            nivelAsignado = NivelManchester.AZUL;
        }

        paciente.setSignosVitales(signos);
        paciente.setNivelManchester(nivelAsignado);

        Hospital.colaPrioridadTriaje.remove(paciente);
        Hospital.colaPrioridadTriaje.add(paciente);

        return nivelAsignado;
    }
}