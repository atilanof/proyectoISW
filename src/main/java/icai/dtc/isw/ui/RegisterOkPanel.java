package icai.dtc.isw.ui;

import javax.swing.*;
import java.awt.*;

public class RegisterOkPanel extends JPanel {
    public RegisterOkPanel(Runnable onOk) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        card.setLayout(new GridBagLayout());

        JLabel lbl = new JLabel("Se ha registrado correctamente.", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(new Color(20, 120, 70));

        JButton btnOK = UIUtils.primaryButton("OK");
        btnOK.addActionListener(e -> onOk.run());

        GridBagConstraints gbc = UIUtils.baseGbc();
        gbc.gridy = 0; gbc.insets = new Insets(6, 8, 8, 8); card.add(lbl, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(8, 8, 0, 8); card.add(btnOK, gbc);

        add(card);
    }
}
