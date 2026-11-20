package modelo.enums;

public enum NivelManchester {
    ROJO(0, 100.0, "Atención inmediata"),
    NARANJA(10, 75.0, "Muy urgente"),
    AMARILLO(60, 50.0, "Urgente"),
    VERDE(120, 25.0, "Urgencia menor"),
    AZUL(240, 10.0, "No urgente");

    private final int tiempoMaximoMinutos;
    private final double pesoBase;
    private final String descripcion;

    NivelManchester(int tiempoMaximoMinutos, double pesoBase, String descripcion) {
        this.tiempoMaximoMinutos = tiempoMaximoMinutos;
        this.pesoBase = pesoBase;
        this.descripcion = descripcion;
    }

    public int getTiempoMaximoMinutos() {
        return tiempoMaximoMinutos;
    }

    public double getPesoBase() {
        return pesoBase;
    }

    public String getDescripcion() {
        return descripcion;
    }
}