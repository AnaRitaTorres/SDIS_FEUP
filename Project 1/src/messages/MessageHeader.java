package messages;

import static messages.HeaderComponents.*;

/**
 * Created by iamgroot on 25/03/17.
 */
public class MessageHeader {

    private Message message;

    public MessageHeader(){

        String header = firstHeader();
        String[] headerElem = header.split("[ ]+");
    }

    public String firstHeader(){
        String headers = message.getHeaders();
        String[] separatedHeaders = headers.split("\r\n");
        return separatedHeaders[M_TYPE];
    }

    public boolean checkVersion(String version){
        if(version.matches("[0-9]+.[0-9]+")) {
            return true;
        }
        else return false;
    }

    public boolean checkRepDegree(String repDegree){
        if(Integer.parseInt(repDegree) > 9)
            return false;
        else return true;
    }

    //TODO: ver se vale a pena que estas funções retornem booleno
    public void checkPutchunk(String[] headerElem){
        message.setMessageType("PUTCHUNK");
        if(checkVersion(headerElem[VERSION]))
            message.setVersion(Float.parseFloat(headerElem[VERSION]));
        message.setSenderId(Integer.parseInt(headerElem[SENDER_ID]));
        message.setFileId(headerElem[FILE_ID]);
        message.setChunkNo(Integer.parseInt(headerElem[CHUNK_NO]));
        if(checkRepDegree(headerElem[REP_DEG]))
            message.setReplicationDeg(Integer.parseInt(headerElem[REP_DEG]));
    }

    public void check(String[] headerElem){
        if(checkVersion(headerElem[VERSION]))
            message.setVersion(Float.parseFloat(headerElem[VERSION]));
        message.setSenderId(Integer.parseInt(headerElem[SENDER_ID]));
        message.setFileId(headerElem[FILE_ID]);
        message.setChunkNo(Integer.parseInt(headerElem[CHUNK_NO]));
    }

    public void checkDelete(String[] headerElem){
        message.setMessageType("DELETE");
        if(checkVersion(headerElem[VERSION]))
            message.setVersion(Float.parseFloat(headerElem[VERSION]));
        message.setSenderId(Integer.parseInt(headerElem[SENDER_ID]));
        message.setFileId(headerElem[FILE_ID]);
    }

    //TODO: só tem verificações básicas, podem ser preciso mais
    public void sortMessages(String[] headerElem){

        MessageType messageType = MessageType.valueOf(headerElem[M_TYPE]);

        switch (messageType){
            case PUTCHUNK:
                checkPutchunk(headerElem);
                break;
            case CHUNK:
                message.setMessageType("CHUNK");
                check(headerElem);
                break;
            case DELETE:
                checkDelete(headerElem);
                break;
            case STORED:
                message.setMessageType("STORED");
                check(headerElem);
                break;
            case REMOVED:
                message.setMessageType("REMOVED");
                check(headerElem);
                break;
            case GETCHUNK:
                message.setMessageType("GETCHUNK");
                check(headerElem);
                break;
                default:
                    break;
        }

    }


}
