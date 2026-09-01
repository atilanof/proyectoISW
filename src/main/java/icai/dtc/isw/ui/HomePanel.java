package icai.dtc.isw.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class HomePanel extends JPanel {

    public HomePanel(Runnable onLogin, Runnable onRegister) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        card.setLayout(new GridBagLayout());

        JLabel lbl = new JLabel("Bienvenido", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(20, 40, 80));

        JButton btnLogin = UIUtils.primaryButton("Iniciar sesión");
        btnLogin.addActionListener(e -> onLogin.run());

        JButton btnRegister = UIUtils.secondaryButton("Registrarse");
        btnRegister.addActionListener(e -> onRegister.run());

        GridBagConstraints gbc = UIUtils.baseGbc();
        gbc.gridy = 0; gbc.insets = new Insets(4, 8, 12, 8);
        card.add(lbl, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(8, 8, 8, 8);
        card.add(btnLogin, gbc);
        gbc.gridy = 2;
        card.add(btnRegister, gbc);

        add(card);
    }
}
