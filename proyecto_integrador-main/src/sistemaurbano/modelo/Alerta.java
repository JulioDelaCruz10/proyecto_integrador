package sistemaurbano.modelo;

import java.time.LocalDateTime;

public class Alerta {
    private String codigoAlerta;
    private String mensaje;
    private String origen; // punto de inicio
    private String destino; // punto de llegada
    private String tipo; // tipo de novedad: Choque/Accidente, Transito pesado, Trabajos en la via, Otro
    private LocalDateTime fechaHoraReporte;

    public Alerta(String codigoAlerta, String mensaje, String origen, String destino) {
        this(codigoAlerta, mensaje, origen, destino, "Otro");
    }

    public Alerta(String codigoAlerta, String mensaje, String origen, String destino, String tipo) {
        this.codigoAlerta = codigoAlerta;
        this.mensaje = mensaje;
        this.origen = origen; // guarda el origen
        this.destino = destino; // guarda el destino
        this.tipo = tipo;
        this.fechaHoraReporte = LocalDateTime.now();
    }

    public void mostrarInfo() {
        // enseña los datos por pantalla
        System.out.println("[" + codigoAlerta + "] Notificación General: " + mensaje);
        System.out.println("Tramo: " + origen + " -> " + destino);
    }

    public String getCodigoAlerta() { return codigoAlerta; }
    public String getMensaje() { return mensaje; }
    public String getOrigen() { return origen; } // da el origen
    public String getDestino() { return destino; } // da el destino
    public String getTipo() { return tipo; }
    public LocalDateTime getFechaHoraReporte() { return fechaHoraReporte; }
}