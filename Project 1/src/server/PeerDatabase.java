package server;

import java.util.Objects;

/**
 * Created by catarina on 03-04-2017.
 */
public class PeerDatabase {

    private String fileId;
    private int chunkNo;

    public PeerDatabase(String fileId, int chunkNo){
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public PeerDatabase (String fileId){
        this.fileId=fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this)
            return true;

        if (!(obj instanceof PeerDatabase))
            return false;

        PeerDatabase info = (PeerDatabase) obj;

        return Objects.equals(fileId, info.fileId) && info.chunkNo == chunkNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }

    @Override
    public String toString(){
        return fileId + " " + chunkNo;
    }
}