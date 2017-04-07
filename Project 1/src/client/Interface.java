package client;

/**
 * Created by catarina on 21-03-2017.
 */

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote {
    void backup(File file, int replicationDeg) throws IOException, InterruptedException;
    void delete(File file) throws IOException;
    void restore(String peer_ap, File file) throws RemoteException;
    void reclaim(int reclaimed_space) throws IOException;
    void state() throws RemoteException;
    void exit() throws RemoteException;
}