package peer.message;

import peer.message.payload.PayLoad;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Message implements Serializable {

    private int messageLength;
    private MessageType messageType;
    private PayLoad payload;

    public Message(int messageLength, MessageType messageType, PayLoad payload) {
        this.messageLength = messageLength;
        this.messageType = messageType;
        this.payload = payload;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public PayLoad getPayload() {
        return payload;
    }

    public void setPayload(PayLoad payload) {
        this.payload = payload;
    }

}
