//Interconexión de Redes
//Luis Alfonso Rojo Sánchez
//A01113049

import java.io.*;
import java.net.*;

public class UDP {
  public static void main(String args[]) {
    try {
      String host = "10.16.127.108";
      int port = 13;

      byte[] message = "Hola desde la playa!".getBytes();

      // Get the internet address of the specified host
      InetAddress address = InetAddress.getByName(host);

      // Initialize a datagram packet with data and address
      DatagramPacket packet = new DatagramPacket(message, message.length,
          address, port);

      // Create a datagram socket, send the packet through it, close it.
      DatagramSocket dsocket = new DatagramSocket();
      dsocket.send(packet);
      dsocket.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
