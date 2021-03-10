package peer;

import config.CommonConfig;
import peer.Peer;

public class PeerProcessThread extends Peer implements Runnable {
    private final CommonConfig cConfig;
    public PeerProcessThread(int id, String address, int port, boolean hasFile, CommonConfig cConfig) {
        super(id, address, port, hasFile);
        this.cConfig = cConfig;
    }

    @Override
    public void run() {
        System.out.println("Starting peer " + this.id);
        // TODO: Start file sharing 
    }
    public void init() {
        Thread t = new Thread(this);
		t.setName("peerProcess-" + this.id);
		t.start();
    }

    public void startServer() {

    }
    public void startBroadcast() {
        // TODO: This should send handshake message to neighboring peers and check bitfields
    }

    public void startConnection() {
        // TODO: This should create a connection between host and neighboring peers
    }

    public void stop() {
        // TODO: This should check if all peers have complete file and then stop 
    }

}