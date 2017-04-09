package database;

import fileManager.FileToRestore;
import server.Peer;
import server.PeerInformation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by catarina on 08-04-2017.
 */
public class PeerDatabase {

    //To store replicationDegree associated with each <fileId, chunkNo>
    //Key - Value

    private static HashMap<PeerInformation, Integer> informationStored;
    private static HashMap<PeerInformation, Integer> storedChunks;
    private static Vector<FileToRestore> filesToRestore;

    public PeerDatabase(){
        informationStored = new HashMap<>();
        storedChunks = new HashMap<>();
        filesToRestore = new Vector<>();
    }

    public static HashMap<PeerInformation, Integer> getInformationStored() { return informationStored; }

    public static HashMap<PeerInformation, Integer> getStoredChunks() { return storedChunks; }

    public static Vector<FileToRestore> getFilesToRestore(){ return filesToRestore; }

    public static void addToInformationStored(String fileId, int chunkNo, int replicationDeg){
        informationStored.put(new PeerInformation(fileId, chunkNo, replicationDeg), 0);
    }

    public static boolean addToStoredChunks(String fileId, int chunkNo, int replicationDeg){

        PeerInformation info = new PeerInformation(fileId, chunkNo, replicationDeg);

        if (!storedChunks.containsKey(info)) {
            System.out.println("vou adicionar " + fileId + " " + chunkNo + " a " + storedChunks);
            storedChunks.put(new PeerInformation(fileId, chunkNo, replicationDeg), 1);
            return true;
        }
        return false;
    }

    public static boolean addToStoredChunks(String fileId, int chunkNo){

        PeerInformation info = new PeerInformation(fileId, chunkNo);

        if (!storedChunks.containsKey(info)) {
            storedChunks.put(new PeerInformation(fileId, chunkNo), 1);
            return true;
        }
        return false;
    }

    public static boolean containsKeyValue(String fileId, int chunkNo){
        PeerInformation database = new PeerInformation(fileId, chunkNo);
        return informationStored.containsKey(database);
    }

    public static void incrementsReplicationDegree(String fileId, int chunkNo){
        PeerInformation database = new PeerInformation(fileId, chunkNo);
        int value = informationStored.get(database) + 1;
        informationStored.put(database, value);
    }

    public static void decreasesReplicationDegree(String fileId, int chunkNo){
        PeerInformation database = new PeerInformation(fileId, chunkNo);
        int replicationDeg;
        int value;

        if (informationStored.containsKey(database)) {
            for (PeerInformation peer: informationStored.keySet()){
                if (peer.equals(database)) {
                    replicationDeg = peer.getReplicationDeg();
                    value = informationStored.get(peer) - 1;
                    informationStored.replace(new PeerInformation(fileId, chunkNo, replicationDeg), value);
                }
            }
        }
    }

    public static boolean biggerReplicationDeg(String fileId, int chunkNo){

        PeerInformation info = new PeerInformation(fileId, chunkNo);
        int replicationDeg = 0;

        for (PeerInformation peerInfo: storedChunks.keySet()){
            if (peerInfo.equals(info)) {
                replicationDeg = peerInfo.getReplicationDeg();
                break;
            }
        }

        if (storedChunks.get(info) > replicationDeg)
            return true;

        return false;
    }

    public static void incrementsStoredChunks(String fileId, int chunkNo, int replicationDeg){
        PeerInformation database = new PeerInformation(fileId, chunkNo);
        int value = storedChunks.get(database) + 1;

        storedChunks.remove(database);

        PeerInformation newDatabase = new PeerInformation(fileId, chunkNo, replicationDeg);

        storedChunks.put(newDatabase, value);
    }

    public static void incrementsStoredChunks(String fileId, int chunkNo){

        PeerInformation database = new PeerInformation(fileId, chunkNo);
        int replicationDeg = 0;

        for (PeerInformation peerInfo: storedChunks.keySet()){
            if (peerInfo.equals(database)) {
                replicationDeg = peerInfo.getReplicationDeg();
                break;
            }
        }
        int value = storedChunks.get(database) + 1;

        storedChunks.remove(database);

        PeerInformation newDatabase = new PeerInformation(fileId, chunkNo, replicationDeg);

        storedChunks.put(newDatabase, value);
    }

    public static void decreasesStoredChunks(String fileId, int chunkNo){
        PeerInformation database = new PeerInformation(fileId, chunkNo);
        int replicationDeg;
        int value;

        if (storedChunks.containsKey(database)) {
            for (PeerInformation peer: storedChunks.keySet()){
                if (peer.equals(database)) {
                    replicationDeg = peer.getReplicationDeg();
                    value = storedChunks.get(peer) - 1;
                    PeerInformation newPeer = new PeerInformation(fileId, chunkNo, replicationDeg);
                    storedChunks.replace(newPeer, value);
                }
            }
        }
    }


    public static int getDesiredReplicationDeg(String fileId, int chunkNo, HashMap<PeerInformation, Integer> structure){
        PeerInformation database = new PeerInformation(fileId, chunkNo);

        if (structure.containsKey(database)) {
            for (PeerInformation peer: structure.keySet()){
                if (peer.equals(database)) {
                    return peer.getReplicationDeg();
                }
            }
        }
        return -1;
    }

    public static void deleteTrashFromStoredChunks(){

        String path = Peer.getPath();

        Set<PeerInformation> set = new HashSet<>();

        for(PeerInformation peer: storedChunks.keySet()){

            path += peer.getFileId();
            File file = new File(path);
            if(!(file.exists() && file.isDirectory())){
                set.add(peer);
            }
        }
        storedChunks.keySet().removeAll(set);
    }

}
