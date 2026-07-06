package sistemaurbano.modelo;

public class Viaje {
    private Ruta rutaAsignada;
    private String horaInicio;
    private String horaFin;
    private int duracionRealMinutos;

    public Viaje(Ruta rutaAsignada, String horaInicio, String horaFin, int duracionRealMinutos) {
        this.rutaAsignada = rutaAsignada;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.duracionRealMinutos = duracionRealMinutos;
    }

    public Ruta getRutaAsignada() { return rutaAsignada; }
    public void setRutaAsignada(Ruta rutaAsignada) { this.rutaAsignada = rutaAsignada; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public int getDuracionRealMinutos() { return duracionRealMinutos; }
    public void setDuracionRealMinutos(int duracionRealMinutos) { this.duracionRealMinutos = duracionRealMinutos; }
}