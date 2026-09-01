package icai.dtc.isw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import icai.dtc.isw.configuration.PropertiesISW;

import icai.dtc.isw.controler.UserControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.User;
import icai.dtc.isw.message.Message;

public class SocketServer extends Thread {
    public static int port = Integer.parseInt(PropertiesISW.getInstance().getProperty("port"));

    protected Socket socket;

    private SocketServer(Socket socket) {
        this.socket = socket;
        // Configure connections
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            // first read the object that has been sent
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            Message mensajeIn = (Message) objectInputStream.readObject();

            // Object to return informations
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            Message mensajeOut = new Message();

            HashMap<String, Object> session = mensajeIn.getSession();



            switch (mensajeIn.getContext()) {

                case "/loginUser": {
                    String username = (String) session.get("username");
                    String password = (String) session.get("password");

                    UserControler uc = new UserControler();
                    User u = uc.login(username, password);

                    mensajeOut.setContext("/loginResponse");
                    if (u != null) {
                        session.put("user", u);
                    } else {
                        session.put("error", "Usuario o contraseña incorrectos");
                    }
                    mensajeOut.setSession(session);
                    objectOutputStream.writeObject(mensajeOut);
                    break;
                }

                case "/registerUser": {
                    String username = (String) session.get("username");
                    String password = (String) session.get("password");
                    String email = (String) session.get("email");

                    UserControler uc = new UserControler();
                    mensajeOut.setContext("/registerResponse");
                    try {
                        User u = uc.register(username, password, email);
                        session.put("ok", true);
                        session.put("user", u);
                    } catch (IllegalArgumentException ex) {
                        session.put("ok", false);
                        session.put("error", ex.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        session.put("ok", false);
                        session.put("error", "REGISTER_FAILED");
                    }
                    objectOutputStream.flush();
                    mensajeOut.setSession(session);
                    objectOutputStream.writeObject(mensajeOut);
                    break;
                }
                case "/empresa/get": {
                    String mail = (String) session.get("mail");
                    icai.dtc.isw.controler.EmpresaControler ec = new icai.dtc.isw.controler.EmpresaControler();
                    icai.dtc.isw.domain.Empresa emp = ec.getEmpresa(mail);

                    mensajeOut.setContext("/empresaGetResponse");
                    session.put("empresa", emp);
                    mensajeOut.setSession(session);
                    objectOutputStream.writeObject(mensajeOut);
                    objectOutputStream.flush();
                    break;
                }

                case "/empresa/save": {
                    String mail      = (String) session.get("mail");
                    String empresa   = (String) session.get("empresa");
                    String nif       = (String) session.get("nif");
                    String sector    = (String) session.get("sector");
                    String ubicacion = (String) session.get("ubicacion"); // NUEVO

                    icai.dtc.isw.controler.EmpresaControler ec = new icai.dtc.isw.controler.EmpresaControler();
                    boolean ok = ec.save(mail, empresa, nif, sector, ubicacion);

                    mensajeOut.setContext("/empresaSaveResponse");
                    session.put("ok", ok);
                    if (!ok) session.put("error", "EMPRESA_SAVE_FAILED");
                    mensajeOut.setSession(session);
                    objectOutputStream.writeObject(mensajeOut);
                    objectOutputStream.flush();
                    break;
                }



                default:
                    System.out.println("\nParámetro no encontrado: " + mensajeIn.getContext());
                    break;
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {}
            try {
                if (out != null) out.close();
            } catch (IOException ignored) {}
            try {
                if (socket != null) socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("SocketServer Example - Listening port " + port);
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while (true) {
                new SocketServer(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null) server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
