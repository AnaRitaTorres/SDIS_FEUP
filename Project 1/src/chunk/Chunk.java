package chunk;

/**
 * Created by catarina on 23-03-2017.
 */
public class Chunk {

    public static int MAX_SIZE = 64000; //size in byte
    private int chunkNo;
    private String fileId;

    private byte[] data;

    public Chunk(int chunkNo, String fileId, byte[] data){
        this.chunkNo = chunkNo;
        this.fileId = fileId;
        this.data = data;
    }
}
