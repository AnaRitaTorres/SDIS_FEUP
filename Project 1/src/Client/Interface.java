package Client;

/**
 * Created by catarina on 21-03-2017.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote {
    String backup() throws RemoteException;
    String delete() throws RemoteException;
    String restore() throws RemoteException;
    String reclaim() throws RemoteException;
}
