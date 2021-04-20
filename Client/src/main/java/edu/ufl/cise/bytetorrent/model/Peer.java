package edu.ufl.cise.bytetorrent.model;
import edu.ufl.cise.bytetorrent.PeerConnectionHandler;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Peer {
	private int peerId;
	private String address;
	private int port;
	private boolean hasFile;
	private byte[] bitField;
	private AtomicInteger noOfPiecesOwned;
	private AtomicBoolean choked;
	private Socket socket;
	private boolean up;
	private PeerConnectionHandler connectionHandler;
	private float dlSpeed;
    private Integer allPeerID;
	private boolean isCompletedDownloading;
	private boolean isInterested;

	public Peer(int id, String address, int port, boolean hasFile) {
		this.peerId = id;
		this.address = address;
		this.port = port;
		this.hasFile = hasFile;
		this.noOfPiecesOwned = new AtomicInteger();
		this.choked = new AtomicBoolean(true);
	}

	public boolean isCompletedDownloading() {
		return isCompletedDownloading;
	}

	public void setCompletedDownloading(boolean completedDownloading) {
		this.isCompletedDownloading = true;
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

	public boolean isInterested() {
		return isInterested;
	}

	public void setInterested(boolean interested) {
		isInterested = interested;
	}

	public PeerConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

	public void setConnectionHandler(PeerConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	public int incrementAndGetNoOfPieces() {
		return noOfPiecesOwned.incrementAndGet();
	}

	public int getNoOfPiecesOwned() {
		return noOfPiecesOwned.get();
	}

	public void setNoOfPiecesOwned(int noOfPiecesOwned) {
		this.noOfPiecesOwned = new AtomicInteger(noOfPiecesOwned);
	}

	public void setDlSpeed(float dlSpeed) {
		this.dlSpeed = dlSpeed;
	}
}



