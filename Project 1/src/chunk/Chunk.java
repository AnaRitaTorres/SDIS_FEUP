package chunk;

import java.io.File;

/**
 * Created by catarina on 23-03-2017.
 */
public class Chunk {

    public static int MAX_SIZE = 64000; //size in byte
    private int chunkNo;
    private String fileId;
    private int replicationDeg;
    private byte[] data;
    //private String chunkId

    public Chunk(int chunkNo, String fileId, int replicationDeg, byte[] data){
        this.chunkNo = chunkNo;
        this.fileId = fileId;
        this.replicationDeg = replicationDeg;
        this.data = data;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public String getFileId() {
        return fileId;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public byte[] getData() {
        return data;
    }

}
