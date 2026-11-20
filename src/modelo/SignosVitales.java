package modelo;

public class SignosVitales {
    private int presionSistolica;
    private int presionDiastolica;
    private int frecuenciaCardiaca;
    private int frecuenciaRespiratoria;
    private double saturacionOxigeno;
    private double temperatura;
    private int nivelDolorEva; // Escala 1 a 10

    public SignosVitales(int presionSistolica, int presionDiastolica, int frecuenciaCardiaca,
                         int frecuenciaRespiratoria, double saturacionOxigeno, double temperatura, int nivelDolorEva) {
        this.presionSistolica = presionSistolica;
        this.presionDiastolica = presionDiastolica;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
        this.saturacionOxigeno = saturacionOxigeno;
        this.temperatura = temperatura;
        this.nivelDolorEva = nivelDolorEva;
    }

    public int getPresionSistolica() { return presionSistolica; }
    public int getPresionDiastolica() { return presionDiastolica; }
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public int getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public double getSaturacionOxigeno() { return saturacionOxigeno; }
    public double getTemperatura() { return temperatura; }
    public int getNivelDolorEva() { return nivelDolorEva; }
}