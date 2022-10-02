package py.una.server.tcp;

import py.una.entidad.NIS;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class TCPMultiServer {

    //variables compartidas
    int puerto = 4444;
    boolean listening = true;
    List<TCPServerHilo> hilosClientes= new ArrayList<>(); //almacenar los hilos (no se utiliza en el ejemplo, se deja para que el alumno lo utilice)
    List<NIS> usuarios = new ArrayList<>(); //almacenar una lista de usuarios (no se utiliza, se deja para que el alumno lo utilice)

    public void ejecutar() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(puerto);
        } catch (IOException e) {
            System.err.println("No se puede iniciar el servidor en el puerto especificado");
            System.exit(1);
        }
        System.out.println("Servidor iniciado en el puerto: " + puerto);

        while (listening) {
            TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept(), this);
            hilosClientes.add(hilo);
            hilo.start();
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        TCPMultiServer tms = new TCPMultiServer();
        tms.ejecutar();
    }
}
