package edu.ufl.cise.bytetorrent.model.message;

public enum MessageType {

    CHOKE(1),
    UNCHOKE(2),
    INTERESTED(3),
    NOT_INTERESTED(4),
    HAVE(5),
    BITFIELD(6),
    REQUEST(7),
    PIECE(8);

    public int id;

    private MessageType(int id) {
        this.id = id;
    }
}
