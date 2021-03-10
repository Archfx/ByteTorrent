package peer;

import peer.Peer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class PeerStateManager extends Thread {
    private ServerSocket ssocket;
    private Peer hostPeer;
    private HashMap<Integer, Peer> peers;

    private static ArrayList<Peer> interestedPeers = new ArrayList<Peer>();
	private static ArrayList<Peer> kNeighborPeers;

    public Object lockMyNeighbors;
    public int numNeighbors;
    public int[] neighbors;
    public boolean[] isInterested;
    public boolean isMyFile;
    public boolean dlFinished;
    public int unchokeInterval;

    public int[] allPeerID;
    public Socket[] allPeerSockets;

    public PeerStateManager(ServerSocket ssocket, Peer hostPeer, HashMap<Integer, Peer> peers) {
		this.ssocket = ssocket;
		this.peers = peers;
		this.hostPeer = hostPeer;
	}

    public List<Peer> choke (List<Peer> neighbors){
        return null;
    }

    public List<Peer> unchoke (List<Peer> neighbors){
        return null;
    }

    public List<Peer> requestPiece (List<Peer> neighbors){
        return null;
    }

    public List<Peer> handshake (List<Peer> neighbors){
        return null;
    }

    public List<Peer> bitField (List<Peer> neighbors){
        return null;
    }

    public List<Peer> isInterested (List<Peer> neighbors){
        return null;
    }
}

