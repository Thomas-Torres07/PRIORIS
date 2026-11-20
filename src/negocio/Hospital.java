package negocio;

import modelo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Hospital {

    public static final List<Paciente> pacientes = new ArrayList<>();
    public static final PriorityQueue<Paciente> colaPrioridadTriaje = new PriorityQueue<>();
    public static final List<Cuarto> cuartos = new ArrayList<>();
    public static final List<PersonalMedico> medicos = new ArrayList<>();
    public static final List<AtencionMedica> atenciones = new ArrayList<>();
}