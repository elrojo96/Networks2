
import java.io.IOException;
import static java.lang.Math.toIntExact;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        //Solicitud y creacion del host
        System.out.println("Introduzca el host: ");

        Scanner scan = new Scanner(System.in);
        String hostName = scan.nextLine();
        InetAddress host = InetAddress.getByName(hostName);

        //Solicitud del puerto
        System.out.println("Introduzca el puerto:");
        scan = new Scanner(System.in);
        int port = scan.nextInt();

        //Especifica la velocidad de transmision
        System.out.println("Introduzca la velocidad de transmision (segundos por paquete):");
        scan = new Scanner(System.in);
        float delay = scan.nextFloat();
        //Especificacion de cantidad de paquetes
        System.out.println("Introduzca la cantidad de paquetes que desea enviar:");
        scan = new Scanner(System.in);
        int total = scan.nextInt();

        int cantidadDePaquetesEnviados = 0;

        //Solicitud del tamaño y creaccion del message
        System.out.println("Introduzca tamanio max de los paquetes:");
        scan = new Scanner(System.in);
        int lengthMax = scan.nextInt();

        //Generador de numeros random
        Random randomLength = new Random();

        //Creacion del socket para envio
        DatagramSocket socket = new DatagramSocket();

        //Numero de secuencia
        System.out.println("Indica el numero de secuencia:");
        scan = new Scanner(System.in);
        int sequenceNumber = scan.nextInt();

        //Ciclo de creacion de paquetes de tipo D
        while (cantidadDePaquetesEnviados < total) {

            cantidadDePaquetesEnviados++;

            //Creacion del header
            String header;
            if (cantidadDePaquetesEnviados == total) {
                header = "E " + Integer.toString(sequenceNumber) + " ";

            } else {
                header = "D " + Integer.toString(sequenceNumber) + " ";
            }

            int length = randomLength.nextInt(lengthMax);
            byte message[] = new byte[length];

            //System.out.println("Introduzca los datos del paquete:");
            //scan = new Scanner(System.in);
            String sentence = " Mensaje del paquete " + (cantidadDePaquetesEnviados);

            header = header + sentence;

            if (sentence.getBytes().length > length) {
                System.out.println("El mensaje supera el tamaño permitido por el paquete \n"
                        + "El paquete sera enviado sin mensaje, intentelo en el siguiente \n");
                String vacio = " ";
                message = vacio.getBytes();

            } else {
                message = header.getBytes();
            }

            DatagramPacket packet = new DatagramPacket(message, message.length, host, port);

            ////Velocidad de envio
            Thread velocidad;

            int waitTime = (int) (delay * 1000);
            

            //System.out.println(waitTime);
            Thread.sleep(waitTime);

            socket.send(packet);
            sequenceNumber++;

            //Respuesta del servidor
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);

        }

        socket.close();

    }

}
