package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerDAOTest {

    private CustomerDAO customerDAO;

    @BeforeEach
    public void setUp() {
        customerDAO = Mockito.mock(CustomerDAO.class);
    }

    @Test
    public void testGetCustomerById() {
        // Crear un objeto de cliente simulado
        Customer expectedCustomer = new Customer("123", "Atilano");

        // Simular el comportamiento del método getCustomerById
        when(customerDAO.getCliente(123)).thenReturn(expectedCustomer);

        // Llamar al método
        Customer actualCustomer = customerDAO.getCliente(123);

        // Verificar que el resultado es correcto
        assertNotNull(actualCustomer);
        assertEquals("123", actualCustomer.getId());
        assertEquals("Atilano", actualCustomer.getName());


    }
}
