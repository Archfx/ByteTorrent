package peer;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Store Peer state
public class Peer {
	public int id;
	public String address;
	public int port;
	public boolean hasFile;
	public byte[] bitField;
	public boolean choked = true;
//	public ConnectionHandle cHandle;
	public Socket socket;
	public boolean up;

	private Float dlSpeed; //For choke unchoke
    private Integer allPeerID; //For choke unchoke
    private Socket peerSockets; //For choke unchoke

	private static Boolean interestedPeer; //For choke unchoke
    private static Peer unChoked; //For choke unchoke



	public Peer(int id, String a, int p, boolean f) {
		this.id = id;
		this.address = a;
		this.port = p;
		this.hasFile = f;
	}



	public static Peer getUnChoked() {
		return unChoked;
	}



	public static void setUnChoked(Peer unChoked) {
		Peer.unChoked = unChoked;
	}



	public Boolean getInterestedPeers() {
		return interestedPeer;
	}



	public static void setInterestedPeers(Boolean interestedPeers) {
		Peer.interestedPeer = interestedPeers;
	}



	public Socket getPeerSockets() {
		return peerSockets;
	}



	public void setPeerSockets(Socket peerSockets) {
		this.peerSockets = peerSockets;
	}



	public Integer getAllPeerID() {
		return allPeerID;
	}



	public void setAllPeerID(Integer allPeerID) {
		this.allPeerID = allPeerID;
	}



	public Float getDlSpeed() {
		return dlSpeed;
	}



	public void setDlSpeed(Float dlSpeed) {
		this.dlSpeed = dlSpeed;
	}





}



