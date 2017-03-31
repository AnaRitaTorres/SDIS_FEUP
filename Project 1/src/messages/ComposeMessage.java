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


    public ComposeMessage(MessageType type,String version, int senderId, String fileId, int chunkNo, int replicationDeg, byte[] body){
        this.header = new ComposeHeader(type,version, senderId, fileId,chunkNo,replicationDeg);
        this.body=body;
    }

    public ComposeMessage(MessageType type,String version, int senderId, String fileId, int chunkNo, byte[] body){
        this.header = new ComposeHeader(type,version, senderId, fileId,chunkNo);
        this.body=body;
    }

    public ComposeMessage(MessageType type,String version, int senderId, String fileId, int chunkNo){
        this.header = new ComposeHeader(type,version, senderId, fileId,chunkNo);
        this.body = null;
    }

    public ComposeMessage(MessageType type,String version, int senderId, String fileId){
        this.header = new ComposeHeader(type,version, senderId, fileId);
        this.body=null;
    }

    public String convertPutchunkMessageToString(){
        return header.convertPutchunkHeaderToString() + CRLF + body.toString();
    }

    public String convertMessageToString(){
        return header.convertHeaderToString() + CRLF + body.toString();
    }

    public String convertMessageToStringWithoutBody(){
        return header.convertHeaderToString() + CRLF;
    }

    public String convertDeleteMessageToString(){
        return header.convertDeleteHeaderToString() + CRLF + body.toString();
    }

}
