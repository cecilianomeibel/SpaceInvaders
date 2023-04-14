//Paquete Servidor
package servidor;

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        // Creamos un socket de servidor en el puerto 8080
        ServerSocket servidorSocket = new ServerSocket(8080);

        // Esperamos a que lleguen conexiones entrantes
        while (true) {
            Socket socket = servidorSocket.accept();
            System.out.println("Cliente conectado desde " + socket.getInetAddress() + ":" + socket.getPort());

            // Creamos un lector y un escritor para el socket
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

            // Leemos la solicitud del cliente y le respondemos
            String solicitud = lector.readLine();
            escritor.println("Hola cliente, recibí tu solicitud: " + solicitud);

            // Cerramos el socket
            socket.close();
        }
    }
}

