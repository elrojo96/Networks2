import java.rmi.*;

public interface RegistroIf extends Remote {
  public String myIP() throws java.rmi.RemoteException;
  public String registrame(String mat, String mac) throws java.rmi.RemoteException;

}
