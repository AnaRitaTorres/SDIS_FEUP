package protocols;

import chunk.Chunk;

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
public class BackupProtocol implements Runnable{

    private int replicationDeg;
    private File file;
    private String fileId;

    public BackupProtocol(File file, int replicationDeg){
        this.file = file;
        this.replicationDeg = replicationDeg;
    }

    //DONE
    public void generateFileId() throws IOException, NoSuchAlgorithmException {

        Path path = file.toPath();
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

        //fileId is name + creationTime + size
        String str = file.getName() + attr.creationTime() + attr.size();
        System.out.println(str);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(str.getBytes("UTF-8"));
        byte[] digest = md.digest();

        fileId = DatatypeConverter.printHexBinary(digest);
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
                Chunk chunk = new Chunk(chunkNo, fileId, chunkArray, chunkFile);
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

    public void run(){


    }

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
            File filetest = new File("/home/catarina/Desktop/test");
            if(!filetest.isFile() || !filetest.exists()){
                System.out.println("lol");
                return;
            }
            BackupProtocol test = new BackupProtocol(filetest, 2);
            test.divideFileInChunks();


    }
}
