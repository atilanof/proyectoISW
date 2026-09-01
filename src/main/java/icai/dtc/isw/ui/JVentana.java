package icai.dtc.isw.ui;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JVentana extends JFrame {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new JVentana().setVisible(true));
    }

    private int id;
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private static final String PANTALLA_HOME = "HOME";
    private static final String PANTALLA_LOGIN = "LOGIN";
    private static final String PANTALLA_REGISTER = "REGISTER";
    private static final String PANTALLA_REGISTER_OK = "REGISTER_OK";

    private JTextField txtLoginEmail;
    private JPasswordField txtLoginPass;
    private JLabel lblLoginError;

    private JTextField txtRegEmail;
    private JTextField txtRegUser;
    private JPasswordField txtRegPass;
    private JLabel lblRegError;

    public JVentana() {
        super("CONNEXA APP");
        Image icon = new ImageIcon(getClass().getResource("/icons/connexa_mini.png")).getImage();
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel pnlNorte = new GradientBar(new Color(10, 23, 42), new Color(20, 40, 80));
        pnlNorte.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("CONNEXA", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        pnlNorte.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlNorte.add(lblTitulo, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);
        root.setBackground(new Color(245, 247, 250));
        root.add(buildHomePanel(), PANTALLA_HOME);
        root.add(buildLoginPanel(), PANTALLA_LOGIN);
        root.add(buildRegisterPanel(), PANTALLA_REGISTER);
        root.add(buildRegisterOkPanel(), PANTALLA_REGISTER_OK);
        add(root, BorderLayout.CENTER);
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        cards.show(root, PANTALLA_HOME);
    }

    private JPanel buildHomePanel() {
        JPanel wrapper = buildCenterWrapper();
        JPanel card = buildCardPanel();
        JLabel lbl = new JLabel("Bienvenido", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(20, 40, 80));
        JButton btnLogin = primaryButton("Iniciar sesión");
        btnLogin.addActionListener(e -> {
            clearLoginErrors();
            cards.show(root, PANTALLA_LOGIN);
        });
        JButton btnRegister = secondaryButton("Registrarse");
        btnRegister.addActionListener(e -> {
            clearRegisterErrors();
            cards.show(root, PANTALLA_REGISTER);
        });
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();
        gbc.gridy = 0; gbc.insets = new Insets(4, 8, 12, 8);
        card.add(lbl, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(8, 8, 8, 8);
        card.add(btnLogin, gbc);
        gbc.gridy = 2;
        card.add(btnRegister, gbc);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildLoginPanel() {
        JPanel wrapper = buildCenterWrapper();
        JPanel card = buildCardPanel();
        JLabel title = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(20, 40, 80));
        JLabel lblEmail = new JLabel("Correo:");
        txtLoginEmail = styledTextField(24);
        JLabel lblPass = new JLabel("Contraseña:");
        txtLoginPass = styledPassField(24);
        lblLoginError = new JLabel(" ");
        lblLoginError.setForeground(new Color(200, 30, 30));
        JButton btnAcceder = primaryButton("Acceder");
        btnAcceder.addActionListener(e -> doLogin());
        JButton btnVolver = ghostButton("Volver");
        btnVolver.addActionListener(e -> cards.show(root, PANTALLA_HOME));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();
        gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(6, 8, 0, 8); card.add(lblEmail, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(2, 8, 6, 8); card.add(txtLoginEmail, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(6, 8, 0, 8); card.add(lblPass, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(2, 8, 6, 8); card.add(txtLoginPass, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(4, 8, 8, 8); card.add(lblLoginError, gbc);
        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setOpaque(false);
        botones.add(btnVolver);
        botones.add(btnAcceder);
        gbc.gridy = 6; gbc.insets = new Insets(10, 8, 0, 8); card.add(botones, gbc);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildRegisterPanel() {
        JPanel wrapper = buildCenterWrapper();
        JPanel card = buildCardPanel();
        JLabel title = new JLabel("Registro de usuario", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(20, 40, 80));
        JLabel lblEmail = new JLabel("Correo:");
        txtRegEmail = styledTextField(24);
        JLabel lblUser = new JLabel("Nombre de usuario:");
        txtRegUser = styledTextField(24);
        JLabel lblPass = new JLabel("Contraseña:");
        txtRegPass = styledPassField(24);
        lblRegError = new JLabel(" ");
        lblRegError.setForeground(new Color(200, 30, 30));
        JButton btnRegistrar = primaryButton("Registrarse");
        btnRegistrar.addActionListener(e -> doRegister());
        JButton btnVolver = ghostButton("Volver");
        btnVolver.addActionListener(e -> cards.show(root, PANTALLA_HOME));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();
        gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(6, 8, 0, 8); card.add(lblEmail, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(2, 8, 6, 8); card.add(txtRegEmail, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(6, 8, 0, 8); card.add(lblUser, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(2, 8, 6, 8); card.add(txtRegUser, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(6, 8, 0, 8); card.add(lblPass, gbc);
        gbc.gridy = 6; gbc.insets = new Insets(2, 8, 6, 8); card.add(txtRegPass, gbc);
        gbc.gridy = 7; gbc.insets = new Insets(4, 8, 8, 8); card.add(lblRegError, gbc);
        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setOpaque(false);
        botones.add(btnVolver);
        botones.add(btnRegistrar);
        gbc.gridy = 8; gbc.insets = new Insets(10, 8, 0, 8); card.add(botones, gbc);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildRegisterOkPanel() {
        JPanel wrapper = buildCenterWrapper();
        JPanel card = buildCardPanel();
        JLabel lbl = new JLabel("Se ha registrado correctamente.", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(new Color(20, 120, 70));
        JButton btnOK = primaryButton("OK");
        btnOK.addActionListener(e -> {
            clearLoginErrors();
            cards.show(root, PANTALLA_HOME);
        });
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();
        gbc.gridy = 0; gbc.insets = new Insets(6, 8, 8, 8); card.add(lbl, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(8, 8, 0, 8); card.add(btnOK, gbc);
        wrapper.add(card);
        return wrapper;
    }

    private void doRegister() {
        clearRegisterErrors();

        String email = txtRegEmail.getText().trim();
        String user  = txtRegUser.getText().trim();
        String pass  = new String(txtRegPass.getPassword());

        if (email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            lblRegError.setText("Rellene todos los campos.");
            return;
        }
        if (!isEmail(email)) {
            lblRegError.setText("Correo no válido.");
            return;
        }

        try {
            String err = registerUser(email, user, pass);
            if (err == null) {
                // Opcional: mensaje de éxito
                JOptionPane.showMessageDialog(
                        this,
                        "Cuenta creada correctamente. Inicia sesión.",
                        "Registro correcto",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Limpia campos de registro
                txtRegEmail.setText("");
                txtRegUser.setText("");
                txtRegPass.setText("");

                // Ir a INICIAR SESIÓN
                cards.show(root, PANTALLA_LOGIN);
            } else {
                lblRegError.setText(
                        "EMAIL_EXISTS".equalsIgnoreCase(err) ? "El correo ya existe en la base de datos." : err
                );
            }
        } catch (Exception ex) {
            lblRegError.setText("Error de comunicación con el servidor.");
        }
    }

    private void doLogin() {
        clearLoginErrors();
        String email = txtLoginEmail.getText().trim();
        String pass  = new String(txtLoginPass.getPassword());
        if (email.isEmpty() || pass.isEmpty()) {
            lblLoginError.setText("Rellene correo y contraseña.");
            return;
        }
        if (!isEmail(email)) {
            lblLoginError.setText("Correo no válido.");
            return;
        }
        try {
            User c = loginUser(email, pass);
            if (c != null) {
                SwingUtilities.invokeLater(() -> new AppMovilMock(c).setVisible(true));
                dispose();
            } else {
                lblLoginError.setText("Credenciales incorrectas.");
            }
        } catch (Exception ex) {
            lblLoginError.setText("Error de comunicación con el servidor.");
        }
    }

    private void clearRegisterErrors() {
        if (lblRegError != null) lblRegError.setText(" ");
    }
    private void clearLoginErrors() {
        if (lblLoginError != null) lblLoginError.setText(" ");
    }

    private boolean isEmail(String value) {
        return value.contains("@") && value.contains(".") && !value.contains(" ");
    }

    private String registerUser(String email, String username, String password) {
        Client cliente = new Client();
        HashMap<String, Object> session = new HashMap<>();
        String context = "/registerUser";
        session.put("email", email);
        session.put("username", username);
        session.put("password", password);
        session = cliente.sentMessage(context, session);
        Object ok = session.get("ok");
        if (Boolean.TRUE.equals(ok)) return null;
        if (session.get("user") instanceof User) return null;
        Object error = session.get("error");
        if (error instanceof String) return (String) error;
        Object c = session.get("Customer");
        if (c instanceof Customer) return null;

        return "No se pudo completar el registro.";
    }

    private User loginUser(String email, String password) {
        Client cliente = new Client();
        HashMap<String, Object> session = new HashMap<>();
        String context = "/loginUser";
        session.put("username", email);
        session.put("password", password);
        session = cliente.sentMessage(context, session);
        User c = (User) session.get("user");
        return c;
    }
    public String recuperarInformacion() {
        Client cliente = new Client();
        HashMap<String,Object> session = new HashMap<>();
        String context = "/getCustomer";
        session.put("id", id);
        session = cliente.sentMessage(context, session);
        Customer cu = (Customer) session.get("Customer");
        String nombre;
        if (cu == null) {
            nombre = "Error - No encontrado en la base de datos";
        } else {
            nombre = cu.getName();
        }
        return nombre;
    }

    static class AppMovilMock extends JFrame {
        private static final String[] CATEGORIAS_GENERALES = new String[] {
                "Hogar y reparaciones",
                "Salud, belleza y cuidados",
                "Educación y cultura",
                "Eventos y ocio",
                "Negocio y administración",
                "Logística y movilidad",
                "Tecnología y digital"
        };
        private static final Map<String, String[]> ESPECIFICAS = new LinkedHashMap<>();
        static {
            ESPECIFICAS.put("Hogar y reparaciones", new String[] {
                    "Electricidad", "Fontanería", "Cerrajería", "Pintura",
                    "Carpintería", "Albañilería", "Climatización", "Limpieza", "Jardinería"
            });
            ESPECIFICAS.put("Salud, belleza y cuidados", new String[] {
                    "Peluquería", "Estética", "Masajistas", "Fisioterapeutas", "Cuidadores", "Entrenamiento personal"
            });
            ESPECIFICAS.put("Educación y cultura", new String[] {
                    "Enseñanza particular", "Academias", "Música", "Traductores e intérpretes"
            });
            ESPECIFICAS.put("Eventos y ocio", new String[] {
                    "Organización de eventos", "Catering y repostería", "Fotógrafos y vídeo", "Animación y sonido"
            });
            ESPECIFICAS.put("Negocio y administración", new String[] {
                    "Marketing local", "Recursos humanos", "Asesoría y gestoría", "Legal básica", "Formación para comercios"
            });
            ESPECIFICAS.put("Logística y movilidad", new String[] {
                    "Repartidores", "Mudanzas y portes", "Mensajería urgente"
            });
            ESPECIFICAS.put("Tecnología y digital", new String[] {
                    "Informáticos", "Soporte a comercios", "Diseño web y e-commerce"
            });
        }

        private final JLabel tituloLabel;
        private final CardLayout cardLayout;
        private final JPanel panelContenido;
        private final User currentUser;

        public AppMovilMock(User user) {
            super("CONNEXA APP");
            Image icon = new ImageIcon(getClass().getResource("/icons/connexa_mini.png")).getImage();
            setIconImage(icon);

            this.currentUser = user;
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(360, 640);
            setMinimumSize(new Dimension(320, 560));
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            JPanel barraSuperior = new GradientBar(new Color(10, 23, 42), new Color(20, 40, 80));
            barraSuperior.setLayout(new BorderLayout());
            barraSuperior.setBorder(new EmptyBorder(12, 12, 12, 12));
            tituloLabel = new JLabel("Perfil", SwingConstants.CENTER);
            tituloLabel.setForeground(Color.WHITE);
            tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            barraSuperior.add(tituloLabel, BorderLayout.CENTER);
            add(barraSuperior, BorderLayout.NORTH);
            cardLayout = new CardLayout();
            panelContenido = new JPanel(cardLayout);
            panelContenido.setBackground(new Color(245, 247, 250));
            panelContenido.add(crearPantallaPerfil(), "PERFIL");
            panelContenido.add(crearPantallaBusquedas(), "BUSQUEDAS");
            panelContenido.add(crearPantalla("Favoritos"), "FAVORITOS");
            add(panelContenido, BorderLayout.CENTER);
            JPanel barraInferior = new JPanel(new GridLayout(1, 3));
            barraInferior.setBorder(new EmptyBorder(8, 8, 8, 8));
            barraInferior.setBackground(Color.WHITE);
            JButton btnPerfil = navButton("Perfil");
            JButton btnBusquedas = navButton("Búsquedas");
            JButton btnFavoritos = navButton("Favoritos");
            btnPerfil.addActionListener(new CambiarPantalla("PERFIL", "Perfil"));
            btnBusquedas.addActionListener(new CambiarPantalla("BUSQUEDAS", "Búsquedas"));
            btnFavoritos.addActionListener(new CambiarPantalla("FAVORITOS", "Favoritos"));
            barraInferior.add(btnPerfil);
            barraInferior.add(btnBusquedas);
            barraInferior.add(btnFavoritos);
            add(barraInferior, BorderLayout.SOUTH);
        }

        private JPanel crearPantalla(String textoCentro) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(new Color(245, 247, 250));
            JPanel card = new JPanel();
            card.setOpaque(true);
            card.setBackground(Color.WHITE);
            card.setBorder(new RoundedBorder(16, new Color(230, 235, 245)));
            card.setLayout(new GridBagLayout());
            JLabel lbl = new JLabel(textoCentro, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setForeground(new Color(30, 33, 40));
            card.add(lbl);
            wrapper.add(card);
            return wrapper;
        }

        private JPanel crearPantallaPerfil() {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(new Color(245, 247, 250));
            JPanel card = new JPanel(new GridBagLayout());
            card.setOpaque(true);
            card.setBackground(Color.WHITE);
            card.setBorder(new RoundedBorder(16, new Color(230, 235, 245)));
            JLabel title = new JLabel("Tu perfil", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 18));
            title.setForeground(new Color(20, 40, 80));
            JPanel grid = new JPanel(new GridBagLayout());
            grid.setOpaque(false);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(4, 8, 4, 8);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0; g.gridy = 0;
            Map<String, String> data = buildUserDataMap(currentUser);
            for (Map.Entry<String,String> e : data.entrySet()) {
                JLabel k = new JLabel(e.getKey() + ":");
                k.setForeground(new Color(95, 105, 125));
                JLabel v = new JLabel(e.getValue());
                v.setForeground(new Color(30, 33, 40));
                g.gridx = 0; grid.add(k, g);
                g.gridx = 1; grid.add(v, g);
                g.gridy++;
            }
            JButton btnLogout = dangerButton("Cerrar sesión");
            btnLogout.addActionListener(ev -> {
                dispose();
                SwingUtilities.invokeLater(() -> new JVentana().setVisible(true));
            });
            GridBagConstraints gbc = baseGbc();
            gbc.gridy = 0; gbc.insets = new Insets(8, 12, 8, 12); card.add(title, gbc);
            gbc.gridy = 1; gbc.insets = new Insets(4, 12, 8, 12); card.add(grid, gbc);
            gbc.gridy = 2; gbc.insets = new Insets(12, 12, 8, 12); card.add(btnLogout, gbc);
            wrapper.add(card);
            return wrapper;
        }

        private Map<String,String> buildUserDataMap(User u) {
            LinkedHashMap<String,String> m = new LinkedHashMap<>();
            putIfPresent(m, "ID", call(u, "getId"));
            putIfPresent(m, "Usuario", call(u, "getUsername"));
            putIfPresent(m, "Nombre", call(u, "getName"));
            putIfPresent(m, "Email", call(u, "getEmail"));
            putIfPresent(m, "Mail", call(u, "getMail"));
            putIfPresent(m, "Teléfono", call(u, "getPhone"));
            putIfPresent(m, "Dirección", call(u, "getAddress"));
            if (m.isEmpty()) m.put("Usuario", String.valueOf(u));
            return m;
        }

        private void putIfPresent(Map<String,String> map, String key, String val) {
            if (val != null && !val.trim().isEmpty() && !"null".equalsIgnoreCase(val.trim())) map.put(key, val);
        }

        private String call(Object obj, String method) {
            try {
                Method m = obj.getClass().getMethod(method);
                Object v = m.invoke(obj);
                return v == null ? null : String.valueOf(v);
            } catch (Exception e) {
                return null;
            }
        }

        private JPanel crearPantallaBusquedas() {
            JPanel contenedor = new JPanel(new BorderLayout());
            contenedor.setBackground(new Color(245, 247, 250));
            contenedor.setBorder(new EmptyBorder(12, 12, 12, 12));
            JPanel filtrosCard = new JPanel(new GridBagLayout());
            filtrosCard.setBackground(Color.WHITE);
            filtrosCard.setBorder(new RoundedBorder(16, new Color(230, 235, 245)));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 10, 6, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0; gbc.gridy = 0;
            JLabel lblGeneral = new JLabel("Categoría");
            lblGeneral.setForeground(new Color(20, 40, 80));
            JComboBox<String> cboGeneral = styledCombo(CATEGORIAS_GENERALES);
            gbc.weightx = 0; filtrosCard.add(lblGeneral, gbc);
            gbc.gridx = 1; gbc.weightx = 1; filtrosCard.add(cboGeneral, gbc);
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
            JLabel lblEspecifico = new JLabel("Trabajo");
            lblEspecifico.setForeground(new Color(20, 40, 80));
            JComboBox<String> cboEspecifico = styledCombo(new String[]{});
            cboEspecifico.setEnabled(false);
            filtrosCard.add(lblEspecifico, gbc);
            gbc.gridx = 1; gbc.weightx = 1; filtrosCard.add(cboEspecifico, gbc);
            JPanel resultadosCard = new JPanel(new BorderLayout());
            resultadosCard.setBackground(Color.WHITE);
            resultadosCard.setBorder(new RoundedBorder(16, new Color(230, 235, 245)));
            resultadosCard.setPreferredSize(new Dimension(320, 420));
            DefaultListModel<String> model = new DefaultListModel<>();
            JList<String> listaResultados = new JList<>(model);
            listaResultados.setCellRenderer(new CleanListCellRenderer());
            JScrollPane scroll = new JScrollPane(listaResultados);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            resultadosCard.add(scroll, BorderLayout.CENTER);
            contenedor.add(filtrosCard, BorderLayout.NORTH);
            contenedor.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
            contenedor.add(resultadosCard, BorderLayout.SOUTH);
            cboGeneral.addActionListener(e -> {
                String general = (String) cboGeneral.getSelectedItem();
                model.clear();
                cboEspecifico.removeAllItems();
                if (general != null && ESPECIFICAS.containsKey(general)) {
                    for (String s : ESPECIFICAS.get(general)) cboEspecifico.addItem(s);
                    cboEspecifico.setEnabled(true);
                    if (cboEspecifico.getItemCount() > 0) cboEspecifico.setSelectedIndex(0);
                } else {
                    cboEspecifico.setEnabled(false);
                }
            });
            cboEspecifico.addActionListener(e -> {
                if (!cboEspecifico.isEnabled()) return;
                model.clear();
            });
            if (cboGeneral.getItemCount() > 0) cboGeneral.setSelectedIndex(0);
            return contenedor;
        }

        private class CambiarPantalla implements java.awt.event.ActionListener {
            private final String card;
            private final String titulo;
            private CambiarPantalla(String card, String titulo) {
                this.card = card;
                this.titulo = titulo;
            }
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                tituloLabel.setText(titulo);
                cardLayout.show(panelContenido, card);
            }
        }

        private JButton navButton(String text) {
            JButton b = new JButton(text);
            b.setUI(new BasicButtonUI());
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.setBorder(new RoundedBorder(12, new Color(220, 226, 235)));
            b.setFont(new Font("SansSerif", Font.BOLD, 12));
            b.setForeground(new Color(20, 40, 80));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return b;
        }
    }

    private JPanel buildCenterWrapper() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(245, 247, 250));
        return wrapper;
    }

    private JPanel buildCardPanel() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new RoundedBorder(16, new Color(230, 235, 245)));
        return card;
    }

    private static GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        return gbc;
    }

    private JTextField styledTextField(int columns) {
        JTextField f = new JTextField(columns);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, new Color(220, 226, 235)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 251, 253));
        return f;
    }

    private JPasswordField styledPassField(int columns) {
        JPasswordField f = new JPasswordField(columns);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, new Color(220, 226, 235)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 251, 253));
        return f;
    }

    private static JComboBox<String> styledCombo(String[] data) {
        JComboBox<String> c = new JComboBox<>(data);
        c.setBackground(new Color(250, 251, 253));
        c.setBorder(new RoundedBorder(12, new Color(220, 226, 235)));
        c.setFocusable(false);
        return c;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI());
        b.setBackground(new Color(20, 82, 255));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new RoundedBorder(14, new Color(20, 82, 255)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(240, 38));
        return b;
    }

    private JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI());
        b.setBackground(new Color(238, 242, 255));
        b.setForeground(new Color(20, 40, 80));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new RoundedBorder(14, new Color(180, 190, 210)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(240, 38));
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI());
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(20, 40, 80));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new RoundedBorder(14, new Color(200, 210, 225)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(240, 38));
        return b;
    }

    private static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI());
        b.setBackground(new Color(220, 53, 69));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new RoundedBorder(14, new Color(220, 53, 69)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(240, 38));
        return b;
    }

    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;
        private final Color line;
        RoundedBorder(int radius, Color line) {
            this.radius = radius;
            this.line = line;
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(12, 12, 12, 12);
        }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(12, 12, 12, 12);
            return insets;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(line);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    static class GradientBar extends JPanel {
        private final Color c1, c2;
        GradientBar(Color c1, Color c2) {
            this.c1 = c1; this.c2 = c2;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class CleanListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            lbl.setBorder(new EmptyBorder(10, 12, 10, 12));
            if (isSelected) {
                lbl.setBackground(new Color(232, 239, 255));
                lbl.setForeground(new Color(20, 40, 80));
            } else {
                lbl.setBackground(Color.WHITE);
                lbl.setForeground(new Color(30, 33, 40));
            }
            return lbl;
        }
    }
}
