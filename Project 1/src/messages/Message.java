package messages;

import java.io.ByteArrayInputStream;

/**
 * Created by iamgroot on 25/03/17.
 */
public class Message {

    private String headers;
    private String body;
    private String messageType = null;
    private String fileId = null;
    private Float version = null;
    private Integer senderId = null;
    private Integer chunkNo = null;
    private Integer replicationDeg = null;

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setVersion(Float version) {
        this.version = version;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setChunkNo(Integer chunkNo) {
        this.chunkNo = chunkNo;
    }

    public void setReplicationDeg(Integer replicationDeg) {
        this.replicationDeg = replicationDeg;
    }

    public String getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Message(String message) {
        String[] m = message.split("\r\n\r\n");
        this.headers = m[0];
        this.body = m[1];

    }

    public  String ByteArraytoString(ByteArrayInputStream is) {
        int size = is.available();
        char[] theChars = new char[size];
        byte[] bytes    = new byte[size];

        is.read(bytes, 0, size);
        for (int i = 0; i < size;)
            theChars[i] = (char)(bytes[i++]&0xff);

        return new String(theChars);
    }

    public static void main(String[] args){

    }

}
