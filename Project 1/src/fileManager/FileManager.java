package fileManager;

import chunk.Chunk;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by iamgroot on 27/03/17.
 */
public class FileManager {

    private File file;
    private String fileId;

    public File getFile() {
        return file;
    }

    public String getFileId() {
        return fileId;
    }

    public FileManager(File file, String FileID){

        this.file = file;
        this.fileId = generateFileId();
    }

    public String generateFileId(){

        String fileID=null;
        try {
            Path path = file.toPath();
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

            //fileId is name + creationTime + size
            String str = file.getName() + attr.creationTime() + attr.size();
            System.out.println(str);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();

            fileID = DatatypeConverter.printHexBinary(digest);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return fileID;
    }

    public void divideFileInChunks() throws IOException {

        long fileSize = file.length(); //filesize in byte
        long remainder = (int) fileSize % Chunk.MAX_SIZE;
        long numChunks = fileSize / Chunk.MAX_SIZE;
        if (remainder != 0){
            numChunks++;
        }

        File chunkFile;

        int chunkNo = 1;

        Path path = file.toPath();
        byte[] data = Files.readAllBytes(path);
        int j=0, i;
        byte[] chunkArray = new byte[Chunk.MAX_SIZE];

        for (i=0; i< fileSize; i++){
            if(i % Chunk.MAX_SIZE == 0 && i>0){

                chunkFile = new File("/home/catarina/Desktop/test" + chunkNo);
                FileOutputStream output = new FileOutputStream(chunkFile);
                output.write(chunkArray);

                //create chunk
                Chunk chunk = new Chunk(chunkNo, this.fileId, chunkArray, chunkFile);
                j=0;
                chunkNo++;
            }
            chunkArray[j] = data[i];
            j++;
        }

        if (chunkNo <= numChunks){
            chunkFile = new File("/home/catarina/Desktop/test" + chunkNo);
            System.out.println("Created File!!");
            FileOutputStream output = new FileOutputStream(chunkFile);
            output.write(chunkArray, 0, (int)remainder);
        }
    }
}
