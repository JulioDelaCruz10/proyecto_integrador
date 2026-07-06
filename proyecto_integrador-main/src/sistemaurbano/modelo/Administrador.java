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

    // muestra por consola todos los accidentes reportados (tipo "Choque / Accidente")
    public void generarReporteAccidentes(ArrayList<Alerta> alertas) {
        System.out.println("\n--- ACCIDENTES REPORTADOS ---");
        int contador = 0;
        for (Alerta a : alertas) {
            if (a.getTipo() != null && a.getTipo().equalsIgnoreCase("Choque / Accidente")) {
                contador++;
                System.out.println("[" + a.getCodigoAlerta() + "] Tramo: " + a.getOrigen() + " -> " + a.getDestino());
                System.out.println("Fecha/Hora: " + a.getFechaHoraReporte());
                System.out.println("Detalle: " + a.getMensaje());
                if (a instanceof AlertaRetraso) {
                    System.out.println("Retraso asociado: " + ((AlertaRetraso) a).getTiempoRetrasoMinutos() + " min");
                }
                System.out.println("-----------------------------------");
            }
        }
        if (contador == 0) {
            System.out.println("No hay accidentes reportados por el momento.");
        }
    }

    // muestra por consola las rutas guardadas de cada usuario
    public void generarReporteRutasPorUsuario(ArrayList<Usuario> listaUsuarios) {
        System.out.println("\n--- RUTAS GUARDADAS POR USUARIO ---");
        for (Usuario u : listaUsuarios) {
            System.out.println("Usuario: " + u.getNombre() + " (" + u.getEmail() + ")");
            if (u.getRutasFrecuentes().isEmpty()) {
                System.out.println("   Sin rutas guardadas.");
            } else {
                for (Ruta r : u.getRutasFrecuentes()) {
                    System.out.println("   - " + r.getNombreRuta() + ": " + r.getOrigen() + " -> " + r.getDestino()
                            + " (registrada: " + r.getFechaRegistro() + ")");
                }
            }
        }
        System.out.println("-----------------------------------");
    }

    // muestra por consola un conteo de alertas agrupadas por tipo de novedad
    public void generarReporteAlertasPorTipo(ArrayList<Alerta> alertas) {
        System.out.println("\n--- ALERTAS POR TIPO DE NOVEDAD ---");
        java.util.LinkedHashMap<String, Integer> conteo = new java.util.LinkedHashMap<>();
        for (Alerta a : alertas) {
            String tipo = (a.getTipo() != null) ? a.getTipo() : "Otro";
            conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
        }
        if (conteo.isEmpty()) {
            System.out.println("Aun no se han registrado alertas.");
        } else {
            for (String tipo : conteo.keySet()) {
                System.out.println(tipo + ": " + conteo.get(tipo));
            }
        }
        System.out.println("-----------------------------------");
    }

    // muestra por consola los usuarios que aun no registran ningun viaje
    public void generarReporteUsuariosInactivos(ArrayList<Usuario> listaUsuarios) {
        System.out.println("\n--- USUARIOS SIN VIAJES REGISTRADOS ---");
        int contador = 0;
        for (Usuario u : listaUsuarios) {
            if (u.getHistorialViajes().isEmpty()) {
                contador++;
                System.out.println("- " + u.getNombre() + " (" + u.getEmail() + ")");
            }
        }
        if (contador == 0) {
            System.out.println("Todos los usuarios tienen al menos un viaje registrado.");
        }
        System.out.println("-----------------------------------");
    }

    public String getCodigoAdministrativo() {
        return codigoAdministrativo;
    }
}