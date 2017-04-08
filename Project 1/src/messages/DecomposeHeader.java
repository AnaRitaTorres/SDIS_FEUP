package messages;

import server.Peer;

import static messages.HeaderComponents.*;

/**
 * Created by iamgroot on 29/03/17.
 */
public class DecomposeHeader {

    private MessageType messageType;
    private String version;
    private int senderId;
    private String fileId;
    private int chunkNo;
    private int replicationDeg;

    public DecomposeHeader(byte[] header){

        String h = new String(header).trim();
        String[] headerComponents = h.split("[ ]+");

        handleMessageType(headerComponents[M_TYPE]);
        handleComponents(headerComponents);
    }

    public void handleComponents(String[] headerComponents){

        if(messageType != null){
            if(headerComponents.length == 6){
                this.version = headerComponents[VERSION];
                this.senderId = Integer.parseInt(headerComponents[SENDER_ID]);
                this.fileId = headerComponents[FILE_ID];
                this.chunkNo = Integer.parseInt(headerComponents[CHUNK_NO]);
                this.replicationDeg = Integer.parseInt(headerComponents[REP_DEG]);
            }
            else if (headerComponents.length == 5){
                this.version = headerComponents[VERSION];
                this.senderId = Integer.parseInt(headerComponents[SENDER_ID]);
                this.fileId = headerComponents[FILE_ID];
                this.chunkNo = Integer.parseInt(headerComponents[CHUNK_NO]);
            }
            else if(headerComponents.length == 4){
                this.version = headerComponents[VERSION];
                this.senderId = Integer.parseInt(headerComponents[SENDER_ID]);
                this.fileId = headerComponents[FILE_ID];
            }
        }
    }

    public void handleMessageType(String messageType){
        switch (messageType){
            case "PUTCHUNK":
                this.messageType = MessageType.PUTCHUNK;
                break;
            case "STORED":
                this.messageType = MessageType.STORED;
                break;
            case "CHUNK":
                this.messageType = MessageType.CHUNK;
                break;
            case "GETCHUNK":
                this.messageType = MessageType.GETCHUNK;
                break;
            case"DELETE":
                this.messageType = MessageType.DELETE;
                break;
            case "REMOVED":
                this.messageType = MessageType.REMOVED;
                break;
            default:
                this.messageType = null;
                break;
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getVersion() {
        return version;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }
}
