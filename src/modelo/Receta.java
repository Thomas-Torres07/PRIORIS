package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Receta {
    private String idReceta;
    private LocalDateTime fechaEmision;
    private List<Medicamento> medicamentos;
    private String indicacionesGenerales;
    private boolean activa;

    public Receta(String idReceta, String indicacionesGenerales) {
        this.idReceta = idReceta;
        this.fechaEmision = LocalDateTime.now();
        this.medicamentos = new ArrayList<>();
        this.indicacionesGenerales = indicacionesGenerales;
        this.activa = true;
    }

    public String getIdReceta() { return idReceta; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public void agregarMedicamento(Medicamento medicamento) { this.medicamentos.add(medicamento); }
    public String getIndicacionesGenerales() { return indicacionesGenerales; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}