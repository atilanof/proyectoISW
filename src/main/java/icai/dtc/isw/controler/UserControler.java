package icai.dtc.isw.controler;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserControler {

    // DEMO: almacenamiento en memoria. Sustituir por DAO/BD si procede.
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();
    CustomerDAO customerDAO=new CustomerDAO();
    public User register(String username, String password, String email) throws IllegalArgumentException {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                email == null || email.isBlank()) {
            throw new IllegalArgumentException("Datos de registro incompletos");
        }
        if (USERS.containsKey(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        User u = new User(username, password, email);
        boolean ok = customerDAO.insertCliente(u);
        if (!ok) {
            throw new IllegalArgumentException("No se ha podido registrar (¿usuario/correo ya existe?)");
        }
        USERS.put(username, u);
        return u;
    }

    public User login(String username, String password) {

        User u = customerDAO.getCliente(username,password);
        if (u == null) return null;
        if (!u.getPassword().equals(password)) return null;
        return u;
    }
}
