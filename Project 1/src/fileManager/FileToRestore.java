package fileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Created by catarina on 07-04-2017.
 */
public class FileToRestore {

    private String fileId;
    private String filename;
    private int numChunks;
    private Vector<byte[]> chunks;

    public FileToRestore(String fileId, String filename, int numChunks){

        this.fileId = fileId;
        this.filename = filename;
        this.numChunks = numChunks;
        chunks = new Vector<>();
        chunks.setSize(numChunks);
    }

    public String getFileId(){
        return fileId;
    }

    public void changePositionInVector(byte[] chunk, int position){
        if (chunks.get(position) == null)
            chunks.set(position, chunk);
    }

    public boolean filledVector(){

        for (int i=0; i<numChunks; i++){
            if (chunks.get(i) == null)
                return false;
        }
        return true;
    }

    public byte[] mergeChunks(){

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        for(byte[] b: chunks)
            os.write(b,0, b.length);

        return os.toByteArray();
    }

    public void saveFile() throws IOException {

        String savePath = "./Restore/" + filename;
        Path path = Paths.get(savePath);

        //If path doesn't exist
        File file = new File(savePath);
        if (!Files.exists(path))
            file.getParentFile().mkdirs();

        byte[] body = mergeChunks();

        Files.write(path, body);
    }
}
