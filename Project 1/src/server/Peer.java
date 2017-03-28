package server;

/**
 * Created by catarina on 21-03-2017.
 */
import channels.MC;
import channels.MDB;
import channels.MDR;
import chunk.Chunk;
import client.Interface;
import fileManager.FileManager;
import protocols.BackupProtocol;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Peer implements Interface{

    static int DEFAULT_PORT= 1099;

    private static MulticastSocket socket;

    //Rmi
    private static Registry registry;
    private static String peer_ap;

    //multicast
    private static InetAddress mcAddress, mdbAddress, mdrAddress;
    private static int mcPort, mdbPort, mdrPort;

    private static MC mc;
    private static MDB mdb;
    private static MDR mdr;


    public static void main(String args[]) throws IOException {

        if (!validArguments(args))
            return;

        //socket = new MulticastSocket();

        mc = new MC(mcPort, mcAddress);
        mdb = new MDB(mdbPort, mdbAddress);
        mdr = new MDR(mdrPort, mdrAddress);

        new Thread(mc).start();
        new Thread(mdb).start();
        new Thread(mdr).start();

        initializeRmi();

    }

    public static boolean validArguments(String[] args) throws UnknownHostException {
        if (args.length != 7) {
            System.out.println("Usage: java server.Peer <peer_ap> <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
            return false;
        }

        peer_ap = args[0];

        mcAddress = InetAddress.getByName(args[1]);
        mcPort = Integer.parseInt(args[2]);

        mdbAddress = InetAddress.getByName(args[3]);
        mdbPort = Integer.parseInt(args[4]);

        mdrAddress = InetAddress.getByName(args[5]);
        mdrPort = Integer.parseInt(args[6]);

        return true;
    }

    public static void initializeRmi(){
        Peer peer = new Peer();
        try {
            Interface rmi = (Interface) UnicastRemoteObject.exportObject(peer, 0);
            try {
                registry = LocateRegistry.createRegistry(DEFAULT_PORT);
                registry.bind(peer_ap, rmi);
            } catch (ExportException e) {
                System.out.println("Peer warning: " + e.toString());
                registry = LocateRegistry.getRegistry(DEFAULT_PORT);
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

    public static MC getMc() {
        return mc;
    }

    public static MDB getMdb() {
        return mdb;
    }

    public static MDR getMdr() {
        return mdr;
    }


    @Override
    public void backup(File file, int replicationDeg) throws IOException {

        FileManager fileManager = new FileManager(file, replicationDeg);
        ArrayList<Chunk> chunksToBackup = fileManager.divideFileInChunks();
        for (int i = 0; i< chunksToBackup.size(); i++){
            BackupProtocol.sendPutchunkMessage();
        }

    }

    @Override
    public void exit() throws RemoteException {
        try {
            // Unregister the RMI
            this.registry.unbind(peer_ap);

            // Un-export; this will also remove us from the rmi runtime
            UnicastRemoteObject.unexportObject(this, true);

            System.out.println("Server exiting.");
        } catch (Exception e) {

        }
    }

    public void delete(String peer_ap, File file){}
    public void restore(String peer_ap, File file){}
    public void reclaim(String peer_ap, int reclaimed_space){}
    public void state(){}
}