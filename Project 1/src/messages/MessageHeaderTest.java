package messages;

/**
 * Created by catarina on 29-03-2017.
 */
public class MessageHeaderTest {

    private MessageType messageType;
    private String version;
    private int senderId;
    private String fileId;
    private int chunkNo;
    private int replicationDeg;

    private final String CRLF = "\r\n";
    private final String SPACE = " ";

    public MessageHeaderTest(String version, int senderId, String fileId, int chunkNo, int replicationDeg){
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDeg = replicationDeg;
    }

    public MessageHeaderTest(String version, int senderId, String fileId, int chunkNo){
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public void createPutchunkHeader(){
        messageType = MessageType.PUTCHUNK;
    }

    public void createStoredHeader(){
        messageType = MessageType.STORED;
    }

    public void createGetchunkHeader(){
        messageType = MessageType.GETCHUNK;
    }

    public void createChunkHeader(){
        messageType = MessageType.CHUNK;
    }

    public void createDeleteHeader(){
        messageType = MessageType.DELETE;
    }

    public void createRemovedHeader(){
        messageType = MessageType.REMOVED;
    }

    public String convertHeaderToString(){

        String message = messageType + SPACE + version + SPACE + senderId + SPACE + fileId + SPACE + chunkNo;

        return messageType + SPACE + version + SPACE + senderId + SPACE + fileId + SPACE + chunkNo + SPACE + replicationDeg + CRLF;

    }
}
