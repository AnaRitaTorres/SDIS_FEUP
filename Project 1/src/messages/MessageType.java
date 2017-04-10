package messages;

import java.lang.String;

public enum MessageType {

    PUTCHUNK("PUTCHUNK"), STORED("STORED"), GETCHUNK("GETCHUNK"), CHUNK("CHUNK"), DELETE("DELETE"), REMOVED("REMOVED");

    private final String value;

    private MessageType(final String value) {
        this.value = value;
    }

    public final String enumToString() {
        return value;
    }

}
