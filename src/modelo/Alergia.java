package modelo;

public class Alergia {
    private String nombreSustancia;
    private String codigoPrincipioActivo;
    private String grupoFarmacologico;

    public Alergia(String nombreSustancia, String codigoPrincipioActivo, String grupoFarmacologico) {
        this.nombreSustancia = nombreSustancia;
        this.codigoPrincipioActivo = codigoPrincipioActivo;
        this.grupoFarmacologico = grupoFarmacologico;
    }

    public String getNombreSustancia() { return nombreSustancia; }
    public String getCodigoPrincipioActivo() { return codigoPrincipioActivo; }
    public String getGrupoFarmacologico() { return grupoFarmacologico; }

    @Override
    public String toString() {
        return nombreSustancia + " (" + grupoFarmacologico + ")";
    }
}