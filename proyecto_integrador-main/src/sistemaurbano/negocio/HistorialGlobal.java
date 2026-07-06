package sistemaurbano.negocio;

import sistemaurbano.modelo.Alerta;
import sistemaurbano.modelo.Viaje;
import sistemaurbano.modelo.Usuario;
import java.util.ArrayList;

public class HistorialGlobal {
    // lista global unica para todos los puntos
    private static ArrayList<String> puntosHistoricos = new ArrayList<>();
    private static ArrayList<Alerta> alertas = new ArrayList<>();

    // datos iniciales para la demo
    static {
        puntosHistoricos.add("Centro Historico");
        puntosHistoricos.add("Norte");
        puntosHistoricos.add("Sur");
        puntosHistoricos.add("Valle");
        puntosHistoricos.add("Cumbaya");
        puntosHistoricos.add("La Carolina");
    }

    // devuelve todos los puntos historicos
    public static ArrayList<String> getPuntosHistoricos() {
        return puntosHistoricos;
    }

    // agrega un punto al historial si no existe
    public static void agregarPunto(String punto) {
        // busca si ya esta en la lista
        boolean existe = false;
        for (String p : puntosHistoricos) {
            if (p.equalsIgnoreCase(punto)) {
                existe = true;
                break;
            }
        }
        // si no existe lo guarda
        if (!existe) {
            puntosHistoricos.add(punto);
        }
    }

    // guarda una nueva alerta en la lista global
    public static void registrarAlerta(Alerta alerta) {
        alertas.add(alerta);
    }

    // devuelve todas las alertas registradas globalmente (incluye accidentes, retrasos, etc.)
    public static ArrayList<Alerta> getAlertas() {
        return alertas;
    }

    // busca alertas asociadas a un tramo especifico
    public static ArrayList<Alerta> obtenerAlertasParaRuta(String origen, String destino) {
        ArrayList<Alerta> encontradas = new ArrayList<>();
        // revisa cada alerta global
        for (Alerta a : alertas) {
            if (a.getOrigen().equalsIgnoreCase(origen) && a.getDestino().equalsIgnoreCase(destino)) {
                encontradas.add(a);
            }
        }
        return encontradas;
    }

    // busca viajes de todos los usuarios que tengan el mismo origen y destino
    public static ArrayList<Viaje> buscarViajesPorOrigenDestino(ArrayList<Usuario> usuarios, String origen, String destino) {
        ArrayList<Viaje> coincidentes = new ArrayList<>();
        // recorre todos los usuarios
        for (Usuario u : usuarios) {
            // recorre el historial de cada usuario
            for (Viaje v : u.getHistorialViajes()) {
                // si el origen y destino coinciden lo agrega
                if (v.getRutaAsignada().getOrigen().equalsIgnoreCase(origen) &&
                    v.getRutaAsignada().getDestino().equalsIgnoreCase(destino)) {
                    coincidentes.add(v);
                }
            }
        }
        return coincidentes;
    }
}
