package sistemaurbano.modelo;

import java.time.LocalDate;

public class Ruta {
    private String nombreRuta;
    private String origen;
    private String destino;
    private LocalDate fechaRegistro;

    public Ruta(String nombreRuta, String origen, String destino) {
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
        this.fechaRegistro = LocalDate.now();
    }

    public String getNombreRuta() { return nombreRuta; }
    public void setNombreRuta(String nombreRuta) { this.nombreRuta = nombreRuta; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
}