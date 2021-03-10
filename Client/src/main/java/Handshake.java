
// This class is serializable to quicly send and recieve
public class Handshake implements Serializable {
    private final String head = "P2PFILESHARINGPROJ";
    private int id;

    public Handshake(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("[Header: %s]\n[Peer ID: %s]", this.head, this.id);
    }

    public int getID() {
        return id;
    }
    public String getHead() {
        return head;
    }

    public void send(OutputStream o) {
        ObjectOuputStream op = new ObjectOuputStream(o);
        op.writeObject(this);
    }

    public int receive(InputStream o) {
        try {
            ObjectInputStream op = new ObjectInputStream(o);
            HandShake res = (HandShake) op.readObject();
            return res != null ? res.id : -1;
        }
        catch (Exception e) {}
        return -1;
    }

}
