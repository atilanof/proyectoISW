package icai.dtc.isw.controler;

import java.util.ArrayList;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Customer;

public class CustomerControler {
	CustomerDAO customerDAO=new CustomerDAO();
	public void getCustomers(ArrayList<Customer> lista) {
		customerDAO.getClientes(lista);
	}
//	public Customer getCustomer(int username, password) {
//        return(customerDAO.getCliente(id));}
}
