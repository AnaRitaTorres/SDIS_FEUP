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

        return (info.fileId == fileId && info.chunkNo == chunkNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }
}