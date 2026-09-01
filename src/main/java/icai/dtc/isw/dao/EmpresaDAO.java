package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Empresa;

import java.sql.*;

public class EmpresaDAO {

    public Empresa findByMail(String mail) {
        if (mail == null || mail.isBlank()) return null;
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT mail, empresa, nif, sector, ubicacion FROM empresa WHERE mail = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, mail);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Empresa e = new Empresa();
                    e.setMail(rs.getString("mail"));
                    e.setEmpresa(rs.getString("empresa"));
                    e.setNif(rs.getString("nif"));
                    e.setSector(rs.getString("sector"));
                    e.setUbicacion(rs.getString("ubicacion"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** Inserta o actualiza (upsert) por mail */
    public boolean upsert(Empresa emp) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = """
            INSERT INTO empresa (mail, empresa, nif, sector, ubicacion)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (mail) DO UPDATE
            SET empresa = EXCLUDED.empresa,
                nif      = EXCLUDED.nif,
                sector   = EXCLUDED.sector,
                ubicacion= EXCLUDED.ubicacion
            """;
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, emp.getMail());
            pst.setString(2, emp.getEmpresa());
            pst.setString(3, emp.getNif());
            pst.setString(4, emp.getSector());
            pst.setString(5, emp.getUbicacion());
            return pst.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
