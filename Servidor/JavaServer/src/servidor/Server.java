package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


//Servidor definitivo
public class Server {
    public static void main(String[] args) throws IOException {
        int puerto = 8080;
        ServerSocket servidor = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto " + puerto);

        while (true) {
            Socket cliente = servidor.accept();
            System.out.println("Conexión establecida con el cliente " + cliente.getInetAddress());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            System.out.println("Hasta aqui todo bien 1");
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
            System.out.println("Hasta aqui todo bien 2");

            // Recibir datos del cliente
            String mensaje = entrada.readLine();
            System.out.println("Hasta aqui todo bien 3");
            System.out.println("Mensaje del cliente recibido: " + mensaje);

            // Procesar los datos
            String respuesta = "Hola, cliente";
            System.out.println("Respuesta enviada al cliente: " + respuesta);

            // Enviar datos al cliente
            salida.println(respuesta);

            // Cerrar la conexión con el cliente
            cliente.close();
            System.out.println("Conexión cerrada");
        }
    }
}