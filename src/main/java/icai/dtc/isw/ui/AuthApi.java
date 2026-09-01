package icai.dtc.isw.ui;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.User;

import java.util.HashMap;

public class AuthApi {

    public String registerUser(String email, String username, String password) {
        Client cliente = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("email", email);
        session.put("username", username);
        session.put("password", password);

        session = cliente.sentMessage("/registerUser", session);

        Object ok = session.get("ok");
        if (Boolean.TRUE.equals(ok)) return null;
        Object error = session.get("error");
        if (error instanceof String) return (String) error;
        return "No se pudo completar el registro.";
    }

    public User loginUser(String email, String password) {
        Client cliente = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("username", email); // el servidor interpreta como email
        session.put("password", password);

        session = cliente.sentMessage("/loginUser", session);
        return (User) session.get("user");
    }

    // (opcional) método auxiliar existente en tu clase original
    public String recuperarInformacion(int id) {
        Client cliente = new Client();
        HashMap<String,Object> session = new HashMap<>();
        session.put("id", id);
        session = cliente.sentMessage("/getCustomer", session);
        Customer cu = (Customer) session.get("Customer");
        return (cu == null) ? "Error - No encontrado en la base de datos" : cu.getName();
    }

    /* Validaciones básicas */
    public static boolean isEmail(String value) {
        return value != null && value.contains("@") && value.contains(".") && !value.contains(" ");
    }
}
