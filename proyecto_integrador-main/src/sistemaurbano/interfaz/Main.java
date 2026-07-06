package sistemaurbano.interfaz;

import sistemaurbano.modelo.*;
import sistemaurbano.negocio.SistemaPrediccion;
import sistemaurbano.negocio.HistorialGlobal;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SistemaPrediccion motorPrediccion = new SistemaPrediccion();

        Administrador admin = new Administrador("admin@sistema.com", "admin123", "ADM-001");

        boolean sistemaActivo = true;

        while (sistemaActivo) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("  SISTEMA DE GESTIÓN DE TRANSPORTE URBANO - QUITO ");
            System.out.println("--------------------------------------------------");
            System.out.println("1. Iniciar Sesion");
            System.out.println("2. Registrar Usuario");
            System.out.println("3. Recuperar Contrasena");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");

            String opcionPrincipal = scanner.nextLine();

            switch (opcionPrincipal) {
                case "1":
                    System.out.println("\n--- INICIO DE SESION ---");
                    System.out.print("Ingrese su email: ");
                    String emailLogin = scanner.nextLine();
                    System.out.print("Ingrese su contrasena: ");
                    String passLogin = scanner.nextLine();

                    if (admin.validarLogin(emailLogin, passLogin)) {
                        System.out.println("\nAcceso concedido. Bienvenido, Administrador.");
                        menuAdministrador(scanner, admin, listaUsuarios);
                        break;
                    }

                    boolean usuarioEncontrado = false;
                    for (int i = 0; i < listaUsuarios.size(); i++) {
                        Usuario u = listaUsuarios.get(i);
                        if (u.validarLogin(emailLogin, passLogin)) {
                            usuarioEncontrado = true;
                            System.out.println("\nAcceso concedido. Bienvenido, " + u.getNombre() + ".");
                            menuUsuario(scanner, u, motorPrediccion, listaUsuarios);
                            break;
                        }
                    }

                    if (!usuarioEncontrado) {
                        System.out.println("Error: Credenciales incorrectas o usuario no registrado.");
                    }
                    break;

                case "2":
                    System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
                    System.out.print("Ingrese su nombre: ");
                    String nombreRegistro = scanner.nextLine();
                    System.out.print("Ingrese su email: ");
                    String emailRegistro = scanner.nextLine();

                    if (!emailRegistro.contains("@")) {
                        System.out.println("Error: El correo ingresado debe contener el simbolo @.");
                        break;
                    }

                    //Validación de correo duplicado
                    boolean correoExiste = false;

                    // Verificar si es el correo del admin
                    if (admin.getEmail().equalsIgnoreCase(emailRegistro)) {
                        correoExiste = true;
                    } else {
                        // Verificar en la lista de usuarios
                        for (int i = 0; i < listaUsuarios.size(); i++) {
                            if (listaUsuarios.get(i).getEmail().equalsIgnoreCase(emailRegistro)) {
                                correoExiste = true;
                                break;
                            }
                        }
                    }

                    if (correoExiste) {
                        System.out.println("Error: El correo ingresado ya se encuentra registrado en el sistema. Intente iniciar sesion.");
                    } else {
                        // Si no existe, pedimos la contraseña y lo guardamos
                        System.out.print("Ingrese su contrasena: ");
                        String passRegistro = scanner.nextLine();

                        Usuario nuevoUsuario = new Usuario(nombreRegistro, emailRegistro, passRegistro);
                        listaUsuarios.add(nuevoUsuario);
                        System.out.println("Usuario registrado con exito. Ya puede iniciar sesion.");
                    }
                    break;

                case "3":
                    System.out.println("\n--- RECUPERAR CONTRASENA ---");
                    System.out.print("Ingrese su email registrado: ");
                    String emailRecuperacion = scanner.nextLine();

                    boolean recuperado = false;
                    for (int i = 0; i < listaUsuarios.size(); i++) {
                        if (listaUsuarios.get(i).getEmail().equalsIgnoreCase(emailRecuperacion)) {
                            System.out.println(listaUsuarios.get(i).recuperarContrasenia());
                            recuperado = true;
                            break;
                        }
                    }
                    if (!recuperado) {
                        System.out.println("El correo ingresado no se encuentra en el sistema.");
                    }
                    break;

                case "4":
                    sistemaActivo = false;
                    System.out.println("Cerrando el sistema. Buen viaje!");
                    break;

                default:
                    System.out.println("Opcion no valida. Por favor, ingrese un numero del 1 al 4.");
                    break;
            }
        }
        scanner.close();
    }

    // SUBMENÚ DE USUARIO
    private static void menuUsuario(Scanner scanner, Usuario usuario, SistemaPrediccion motor, ArrayList<Usuario> listaUsuarios) {
        boolean enMenuUsuario = true;

        while (enMenuUsuario) {
            System.out.println("\n--- MENU DE USUARIO: " + usuario.getNombre().toUpperCase() + " ---");
            System.out.println("1. Registrar nueva ruta frecuente");
            System.out.println("2. Registrar un viaje realizado (Historial)");
            System.out.println("3. Consultar/Planificar un viaje futuro");
            System.out.println("4. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");

            String opcionUsuario = scanner.nextLine();

            switch (opcionUsuario) {
                case "1":
                    System.out.println("\n--- REGISTRO DE RUTA ---");
                    System.out.print("Nombre de la ruta (Ej: Casa-Universidad): ");
                    String nombreRuta = scanner.nextLine();

                    // muestra los origenes globales (historial unificado)
                    System.out.println("\nPuntos de origen historicos:");
                    ArrayList<String> puntosOrigen = HistorialGlobal.getPuntosHistoricos();
                    for (int i = 0; i < puntosOrigen.size(); i++) {
                        System.out.println((i + 1) + ". " + puntosOrigen.get(i));
                    }
                    System.out.println((puntosOrigen.size() + 1) + ". [Ingresar un nuevo origen]");
                    System.out.print("Seleccione una opcion: ");
                    int opcionOrigen = leerEntero(scanner);
                    String origen = "";
                    if (opcionOrigen >= 1 && opcionOrigen <= puntosOrigen.size()) {
                        origen = puntosOrigen.get(opcionOrigen - 1);
                    } else {
                        System.out.print("Escriba el punto de origen: ");
                        origen = scanner.nextLine();
                        // agrega el nuevo punto al historial unificado
                        HistorialGlobal.agregarPunto(origen);
                    }

                    // muestra los destinos globales (historial unificado)
                    System.out.println("\nPuntos de destino historicos:");
                    ArrayList<String> puntosDestino = HistorialGlobal.getPuntosHistoricos();
                    for (int i = 0; i < puntosDestino.size(); i++) {
                        System.out.println((i + 1) + ". " + puntosDestino.get(i));
                    }
                    System.out.println((puntosDestino.size() + 1) + ". [Ingresar un nuevo destino]");
                    System.out.print("Seleccione una opcion: ");
                    int opcionDestino = leerEntero(scanner);
                    String destino = "";
                    if (opcionDestino >= 1 && opcionDestino <= puntosDestino.size()) {
                        destino = puntosDestino.get(opcionDestino - 1);
                    } else {
                        System.out.print("Escriba el punto de destino: ");
                        destino = scanner.nextLine();
                        // agrega el nuevo punto al historial unificado
                        HistorialGlobal.agregarPunto(destino);
                    }

                    // valida que origen y destino no sean iguales
                    if (origen.equalsIgnoreCase(destino)) {
                        System.out.println("Error: El punto de origen y el de destino no pueden ser el mismo.");
                        break;
                    }

                    Ruta nuevaRuta = new Ruta(nombreRuta, origen, destino);
                    usuario.registrarRuta(nuevaRuta);
                    System.out.println("Ruta guardada exitosamente.");
                    break;

                case "2":
                    if (usuario.getRutasFrecuentes().isEmpty()) {
                        System.out.println("Debe registrar al menos una ruta antes de agregar un viaje.");
                        break;
                    }

                    System.out.println("\n--- REGISTRO DE VIAJE REALIZADO ---");
                    System.out.println("Rutas disponibles:");
                    for (int i = 0; i < usuario.getRutasFrecuentes().size(); i++) {
                        System.out.println((i + 1) + ". " + usuario.getRutasFrecuentes().get(i).getNombreRuta() + 
                                           " (" + usuario.getRutasFrecuentes().get(i).getOrigen() + " -> " + 
                                           usuario.getRutasFrecuentes().get(i).getDestino() + ")");
                    }

                    System.out.print("Seleccione el numero de la ruta: ");
                    int indiceRutaRegistro = leerEntero(scanner) - 1;

                    if (indiceRutaRegistro >= 0 && indiceRutaRegistro < usuario.getRutasFrecuentes().size()) {
                        Ruta rutaSeleccionada = usuario.getRutasFrecuentes().get(indiceRutaRegistro);

                        System.out.print("Hora de inicio (Formato 24h, Ej: 07:00): ");
                        String horaInicio = scanner.nextLine();
                        System.out.print("Hora de fin (Formato 24h, Ej: 10:00): ");
                        String horaFin = scanner.nextLine();

                        try {
                            String[] partesInicio = horaInicio.split(":");
                            String[] partesFin = horaFin.split(":");

                            int minutosInicio = (Integer.parseInt(partesInicio[0].trim()) * 60) + Integer.parseInt(partesInicio[1].trim());
                            int minutosFin = (Integer.parseInt(partesFin[0].trim()) * 60) + Integer.parseInt(partesFin[1].trim());

                            int duracion = minutosFin - minutosInicio;
                            if (duracion < 0) {
                                duracion += 1440;
                            }

                            Viaje nuevoViaje = new Viaje(rutaSeleccionada, horaInicio, horaFin, duracion);
                            usuario.registrarViaje(nuevoViaje);

                            System.out.println("-> El sistema ha calculado un tiempo de viaje de: " + duracion + " minutos.");
                            System.out.println("Viaje registrado en el historial correctamente.");

                            // pregunta si paso algo raro en el viaje
                            System.out.print("\n¿Hubo alguna novedad o accidente en este viaje? (1. Si, 2. No): ");
                            int tieneNovedad = leerEntero(scanner);
                            if (tieneNovedad == 1) {
                                System.out.println("Seleccione el tipo de novedad:");
                                System.out.println("1. Choque / Accidente");
                                System.out.println("2. Transito pesado");
                                System.out.println("3. Trabajos en la via");
                                System.out.println("4. Otro");
                                System.out.print("Opcion: ");
                                int tipoNovedad = leerEntero(scanner);
                                String[] tiposNovedad = {"Choque / Accidente", "Transito pesado", "Trabajos en la via", "Otro"};
                                String tipoTexto = (tipoNovedad >= 1 && tipoNovedad <= 4) ? tiposNovedad[tipoNovedad - 1] : "Otro";
                                System.out.print("Ingrese un mensaje descriptivo: ");
                                String mensajeNovedad = scanner.nextLine();

                                // codigo unico simple
                                String codigoAlerta = "AL-" + (int)(Math.random() * 900 + 100);

                                if (tipoNovedad == 1 || tipoNovedad == 2) {
                                    System.out.print("Ingrese el tiempo aproximado de retraso (en minutos): ");
                                    int retraso = leerEntero(scanner);
                                    if (retraso < 0) retraso = 0;
                                    AlertaRetraso alerta = new AlertaRetraso(codigoAlerta, mensajeNovedad, rutaSeleccionada.getOrigen(), rutaSeleccionada.getDestino(), retraso, tipoTexto);
                                    HistorialGlobal.registrarAlerta(alerta);
                                    System.out.println("Alerta de retraso registrada globalmente.");
                                } else {
                                    Alerta alerta = new Alerta(codigoAlerta, mensajeNovedad, rutaSeleccionada.getOrigen(), rutaSeleccionada.getDestino(), tipoTexto);
                                    HistorialGlobal.registrarAlerta(alerta);
                                    System.out.println("Alerta general registrada globalmente.");
                                }
                            }
                        } catch (Exception e) {
                            // si la hora tiene formato incorrecto, atrapa el error
                            System.out.println("Error: Formato de hora incorrecto. Use HH:MM (Ej: 08:30).");
                        }
                    } else {
                        System.out.println("Seleccion de ruta invalida.");
                    }
                    break;

                case "3":
                    if (usuario.getRutasFrecuentes().isEmpty()) {
                        System.out.println("Debe registrar al menos una ruta antes de consultar.");
                        break;
                    }

                    System.out.println("\n--- PLANIFICACION DE VIAJE FUTURO ---");
                    System.out.println("Rutas disponibles:");
                    for (int i = 0; i < usuario.getRutasFrecuentes().size(); i++) {
                        System.out.println((i + 1) + ". " + usuario.getRutasFrecuentes().get(i).getNombreRuta() +
                                           " (" + usuario.getRutasFrecuentes().get(i).getOrigen() + " -> " + 
                                           usuario.getRutasFrecuentes().get(i).getDestino() + ")");
                    }

                    System.out.print("Seleccione el numero de la ruta: ");
                    int indiceRutaConsulta = leerEntero(scanner) - 1;

                    if (indiceRutaConsulta >= 0 && indiceRutaConsulta < usuario.getRutasFrecuentes().size()) {
                        Ruta rutaConsulta = usuario.getRutasFrecuentes().get(indiceRutaConsulta);

                        System.out.print("Hora planificada de salida (Formato 24h, Ej: 07:00): ");
                        String horaSalida = scanner.nextLine();

                        // obtiene todos los viajes guardados
                        ArrayList<Viaje> viajesGlobales = new ArrayList<>();
                        for (int k = 0; k < listaUsuarios.size(); k++) {
                            Usuario iteradorUsuario = listaUsuarios.get(k);
                            for (int m = 0; m < iteradorUsuario.getHistorialViajes().size(); m++) {
                                viajesGlobales.add(iteradorUsuario.getHistorialViajes().get(m));
                            }
                        }

                        // muestra las alertas de esta ruta usando polimorfismo
                        ArrayList<Alerta> alertasActivas = HistorialGlobal.obtenerAlertasParaRuta(rutaConsulta.getOrigen(), rutaConsulta.getDestino());
                        if (!alertasActivas.isEmpty()) {
                            System.out.println("\n--- REPORTES EN ESTA RUTA ---");
                            for (Alerta a : alertasActivas) {
                                a.mostrarInfo();
                            }
                        } else {
                            System.out.println("\nNo hay novedades reportadas para este tramo.");
                        }

                        try {
                            int tiempoPromedio = motor.calcularTiempoPromedio(viajesGlobales, rutaConsulta.getOrigen(), rutaConsulta.getDestino());

                            System.out.println("\n--- REPORTE DE VIAJE ---");
                            if (tiempoPromedio > 0) {
                                System.out.println("Demora estimada: " + tiempoPromedio + " minutos.");

                                String[] partesSalida = horaSalida.split(":");
                                int minTotalesSalida = (Integer.parseInt(partesSalida[0].trim()) * 60) + Integer.parseInt(partesSalida[1].trim());
                                int minTotalesLlegada = minTotalesSalida + tiempoPromedio;

                                int horaLlegada = (minTotalesLlegada / 60) % 24;
                                int minLlegada = minTotalesLlegada % 60;

                                String formatoLlegada = (horaLlegada < 10 ? "0" + horaLlegada : horaLlegada) + ":" + (minLlegada < 10 ? "0" + minLlegada : minLlegada);
                                System.out.println("Llegada estimada a su destino: " + formatoLlegada);
                            } else {
                                System.out.println("No hay datos historicos suficientes para estimar la demora en esta ruta.");
                            }

                            System.out.println("Prediccion de Trafico: " + motor.predecirMejorHorario(viajesGlobales, rutaConsulta.getOrigen(), rutaConsulta.getDestino()));
                        } catch (Exception e) {
                            // atrapa si la hora ingresada es invalida
                            System.out.println("Error: Formato de hora planificada incorrecto. Use HH:MM.");
                        }
                    } else {
                        System.out.println("Seleccion de ruta invalida.");
                    }
                    break;

                case "4":
                    enMenuUsuario = false;
                    System.out.println("Sesion cerrada.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        }
    }

    private static void menuAdministrador(Scanner scanner, Administrador admin, ArrayList<Usuario> listaUsuarios) {
        boolean enMenuAdmin = true;

        while (enMenuAdmin) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Generar Reporte General");
            System.out.println("2. Generar Reporte Individual (por email)");
            System.out.println("3. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");

            String opcionAdmin = scanner.nextLine();

            switch (opcionAdmin) {
                case "1":
                    admin.generarReporteGeneral(listaUsuarios);
                    break;

                case "2":
                    System.out.print("\nIngrese el email del usuario a consultar: ");
                    String emailConsulta = scanner.nextLine();
                    boolean encontrado = false;

                    for (int i = 0; i < listaUsuarios.size(); i++) {
                        if (listaUsuarios.get(i).getEmail().equalsIgnoreCase(emailConsulta)) {
                            admin.generarReporteIndividual(listaUsuarios.get(i));
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        System.out.println("Usuario no encontrado en la base de datos.");
                    }
                    break;

                case "3":
                    enMenuAdmin = false;
                    System.out.println("Sesion cerrada.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        }
    }

    // lee un numero de la consola de forma segura
    private static int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            // si el usuario escribe letras en vez de numeros, atrapa el error
            return -1;
        }
    }
}