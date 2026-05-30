package sistemaurbano.modelo;
import java.util.ArrayList;

public class Administrador extends Cuenta {
    private String codigoAdministrativo;

    public Administrador(String email, String contrasenia, String codigoAdministrativo) {
        super(email, contrasenia);
        this.codigoAdministrativo = codigoAdministrativo;
    }

    @Override
    public String recuperarContrasenia() {
        return "Por políticas de seguridad, contacte al departamento de TI para restablecer credenciales de administrador.";
    }

    public void generarReporteGeneral(ArrayList<Usuario> listaUsuarios) {
        int totalRutas = 0;
        int totalViajes = 0;

        for (int i = 0; i < listaUsuarios.size(); i++) {
            totalRutas += listaUsuarios.get(i).getRutasFrecuentes().size();
            totalViajes += listaUsuarios.get(i).getHistorialViajes().size();
        }

        System.out.println("\n--- REPORTE GENERAL DEL SISTEMA ---");
        System.out.println("Total de usuarios registrados: " + listaUsuarios.size());
        System.out.println("Total de rutas globales: " + totalRutas);
        System.out.println("Total de viajes registrados: " + totalViajes);
        System.out.println("-----------------------------------");
    }

    public void generarReporteIndividual(Usuario usuario) {
        System.out.println("\n--- REPORTE INDIVIDUAL ---");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Cantidad de rutas guardadas: " + usuario.getRutasFrecuentes().size());
        System.out.println("Cantidad de viajes realizados: " + usuario.getHistorialViajes().size());
        System.out.println("--------------------------");
    }

    public String getCodigoAdministrativo() {
        return codigoAdministrativo;
    }
}