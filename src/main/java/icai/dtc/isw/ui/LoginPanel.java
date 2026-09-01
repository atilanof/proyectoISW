package icai.dtc.isw.ui;

import icai.dtc.isw.domain.User;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {

    private final AuthApi auth;
    private final Consumer<User> onLoginOk;
    private final Runnable onBack;

    private JTextField txtLoginEmail;
    private JPasswordField txtLoginPass;
    private JLabel lblLoginError;

    public LoginPanel(AuthApi auth, Consumer<User> onLoginOk, Runnable onBack) {
        this.auth = auth;
        this.onLoginOk = onLoginOk;
        this.onBack = onBack;

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        card.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(20, 40, 80));

        JLabel lblEmail = new JLabel("Correo:");
        txtLoginEmail = UIUtils.styledTextField(24);

        JLabel lblPass = new JLabel("Contraseña:");
        txtLoginPass = UIUtils.styledPassField(24);

        lblLoginError = new JLabel(" ");
        lblLoginError.setForeground(new Color(200, 30, 30));

        JButton btnAcceder = UIUtils.primaryButton("Acceder");
        btnAcceder.addActionListener(e -> doLogin());

        JButton btnVolver = UIUtils.ghostButton("Volver");
        btnVolver.addActionListener(e -> onBack.run());

        GridBagConstraints gbc = UIUtils.baseGbc();
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

        add(card);
    }

    private void doLogin() {
        lblLoginError.setText(" ");
        String email = txtLoginEmail.getText().trim();
        String pass = new String(txtLoginPass.getPassword());
        if (email.isEmpty() || pass.isEmpty()) {
            lblLoginError.setText("Rellene correo y contraseña.");
            return;
        }
        if (!AuthApi.isEmail(email)) {
            lblLoginError.setText("Correo no válido.");
            return;
        }
        try {
            User u = auth.loginUser(email, pass);
            if (u != null) onLoginOk.accept(u);
            else lblLoginError.setText("Credenciales incorrectas.");
        } catch (Exception ex) {
            lblLoginError.setText("Error de comunicación con el servidor.");
        }
    }
}
