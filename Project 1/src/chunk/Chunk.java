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
    private File file;
    private byte[] data;

    public Chunk(int chunkNo, String fileId, int replicationDeg, byte[] data){
        this.chunkNo = chunkNo;
        this.fileId = fileId;
        //o chunk precisa de guardar o numero de grau de replica
        this.replicationDeg = replicationDeg;
        this.data = data;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public String getFileId() {
        return fileId;
    }

    public byte[] getData() {
        return data;
    }

    public File getFile(){
        return file;
    }

}
