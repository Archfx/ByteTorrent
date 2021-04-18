package peer.message;

import java.nio.ByteBuffer;

import static peer.message.MessageType.*;

public class MessageGenerator {

    public static Message choke() {
        return new Message(0, CHOKE, new byte[0]);
    }

    public static Message unChoke() {
        return new Message(0, UNCHOKE, new byte[0]);
    }

    public static Message interested() {
        return new Message(0, INTERESTED, new byte[0]);
    }

    public static Message notInterested() {
        return new Message(0, NOT_INTERESTED, new byte[0]);
    }

    public static Message have(int index) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(index);
        return new Message(4, HAVE, b.array());
    }

    public static Message bitfield(byte[] bitField) {
        return new Message(bitField.length, BITFIELD, bitField);
    }

    public static Message request(int index) {
        return new Message(4, REQUEST, intToByteArray(index));
    }

    public static Message piece(int index, byte[] piece) {
        return new Message(4 + piece.length, PIECE, concatArray(intToByteArray(index), piece));
    }


    private static byte[] concatArray(byte[] a, byte[] b){
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    private static byte[] intToByteArray(int intVal){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(intVal);
        return b.array();
    }
}
