package sistemaurbano.interfaz;

import sistemaurbano.modelo.*;
import sistemaurbano.negocio.SistemaPrediccion;
import sistemaurbano.negocio.HistorialGlobal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Interfaz grafica (Swing) para el Sistema de Gestion de Transporte Urbano - Quito.
 * No modifica ninguna clase de modelo ni de negocio: unicamente consume la
 * misma logica que ya usaba la version de consola (Main.java).
 */
public class MainGUI extends JFrame {

    // =================================================================
    // PALETA DE COLORES
    // =================================================================
    private static final Color PRIMARY = new Color(0x1B3B6F);      // azul marino (encabezado)
    private static final Color PRIMARY_DARK = new Color(0x12294F);
    private static final Color ACCENT = new Color(0x00A8A0);       // teal (accion principal)
    private static final Color ACCENT_DARK = new Color(0x00857F);
    private static final Color SUCCESS = new Color(0x2E9E4F);
    private static final Color SUCCESS_DARK = new Color(0x257F3F);
    private static final Color DANGER = new Color(0xD64545);
    private static final Color DANGER_DARK = new Color(0xB23636);
    private static final Color NEUTRAL = new Color(0x64748B);
    private static final Color NEUTRAL_DARK = new Color(0x4B5768);
    private static final Color BG_APP = new Color(0xEEF2F8);       // fondo general
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xD7DEE9);
    private static final Color TEXT_PRIMARY = new Color(0x1F2A44);
    private static final Color TEXT_SECONDARY = new Color(0x5B6B84);

    private static final Font FONT_TITULO = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BOTON = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 18);

    // ---- Datos / estado de la aplicacion ----
    private final ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private final SistemaPrediccion motorPrediccion = new SistemaPrediccion();
    private final Administrador admin = new Administrador("admin@sistema.com", "admin123", "ADM-001");
    private Usuario usuarioActual;

    // ---- Navegacion ----
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedor = new JPanel(cardLayout);
    private JLabel lblEstadoSesion;

    private static final String LOGIN = "LOGIN";
    private static final String REGISTRO = "REGISTRO";
    private static final String RECUPERAR = "RECUPERAR";
    private static final String MENU_USUARIO = "MENU_USUARIO";
    private static final String MENU_ADMIN = "MENU_ADMIN";
    private static final String REGISTRAR_RUTA = "REGISTRAR_RUTA";
    private static final String REGISTRAR_VIAJE = "REGISTRAR_VIAJE";
    private static final String PLANIFICAR_VIAJE = "PLANIFICAR_VIAJE";

    // Etiqueta de bienvenida del menu de usuario (se actualiza al entrar)
    private JLabel lblBienvenidaUsuario;

    public MainGUI() {
        super("Sistema de Gestion de Transporte Urbano - Quito");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setMinimumSize(new Dimension(680, 520));
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG_APP);
        setLayout(new BorderLayout());

        add(crearEncabezado(), BorderLayout.NORTH);

        panelContenedor.setBackground(BG_APP);
        panelContenedor.add(crearPanelLogin(), LOGIN);
        panelContenedor.add(crearPanelRegistro(), REGISTRO);
        panelContenedor.add(crearPanelRecuperar(), RECUPERAR);
        panelContenedor.add(crearPanelMenuUsuario(), MENU_USUARIO);
        panelContenedor.add(crearPanelMenuAdmin(), MENU_ADMIN);
        panelContenedor.add(crearPanelRegistrarRuta(), REGISTRAR_RUTA);
        panelContenedor.add(crearPanelRegistrarViaje(), REGISTRAR_VIAJE);
        panelContenedor.add(crearPanelPlanificarViaje(), PLANIFICAR_VIAJE);

        add(panelContenedor, BorderLayout.CENTER);
        mostrar(LOGIN);
    }

    private void mostrar(String nombre) {
        cardLayout.show(panelContenedor, nombre);
        if (LOGIN.equals(nombre)) {
            lblEstadoSesion.setText("Sin sesion iniciada");
        } else if (MENU_ADMIN.equals(nombre)) {
            lblEstadoSesion.setText("Sesion: Administrador");
        } else if (usuarioActual != null) {
            lblEstadoSesion.setText("Sesion: " + usuarioActual.getNombre());
        }
    }

    // =================================================================
    // ENCABEZADO SUPERIOR (barra de color persistente)
    // =================================================================
    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel titulo = new JLabel("\uD83D\uDE8C  Sistema de Gestion de Transporte Urbano - Quito");
        titulo.setFont(FONT_HEADER);
        titulo.setForeground(Color.WHITE);

        lblEstadoSesion = new JLabel("Sin sesion iniciada");
        lblEstadoSesion.setFont(FONT_LABEL);
        lblEstadoSesion.setForeground(new Color(0xC9D6EA));

        header.add(titulo, BorderLayout.WEST);
        header.add(lblEstadoSesion, BorderLayout.EAST);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(header, BorderLayout.CENTER);
        JPanel linea = new JPanel();
        linea.setBackground(ACCENT);
        linea.setPreferredSize(new Dimension(10, 4));
        contenedor.add(linea, BorderLayout.SOUTH);

        return contenedor;
    }

    // =================================================================
    // COMPONENTES ESTILIZADOS REUTILIZABLES
    // =================================================================

    /** Boton con esquinas redondeadas, color solido y efecto hover. */
    private static class BotonEstilizado extends JButton {
        private final Color colorBase;
        private final Color colorHover;
        private final Color colorPresionado;
        private boolean sobreBoton = false;
        private boolean presionado = false;

        BotonEstilizado(String texto, Color colorBase, Color colorHover) {
            super(texto);
            this.colorBase = colorBase;
            this.colorHover = colorHover;
            this.colorPresionado = colorHover.darker();
            setFont(FONT_BOTON);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 18, 10, 18));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { sobreBoton = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { sobreBoton = false; repaint(); }
                @Override
                public void mousePressed(MouseEvent e) { presionado = true; repaint(); }
                @Override
                public void mouseReleased(MouseEvent e) { presionado = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color relleno = presionado ? colorPresionado : (sobreBoton ? colorHover : colorBase);
            g2.setColor(isEnabled() ? relleno : new Color(0xB9C1CE));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JButton crearBotonPrimario(String texto) {
        return new BotonEstilizado(texto, ACCENT, ACCENT_DARK);
    }

    private JButton crearBotonSecundario(String texto) {
        return new BotonEstilizado(texto, PRIMARY, PRIMARY_DARK);
    }

    private JButton crearBotonNeutro(String texto) {
        return new BotonEstilizado(texto, NEUTRAL, NEUTRAL_DARK);
    }

    private JButton crearBotonPeligro(String texto) {
        return new BotonEstilizado(texto, DANGER, DANGER_DARK);
    }

    private JButton crearBotonExito(String texto) {
        return new BotonEstilizado(texto, SUCCESS, SUCCESS_DARK);
    }

    private JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FONT_TITULO);
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    private void estilizarCampo(JTextComponent campo) {
        campo.setFont(FONT_LABEL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(FONT_LABEL);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(2, 6, 2, 6)));
    }

    /** Panel tipo "tarjeta" blanco, con borde suave, centrado dentro de un fondo de color. */
    private JPanel envolverEnTarjeta(JPanel contenido, int anchoMax) {
        contenido.setBackground(CARD_BG);
        contenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(26, 34, 26, 34)));

        JPanel envoltura = new JPanel(new GridBagLayout());
        envoltura.setBackground(BG_APP);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        contenido.setMaximumSize(new Dimension(anchoMax, Integer.MAX_VALUE));
        envoltura.add(contenido, gc);
        return envoltura;
    }

    // ---------------------------------------------------------------
    // PANEL: LOGIN
    // ---------------------------------------------------------------
    private JPanel crearPanelLogin() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        JLabel titulo = crearTitulo("Iniciar Sesion");
        JLabel subtitulo = crearEtiqueta("Ingresa tus credenciales para continuar");

        JTextField txtEmail = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        estilizarCampo(txtEmail);
        estilizarCampo(txtPass);

        JButton btnLogin = crearBotonPrimario("Iniciar Sesion");
        JButton btnRegistro = crearBotonSecundario("Registrar Usuario");
        JButton btnRecuperar = crearBotonNeutro("Recuperar Contrasena");
        JButton btnSalir = crearBotonPeligro("Salir");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        tarjeta.add(titulo, c);
        c.gridy = 1; tarjeta.add(subtitulo, c);

        c.insets = new Insets(14, 6, 4, 6);
        c.gridwidth = 2; c.gridy = 2; tarjeta.add(crearEtiqueta("Email"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 3; tarjeta.add(txtEmail, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 4; tarjeta.add(crearEtiqueta("Contrasena"), c);
        c.insets = new Insets(0, 6, 16, 6);
        c.gridy = 5; tarjeta.add(txtPass, c);

        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = 6; tarjeta.add(btnLogin, c);
        c.gridy = 7; tarjeta.add(btnRegistro, c);
        c.gridy = 8; tarjeta.add(btnRecuperar, c);
        c.gridy = 9; tarjeta.add(btnSalir, c);

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (admin.validarLogin(email, pass)) {
                JOptionPane.showMessageDialog(this, "Acceso concedido. Bienvenido, Administrador.");
                txtEmail.setText(""); txtPass.setText("");
                mostrar(MENU_ADMIN);
                return;
            }

            for (Usuario u : listaUsuarios) {
                if (u.validarLogin(email, pass)) {
                    usuarioActual = u;
                    JOptionPane.showMessageDialog(this, "Acceso concedido. Bienvenido, " + u.getNombre() + ".");
                    lblBienvenidaUsuario.setText("Menu de Usuario: " + u.getNombre().toUpperCase());
                    txtEmail.setText(""); txtPass.setText("");
                    mostrar(MENU_USUARIO);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Error: Credenciales incorrectas o usuario no registrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        });

        btnRegistro.addActionListener(e -> mostrar(REGISTRO));
        btnRecuperar.addActionListener(e -> mostrar(RECUPERAR));
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Cerrar la aplicacion?", "Salir",
                    JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        return envolverEnTarjeta(tarjeta, 420);
    }

    // ---------------------------------------------------------------
    // PANEL: REGISTRO DE USUARIO
    // ---------------------------------------------------------------
    private JPanel crearPanelRegistro() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        JLabel titulo = crearTitulo("Registro de Nuevo Usuario");

        JTextField txtNombre = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        estilizarCampo(txtNombre);
        estilizarCampo(txtEmail);
        estilizarCampo(txtPass);

        JButton btnGuardar = crearBotonPrimario("Registrar");
        JButton btnVolver = crearBotonNeutro("Volver");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 14, 6);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        tarjeta.add(titulo, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 1; tarjeta.add(crearEtiqueta("Nombre"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 2; tarjeta.add(txtNombre, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 3; tarjeta.add(crearEtiqueta("Email"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 4; tarjeta.add(txtEmail, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 5; tarjeta.add(crearEtiqueta("Contrasena"), c);
        c.insets = new Insets(0, 6, 16, 6);
        c.gridy = 6; tarjeta.add(txtPass, c);

        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = 7; tarjeta.add(btnGuardar, c);
        c.gridy = 8; tarjeta.add(btnVolver, c);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this,
                        "Error: El correo ingresado debe contener el simbolo @.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean correoExiste = admin.getEmail().equalsIgnoreCase(email);
            if (!correoExiste) {
                for (Usuario u : listaUsuarios) {
                    if (u.getEmail().equalsIgnoreCase(email)) {
                        correoExiste = true;
                        break;
                    }
                }
            }

            if (correoExiste) {
                JOptionPane.showMessageDialog(this,
                        "Error: El correo ingresado ya se encuentra registrado en el sistema. Intente iniciar sesion.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario(nombre, email, pass);
            listaUsuarios.add(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario registrado con exito. Ya puede iniciar sesion.");

            txtNombre.setText(""); txtEmail.setText(""); txtPass.setText("");
            mostrar(LOGIN);
        });

        btnVolver.addActionListener(e -> mostrar(LOGIN));

        return envolverEnTarjeta(tarjeta, 420);
    }

    // ---------------------------------------------------------------
    // PANEL: RECUPERAR CONTRASENA
    // ---------------------------------------------------------------
    private JPanel crearPanelRecuperar() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        JLabel titulo = crearTitulo("Recuperar Contrasena");

        JTextField txtEmail = new JTextField(20);
        estilizarCampo(txtEmail);

        JTextArea resultado = new JTextArea(3, 25);
        resultado.setEditable(false);
        resultado.setLineWrap(true);
        resultado.setWrapStyleWord(true);
        resultado.setFont(FONT_LABEL);
        resultado.setBackground(new Color(0xF5F8FC));
        resultado.setForeground(TEXT_PRIMARY);
        resultado.setBorder(new EmptyBorder(8, 8, 8, 8));

        JButton btnRecuperar = crearBotonPrimario("Recuperar");
        JButton btnVolver = crearBotonNeutro("Volver");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 14, 6);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        tarjeta.add(titulo, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 1; tarjeta.add(crearEtiqueta("Email registrado"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 2; tarjeta.add(txtEmail, c);

        c.insets = new Insets(4, 6, 14, 6);
        c.gridy = 3;
        JScrollPane scrollResultado = new JScrollPane(resultado);
        scrollResultado.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        tarjeta.add(scrollResultado, c);

        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = 4; tarjeta.add(btnRecuperar, c);
        c.gridy = 5; tarjeta.add(btnVolver, c);

        btnRecuperar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            boolean recuperado = false;
            for (Usuario u : listaUsuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    resultado.setText(u.recuperarContrasenia());
                    recuperado = true;
                    break;
                }
            }
            if (!recuperado) {
                resultado.setText("El correo ingresado no se encuentra en el sistema.");
            }
        });

        btnVolver.addActionListener(e -> {
            txtEmail.setText("");
            resultado.setText("");
            mostrar(LOGIN);
        });

        return envolverEnTarjeta(tarjeta, 460);
    }

    // ---------------------------------------------------------------
    // PANEL: MENU DE USUARIO
    // ---------------------------------------------------------------
    private JPanel crearPanelMenuUsuario() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        lblBienvenidaUsuario = crearTitulo("Menu de Usuario");

        JButton btnRuta = crearBotonPrimario("\uD83D\uDCCD  Registrar nueva ruta frecuente");
        JButton btnViaje = crearBotonSecundario("\uD83D\uDD52  Registrar un viaje realizado (Historial)");
        JButton btnPlanificar = crearBotonExito("\uD83D\uDCC8  Consultar/Planificar un viaje futuro");
        JButton btnCerrarSesion = crearBotonPeligro("Cerrar Sesion");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 18, 8);
        c.gridx = 0; c.gridy = 0;
        tarjeta.add(lblBienvenidaUsuario, c);

        c.insets = new Insets(8, 8, 8, 8);
        c.gridy = 1; tarjeta.add(btnRuta, c);
        c.gridy = 2; tarjeta.add(btnViaje, c);
        c.gridy = 3; tarjeta.add(btnPlanificar, c);
        c.gridy = 4; c.insets = new Insets(20, 8, 8, 8); tarjeta.add(btnCerrarSesion, c);

        btnRuta.addActionListener(e -> {
            actualizarCombosRuta();
            mostrar(REGISTRAR_RUTA);
        });
        btnViaje.addActionListener(e -> {
            if (usuarioActual.getRutasFrecuentes().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe registrar al menos una ruta antes de agregar un viaje.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            actualizarComboRutasViaje();
            mostrar(REGISTRAR_VIAJE);
        });
        btnPlanificar.addActionListener(e -> {
            if (usuarioActual.getRutasFrecuentes().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe registrar al menos una ruta antes de consultar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            actualizarComboRutasPlanificar();
            mostrar(PLANIFICAR_VIAJE);
        });
        btnCerrarSesion.addActionListener(e -> {
            usuarioActual = null;
            mostrar(LOGIN);
        });

        return envolverEnTarjeta(tarjeta, 460);
    }

    // ---------------------------------------------------------------
    // PANEL: REGISTRAR RUTA
    // ---------------------------------------------------------------
    private JComboBox<String> comboOrigenRuta;
    private JComboBox<String> comboDestinoRuta;
    private JTextField txtNombreRuta;

    private JPanel crearPanelRegistrarRuta() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        JLabel titulo = crearTitulo("Registro de Ruta");

        txtNombreRuta = new JTextField(20);
        estilizarCampo(txtNombreRuta);
        comboOrigenRuta = new JComboBox<>();
        comboDestinoRuta = new JComboBox<>();
        estilizarCombo(comboOrigenRuta);
        estilizarCombo(comboDestinoRuta);

        JButton btnGuardar = crearBotonPrimario("Guardar Ruta");
        JButton btnVolver = crearBotonNeutro("Volver");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 14, 6);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        tarjeta.add(titulo, c);

        c.gridwidth = 1;
        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 1; c.gridx = 0; tarjeta.add(crearEtiqueta("Nombre de la ruta"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 2; tarjeta.add(txtNombreRuta, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 3; tarjeta.add(crearEtiqueta("Origen"), c);
        c.insets = new Insets(0, 6, 10, 6);
        c.gridy = 4; tarjeta.add(comboOrigenRuta, c);

        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 5; tarjeta.add(crearEtiqueta("Destino"), c);
        c.insets = new Insets(0, 6, 16, 6);
        c.gridy = 6; tarjeta.add(comboDestinoRuta, c);

        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = 7; tarjeta.add(btnGuardar, c);
        c.gridy = 8; tarjeta.add(btnVolver, c);

        btnGuardar.addActionListener(e -> {
            String nombreRuta = txtNombreRuta.getText().trim();
            if (nombreRuta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre para la ruta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String origen = resolverPuntoSeleccionado((String) comboOrigenRuta.getSelectedItem(), "origen");
            if (origen == null) return;
            String destino = resolverPuntoSeleccionado((String) comboDestinoRuta.getSelectedItem(), "destino");
            if (destino == null) return;

            if (origen.equalsIgnoreCase(destino)) {
                JOptionPane.showMessageDialog(this,
                        "Error: El punto de origen y el de destino no pueden ser el mismo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Ruta nuevaRuta = new Ruta(nombreRuta, origen, destino);
            usuarioActual.registrarRuta(nuevaRuta);
            JOptionPane.showMessageDialog(this, "Ruta guardada exitosamente.");

            txtNombreRuta.setText("");
            mostrar(MENU_USUARIO);
        });

        btnVolver.addActionListener(e -> mostrar(MENU_USUARIO));

        return envolverEnTarjeta(tarjeta, 460);
    }

    // Si el usuario elige "[Ingresar nuevo...]" pide el texto por dialogo y lo agrega al historial global
    private String resolverPuntoSeleccionado(String seleccion, String etiqueta) {
        if (seleccion == null) return null;
        if (seleccion.startsWith("[Ingresar")) {
            String nuevo = JOptionPane.showInputDialog(this, "Escriba el punto de " + etiqueta + ":");
            if (nuevo == null || nuevo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un valor valido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            nuevo = nuevo.trim();
            HistorialGlobal.agregarPunto(nuevo);
            return nuevo;
        }
        return seleccion;
    }

    private void actualizarCombosRuta() {
        comboOrigenRuta.removeAllItems();
        comboDestinoRuta.removeAllItems();
        for (String punto : HistorialGlobal.getPuntosHistoricos()) {
            comboOrigenRuta.addItem(punto);
            comboDestinoRuta.addItem(punto);
        }
        comboOrigenRuta.addItem("[Ingresar un nuevo origen]");
        comboDestinoRuta.addItem("[Ingresar un nuevo destino]");
    }

    // ---------------------------------------------------------------
    // PANEL: REGISTRAR VIAJE
    // ---------------------------------------------------------------
    private JComboBox<String> comboRutasViaje;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JCheckBox chkNovedad;
    private JComboBox<String> comboTipoNovedad;
    private JTextField txtMensajeNovedad;
    private JTextField txtRetraso;

    private JPanel crearPanelRegistrarViaje() {
        JPanel tarjeta = new JPanel(new GridBagLayout());

        JLabel titulo = crearTitulo("Registro de Viaje Realizado");

        comboRutasViaje = new JComboBox<>();
        estilizarCombo(comboRutasViaje);
        txtHoraInicio = new JTextField(10);
        txtHoraFin = new JTextField(10);
        estilizarCampo(txtHoraInicio);
        estilizarCampo(txtHoraFin);

        chkNovedad = new JCheckBox("¿Hubo alguna novedad o accidente en este viaje?");
        chkNovedad.setFont(FONT_LABEL);
        chkNovedad.setForeground(TEXT_PRIMARY);
        chkNovedad.setOpaque(false);

        comboTipoNovedad = new JComboBox<>(new String[]{
                "Choque / Accidente", "Transito pesado", "Trabajos en la via", "Otro"
        });
        estilizarCombo(comboTipoNovedad);
        comboTipoNovedad.setEnabled(false);
        txtMensajeNovedad = new JTextField(20);
        estilizarCampo(txtMensajeNovedad);
        txtMensajeNovedad.setEnabled(false);
        txtRetraso = new JTextField(6);
        estilizarCampo(txtRetraso);
        txtRetraso.setEnabled(false);

        chkNovedad.addActionListener(e -> {
            boolean marcado = chkNovedad.isSelected();
            comboTipoNovedad.setEnabled(marcado);
            txtMensajeNovedad.setEnabled(marcado);
            actualizarEstadoRetraso();
        });
        comboTipoNovedad.addActionListener(e -> actualizarEstadoRetraso());

        JButton btnGuardar = crearBotonPrimario("Guardar Viaje");
        JButton btnVolver = crearBotonNeutro("Volver");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        tarjeta.add(titulo, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; tarjeta.add(crearEtiqueta("Ruta"), c);
        c.gridx = 1; tarjeta.add(comboRutasViaje, c);

        c.gridy = 2; c.gridx = 0; tarjeta.add(crearEtiqueta("Hora inicio (HH:MM)"), c);
        c.gridx = 1; tarjeta.add(txtHoraInicio, c);

        c.gridy = 3; c.gridx = 0; tarjeta.add(crearEtiqueta("Hora fin (HH:MM)"), c);
        c.gridx = 1; tarjeta.add(txtHoraFin, c);

        c.gridy = 4; c.gridx = 0; c.gridwidth = 2;
        c.insets = new Insets(14, 6, 5, 6);
        tarjeta.add(chkNovedad, c);

        c.gridwidth = 1;
        c.insets = new Insets(5, 6, 5, 6);
        c.gridy = 5; c.gridx = 0; tarjeta.add(crearEtiqueta("Tipo de novedad"), c);
        c.gridx = 1; tarjeta.add(comboTipoNovedad, c);

        c.gridy = 6; c.gridx = 0; tarjeta.add(crearEtiqueta("Mensaje descriptivo"), c);
        c.gridx = 1; tarjeta.add(txtMensajeNovedad, c);

        c.gridy = 7; c.gridx = 0; tarjeta.add(crearEtiqueta("Retraso (minutos)"), c);
        c.gridx = 1; tarjeta.add(txtRetraso, c);

        c.gridy = 8; c.gridx = 0; c.gridwidth = 2;
        c.insets = new Insets(16, 6, 6, 6);
        tarjeta.add(btnGuardar, c);
        c.gridy = 9; c.insets = new Insets(6, 6, 6, 6);
        tarjeta.add(btnVolver, c);

        btnGuardar.addActionListener(e -> {
            int indice = comboRutasViaje.getSelectedIndex();
            if (indice < 0 || indice >= usuarioActual.getRutasFrecuentes().size()) {
                JOptionPane.showMessageDialog(this, "Seleccion de ruta invalida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ruta rutaSeleccionada = usuarioActual.getRutasFrecuentes().get(indice);

            try {
                String horaInicio = txtHoraInicio.getText().trim();
                String horaFin = txtHoraFin.getText().trim();

                String[] partesInicio = horaInicio.split(":");
                String[] partesFin = horaFin.split(":");

                int minutosInicio = (Integer.parseInt(partesInicio[0].trim()) * 60) + Integer.parseInt(partesInicio[1].trim());
                int minutosFin = (Integer.parseInt(partesFin[0].trim()) * 60) + Integer.parseInt(partesFin[1].trim());

                int duracion = minutosFin - minutosInicio;
                if (duracion < 0) {
                    duracion += 1440;
                }

                Viaje nuevoViaje = new Viaje(rutaSeleccionada, horaInicio, horaFin, duracion);
                usuarioActual.registrarViaje(nuevoViaje);

                StringBuilder mensajeFinal = new StringBuilder();
                mensajeFinal.append("El sistema ha calculado un tiempo de viaje de: ").append(duracion).append(" minutos.\n");
                mensajeFinal.append("Viaje registrado en el historial correctamente.");

                if (chkNovedad.isSelected()) {
                    String mensajeNovedad = txtMensajeNovedad.getText().trim();
                    int tipoNovedad = comboTipoNovedad.getSelectedIndex() + 1; // 1..4
                    String tipoTexto = (String) comboTipoNovedad.getSelectedItem();
                    String codigoAlerta = "AL-" + (int) (Math.random() * 900 + 100);

                    if (tipoNovedad == 1 || tipoNovedad == 2) {
                        int retraso;
                        try {
                            retraso = Integer.parseInt(txtRetraso.getText().trim());
                        } catch (NumberFormatException nfe) {
                            retraso = 0;
                        }
                        if (retraso < 0) retraso = 0;
                        AlertaRetraso alerta = new AlertaRetraso(codigoAlerta, mensajeNovedad,
                                rutaSeleccionada.getOrigen(), rutaSeleccionada.getDestino(), retraso, tipoTexto);
                        HistorialGlobal.registrarAlerta(alerta);
                        mensajeFinal.append("\nAlerta de retraso registrada globalmente.");
                    } else {
                        Alerta alerta = new Alerta(codigoAlerta, mensajeNovedad,
                                rutaSeleccionada.getOrigen(), rutaSeleccionada.getDestino(), tipoTexto);
                        HistorialGlobal.registrarAlerta(alerta);
                        mensajeFinal.append("\nAlerta general registrada globalmente.");
                    }
                }

                JOptionPane.showMessageDialog(this, mensajeFinal.toString());
                limpiarFormularioViaje();
                mostrar(MENU_USUARIO);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: Formato de hora incorrecto. Use HH:MM (Ej: 08:30).",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarFormularioViaje();
            mostrar(MENU_USUARIO);
        });

        return envolverEnTarjeta(tarjeta, 520);
    }

    private void actualizarEstadoRetraso() {
        boolean habilitarRetraso = chkNovedad.isSelected() &&
                (comboTipoNovedad.getSelectedIndex() == 0 || comboTipoNovedad.getSelectedIndex() == 1);
        txtRetraso.setEnabled(habilitarRetraso);
    }

    private void limpiarFormularioViaje() {
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        chkNovedad.setSelected(false);
        comboTipoNovedad.setEnabled(false);
        comboTipoNovedad.setSelectedIndex(0);
        txtMensajeNovedad.setEnabled(false);
        txtMensajeNovedad.setText("");
        txtRetraso.setEnabled(false);
        txtRetraso.setText("");
    }

    private void actualizarComboRutasViaje() {
        comboRutasViaje.removeAllItems();
        for (Ruta r : usuarioActual.getRutasFrecuentes()) {
            comboRutasViaje.addItem(r.getNombreRuta() + " (" + r.getOrigen() + " -> " + r.getDestino() + ")");
        }
    }

    // ---------------------------------------------------------------
    // PANEL: PLANIFICAR VIAJE
    // ---------------------------------------------------------------
    private JComboBox<String> comboRutasPlanificar;
    private JTextField txtHoraSalida;
    private JTextArea areaResultadoPlanificacion;

    private JPanel crearPanelPlanificarViaje() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BG_APP);
        panel.setBorder(new EmptyBorder(22, 30, 22, 30));

        JLabel titulo = crearTitulo("Planificacion de Viaje Futuro");
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(CARD_BG);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(18, 20, 18, 20)));

        comboRutasPlanificar = new JComboBox<>();
        estilizarCombo(comboRutasPlanificar);
        txtHoraSalida = new JTextField(10);
        estilizarCampo(txtHoraSalida);
        JButton btnConsultar = crearBotonPrimario("Consultar");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; panelFormulario.add(crearEtiqueta("Ruta"), c);
        c.gridx = 1; panelFormulario.add(comboRutasPlanificar, c);
        c.gridx = 0; c.gridy = 1; panelFormulario.add(crearEtiqueta("Hora planificada de salida (HH:MM)"), c);
        c.gridx = 1; panelFormulario.add(txtHoraSalida, c);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.insets = new Insets(12, 6, 6, 6);
        panelFormulario.add(btnConsultar, c);

        areaResultadoPlanificacion = new JTextArea(12, 40);
        areaResultadoPlanificacion.setEditable(false);
        areaResultadoPlanificacion.setLineWrap(true);
        areaResultadoPlanificacion.setWrapStyleWord(true);
        areaResultadoPlanificacion.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultadoPlanificacion.setBackground(new Color(0xF5F8FC));
        areaResultadoPlanificacion.setForeground(TEXT_PRIMARY);
        areaResultadoPlanificacion.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(areaResultadoPlanificacion);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));

        JButton btnVolver = crearBotonNeutro("Volver");
        JPanel panelBotonVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonVolver.setBackground(BG_APP);
        panelBotonVolver.add(btnVolver);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(BG_APP);
        panelCentro.add(panelFormulario, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);
        panel.add(panelBotonVolver, BorderLayout.SOUTH);

        btnConsultar.addActionListener(e -> {
            int indice = comboRutasPlanificar.getSelectedIndex();
            if (indice < 0 || indice >= usuarioActual.getRutasFrecuentes().size()) {
                JOptionPane.showMessageDialog(this, "Seleccion de ruta invalida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ruta rutaConsulta = usuarioActual.getRutasFrecuentes().get(indice);
            String horaSalida = txtHoraSalida.getText().trim();

            ArrayList<Viaje> viajesGlobales = new ArrayList<>();
            for (Usuario u : listaUsuarios) {
                viajesGlobales.addAll(u.getHistorialViajes());
            }

            StringBuilder resultado = new StringBuilder();

            ArrayList<Alerta> alertasActivas = HistorialGlobal.obtenerAlertasParaRuta(
                    rutaConsulta.getOrigen(), rutaConsulta.getDestino());
            if (!alertasActivas.isEmpty()) {
                resultado.append("--- REPORTES EN ESTA RUTA ---\n");
                for (Alerta a : alertasActivas) {
                    if (a instanceof AlertaRetraso) {
                        AlertaRetraso ar = (AlertaRetraso) a;
                        resultado.append("!!! ALERTA DE TRAFICO / RETRASO !!!\n");
                        resultado.append("Codigo: ").append(ar.getCodigoAlerta()).append("\n");
                        resultado.append("Tramo: ").append(ar.getOrigen()).append(" -> ").append(ar.getDestino()).append("\n");
                        resultado.append("Detalle: ").append(ar.getMensaje()).append("\n");
                        resultado.append("Tiempo estimado de retraso: ").append(ar.getTiempoRetrasoMinutos()).append(" minutos.\n");
                    } else {
                        resultado.append("[").append(a.getCodigoAlerta()).append("] Notificacion General: ").append(a.getMensaje()).append("\n");
                        resultado.append("Tramo: ").append(a.getOrigen()).append(" -> ").append(a.getDestino()).append("\n");
                    }
                    resultado.append("-----------------------------------\n");
                }
            } else {
                resultado.append("No hay novedades reportadas para este tramo.\n");
            }

            try {
                int tiempoPromedio = motorPrediccion.calcularTiempoPromedio(
                        viajesGlobales, rutaConsulta.getOrigen(), rutaConsulta.getDestino());

                resultado.append("\n--- REPORTE DE VIAJE ---\n");
                if (tiempoPromedio > 0) {
                    resultado.append("Demora estimada: ").append(tiempoPromedio).append(" minutos.\n");

                    String[] partesSalida = horaSalida.split(":");
                    int minTotalesSalida = (Integer.parseInt(partesSalida[0].trim()) * 60) + Integer.parseInt(partesSalida[1].trim());
                    int minTotalesLlegada = minTotalesSalida + tiempoPromedio;

                    int horaLlegada = (minTotalesLlegada / 60) % 24;
                    int minLlegada = minTotalesLlegada % 60;

                    String formatoLlegada = (horaLlegada < 10 ? "0" + horaLlegada : horaLlegada) + ":" +
                            (minLlegada < 10 ? "0" + minLlegada : minLlegada);
                    resultado.append("Llegada estimada a su destino: ").append(formatoLlegada).append("\n");
                } else {
                    resultado.append("No hay datos historicos suficientes para estimar la demora en esta ruta.\n");
                }

                resultado.append("Prediccion de Trafico: ").append(
                        motorPrediccion.predecirMejorHorario(viajesGlobales, rutaConsulta.getOrigen(), rutaConsulta.getDestino()));

                areaResultadoPlanificacion.setText(resultado.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: Formato de hora planificada incorrecto. Use HH:MM.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            txtHoraSalida.setText("");
            areaResultadoPlanificacion.setText("");
            mostrar(MENU_USUARIO);
        });

        return panel;
    }

    private void actualizarComboRutasPlanificar() {
        comboRutasPlanificar.removeAllItems();
        for (Ruta r : usuarioActual.getRutasFrecuentes()) {
            comboRutasPlanificar.addItem(r.getNombreRuta() + " (" + r.getOrigen() + " -> " + r.getDestino() + ")");
        }
    }

    // ---------------------------------------------------------------
    // PANEL: MENU ADMINISTRADOR
    // ---------------------------------------------------------------
    private JPanel crearPanelMenuAdmin() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BG_APP);
        panel.setBorder(new EmptyBorder(22, 30, 22, 30));

        JLabel titulo = crearTitulo("Menu Administrador");
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 12, 10));
        panelBotones.setBackground(BG_APP);
        JButton btnReporteGeneral = crearBotonPrimario("Reporte General");
        JButton btnReporteIndividual = crearBotonSecundario("Reporte Individual (por email)");
        JButton btnAccidentes = crearBotonSecundario("Ver Accidentes Reportados");
        JButton btnRutasPorUsuario = crearBotonSecundario("Ver Rutas Guardadas por Usuario");
        JButton btnAlertasPorTipo = crearBotonSecundario("Estadisticas de Alertas");
        JButton btnCerrarSesion = crearBotonPeligro("Cerrar Sesion");
        panelBotones.add(btnReporteGeneral);
        panelBotones.add(btnReporteIndividual);
        panelBotones.add(btnAccidentes);
        panelBotones.add(btnRutasPorUsuario);
        panelBotones.add(btnAlertasPorTipo);
        panelBotones.add(btnCerrarSesion);

        JTextArea areaReporte = new JTextArea(14, 40);
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaReporte.setBackground(new Color(0xF5F8FC));
        areaReporte.setForeground(TEXT_PRIMARY);
        areaReporte.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(areaReporte);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));

        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBackground(BG_APP);
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panel.add(panelPrincipal, BorderLayout.CENTER);

        btnReporteGeneral.addActionListener(e -> {
            int totalRutas = 0;
            int totalViajes = 0;
            for (Usuario u : listaUsuarios) {
                totalRutas += u.getRutasFrecuentes().size();
                totalViajes += u.getHistorialViajes().size();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("--- REPORTE GENERAL DEL SISTEMA ---\n");
            sb.append("Total de usuarios registrados: ").append(listaUsuarios.size()).append("\n");
            sb.append("Total de rutas globales: ").append(totalRutas).append("\n");
            sb.append("Total de viajes registrados: ").append(totalViajes).append("\n");
            sb.append("-----------------------------------\n");
            areaReporte.setText(sb.toString());
        });

        btnReporteIndividual.addActionListener(e -> {
            String email = JOptionPane.showInputDialog(this, "Ingrese el email del usuario a consultar:");
            if (email == null) return;
            boolean encontrado = false;
            for (Usuario u : listaUsuarios) {
                if (u.getEmail().equalsIgnoreCase(email.trim())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("--- REPORTE INDIVIDUAL ---\n");
                    sb.append("Usuario: ").append(u.getNombre()).append("\n");
                    sb.append("Email: ").append(u.getEmail()).append("\n");
                    sb.append("Cantidad de rutas guardadas: ").append(u.getRutasFrecuentes().size()).append("\n");
                    sb.append("Cantidad de viajes realizados: ").append(u.getHistorialViajes().size()).append("\n");
                    sb.append("--------------------------\n");
                    areaReporte.setText(sb.toString());
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado en la base de datos.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAccidentes.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("--- ACCIDENTES REPORTADOS ---\n");
            int contador = 0;
            for (Alerta a : HistorialGlobal.getAlertas()) {
                if (a.getTipo() != null && a.getTipo().equalsIgnoreCase("Choque / Accidente")) {
                    contador++;
                    sb.append("[").append(a.getCodigoAlerta()).append("] Tramo: ")
                            .append(a.getOrigen()).append(" -> ").append(a.getDestino()).append("\n");
                    sb.append("Fecha/Hora: ").append(a.getFechaHoraReporte()).append("\n");
                    sb.append("Detalle: ").append(a.getMensaje()).append("\n");
                    if (a instanceof AlertaRetraso) {
                        sb.append("Retraso asociado: ").append(((AlertaRetraso) a).getTiempoRetrasoMinutos()).append(" min\n");
                    }
                    sb.append("-----------------------------------\n");
                }
            }
            if (contador == 0) {
                sb.append("No hay accidentes reportados por el momento.\n");
            }
            areaReporte.setText(sb.toString());
        });

        btnRutasPorUsuario.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("--- RUTAS GUARDADAS POR USUARIO ---\n");
            for (Usuario u : listaUsuarios) {
                sb.append("Usuario: ").append(u.getNombre()).append(" (").append(u.getEmail()).append(")\n");
                if (u.getRutasFrecuentes().isEmpty()) {
                    sb.append("   Sin rutas guardadas.\n");
                } else {
                    for (Ruta r : u.getRutasFrecuentes()) {
                        sb.append("   - ").append(r.getNombreRuta()).append(": ")
                                .append(r.getOrigen()).append(" -> ").append(r.getDestino())
                                .append(" (registrada: ").append(r.getFechaRegistro()).append(")\n");
                    }
                }
            }
            sb.append("-----------------------------------\n");
            areaReporte.setText(sb.toString());
        });

        btnAlertasPorTipo.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("--- ALERTAS POR TIPO DE NOVEDAD ---\n");
            java.util.LinkedHashMap<String, Integer> conteo = new java.util.LinkedHashMap<>();
            for (Alerta a : HistorialGlobal.getAlertas()) {
                String tipo = (a.getTipo() != null) ? a.getTipo() : "Otro";
                conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
            }
            if (conteo.isEmpty()) {
                sb.append("Aun no se han registrado alertas.\n");
            } else {
                for (String tipo : conteo.keySet()) {
                    sb.append(tipo).append(": ").append(conteo.get(tipo)).append("\n");
                }
            }
            sb.append("-----------------------------------\n");
            areaReporte.setText(sb.toString());
        });

        btnCerrarSesion.addActionListener(e -> {
            areaReporte.setText("");
            mostrar(LOGIN);
        });

        return panel;
    }

    // ---------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // si falla, se usa el look and feel por defecto
            }
            new MainGUI().setVisible(true);
        });
    }
}
