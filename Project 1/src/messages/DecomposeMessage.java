package messages;

import java.net.DatagramPacket;
import static java.util.Arrays.copyOfRange;

/**
 * Created by iamgroot on 29/03/17.
 */
public class DecomposeMessage {

    public static final byte CR = 0xD;
    public static final byte LF = 0xA;

    private byte[] header;
    private byte[] body;

    public DecomposeMessage(DatagramPacket packet){

        byte[] message = packet.getData();

        int i;
        for(i=3; i < message.length; i++){
            if(message[i] == LF && message[i-1] == CR && message[i-2]== LF && message[i-3]== CR)
                break;
        }

        header = copyOfRange(message,0, i-3);
        body = copyOfRange(message,header.length + 4, packet.getLength());
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

}
