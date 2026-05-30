package sistemaurbano.modelo;

public class Ruta {
    private String nombreRuta;
    private String origen;
    private String destino;

    public Ruta(String nombreRuta, String origen, String destino) {
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
    }

    public String getNombreRuta() { return nombreRuta; }
    public void setNombreRuta(String nombreRuta) { this.nombreRuta = nombreRuta; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
}