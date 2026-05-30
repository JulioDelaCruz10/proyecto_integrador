package sistemaurbano.modelo;

public class Alerta {
    private String codigoAlerta;
    private String mensaje;

    public Alerta(String codigoAlerta, String mensaje) {
        this.codigoAlerta = codigoAlerta;
        this.mensaje = mensaje;
    }

    public void mostrarInfo() {
        System.out.println("[" + codigoAlerta + "] Notificación General: " + mensaje);
    }

    public String getCodigoAlerta() { return codigoAlerta; }
    public String getMensaje() { return mensaje; }
}