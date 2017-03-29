package messages;

/**
 * Created by catarina on 29-03-2017.
 */
public class MessageTest {

    private MessageHeaderTest header;
    private byte[] body;

    private final String CRLF = "\r\n";

    public MessageTest(String version, int senderId, String fileId, int chunkNo, int replicationDeg, byte[] body){
        this.header = new MessageHeaderTest(version, senderId, fileId, chunkNo, replicationDeg);
        this.body = body;
    }

    public MessageTest(String version, int senderId, String fileId, int chunkNo, int replicationDeg){
        this.header = new MessageHeaderTest(version, senderId, fileId, chunkNo, replicationDeg);
        this.body = null;
    }

    public MessageTest(String version, int senderId, String fileId, int chunkNo){
        this.header = new MessageHeaderTest(version, senderId, fileId, chunkNo);
    }

    public String convertMessageToString(){
        return header.convertHeaderToString() + CRLF + body.toString();
    }

    public void createPutchunkMessage(){
        header.createPutchunkHeader();
    }

}
