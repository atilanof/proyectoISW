package icai.dtc.isw.ui;

import javax.swing.*;
import java.awt.*;

public class ChatsPanel extends JPanel {
    public ChatsPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        JLabel lbl = new JLabel("Chats (próximamente)", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(new Color(30, 33, 40));
        card.add(lbl);

        add(card);
    }
}
