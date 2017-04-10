package fileManager;

import chunk.Chunk;
import server.Peer;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iamgroot on 27/03/17.
 */
public class FileManager {

    private static File file;
    private static String fileId;
    private static int replicationDeg;

    public FileManager(File file, int replicationDeg) {

        this.file = file;
        this.fileId = generateFileId(file);
        this.replicationDeg = replicationDeg;
    }

    public FileManager(File file){
        this.file = file;
        this.fileId = generateFileId(file);
    }

    public String getFileId() {
        return fileId;
    }

    public static String generateFileId(File file) {

        String fileID = null;
        try {
            Path path = file.toPath();
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

            //fileId is name + creationTime + size
            String str = file.getName() + attr.creationTime() + attr.size();

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();

            fileID = DatatypeConverter.printHexBinary(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileID;
    }

    public ArrayList<Chunk> divideFileInChunks() throws IOException {

        ArrayList<Chunk> chunks = new ArrayList<>();

        long fileSize = file.length(); //filesize in byte
        long remainder = (int) fileSize % Chunk.MAX_SIZE;
        long numChunks = getNumChunks(file);
        int chunkNo = 1;

        Path path = file.toPath();
        byte[] data = Files.readAllBytes(path);
        int j = 0;
        byte[] chunkArray = new byte[Chunk.MAX_SIZE];

        for (int i = 0; i < fileSize; i++) {
            if (i % Chunk.MAX_SIZE == 0 && i > 0) {

                //create chunk and add it to arraylist
                chunks.add(new Chunk(chunkNo, this.fileId, this.replicationDeg, chunkArray));

                j = 0;
                chunkArray = new byte[Chunk.MAX_SIZE];
                chunkNo++;
            }
            chunkArray[j] = data[i];
            j++;
        }

        if (chunkNo <= numChunks) {
            chunkArray = Arrays.copyOfRange(chunkArray, 0, (int) remainder);
            chunks.add(new Chunk(chunkNo, this.fileId, this.replicationDeg, chunkArray));
        }

        return chunks;
    }

    public static void saveFile(byte[] body, String fileId, int chunkNo) throws IOException {

        String savePath = Peer.getPath() + fileId + "/";
        Path path = Paths.get(savePath);

        //If path doesn't exist
        File file = new File(savePath + chunkNo);
        if (!Files.exists(path)){
            file.getParentFile().mkdirs();
        }

        FileOutputStream output = new FileOutputStream(new File(savePath + chunkNo));
        output.write(body);

        Peer.updateOccupiedSize(body.length);
    }

    public static void deleteDirectory(String fileId) {

        String path = Peer.getPath() + fileId;
        File file = new File(path);
        int spaceRelease = 0;

        if (file.exists()){
            File[] files = file.listFiles();
            if(files != null){
                for (int i=0; i<files.length; i++) {
                    spaceRelease += files[i].length();
                    files[i].delete();
                }
            }
            Peer.releaseSpace(spaceRelease);
            file.delete();
        }
    }

    public static void deleteFile(String fileId) {

        String path = Peer.getPath() + fileId;
        File file = new File(path);

        if (file.exists()){
            Peer.releaseSpace((int)file.length());
            file.delete();
        }
    }

    public static int getNumChunks(File file){

        int numChunks = 0;

        if (file.exists()){
            numChunks = (int) file.length() / Chunk.MAX_SIZE;

            if (file.length() % Chunk.MAX_SIZE != 0)
                numChunks++;
        }
        else
            System.err.println("File doesn't exist.");

        return numChunks;
    }

    public static void writeStoredChunksFile() throws IOException {
        String savePath = "./Stored/Peer" + Peer.getServerId();
        Path path = Paths.get(savePath);
        File file = new File(savePath);

        if (!Files.exists(path))
            file.getParentFile().mkdirs();

        FileOutputStream output = new FileOutputStream(file);

        //se o ficheiro não existir
        if (!file.exists()) {
            file.createNewFile();
        }

        Iterator it = Peer.getDatabase().getStoredChunks().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            String key = pair.getKey().toString();
            String value = pair.getValue().toString();
            String line = key + " " + value + "\n";
            byte[] contentInBytes = line.getBytes();
            output.write(contentInBytes);
        }
        output.flush();
        output.close();

    }
}
