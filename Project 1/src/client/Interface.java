package client;

/**
 * Created by catarina on 21-03-2017.
 */

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote {
    void backup(File file, int replicationDeg) throws IOException;
    void delete(String peer_ap, File file) throws RemoteException;
    void restore(String peer_ap, File file) throws RemoteException;
    void reclaim(String peer_ap, int reclaimed_space) throws RemoteException;
    void state() throws RemoteException;
    void exit() throws RemoteException;
}
