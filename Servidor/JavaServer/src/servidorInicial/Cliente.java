package servidorInicial;


import java.net.*;
import java.io.*;

//Cliente en Java de Prueba
public class Cliente {
    public static void main(String[] args) throws IOException {
        // Creamos un socket y nos conectamos al servidor en el puerto 8080
        Socket socket = new Socket("localhost", 8080);
        System.out.println("Conectado al servidor en " + socket.getInetAddress() + ":" + socket.getPort());

        // Creamos un lector y un escritor para el socket
        BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

        // Enviamos una solicitud al servidor
        escritor.println("Hola servidor, soy el cliente.");

        // Leemos la respuesta del servidor y la imprimimos en pantalla
        String respuesta = lector.readLine();
        System.out.println("Respuesta del servidor: " + respuesta);

        // Cerramos el socket
        socket.close();
    }
}