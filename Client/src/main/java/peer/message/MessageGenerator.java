package peer.message;

import java.nio.ByteBuffer;

import static peer.message.MessageType.*;

public class MessageGenerator {

    public static String choke() {
        return new Message(0, CHOKE, new byte[0]).toString();
    }

    public static String unChoke() {
        return new Message(0, UNCHOKE, new byte[0]).toString();
    }

    public static String interested() {
        return new Message(0, INTERESTED, new byte[0]).toString();
    }

    public static String notInterested() {
        return new Message(0, NOT_INTERESTED, new byte[0]).toString();
    }

    public static String have(int index) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(index);
        return new Message(4, HAVE, b.array()).toString();
    }

    public static String bitfield(int size, byte[] bitField) {
        return new Message(size, BITFIELD, bitField).toString();
    }

    public static String request(int index) {
        return new Message(4, REQUEST, intToByteArray(index)).toString();
    }

    public static String piece(int index, byte[] piece) {
        return new Message(4 + piece.length, PIECE, concatArray(intToByteArray(index), piece)).toString();
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
