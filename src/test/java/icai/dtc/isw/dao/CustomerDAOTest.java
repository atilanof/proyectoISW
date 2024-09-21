package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerDAOTest {

    @Mock
    private CustomerDAO customerDAO;  // Simulación de CustomerDAO

    @BeforeEach
    public void setUp() {
        // Inicializar los mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomerById() {
        // Crear un objeto de cliente simulado
        Customer expectedCustomer = new Customer("123", "Atilano");

        // Simular el comportamiento del método getCliente
        when(customerDAO.getCliente(123)).thenReturn(expectedCustomer);

        // Llamar al método
        Customer actualCustomer = customerDAO.getCliente(123);

        // Verificar que el resultado es correcto
        assertNotNull(actualCustomer);
        assertEquals("123", actualCustomer.getId());
        assertEquals("Atilano", actualCustomer.getName());

        // Verificar que el método getCliente fue llamado
        verify(customerDAO, times(1)).getCliente(123);
    }
}
