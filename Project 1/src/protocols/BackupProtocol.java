package protocols;

import chunk.Chunk;
import server.Peer;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by catarina on 23-03-2017.
 */

//NOTE:it's better to implement Runnable instead of extending Thread,
// because you can implement many interfaces but extend only from a single class
public class BackupProtocol implements Runnable{

    private int replicationDeg;
    private Chunk chunk;
    public Peer peer;


    public BackupProtocol(Chunk chunk, int replicationDeg){
        this.chunk = chunk;
        this.replicationDeg = replicationDeg;
    }

      public void run(){

       while(true){

           //TODO:peer envia PUTCHUNK por MDB


       }

    }

    public static void main(String args[]){

    }
}
