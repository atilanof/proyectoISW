package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.User;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

	/** Lista de clientes de demo: id + username (no el mail) */
	public void getClientes(ArrayList<Customer> lista) {
		Connection con = ConnectionDAO.getInstance().getConnection();
		String sql = "SELECT id, username FROM users";
		try (PreparedStatement pst = con.prepareStatement(sql);
			 ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				lista.add(new Customer(
						rs.getString("id"),
						rs.getString("username")
				));
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Login: busca por mail y password y construye correctamente el User
	 * Columnas reales: id, mail, password, username
	 */
	public User getCliente(String email, String password) {
		Connection con = ConnectionDAO.getInstance().getConnection();
		String sql = "SELECT id, username, password, mail FROM users WHERE mail = ? AND password = ?";
		try (PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, email);
			pst.setString(2, password);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					// Orden del constructor: (id, username, password, email)
					return new User(
							rs.getString("id"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("mail")
					);
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	/** Comprueba si existe un usuario por email (mail) */
	public boolean existsByEmail(String email) {
		Connection con = ConnectionDAO.getInstance().getConnection();
		String sql = "SELECT 1 FROM users WHERE mail = ? LIMIT 1";
		try (PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, email);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** Inserta un usuario respetando el orden real de columnas en la BD */
	public boolean insertCliente(User user) {
		Connection con = ConnectionDAO.getInstance().getConnection();
		String sql = "INSERT INTO users (id, mail, password, username) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pst = con.prepareStatement(sql)) {
			int id = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
			pst.setInt(1, id);
			pst.setString(2, user.getEmail());     // mail
			pst.setString(3, user.getPassword());  // password
			pst.setString(4, user.getUsername());  // username
			return pst.executeUpdate() == 1;
		} catch (SQLException e) {
			// Duplicado: Postgres 23505, MySQL 1062
			if ("23505".equals(e.getSQLState()) || e.getErrorCode() == 1062) {
				return false;
			}
			e.printStackTrace();
			return false;
		}
	}

	/* --- (Opcional) utilidades extra si las necesitas más adelante --- */

	public User getByEmail(String email) {
		Connection con = ConnectionDAO.getInstance().getConnection();
		String sql = "SELECT id, username, password, mail FROM users WHERE mail = ?";
		try (PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, email);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getString("id"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("mail")
					);
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
}
