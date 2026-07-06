package sistemaurbano.modelo;

public class AlertaRetraso extends Alerta {
    private int tiempoRetrasoMinutos;

    public AlertaRetraso(String codigoAlerta, String mensaje, String origen, String destino, int tiempoRetrasoMinutos) {
        // llama al constructor de la clase padre
        super(codigoAlerta, mensaje, origen, destino);
        this.tiempoRetrasoMinutos = tiempoRetrasoMinutos;
    }

    public AlertaRetraso(String codigoAlerta, String mensaje, String origen, String destino, int tiempoRetrasoMinutos, String tipo) {
        // llama al constructor de la clase padre incluyendo el tipo
        super(codigoAlerta, mensaje, origen, destino, tipo);
        this.tiempoRetrasoMinutos = tiempoRetrasoMinutos;
    }

    @Override
    public void mostrarInfo() {
        // muestra los datos de la alerta de retraso
        System.out.println("\n!!! ALERTA DE TRÁFICO / RETRASO !!!");
        System.out.println("Código: " + getCodigoAlerta());
        System.out.println("Tramo: " + getOrigen() + " -> " + getDestino());
        System.out.println("Detalle: " + getMensaje());
        System.out.println("Tiempo estimado de retraso: " + tiempoRetrasoMinutos + " minutos.");
        System.out.println("-----------------------------------");
    }

    public int getTiempoRetrasoMinutos() { return tiempoRetrasoMinutos; }
}