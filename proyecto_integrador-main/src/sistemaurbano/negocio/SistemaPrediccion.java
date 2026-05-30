package sistemaurbano.negocio;
import sistemaurbano.modelo.Viaje;
import java.util.ArrayList;

public class SistemaPrediccion {

    public int calcularTiempoPromedio(ArrayList<Viaje> todosLosViajes, String nombreRuta) {
        int sumaTiempos = 0;
        int contador = 0;

        for (int i = 0; i < todosLosViajes.size(); i++) {
            Viaje v = todosLosViajes.get(i);

            if (v.getRutaAsignada().getNombreRuta().equalsIgnoreCase(nombreRuta)) {
                sumaTiempos += v.getDuracionRealMinutos();
                contador++;
            }
        }

        if (contador == 0) {
            return 0;
        }

        double promedioDouble = (double) sumaTiempos / contador;
        return (int) Math.round(promedioDouble);
    }

    public String predecirMejorHorario(ArrayList<Viaje> todosLosViajes, String nombreRuta) {
        int[] sumatoriaTiempos = new int[24];
        int[] contadorViajes = new int[24];

        // Agrupar los viajes por hora de inicio
        for (int i = 0; i < todosLosViajes.size(); i++) {
            Viaje v = todosLosViajes.get(i);
            if (v.getRutaAsignada().getNombreRuta().equalsIgnoreCase(nombreRuta)) {
                String[] partesInicio = v.getHoraInicio().split(":");
                int hora = Integer.parseInt(partesInicio[0]);

                sumatoriaTiempos[hora] += v.getDuracionRealMinutos();
                contadorViajes[hora]++;
            }
        }

        // Verificar cantidad de horarios distintos
        int horasDiferentes = 0;
        for (int i = 0; i < 24; i++) {
            if (contadorViajes[i] > 0) {
                horasDiferentes++;
            }
        }

        if (horasDiferentes < 2) {
            return "No hay datos suficientes para comparar horarios en esta ruta.";
        }

        // Calcular la mejor y peor hora
        int minPromedio = Integer.MAX_VALUE;
        int mejorHora = -1;
        int maxPromedio = -1;
        int peorHora = -1;

        for (int i = 0; i < 24; i++) {
            if (contadorViajes[i] > 0) {
                int promedio = sumatoriaTiempos[i] / contadorViajes[i];

                if (promedio < minPromedio) {
                    minPromedio = promedio;
                    mejorHora = i;
                }
                if (promedio > maxPromedio) {
                    maxPromedio = promedio;
                    peorHora = i;
                }
            }
        }

        // Formatear las horas agregando el cero a la izquierda si es necesario
        String horaMejorStr = (mejorHora < 10 ? "0" + mejorHora : mejorHora) + ":00";
        String horaPeorStr = (peorHora < 10 ? "0" + peorHora : peorHora) + ":00";

        return "Hora recomendada: " + horaMejorStr + " (" + minPromedio + " min). Hora con mas trafico: " + horaPeorStr + " (" + maxPromedio + " min).";
    }
}