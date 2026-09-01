package icai.dtc.isw.ui;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Set;

/** Tarjeta con mapa interactivo + botones +/- y popup al pin. */
public class CompanyMapPanel extends JPanel {

    private final JXMapViewer map;
    private final JLabel status;

    // Datos del pin
    private GeoPosition pinPos;
    private String pinTitle = "";

    public CompanyMapPanel() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(new UIUtils.RoundedBorder(16, new Color(230, 235, 245)));

        // ---- Definir User-Agent global (algunos servidores OSM lo requieren)
        try {
            String agent = System.getProperty("http.agent");
            if (agent == null || agent.isBlank()) {
                System.setProperty("http.agent", "Connexa/1.0 (+https://connexa.local)");
            }
        } catch (SecurityException ignored) {}

        // ---- TileFactoryInfo en HTTPS
        TileFactoryInfo httpsOSM = new TileFactoryInfo(
                1, 17, 17, 256, true, true,
                "https://tile.openstreetmap.org",  // base
                "x", "y", "z") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                // JXMapViewer usa "zoom inverso": 1 (lejos) .. 17 (cerca)
                int z = 17 - zoom;
                return String.format("%s/%d/%d/%d.png", baseURL, z, x, y);
            }
        };

        // --- Mapa OSM (HTTPS)
        map = new JXMapViewer();
        var tileFactory = new DefaultTileFactory(httpsOSM);
        tileFactory.setThreadPoolSize(8);
        map.setTileFactory(tileFactory);
        map.setZoom(5);
        map.setAddressLocation(new GeoPosition(40.4168, -3.7038)); // Madrid por defecto
        map.setPreferredSize(new Dimension(320, 240));

        // --- Estado
        status = new JLabel("Cargando mapa…");
        status.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        status.setForeground(new Color(95, 105, 125));

        // --- Overlay con botones +/- (capa superior)
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(new OverlayLayout(overlay));

        // capa base: el mapa
        overlay.add(map);

        // capa superior: panel botones en esquina inferior derecha
        JPanel zoomPanel = new JPanel();
        zoomPanel.setOpaque(false);
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.Y_AXIS));
        zoomPanel.setAlignmentX(1.0f);
        zoomPanel.setAlignmentY(1.0f);
        zoomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        JButton btnPlus  = new JButton("+");
        JButton btnMinus = new JButton("–");
        for (JButton b : new JButton[]{btnPlus, btnMinus}) {
            b.setFocusable(false);
            b.setBackground(Color.WHITE);
            b.setBorder(new UIUtils.RoundedBorder(10, new Color(220,226,235)));
            b.setPreferredSize(new Dimension(38, 32));
            b.setMaximumSize(b.getPreferredSize());
            b.setAlignmentX(1.0f);
        }
        btnPlus.addActionListener(e -> map.setZoom(Math.max(1, map.getZoom() - 1)));
        btnMinus.addActionListener(e -> map.setZoom(map.getZoom() + 1));
        zoomPanel.add(btnPlus);
        zoomPanel.add(Box.createVerticalStrut(8));
        zoomPanel.add(btnMinus);

        overlay.add(zoomPanel); // queda encima

        add(status, BorderLayout.NORTH);
        add(overlay, BorderLayout.CENTER);

        // --- Popup cuando se hace click cerca del pin
        map.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (pinPos == null) return;

                // Posición del pin en coordenadas de píxel del mundo (para el zoom actual)
                Point2D world = map.getTileFactory().geoToPixel(pinPos, map.getZoom());
                Rectangle viewport = map.getViewportBounds();

                // Convertir a coordenadas relativas al viewport
                int px = (int) Math.round(world.getX() - viewport.getX());
                int py = (int) Math.round(world.getY() - viewport.getY());
                Point pinPoint = new Point(px, py);

                // Si el click es cercano al pin (radio 14 px), mostramos popup
                if (pinPoint.distance(e.getPoint()) <= 14) {
                    JOptionPane.showMessageDialog(
                            CompanyMapPanel.this,
                            (pinTitle == null || pinTitle.isBlank()) ? "Ubicación" : pinTitle,
                            "Empresa",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    /** Centra el mapa y coloca un pin con título (nombre de la empresa). */
    public void setAddressAsync(String address, String title) {
        this.pinTitle = title;
        status.setText("Buscando ubicación: " + address);
        new SwingWorker<Geocoding.LatLon, Void>() {
            @Override protected Geocoding.LatLon doInBackground() throws Exception {
                return Geocoding.geocode(address);
            }
            @Override protected void done() {
                try {
                    Geocoding.LatLon ll = get();
                    if (ll == null) {
                        status.setText("No se pudo localizar la dirección.");
                        return;
                    }
                    pinPos = new GeoPosition(ll.lat, ll.lon);
                    map.setAddressLocation(pinPos);
                    map.setZoom(4); // acercamos
                    putMarker(pinPos);
                    status.setText("Ubicación: " + String.format("%.5f, %.5f", ll.lat, ll.lon));
                } catch (Exception e) {
                    status.setText("Error de geocodificación.");
                }
            }
        }.execute();
    }

    private void putMarker(GeoPosition pos) {
        // Usar DefaultWaypoint en lugar de instanciar la interfaz Waypoint
        DefaultWaypoint wp = new DefaultWaypoint(pos);
        Set<DefaultWaypoint> set = Collections.singleton(wp);

        WaypointPainter<DefaultWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(set);
        map.setOverlayPainter(painter);
    }
}
