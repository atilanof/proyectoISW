package icai.dtc.isw.ui;

import icai.dtc.isw.domain.Empresa;
import icai.dtc.isw.domain.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmpresaPanel extends JPanel {

    private final EmpresaApi api = new EmpresaApi();
    private final User currentUser;
    private final String[] sectores;
    private final AppMovilMock host; // referencia a la ventana para refrescar PERFIL

    private JPanel contentCard;

    public EmpresaPanel(User user, String[] sectores, AppMovilMock host) {
        this.currentUser = user;
        this.sectores = sectores.clone();
        this.host = host;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        contentCard = new JPanel(new GridBagLayout());
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));
        add(contentCard, BorderLayout.CENTER);

        cargarEstado();
    }

    private void cargarEstado() {
        contentCard.removeAll();

        String mail = safeEmail();
        Empresa emp = api.getEmpresa(mail);

        JLabel title = new JLabel("Mi Empresa", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(20, 40, 80));

        GridBagConstraints gbc = UIUtils.baseGbc();
        gbc.gridy = 0; gbc.insets = new Insets(8, 12, 8, 12); contentCard.add(title, gbc);

        if (emp == null) {
            JLabel info = new JLabel("Aún no has completado tu perfil de empresa.");
            info.setForeground(new Color(95, 105, 125));

            JButton btnCompletar = UIUtils.primaryButton("Completar perfil");
            btnCompletar.addActionListener(e -> mostrarFormulario());

            gbc.gridy = 1; gbc.insets = new Insets(4, 12, 8, 12); contentCard.add(info, gbc);
            gbc.gridy = 2; gbc.insets = new Insets(12, 12, 12, 12); contentCard.add(btnCompletar, gbc);
        } else {
            JButton btnAnuncio = UIUtils.primaryButton("Poner anuncio");
            btnAnuncio.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Funcionalidad próximamente.", "Poner anuncio", JOptionPane.INFORMATION_MESSAGE));
            gbc.gridy = 1; gbc.insets = new Insets(12, 12, 12, 12);
            contentCard.add(btnAnuncio, gbc);
        }

        contentCard.revalidate();
        contentCard.repaint();
    }

    private void mostrarFormulario() {
        JTextField txtNombre    = UIUtils.styledTextField(22);
        JTextField txtNif       = UIUtils.styledTextField(22);
        JComboBox<String> cboSector = UIUtils.styledCombo(sectores);
        JTextField txtUbicacion = UIUtils.styledTextField(22);

        if (cboSector.getItemCount() > 0) cboSector.setSelectedIndex(0);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(8,8,8,8));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = UIUtils.baseGbc();
        gbc.gridy=0; form.add(new JLabel("Nombre de la empresa:"), gbc);
        gbc.gridy=1; form.add(txtNombre, gbc);
        gbc.gridy=2; form.add(new JLabel("NIF/CIF:"), gbc);
        gbc.gridy=3; form.add(txtNif, gbc);
        gbc.gridy=4; form.add(new JLabel("Sector:"), gbc);
        gbc.gridy=5; form.add(cboSector, gbc);
        gbc.gridy=6; form.add(new JLabel("Ubicación:"), gbc);
        gbc.gridy=7; form.add(txtUbicacion, gbc);

        int r = JOptionPane.showConfirmDialog(
                this, form, "Completar perfil de empresa",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (r == JOptionPane.OK_OPTION) {
            String nombre    = txtNombre.getText().trim();
            String nif       = txtNif.getText().trim();
            String sector    = (String) cboSector.getSelectedItem();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || nif.isEmpty() || sector == null || sector.isBlank() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = api.saveEmpresa(safeEmail(), nombre, nif, sector, ubicacion);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Perfil guardado correctamente.", "Mi Empresa", JOptionPane.INFORMATION_MESSAGE);

                // 🔄 Notificar al host para que reconstruya la tarjeta PERFIL y navegar allí
                if (host != null) {
                    host.refreshPerfil();
                    host.showPerfil();
                }

                cargarEstado(); // Este panel pasa a mostrar "Poner anuncio"
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String safeEmail() {
        try { return (String) currentUser.getClass().getMethod("getEmail").invoke(currentUser); }
        catch (Exception e) { return null; }
    }
}
