package client;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote {
    void backup(File file, int replicationDeg, boolean useEnhancements) throws IOException, InterruptedException;
    void delete(File file) throws IOException,RemoteException;
    void restore(File file) throws IOException, RemoteException;
    void reclaim(int reclaimed_space) throws IOException;
    void state() throws RemoteException;
    void exit() throws RemoteException;
}
