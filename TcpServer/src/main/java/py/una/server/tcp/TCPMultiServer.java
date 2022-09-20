package py.una.server.tcp;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;



public class TCPMultiServer {

	//variables compartidas
	boolean listening = true;
	List<TCPServerHilo> hilosClientes; //almacenar los hilos (no se utiliza en el ejemplo, se deja para que el alumno lo utilice)
	List<String> usuarios; //almacenar una lista de usuarios (no se utiliza, se deja para que el alumno lo utilice)

    public void ejecutar() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);//abre un socket para el servidor en el puerto 4444
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 4444.");
            System.exit(1);
        }
        System.out.println("Puerto abierto: 4444.");

        //while que atiende a todos los clientes
        while (listening) {
        	//se queda escuhando, por cada entrada de solicitud de un cliente abre un nuevo hilo y lo ejecuta
        	TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept()/*socket del cliente*/, this);
            hilosClientes.add(hilo);
            hilo.start();
        }

        serverSocket.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	TCPMultiServer tms = new TCPMultiServer();
    	
    	tms.hilosClientes = new ArrayList<TCPServerHilo>();
    	tms.usuarios = new ArrayList<String>();
    	
    	tms.ejecutar();
    	
    }
}
