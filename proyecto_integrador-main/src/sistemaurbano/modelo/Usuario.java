package sistemaurbano.modelo;
import java.util.ArrayList;

public class Usuario extends Cuenta {
    private String nombre;
    private ArrayList<Ruta> rutasFrecuentes;
    private ArrayList<Viaje> historialViajes;

    public Usuario(String nombre, String email, String contrasenia) {
        super(email, contrasenia);
        this.nombre = nombre;
        this.rutasFrecuentes = new ArrayList<>();
        this.historialViajes = new ArrayList<>();
    }

    @Override
    public String recuperarContrasenia() {
        return "Tu contraseña actual es: " + getContrasenia();
    }

    public void registrarRuta(Ruta nuevaRuta) {
        this.rutasFrecuentes.add(nuevaRuta);
    }

    public void registrarViaje(Viaje nuevoViaje) {
        this.historialViajes.add(nuevoViaje);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Ruta> getRutasFrecuentes() {
        return rutasFrecuentes;
    }

    public ArrayList<Viaje> getHistorialViajes() {
        return historialViajes;
    }
}