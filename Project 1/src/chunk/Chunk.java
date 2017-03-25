package chunk;

import java.io.File;

/**
 * Created by catarina on 23-03-2017.
 */
public class Chunk {

    public static int MAX_SIZE = 64000; //size in byte
    private int chunkNo;
    private String fileId;
    private File file;
    private byte[] data;

    public Chunk(int chunkNo, String fileId, byte[] data, File file){
        this.chunkNo = chunkNo;
        this.fileId = fileId;
        this.data = data;
        this.file = file;
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
