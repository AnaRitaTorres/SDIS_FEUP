package database;

import fileManager.FileToRestore;
import server.Peer;
import server.PeerInformation;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by catarina on 08-04-2017.
 */
public class PeerDatabase {

    //To store replicationDegree associated with each <fileId, chunkNo>
    //Key - Value
    private static HashMap<PeerInformation, Integer> informationStored = new HashMap<>();

    private static HashMap<PeerInformation, Integer> storedChunks = new HashMap<>();

    private static Vector<FileToRestore> filesToRestore = new Vector<>();


    public static HashMap<PeerInformation, Integer> getInformationStored() { return informationStored; }

    public static Vector<FileToRestore> getFilesToRestore(){ return filesToRestore; }

    public static void addToInformationStored(String fileId, int chunkNo, int replicationDeg){
        informationStored.put(new PeerInformation(fileId, chunkNo, replicationDeg), 0);
    }

    public static void addToStoredChunks(String fileId, int chunkNo, int replicationDeg){
        informationStored.put(new PeerInformation(fileId, chunkNo, replicationDeg), 0);
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
        int value = informationStored.get(database) - 1;
        informationStored.replace(database,value);
    }


}
