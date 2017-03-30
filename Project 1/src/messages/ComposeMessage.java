package messages;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;

/**
 * Created by iamgroot on 25/03/17.
 */
public class ComposeMessage {

    private ComposeHeader header;
    private byte[] body;
    private final String CRLF = "\r\n";


    public ComposeMessage(MessageType type,String version, int senderId, String fileId, int chunkNo, int replicationDeg){
        this.header = new ComposeHeader(type,version, senderId, fileId,chunkNo,replicationDeg);
        this.body=body;
    }

    public ComposeMessage(MessageType type,String version, int senderId, String fileId, int chunkNo){
        this.header = new ComposeHeader(type,version, senderId, fileId,chunkNo);
        this.body=body;
    }

    public ComposeMessage(MessageType type,String version, int senderId, String fileId){
        this.header = new ComposeHeader(type,version, senderId, fileId);
        this.body=body;
    }

    public String convertMessageToString(MessageType type,String version, int senderId, String fileId, int chunkNo, int replicationDeg){
        return header.convertHeaderToString(type,version,senderId, fileId, chunkNo, replicationDeg) + CRLF + body.toString();
    }

    public String convertMessageToString(MessageType type,String version, int senderId, String fileId, int chunkNo){
        return header.convertHeaderToString(type,version,senderId, fileId, chunkNo) + CRLF + body.toString();
    }

    public String convertMessageTypeToString(MessageType type,String version, int senderId, String fileId){
        return header.convertHeaderToString(type,version,senderId, fileId) + CRLF + body.toString();
    }

}
