package icai.dtc.isw.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JVentana extends JFrame {

    public static final String PANTALLA_HOME = "HOME";
    public static final String PANTALLA_LOGIN = "LOGIN";
    public static final String PANTALLA_REGISTER = "REGISTER";
    public static final String PANTALLA_REGISTER_OK = "REGISTER_OK";

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final AuthApi auth = new AuthApi();

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

    public JVentana() {
        super("CONNEXA APP");
        Image icon = new ImageIcon(getClass().getResource("/icons/connexa_mini.png")).getImage();
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pnlNorte = new UIUtils.GradientBar(new Color(10, 23, 42), new Color(20, 40, 80));
        pnlNorte.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("CONNEXA", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        pnlNorte.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlNorte.add(lblTitulo, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        root.setBackground(new Color(245, 247, 250));
        add(root, BorderLayout.CENTER);

        // Pantallas
        root.add(new HomePanel(
                this::irLogin,
                this::irRegister
        ), PANTALLA_HOME);

        root.add(new LoginPanel(
                auth,
                user -> { // onLoginOk
                    SwingUtilities.invokeLater(() -> new AppMovilMock(user).setVisible(true));
                    dispose();
                },
                this::irHome
        ), PANTALLA_LOGIN);

        root.add(new RegisterPanel(
                auth,
                () -> cards.show(root, PANTALLA_LOGIN), // tras OK, ir a login
                this::irHome
        ), PANTALLA_REGISTER);

        root.add(new RegisterOkPanel(() -> cards.show(root, PANTALLA_HOME)), PANTALLA_REGISTER_OK);

        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        cards.show(root, PANTALLA_HOME);
    }

    /* Navegación rápida */
    private void irHome()    { cards.show(root, PANTALLA_HOME); }
    private void irLogin()   { cards.show(root, PANTALLA_LOGIN); }
    private void irRegister(){ cards.show(root, PANTALLA_REGISTER); }
}
