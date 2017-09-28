import java.rmi.*;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
public class Registro {

  public static void main(String args[]){
        
    try{
      String server="//10.16.127.108/bitacora";
      RegistroIf registro = (RegistroIf) Naming.lookup(server);
      InetAddress ip;

      ip = InetAddress.getByName(registro.myIP()); //para usuarios de linux getLocalHost() falla
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mc = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mc.length; i++) {
			sb.append(String.format("%02X%s", mc[i], (i < mc.length - 1) ? "-" : ""));
           }		
String smc= sb.toString();


      String matricula;
      BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Teclear el numero de matricula sin letras ni ceros iniciales");
      matricula = userInput.readLine();
      String resultado=registro.registrame(matricula, smc);
      System.out.println(resultado);
    } catch (Exception e) {
      System.out.println("Exception in main: " + e);
    }

  }

}
