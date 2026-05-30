package sistemaurbano.modelo;

public class AlertaRetraso extends Alerta {
    private int tiempoRetrasoMinutos;

    public AlertaRetraso(String codigoAlerta, String mensaje, int tiempoRetrasoMinutos) {
        super(codigoAlerta, mensaje);
        this.tiempoRetrasoMinutos = tiempoRetrasoMinutos;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("\n!!! ALERTA DE TRÁFICO / RETRASO !!!");
        System.out.println("Código: " + getCodigoAlerta());
        System.out.println("Detalle: " + getMensaje());
        System.out.println("Tiempo estimado de retraso: " + tiempoRetrasoMinutos + " minutos.");
        System.out.println("-----------------------------------");
    }

    public int getTiempoRetrasoMinutos() { return tiempoRetrasoMinutos; }
}