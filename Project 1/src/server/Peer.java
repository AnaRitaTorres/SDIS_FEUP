package server;

/**
 * Created by catarina on 21-03-2017.
 */
import channels.MC;
import channels.MDB;
import channels.MDR;
import client.Interface;
import java.io.File;
import java.io.IOException;
import java.net.MulticastSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class Peer implements Interface{

    static int DEFAULT_PORT= 1099;

    private static MC mc;
    private static MDB mdb;
    private static MDR mdr;
    private static MulticastSocket socket;

    public void delete(String peer_ap, File file){}
    public void restore(String peer_ap, File file){}
    public void reclaim(String peer_ap, int reclaimed_space){}
    public void state(){}

    private String peer_ap;

    public static void main(String args[]) throws IOException{

        Peer peer = new Peer();

        socket = new MulticastSocket();

        new Thread(mc).start();
        new Thread(mdb).start();
        new Thread(mdr).start();

        peer.run(args);
    }

    public void run(String[] args){


        if (args.length != 1){
            System.out.println("Wrong number of arguments");
            return;
        }

        peer_ap = args[0];

        initializeRmi();

    }

    public void initializeRmi(){
        Registry registry;
        Peer peer = new Peer();
        try {
            Interface rmi = (Interface) UnicastRemoteObject.exportObject(peer, 0);
            try {
                registry = LocateRegistry.createRegistry(DEFAULT_PORT);
                registry.bind(peer_ap, rmi);
            } catch (ExportException e) {
                System.out.println("Peer warning: " + e.toString());
                registry = LocateRegistry.getRegistry(1099);
            }
            catch(AlreadyBoundException e){
                System.out.println("Peer warning: " + e.toString());
            }
        }
        catch(RemoteException e){
            System.err.println("Peer error: " + e.toString());
            e.printStackTrace();
        }
    }

    public void backup(File file, int replicationDeg){

        //Chamar o BackupProtocol

    }
}