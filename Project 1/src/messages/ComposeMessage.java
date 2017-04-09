package messages;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;

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

    public byte[] convertPutchunkMessageToByteArray(){
        byte[] buf = header.convertPutchunkHeaderToString().getBytes();
        byte[] crfl = CRLF.getBytes();
        byte[] message = new byte[buf.length + crfl.length + body.length];

        System.arraycopy(buf, 0, message, 0, buf.length);
        System.arraycopy(crfl, 0, message, buf.length, crfl.length);
        System.arraycopy(body, 0, message, buf.length + crfl.length, body.length);

        return message;
    }

    public byte[] convertMessageToByteArray(){

        byte[] buf = header.convertHeaderToString().getBytes();
        byte[] crfl = CRLF.getBytes();
        byte[] message = new byte[buf.length + crfl.length + body.length];

        System.arraycopy(buf, 0, message, 0, buf.length);
        System.arraycopy(crfl, 0, message, buf.length, crfl.length);
        System.arraycopy(body, 0, message, buf.length + crfl.length, body.length);

        return message;
    }
   public byte[] convertMessageWithoutBodyToByteArray(){

        byte[] buf = header.convertHeaderToString().getBytes();
        byte[] crfl = CRLF.getBytes();
        byte[] message = new byte[buf.length + crfl.length];

        System.arraycopy(buf, 0, message, 0, buf.length);
        System.arraycopy(crfl, 0, message, buf.length, crfl.length);

        return message;
    }

   public String convertMessageToStringWithoutBody(){
        return header.convertHeaderToString() + CRLF;
    }

    public byte[] convertDeleteMessageToByteArray(){

        //TODO: criar função para não repetir código

        byte[] buf = header.convertDeleteHeaderToString().getBytes();
        byte[] crfl = CRLF.getBytes();
        byte[] message = new byte[buf.length + crfl.length + body.length];

        System.arraycopy(buf, 0, message, 0, buf.length);
        System.arraycopy(crfl, 0, message, buf.length, crfl.length);
        System.arraycopy(body, 0, message, buf.length + crfl.length, body.length);

        return message;
    }

}