package py.una.server.tcp;

import java.io.*;
import java.net.*;

public class TCPClient_timeout {

    public static void main(String[] args) throws Exception {

        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int TimeOutConexion = 7000; //milisegundos para que no tarde mas de 7 segundos en conectarse
		int TimeOutRecepcion = 5000; //milisegundos para que no tarde mas de 5 segundos en leer un mensaje
        long ini = 0;
        long fin = 0;


        try {

            SocketAddress sockaddr = new InetSocketAddress("localhost", 4444);// crea el socket para la conexion al servidor
            //SocketAddress sockaddr = new InetSocketAddress("200.10.229.161", 8080);
            kkSocket = new Socket(); //creo un nuevo socket

   	        ini = System.currentTimeMillis();//devuelve el valor en milosegundos del tiempo actual
            kkSocket.connect(sockaddr, TimeOutConexion);//el connect trata de conectar al socket del servidor creado anteriormente, le paso el socket y le paso el time en milisegundos de la conexion
            kkSocket.setSoTimeout(TimeOutRecepcion);//agrego el time out de recepcion
			
            // enviamos nosotros como cliente al servidor
            out = new PrintWriter(kkSocket.getOutputStream(), true);

            //viene del servidor a nosotros el cliente
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));

        }catch (SocketTimeoutException e){
            fin = System.currentTimeMillis();
            System.err.println("Fallo de Timeout de conexion en " + TimeOutConexion);
            System.err.println("Duracion " + (fin-ini));
            System.exit(1);
        }catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//buffer de salida es para escribir el teclado, parte de la consola
        String fromServer;//lo que viene del servidor
        String fromUser;//lo que el usuario teclea

		try{
			while ((fromServer = in.readLine()) != null) {//asigna a fromServer lo que viene del servidor y ve si no es null
				System.out.println("Servidor: " + fromServer);//imprime en consola lo que viene del servidor
				if (fromServer.equals("Bye")) {//si me dice bye entonces kill client
					break;
				}
				fromUser = stdIn.readLine();// lo que yo le escribo como usuario desde mi teclado
				if (fromUser != null) {
					System.out.println("Cliente: " + fromUser);//si cargo algo imprime en pantalla
					//escribimos al servidor
					out.println(fromUser);
				}
			}
		}catch(SocketTimeoutException exTime){
			System.out.println("Tiempo de espera agotado para recepcion de datos del servidor " );
		}
		//Cerrramos las conexiones
        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }
}
