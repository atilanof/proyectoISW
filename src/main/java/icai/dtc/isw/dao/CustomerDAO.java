package icai.dtc.isw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.User;

public class CustomerDAO {

	public void getClientes(ArrayList<Customer> lista) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM users");
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
            	lista.add(new Customer(rs.getString(1),rs.getString(2)));
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }

	}
	public User getCliente(String username,String password) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		User cu=null;
		String consulta = "SELECT * FROM users WHERE mail = ? and password= ?";

		try (PreparedStatement pst = con.prepareStatement(consulta)) {
			// Asignar el valor del parámetro
			pst.setString(1,username);  // El primer parámetro "?" se reemplaza por el valor de 'id'
			pst.setString(2,password);
			try (ResultSet rs = pst.executeQuery()) {
				// Procesar el resultado
				if (rs.next()) {
					cu = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));  // Obtener los datos de la fila resultante

				}
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return cu;
		//return new Customer("1","Atilano");
	}

    public boolean insertCliente(User user) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO users (id, mail, password, username) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            int id = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
            pst.setInt(1, id);
            pst.setString(2, user.getEmail());     // ojo: columna = mail
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getUsername());
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
		
		CustomerDAO customerDAO=new CustomerDAO();
		ArrayList<Customer> lista= new ArrayList<>();
		customerDAO.getClientes(lista);
		
		
		 for (Customer customer : lista) {			
			System.out.println("He leído el id: "+customer.getId()+" con nombre: "+customer.getName());
		}
		
	
	}

}
