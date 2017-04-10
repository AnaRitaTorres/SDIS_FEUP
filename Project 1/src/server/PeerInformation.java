package server;

import java.util.Objects;

public class PeerInformation {

    private String fileId;
    private int chunkNo;
    private int replicationDeg;

    public PeerInformation(String fileId, int chunkNo){
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDeg = 0;
    }

    public PeerInformation(String fileId, int chunkNo, int replicationDeg){
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDeg = replicationDeg;
    }

    public String getFileId() {
        return fileId;
    }

    public int getReplicationDeg() { return replicationDeg; }

    public int getChunkNo() { return chunkNo; }

    @Override
    public boolean equals(Object obj) {

        if (obj == this)
            return true;

        if (!(obj instanceof PeerInformation))
            return false;

        PeerInformation info = (PeerInformation) obj;

        return Objects.equals(fileId, info.fileId) && info.chunkNo == chunkNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }

    @Override
    public String toString(){
        return fileId + " " + chunkNo + " " + replicationDeg;
    }
}