package py.una.server.tcp;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import py.una.entidad.NIS;

import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;


public class TCPServerHilo extends Thread {
    private Socket socket;
    TCPMultiServer servidor;

    private static class  ClaveJson{
        private static final String nroNis ="nroNis";
        private static final String tipoOperacion ="tipoOperacion";
        private static final String listActivo ="listActivo";
        private static final String listInactivo ="listInactivo";
        private static final String consumo ="consumo";
        private static final String mensaje ="mensaje";
        private static final String estado ="estado";
        private static final String conectividad ="conectividad";
    }
    public TCPServerHilo(Socket socket, TCPMultiServer servidor ) {
        super("TCPServerHilo");
        this.socket = socket;
        this.servidor = servidor;
    }
    public static void generarLog(String datos){
        //generar los log en un archivo de texto
        try {
            File file = new File("log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n"+datos);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try
        {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine, outputLine="";
            Integer tipoOperacion= 0;
            Integer nroNIS= 0;
            boolean existe = false;
            NIS Nis = null;
            inputLine = in.readLine();
            JsonObject jsonObject = JsonParser.parseString(inputLine).getAsJsonObject();
            nroNIS=Integer.parseInt(jsonObject.get(ClaveJson.nroNis).getAsString());
            tipoOperacion = Integer.parseInt(jsonObject.get(ClaveJson.tipoOperacion).getAsString());
            Iterator<NIS> iter = servidor.usuarios.iterator();
            while (iter.hasNext()) {
                NIS nis = iter.next();
                if (nis.nroNIS.equals(nroNIS)) {
                    existe = true;
                    Nis = nis;
                    break;
                }
            }
            if (!existe)
            {
                Nis = new NIS(nroNIS, false,0.0f);
                servidor.usuarios.add(Nis);
            }
            do
            {
                JSONObject respuestaJson = new JSONObject();
                if (tipoOperacion.intValue() == 1 )//orden de conexion
                {
                    try {
                        System.out.println("Ejecutando orden de conexion del Nis número: " + nroNIS);
                        Nis.isActivo = true;
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 1);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        respuestaJson.put(ClaveJson.estado, 0);
                        outputLine = respuestaJson.toJSONString();
                    }
                    catch (Exception e)
                    {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 1);
                        respuestaJson.put(ClaveJson.mensaje,e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }

                }else if(tipoOperacion.intValue() == 2 && Nis.isActivo)//registrar consumo
                {
                    try {
                        System.out.println("Ejecutando orden de registro de consumo del Nis número : " + nroNIS + ", con un consumo de: "+ Float.parseFloat(jsonObject.get(ClaveJson.consumo).getAsString()));
                        Nis.consumo = Nis.consumo+Float.parseFloat(jsonObject.get(ClaveJson.consumo).getAsString());
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 2);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        outputLine = respuestaJson.toJSONString();
                    }catch (Exception e)
                    {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 2);
                        respuestaJson.put(ClaveJson.mensaje,e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }
                }
                else if(tipoOperacion.intValue()==3 && Nis.isActivo)//informar conectividad
                {
                    try {
                        System.out.println("Ejecutando orden de informe de conectividad del Nis número: " + nroNIS);
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 3);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        respuestaJson.put(ClaveJson.estado, 0);
                        respuestaJson.put(ClaveJson.conectividad, "Estado Conectividad: " + Nis.isActivo + "; Servidor: " + socket.getLocalSocketAddress().toString() + "; Cliente: " + socket.getRemoteSocketAddress().toString());
                        outputLine = respuestaJson.toJSONString();

                    } catch (Exception e) {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 3);
                        respuestaJson.put(ClaveJson.mensaje, e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }
                }
                else if(tipoOperacion.intValue()==4 && Nis.isActivo)//orde de desconexión
                {
                    try {
                        System.out.println("Ejecutando orden de desconexion del Nis número: " + nroNIS);
                        Nis.isActivo=false;
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 4);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        respuestaJson.put(ClaveJson.estado, 0);
                        outputLine = respuestaJson.toJSONString();
                        out.println(outputLine);
                        break;
                    }catch (Exception e)
                    {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 4);
                        respuestaJson.put(ClaveJson.mensaje,e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }
                }
                else if(tipoOperacion.intValue()==5 && Nis.isActivo)//Listar NIS activos
                {
                    try {
                        System.out.println("Ejecutando orden de listar NIS activos al Nis número: " + nroNIS);
                        respuestaJson = new JSONObject();
                        ArrayList<Integer> usuarioString = new ArrayList<>();
                        iter = servidor.usuarios.iterator();
                        while (iter.hasNext()) {
                            NIS nis = iter.next();
                            if(nis.isActivo)
                                usuarioString.add(nis.nroNIS);
                        }
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 5);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        respuestaJson.put(ClaveJson.estado, 0);
                        respuestaJson.put(ClaveJson.listActivo, usuarioString);
                        outputLine = respuestaJson.toJSONString();
                    }catch (Exception e)
                    {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 5);
                        respuestaJson.put(ClaveJson.mensaje,e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }
                }
                else if(tipoOperacion.intValue()==6 && Nis.isActivo)//Listar NIS inactivos
                {
                    try {
                        System.out.println("Ejecutando orden de listar NIS inactivos al Nis número: " + nroNIS);
                        respuestaJson = new JSONObject();
                        ArrayList<Integer> usuarioString = new ArrayList<>();
                        iter = servidor.usuarios.iterator();
                        while (iter.hasNext()) {
                            NIS nis = iter.next();
                            if(!nis.isActivo)
                                usuarioString.add(nis.nroNIS);
                        }
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 6);
                        respuestaJson.put(ClaveJson.mensaje, "ok");
                        respuestaJson.put(ClaveJson.estado, 0);
                        respuestaJson.put(ClaveJson.listInactivo, usuarioString);
                        outputLine = respuestaJson.toJSONString();
                    }catch (Exception e)
                    {
                        respuestaJson.put(ClaveJson.nroNis, nroNIS);
                        respuestaJson.put(ClaveJson.tipoOperacion, 6);
                        respuestaJson.put(ClaveJson.mensaje,e.toString());
                        respuestaJson.put(ClaveJson.estado, -1);
                        outputLine = respuestaJson.toJSONString();
                    }
                }
                else//error
                {
                    respuestaJson = new JSONObject();
                    respuestaJson.put(ClaveJson.tipoOperacion, -1);
                    respuestaJson.put(ClaveJson.mensaje, "error de operacion");
                    outputLine = respuestaJson.toJSONString();
                }
                generarLog(LocalDateTime.now().toString()+" "+socket.getRemoteSocketAddress().toString()+" "+socket.getLocalSocketAddress().toString()+" "+tipoOperacion);
                generarLog(LocalDateTime.now().toString()+" "+socket.getLocalSocketAddress().toString()+" "+socket.getRemoteSocketAddress().toString()+" "+"R");
                out.println(outputLine);
                inputLine = in.readLine();
                jsonObject = JsonParser.parseString(inputLine).getAsJsonObject();
                tipoOperacion = Integer.parseInt(jsonObject.get(ClaveJson.tipoOperacion).getAsString());
            }while (true);
            out.close();
            in.close();
            socket.close();
            System.out.println("Conexion cerrada con el cliente: "+nroNIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

