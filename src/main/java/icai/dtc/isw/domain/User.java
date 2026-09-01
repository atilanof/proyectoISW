package icai.dtc.isw.domain;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String username;
    private final String password; // En demo; en real, usar hash
    private final String email;

    public User( String username, String password, String email) {
        this.id="";
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(String id, String username, String password, String email) {
        this.id=id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getId() { return id; }
}
