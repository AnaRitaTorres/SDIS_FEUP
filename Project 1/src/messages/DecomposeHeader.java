package messages;

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

        String h = new String(header);
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
            case "STORED":
                this.messageType = MessageType.STORED;
            case "CHUNK":
                this.messageType = MessageType.CHUNK;
            case "GETCHUNK":
                this.messageType = MessageType.GETCHUNK;
            case"DELETE":
                this.messageType = MessageType.DELETE;
            case "REMOVED":
                this.messageType = MessageType.REMOVED;
            default:
                this.messageType = null;
        }
    }



}
