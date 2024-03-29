package edu.ufl.cise.bytetorrent.model.message;

import edu.ufl.cise.bytetorrent.model.message.payload.BitFieldPayLoad;
import edu.ufl.cise.bytetorrent.model.message.payload.HavePayLoad;
import edu.ufl.cise.bytetorrent.model.message.payload.PiecePayLoad;
import edu.ufl.cise.bytetorrent.model.message.payload.RequestPayLoad;

import static edu.ufl.cise.bytetorrent.model.message.MessageType.*;

public class MessageGenerator {

    public static Message choke() {
        return new Message(0, CHOKE, null);
    }

    public static Message unChoke() {
        return new Message(0, UNCHOKE, null);
    }

    public static Message interested() {
        return new Message(0, INTERESTED, null);
    }

    public static Message notInterested() {
        return new Message(0, NOT_INTERESTED, null);
    }

    public static Message have(int index) {
        return new Message(4, HAVE, new HavePayLoad(index));
    }

    public static Message bitfield(byte[] bitField) {
        return new Message(bitField.length, BITFIELD, new BitFieldPayLoad(bitField));
    }

    public static Message request(int index) {
        return new Message(4, REQUEST, new RequestPayLoad(index));
    }

    public static Message piece(int index, byte[] piece) {
        return new Message(4 + piece.length, PIECE, new PiecePayLoad(piece, index));
    }

}
