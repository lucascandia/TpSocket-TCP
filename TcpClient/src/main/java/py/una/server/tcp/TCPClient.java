package py.una.server.tcp;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class TCPClient {
    private static int menu() {
        Scanner in = new Scanner(System.in);
        int entrada = 0;
        do {
            System.out.println("1. Enviar orden de reconexion");
            System.out.println("2. Registrar consumo");
            System.out.println("3. Informar Conectividad");
            System.out.println("4. Enviar orden de desconexion");
            System.out.println("5. Listar NIS activos");
            System.out.println("6. Listar NIS inactivos");
            System.out.println("7. Salir");
            entrada = in.nextInt();
        } while (entrada != 1 && entrada != 2 && entrada != 3 && entrada != 4 && entrada != 5 && entrada != 6 && entrada != 7);
        return entrada;
    }
    private static class ClaveJson {
        public static final String nroNis ="nroNis";
        public static final String tipoOperacion ="tipoOperacion";
        public static final String listActivo ="listActivo";
        public static final String listInactivo ="listInactivo";
        public static final String consumo ="consumo";
        public static final String mensaje ="mensaje";
        public static final String estado ="estado";
        public static final String conectividad ="conectividad";
    }

    public static void main(String[] args) throws IOException {

        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int puerto = 4444;
        Integer nroNIS;
        boolean isActivo = false;
        int seleccion = 1;
        String fromServer;
        String fromUser;
        float consumo = 0.0f;
        JSONObject PeticionJson = new JSONObject();

        System.out.println("Ingrese el numero de NIS");
        Scanner inNIS = new Scanner(System.in);
        nroNIS = inNIS.nextInt();

        do {
            if (seleccion == 1) {
                if(!isActivo)
                {
                    try {
                        unSocket = new Socket("localhost", puerto);
                        out = new PrintWriter(unSocket.getOutputStream(), true);// enviamos nosotros
                        in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));//viene del servidor
                    } catch (UnknownHostException e) {
                        System.err.println("Host desconocido");
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println("Error de I/O en la conexion al host. Inicia el servidor");
                        System.exit(1);
                    }
                    isActivo = true;
                    JSONObject obj = new JSONObject();
                    obj.put(ClaveJson.tipoOperacion, seleccion);
                    obj.put(ClaveJson.nroNis, nroNIS);
                    fromUser = obj.toJSONString();
                    out.println(fromUser);
                    fromServer = in.readLine();
                    System.out.println("Servidor: " + fromServer);
                }
                else
                {
                    System.out.println("Ya conectado al servidor");
                }
            } else if (seleccion == 2 && isActivo == true)//registrar consumo
            {
                Scanner inConsumo = new Scanner(System.in);
                System.out.println("Ingrese el consumo");
                consumo = inConsumo.nextFloat();
                PeticionJson.put(ClaveJson.nroNis, nroNIS);
                PeticionJson.put(ClaveJson.tipoOperacion, seleccion);
                PeticionJson.put(ClaveJson.consumo, consumo);
                fromUser = PeticionJson.toJSONString();
                out.println(fromUser);
                fromServer = in.readLine();
                System.out.println("Servidor: " + fromServer);
            } else if (seleccion == 3) //informar conectividad
            {
                if(isActivo == true)
                {
                    PeticionJson.put(ClaveJson.nroNis, nroNIS);
                    PeticionJson.put(ClaveJson.tipoOperacion, seleccion);
                    fromUser = PeticionJson.toJSONString();
                    out.println(fromUser);
                    fromServer = in.readLine();
                    JsonObject jsonObject = JsonParser.parseString(fromServer).getAsJsonObject();
                    fromServer=jsonObject.toString();
                    System.out.println("Servidor: " + fromServer);
                }
                else
                {
                    System.out.println("Cliente no conectado al servidor");
                }
            } else if (seleccion == 4 || seleccion==7) //enviar orden de desconexion
            {
                if(isActivo)
                {
                    PeticionJson.put(ClaveJson.tipoOperacion,4);
                    PeticionJson.put(ClaveJson.nroNis, nroNIS);
                    fromUser = PeticionJson.toJSONString();
                    out.println(fromUser);
                    fromServer = in.readLine();
                    out.close();
                    in.close();
                    unSocket.close();
                    System.out.println("Servidor: " + fromServer);
                    isActivo = false;
                }
                else if (seleccion==4 && !isActivo)
                {
                    System.out.println("Cliente no conectado al servidor");
                }
                if (seleccion==7)
                    break;
            } else if (seleccion == 5 && isActivo==true)  //listar NIS activos
            {
                PeticionJson.put(ClaveJson.tipoOperacion, seleccion);
                PeticionJson.put(ClaveJson.nroNis, nroNIS);
                fromUser = PeticionJson.toJSONString();
                out.println(fromUser);
                fromServer = in.readLine();
                System.out.println("Servidor: " + fromServer);
            } else if (seleccion == 6 && isActivo==true) //listar NIS inactivos
            {
                PeticionJson.put(ClaveJson.tipoOperacion, seleccion);
                PeticionJson.put(ClaveJson.nroNis, nroNIS);
                fromUser = PeticionJson.toJSONString();
                out.println(fromUser);
                fromServer = in.readLine();
                System.out.println("Servidor: " + fromServer);
            }
            seleccion = menu();
        }while (true);
        System.out.println("Saliendo...");

    }
}



