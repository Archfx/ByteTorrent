package peer.message;

public class Message {

    private int messageLength;
    private MessageType messageType;
    private byte[] payload;

    public Message(int messageLength, MessageType messageType, byte[] payload) {
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

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
