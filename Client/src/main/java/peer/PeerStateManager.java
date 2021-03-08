import java.net.ServerSocket;
import java.util.*;

public class PeerStateManager extends Thread {
    private ServerSocket ssocket;
    private Peer hostPeer;
    private HashMap<Integer, Peer> peers;

    private static ArrayList<Peer> interestedPeers = new ArrayList<Peer>();
	private static ArrayList<Peer> kNeighborPeers;

    public PeerStateManager(ServerSocket ssocket, Peer hostPeer, HashMap<Integer, Peer> peers) {
		this.ssocket = ssocket;
		this.peers = peers;
		this.hostPeer = hostPeer;
	}
}