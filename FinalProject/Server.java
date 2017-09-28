
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Server {

    //Metodo para obtener fecha con milisegundos
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static void main(String[] args) throws IOException {

        //Contador de ejecucion
        long startTime = System.currentTimeMillis();

        System.out.println("SERVIDOR UDP");
        System.out.println("Cada cuantos paquetes recibidos quiere que se le pregunte"
                + " si el servidor debe ser apagado?: ");
        Scanner scan = new Scanner(System.in);
        int numPregunta = scan.nextInt();

        //Puerto de recepcion y creacion del socket
        System.out.println("Introduzca el puerto de recepcion:");
        scan = new Scanner(System.in);
        int portListenner = scan.nextInt();

        DatagramSocket serverSocket = new DatagramSocket(portListenner);

        //Tamaño y creacion de paquetes
        //System.out.println("Introduzca tamanio max del paquete:");
        //scan = new Scanner(System.in);
        int length = 32768;

        //
        boolean continuar = true;

        int totalPaquetes = 0;
        int totalBytes = 0;
        PrintStream FILE = new PrintStream(new FileOutputStream("server.txt"));
        long totalTimeTransmission = 0;
        //Contador de tiempo para paquetes
        long startTimePacket = System.currentTimeMillis();

        long startDelayStart = System.currentTimeMillis();
        long totalTimeDelay = 0;

        //Ciclo a la escucha de paquete
        while (continuar) {
            boolean recibido = false;
            if (totalPaquetes == 1) {
                long endTimeDelay = System.currentTimeMillis();
                totalTimeDelay = endTimeDelay - startDelayStart;
            }

            System.out.println("Esperando paquetes...");

            byte[] receiveData = new byte[length];
            byte[] sendData = new byte[length];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            if (receivePacket.getLength() > 0) {
                recibido = true;
            }

            if (recibido) {
                //Contador para paquetes recibidos concluido
                long endTimePacket = System.currentTimeMillis();
                long totalTimePacket = endTimePacket - startTimePacket;
                totalTimeTransmission = totalTimeTransmission + totalTimePacket;
                startTimePacket = System.currentTimeMillis();
                recibido = false;
            }

            if (receivePacket.getLength() < length) {

                String hora = getCurrentTimeStamp();

                byte[] outBuffer = new java.util.Date().toString().getBytes();
                String data = new String(receivePacket.getData());

                String[] headers = data.split("\\s+");

                String type = headers[0];

                String sequenceNumber = headers[1];

                String message = "";

                for (int i = 2; i < headers.length; i++) {
                    message += headers[i];
                    message += " ";
                }

                totalBytes = totalBytes + receivePacket.getLength();

                System.out.println(
                        "IP de origen: " + receivePacket.getAddress() + "\n"
                        + "Puerto: " + receivePacket.getPort() + "\n"
                        + "Tamaño del paquete: " + receivePacket.getLength() + " Bytes \n"
                        + "Tamaño del mensaje: "
                        + (receivePacket.getLength() - (type.getBytes().length + sequenceNumber.getBytes().length))
                        + " Bytes \n"
                        + "Numero de secuencia: " + sequenceNumber + "\n"
                        + "Hora de recibido: " + hora + "\n"
                        + "Mensaje: " + message + "\n"
                );

                FILE.println(
                        "IP de origen: " + receivePacket.getAddress()
                        + " Puerto: " + receivePacket.getPort()
                        + " Tamaño del paquete: " + receivePacket.getLength() + " Bytes"
                        + " Tamaño del mensaje: "
                        + (receivePacket.getLength() - (type.getBytes().length + sequenceNumber.getBytes().length))
                        + " Bytes "
                        + " Numero de secuencia: " + sequenceNumber
                        + " Hora de recibido: " + hora
                        + " Mensaje: " + message
                );

                FILE.println("______________________________________");

                //Respuesta al cliente
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                String capitalizedSentence = message.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket
                        = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
                totalPaquetes++;

                if ((totalPaquetes % numPregunta) == 0) {
                    //Continuar?
                    System.out.println("Desea apagar el servidor? y/n");
                    scan = new Scanner(System.in);

                    if (scan.nextLine().equals("y")) {
                        continuar = false;
                        serverSocket.close();
                    }
                }

                if (type.equals("E")) {
                    break;
                }

            } else {
                System.out.println("Se ha recibido un paquete que excede el tamaño maximo");
            }

        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("------------------------------- \n"
                + "-------RESUMEN----------------- \n"
                + "-------------------------------");

        FILE.println("------------------------------- \n"
                + "-------RESUMEN----------------- \n"
                + "-------------------------------");

        System.out.println("Total de paquetes recibidos: " + totalPaquetes + "\n"
                + "Total de Bytes Recibidos: " + totalBytes + "\n"
                + "Promedio de paquetes paquetes/segundo: " + ((totalTimeTransmission - totalTimeDelay) / 1000.0) / (totalPaquetes - 1) + "\n"
                + "Promedio de Bytes/segundo: " + (totalBytes / ((totalTimeTransmission - totalTimeDelay) / 1000.0)) + "\n"
                + "Duracion de la prueba: " + totalTime / 1000 + " segundos"
        );

        FILE.println("Total de paquetes recibidos: " + totalPaquetes);
        FILE.println("Total de Bytes Recibidos: " + totalBytes);
        FILE.println("Promedio de paquetes paquetes/segundo: " + totalPaquetes / ((totalTimeTransmission - totalTimeDelay) / 1000.0));
        FILE.println("Promedio de Bytes/segundo: " + totalBytes / ((totalTimeTransmission - totalTimeDelay) / 1000.0));
        FILE.println("Duracion de la prueba: " + totalTime / 1000 + " segundos");

    }
}
