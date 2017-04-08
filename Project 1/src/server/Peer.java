package server;

/**
 * Created by catarina on 21-03-2017.
 */
import channels.MC;
import channels.MDB;
import channels.MDR;
import chunk.Chunk;
import client.Interface;
import database.PeerDatabase;
import fileManager.FileManager;
import protocols.BackupProtocol;
import protocols.DeleteProtocol;
import protocols.RestoreProtocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
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

    private static String protocol_version;
    private static int server_id;
    private static int max_size_to_save = 5000000;
    private static int size_occupied = 0;
    private static String path;

    //Rmi
    private static Registry registry;
    private static String peer_ap;

    //multicast
    private static InetAddress mcAddress, mdbAddress, mdrAddress;
    private static int mcPort, mdbPort, mdrPort;

    private static MC mc;
    private static MDB mdb;
    private static MDR mdr;

    private static boolean use_enhancements;

    private static PeerDatabase database = new PeerDatabase();

    public static void main(String args[]) throws IOException {

        if (!validArguments(args))
            return;

        mc = new MC(mcPort, mcAddress);
        mdb = new MDB(mdbPort, mdbAddress);
        mdr = new MDR(mdrPort, mdrAddress);

        new Thread(mc).start();
        new Thread(mdb).start();
        new Thread(mdr).start();

        initializeRmi();

    }

    public static boolean validArguments(String[] args) throws UnknownHostException {
        if (args.length != 9) {
            System.out.println("Usage: java server.Peer <protocol_version> <serverId> <peer_ap> <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
            return false;
        }

        if(!args[0].matches("[1-9][0-9]*.[0-9]+")) {
            return false;
        }

        protocol_version = args[0];
        server_id = Integer.parseInt(args[1]);
        path = "./BackUp/peer" + server_id + "/";
        peer_ap = args[2];

        mcAddress = InetAddress.getByName(args[3]);
        mcPort = Integer.parseInt(args[4]);

        mdbAddress = InetAddress.getByName(args[5]);
        mdbPort = Integer.parseInt(args[6]);

        mdrAddress = InetAddress.getByName(args[7]);
        mdrPort = Integer.parseInt(args[8]);

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

    public static String getVersion() {
        return protocol_version;
    }

    public static int getServerId() {
        return server_id;
    }

    public static int getMaxSizeToSave() {
        return max_size_to_save;
    }

    public static void setMaxSizeToSave(int size){
        max_size_to_save = size;
    }

    public static String getPath(){ return path; }

    public static int getOccupiedSize() {
        return size_occupied;
    }

    public static void updateOccupiedSize(int size){
        size_occupied += size;
    }

    public static PeerDatabase getDatabase(){ return database; }

    public static void writeStoredFile() throws IOException,FileNotFoundException {
        String path = "./Stored";
        File file = new File(path);
        FileOutputStream output = new FileOutputStream(file);

        //se o ficheiro não existir
        if (!file.exists()) {
            file.createNewFile();
        }

       int size = getDatabase().getInformationStored().size();

        for(int i=0; i < size; i++){
           byte[] contentInBytes = getDatabase().getInformationStored().toString().getBytes();
            output.write(contentInBytes);
        }
        output.flush();
        output.close();
    }


    @Override
    public void backup(File file, int replicationDeg) throws IOException, InterruptedException {

        FileManager fileManager = new FileManager(file, replicationDeg);
        ArrayList<Chunk> chunksToBackup = fileManager.divideFileInChunks();

        for (int i = 0; i< chunksToBackup.size(); i++) {
            BackupProtocol.sendPutchunkMessage(chunksToBackup.get(i));
        }
        writeStoredFile();

    }

    @Override
    public void delete(File file) throws IOException{
        FileManager fM = new FileManager(file);
        DeleteProtocol.sendDeleteMessage(fM.getFileId());
    }

    @Override
    public void restore(File file) throws IOException {

        RestoreProtocol protocol = new RestoreProtocol();
        protocol.sendGetchunkMessage(file);
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

    public void reclaim(int reclaimed_space){}
    public void state(){}
}