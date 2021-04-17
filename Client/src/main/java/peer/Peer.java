package peer;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Store Peer state
public class Peer {
	private int id;
	private String address;
	private int port;
	private boolean hasFile;
	private byte[] bitField;
	private boolean choked = true;
//	public ConnectionHandle cHandle;
	private Socket socket;
	private boolean up;

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


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

	public byte[] getBitField() {
		return bitField;
	}

	public void setBitField(byte[] bitField) {
		this.bitField = bitField;
	}

	public boolean isChoked() {
		return choked;
	}

	public void setChoked(boolean choked) {
		this.choked = choked;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public static Boolean getInterestedPeer() {
		return interestedPeer;
	}

	public static void setInterestedPeer(Boolean interestedPeer) {
		Peer.interestedPeer = interestedPeer;
	}
}



