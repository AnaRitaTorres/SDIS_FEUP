package messages;

/**
 * Created by iamgroot on 29/03/17.
 */
public class ComposeHeader {

    private final String CRLF = "\r\n";
    private final String SPACE = " ";

    private MessageType messageType;
    private String version;
    private int senderId;
    private String fileId;
    private int chunkNo;
    private int replicationDeg;


    public ComposeHeader(MessageType messageType,String version, int senderId, String fileId, int chunkNo, int replicationDegree) {
        this.messageType = messageType;
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDeg = replicationDeg;
    }

    public ComposeHeader(MessageType messageType,String version, int senderId, String fileId, int chunkNo){
        this.messageType = messageType;
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public ComposeHeader(MessageType messageType,String version, int senderId, String fileId){
        this.messageType = messageType;
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
    }


    public String convertHeaderToString(MessageType messageType,String version, int senderId, String fileId, int chunkNo, int replicationDegree){

        String header = messageType + SPACE + version + SPACE + senderId + SPACE + fileId + SPACE + chunkNo + SPACE + replicationDegree + CRLF + CRLF;
        return header;
    }

    public String convertHeaderToString(MessageType messageType,String version, int senderId, String fileId, int chunkNo){

        String header = messageType + SPACE + version + SPACE + senderId + SPACE + fileId + SPACE + chunkNo + CRLF + CRLF;
        return header;
    }

    public String convertHeaderToString(MessageType messageType,String version, int senderId, String fileId){

        String header = messageType + SPACE + version + SPACE + senderId + SPACE + fileId + CRLF + CRLF;
        return header;
    }

}
