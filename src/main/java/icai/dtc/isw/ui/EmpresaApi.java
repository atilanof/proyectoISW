package icai.dtc.isw.ui;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Empresa;

import java.util.HashMap;

public class EmpresaApi {

    public Empresa getEmpresa(String mail) {
        Client c = new Client();
        HashMap<String,Object> s = new HashMap<>();
        s.put("mail", mail);
        s = c.sentMessage("/empresa/get", s);
        Object o = s.get("empresa");
        return (o instanceof Empresa) ? (Empresa) o : null;
    }

    public boolean saveEmpresa(String mail, String empresa, String nif, String sector, String ubicacion) {
        Client c = new Client();
        HashMap<String,Object> s = new HashMap<>();
        s.put("mail", mail);
        s.put("empresa", empresa);
        s.put("nif", nif);
        s.put("sector", sector);
        s.put("ubicacion", ubicacion); // NUEVO
        s = c.sentMessage("/empresa/save", s);
        return Boolean.TRUE.equals(s.get("ok"));
    }
}
