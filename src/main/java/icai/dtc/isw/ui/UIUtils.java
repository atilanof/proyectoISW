package icai.dtc.isw.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public final class UIUtils {

    private UIUtils() {}

    /* ----- Estilos y componentes ----- */

    public static GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        return gbc;
    }

    public static JTextField styledTextField(int columns) {
        JTextField f = new JTextField(columns);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, new Color(220, 226, 235)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 251, 253));
        return f;
    }

    public static JPasswordField styledPassField(int columns) {
        JPasswordField f = new JPasswordField(columns);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, new Color(220, 226, 235)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 251, 253));
        return f;
    }

    public static JComboBox<String> styledCombo(String[] data) {
        JComboBox<String> c = new JComboBox<>(data);
        c.setBackground(new Color(250, 251, 253));
        c.setBorder(new RoundedBorder(12, new Color(220, 226, 235)));
        c.setFocusable(false);
        return c;
    }

    public static JButton primaryButton(String text) {
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

    public static JButton secondaryButton(String text) {
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

    public static JButton ghostButton(String text) {
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

    public static JButton dangerButton(String text) {
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

    /* ----- Bordes y gráficos ----- */

    public static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;
        private final Color line;
        public RoundedBorder(int radius, Color line) {
            this.radius = radius;
            this.line = line;
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(12, 12, 12, 12); }
        @Override public Insets getBorderInsets(Component c, Insets insets) { insets.set(12, 12, 12, 12); return insets; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(line);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
    }

    public static class GradientBar extends JPanel {
        private final Color c1, c2;
        public GradientBar(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class CleanListCellRenderer extends DefaultListCellRenderer {
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
