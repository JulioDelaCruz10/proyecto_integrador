package sistemaurbano.interfaz;

import sistemaurbano.modelo.*;
import sistemaurbano.negocio.SistemaPrediccion;
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
                    System.out.print("Punto de origen: ");
                    String origen = scanner.nextLine();
                    System.out.print("Punto de destino: ");
                    String destino = scanner.nextLine();

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
                        System.out.println((i + 1) + ". " + usuario.getRutasFrecuentes().get(i).getNombreRuta());
                    }

                    System.out.print("Seleccione el numero de la ruta: ");
                    int indiceRutaRegistro = Integer.parseInt(scanner.nextLine()) - 1;

                    if (indiceRutaRegistro >= 0 && indiceRutaRegistro < usuario.getRutasFrecuentes().size()) {
                        Ruta rutaSeleccionada = usuario.getRutasFrecuentes().get(indiceRutaRegistro);

                        System.out.print("Hora de inicio (Formato 24h, Ej: 07:00): ");
                        String horaInicio = scanner.nextLine();
                        System.out.print("Hora de fin (Formato 24h, Ej: 10:00): ");
                        String horaFin = scanner.nextLine();

                        String[] partesInicio = horaInicio.split(":");
                        String[] partesFin = horaFin.split(":");

                        int minutosInicio = (Integer.parseInt(partesInicio[0]) * 60) + Integer.parseInt(partesInicio[1]);
                        int minutosFin = (Integer.parseInt(partesFin[0]) * 60) + Integer.parseInt(partesFin[1]);

                        int duracion = minutosFin - minutosInicio;
                        if (duracion < 0) {
                            duracion += 1440;
                        }

                        Viaje nuevoViaje = new Viaje(rutaSeleccionada, horaInicio, horaFin, duracion);
                        usuario.registrarViaje(nuevoViaje);

                        System.out.println("-> El sistema ha calculado un tiempo de viaje de: " + duracion + " minutos.");
                        System.out.println("Viaje registrado en el historial correctamente.");
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
                        System.out.println((i + 1) + ". " + usuario.getRutasFrecuentes().get(i).getNombreRuta());
                    }

                    System.out.print("Seleccione el numero de la ruta: ");
                    int indiceRutaConsulta = Integer.parseInt(scanner.nextLine()) - 1;

                    if (indiceRutaConsulta >= 0 && indiceRutaConsulta < usuario.getRutasFrecuentes().size()) {
                        Ruta rutaConsulta = usuario.getRutasFrecuentes().get(indiceRutaConsulta);

                        System.out.print("Hora planificada de salida (Formato 24h, Ej: 07:00): ");
                        String horaSalida = scanner.nextLine();

                        ArrayList<Viaje> viajesGlobales = new ArrayList<>();
                        for (int k = 0; k < listaUsuarios.size(); k++) {
                            Usuario iteradorUsuario = listaUsuarios.get(k);
                            for (int m = 0; m < iteradorUsuario.getHistorialViajes().size(); m++) {
                                viajesGlobales.add(iteradorUsuario.getHistorialViajes().get(m));
                            }
                        }

                        int tiempoPromedio = motor.calcularTiempoPromedio(viajesGlobales, rutaConsulta.getNombreRuta());

                        System.out.println("\n--- REPORTE DE VIAJE ---");
                        if (tiempoPromedio > 0) {
                            System.out.println("Demora estimada: " + tiempoPromedio + " minutos.");

                            // Calculo de hora de llegada
                            String[] partesSalida = horaSalida.split(":");
                            int minTotalesSalida = (Integer.parseInt(partesSalida[0]) * 60) + Integer.parseInt(partesSalida[1]);
                            int minTotalesLlegada = minTotalesSalida + tiempoPromedio;

                            int horaLlegada = (minTotalesLlegada / 60) % 24;
                            int minLlegada = minTotalesLlegada % 60;

                            String formatoLlegada = (horaLlegada < 10 ? "0" + horaLlegada : horaLlegada) + ":" + (minLlegada < 10 ? "0" + minLlegada : minLlegada);
                            System.out.println("Llegada estimada a su destino: " + formatoLlegada);
                        } else {
                            System.out.println("No hay datos historicos suficientes para estimar la demora en esta ruta.");
                        }

                        System.out.println("Prediccion de Trafico: " + motor.predecirMejorHorario(viajesGlobales, rutaConsulta.getNombreRuta()));
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
}