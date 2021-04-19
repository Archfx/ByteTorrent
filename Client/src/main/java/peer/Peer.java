package peer;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Peer {
	private int peerId;
	private String address;
	private int port;
	private boolean hasFile;
	private byte[] bitField;
	private AtomicBoolean choked = new AtomicBoolean(true);
	private Socket socket;
	private boolean up;
	private PeerConnectionHandler connectionHandler;

	private Float dlSpeed; //For choke unchoke
    private Integer allPeerID; //For choke unchoke
    private Socket peerSockets; //For choke unchoke

	private boolean doneDonwloading;

	private boolean isInterested; //For choke unchoke
    private Peer unChoked; //For choke unchoke



	public Peer(int id, String address, int port, boolean hasFile) {
		this.peerId = id;
		this.address = address;
		this.port = port;
		this.hasFile = hasFile;
	}

	public boolean isDoneDonwloading() {
		return doneDonwloading;
	}

	public void setDoneDonwloading(boolean doneDonwloading) {
		this.doneDonwloading = true;//doneDonwloading;
	}

	public Peer(Peer peer) {
		this.peerId = peer.getPeerId();
		this.address = peer.getAddress();
		this.port = peer.getPort();
		this.hasFile = peer.isHasFile();
	}


	public int getPeerId() {
		return peerId;
	}

	public void setPeerId(int peerId) {
		this.peerId = peerId;
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

	public boolean getChoked() {
		return choked.get();
	}

	public void setChoked(boolean choked) {
		this.choked = new AtomicBoolean(choked);
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

	public Float getDlSpeed() {
		return dlSpeed;
	}

	public void setDlSpeed(Float dlSpeed) {
		this.dlSpeed = dlSpeed;
	}

	public Integer getAllPeerID() {
		return allPeerID;
	}

	public void setAllPeerID(Integer allPeerID) {
		this.allPeerID = allPeerID;
	}

	public Socket getPeerSockets() {
		return peerSockets;
	}

	public void setPeerSockets(Socket peerSockets) {
		this.peerSockets = peerSockets;
	}

	public boolean isInterested() {
		return isInterested;
	}

	public void setInterested(boolean interested) {
		isInterested = interested;
	}

	public Peer getUnChoked() {
		return unChoked;
	}

	public void setUnChoked(Peer unChoked) {
		this.unChoked = unChoked;
	}

	public PeerConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

	public void setConnectionHandler(PeerConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}
}



