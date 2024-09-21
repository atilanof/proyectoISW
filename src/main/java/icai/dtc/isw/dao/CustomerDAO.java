package icai.dtc.isw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import icai.dtc.isw.domain.Customer;

public class CustomerDAO {

	public void getClientes(ArrayList<Customer> lista) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios");
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
            	lista.add(new Customer(rs.getString(1),rs.getString(2)));
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }

	}
	public Customer getCliente(int id) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		Customer cu=null;
		String consulta = "SELECT * FROM usuarios WHERE id = ?";

		try (PreparedStatement pst = con.prepareStatement(consulta)) {
			// Asignar el valor del parámetro
			pst.setInt(1, id);  // El primer parámetro "?" se reemplaza por el valor de 'id'

			try (ResultSet rs = pst.executeQuery()) {
				// Procesar el resultado
				if (rs.next()) {
					cu = new Customer(rs.getString(1), rs.getString(2));  // Obtener los datos de la fila resultante
				}
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return cu;
		//return new Customer("1","Atilano");
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
