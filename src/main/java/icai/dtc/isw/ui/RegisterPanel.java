package icai.dtc.isw.ui;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private final AuthApi auth;
    private final Runnable onOkGoLogin;
    private final Runnable onBack;

    private JTextField txtRegEmail;
    private JTextField txtRegUser;
    private JPasswordField txtRegPass;
    private JLabel lblRegError;

    public RegisterPanel(AuthApi auth, Runnable onOkGoLogin, Runnable onBack) {
        this.auth = auth;
        this.onOkGoLogin = onOkGoLogin;
        this.onBack = onBack;

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        card.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Registro de usuario", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(20, 40, 80));

        JLabel lblEmail = new JLabel("Correo:");
        txtRegEmail = UIUtils.styledTextField(24);

        JLabel lblUser = new JLabel("Nombre de usuario:");
        txtRegUser = UIUtils.styledTextField(24);

        JLabel lblPass = new JLabel("Contraseña:");
        txtRegPass = UIUtils.styledPassField(24);

        lblRegError = new JLabel(" ");
        lblRegError.setForeground(new Color(200, 30, 30));

        JButton btnRegistrar = UIUtils.primaryButton("Registrarse");
        btnRegistrar.addActionListener(e -> doRegister());

        JButton btnVolver = UIUtils.ghostButton("Volver");
        btnVolver.addActionListener(e -> onBack.run());

        GridBagConstraints gbc = UIUtils.baseGbc();
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

        add(card);
    }

    private void doRegister() {
        lblRegError.setText(" ");
        String email = txtRegEmail.getText().trim();
        String user  = txtRegUser.getText().trim();
        String pass  = new String(txtRegPass.getPassword());

        if (email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            lblRegError.setText("Rellene todos los campos.");
            return;
        }
        if (!AuthApi.isEmail(email)) {
            lblRegError.setText("Correo no válido.");
            return;
        }
        try {
            String err = auth.registerUser(email, user, pass);
            if (err == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cuenta creada correctamente. Inicia sesión.",
                        "Registro correcto",
                        JOptionPane.INFORMATION_MESSAGE
                );
                txtRegEmail.setText("");
                txtRegUser.setText("");
                txtRegPass.setText("");
                onOkGoLogin.run();
            } else {
                lblRegError.setText("EMAIL_EXISTS".equalsIgnoreCase(err) ? "El correo ya existe en la base de datos." : err);
            }
        } catch (Exception ex) {
            lblRegError.setText("Error de comunicación con el servidor.");
        }
    }
}
