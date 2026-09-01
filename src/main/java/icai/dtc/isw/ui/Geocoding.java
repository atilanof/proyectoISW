package icai.dtc.isw.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Geocoding {
    public static class LatLon {
        public final double lat, lon;
        public LatLon(double lat, double lon) { this.lat = lat; this.lon = lon; }
    }
    public static LatLon geocode(String address) throws Exception {
        if (address == null || address.isBlank()) return null;
        String q = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://nominatim.openstreetmap.org/search?q=" + q + "&format=json&limit=1";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("User-Agent", "Connexa/1.0 (contacto: soporte@connexa.local)");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line; while ((line = br.readLine()) != null) sb.append(line);
            JSONArray arr = new JSONArray(sb.toString());
            if (arr.length() == 0) return null;
            JSONObject obj = arr.getJSONObject(0);
            return new LatLon(Double.parseDouble(obj.getString("lat")), Double.parseDouble(obj.getString("lon")));
        }
    }
}
