package modelo;

public class Medicamento {
    private String nombreComercial;
    private String codigoPrincipioActivo;
    private String grupoFarmacologico;
    private String dosis;
    private String frecuencia;

    public Medicamento(String nombreComercial, String codigoPrincipioActivo, String grupoFarmacologico, String dosis, String frecuencia) {
        this.nombreComercial = nombreComercial;
        this.codigoPrincipioActivo = codigoPrincipioActivo;
        this.grupoFarmacologico = grupoFarmacologico;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
    }

    public String getNombreComercial() { return nombreComercial; }
    public String getCodigoPrincipioActivo() { return codigoPrincipioActivo; }
    public String getGrupoFarmacologico() { return grupoFarmacologico; }
    public String getDosis() { return dosis; }
    public String getFrecuencia() { return frecuencia; }

    @Override
    public String toString() {
        return nombreComercial + " [" + codigoPrincipioActivo + "] - " + dosis + " cada " + frecuencia;
    }
}