package config;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RemotePeerInfo {
	private int id;
	private String address;
	private int port;
	private boolean hasFile;
	private AtomicInteger bytesDownloadedFrom = new AtomicInteger(0);
	private BitSet receivedParts = new BitSet();
	private AtomicBoolean interested = new AtomicBoolean(false);

    public RemotePeerInfo(int peerId) {
        this(peerId, "127.0.0.1", 0, false);
    }

    public RemotePeerInfo(int id, String a, int p, boolean f) {
        this.id = id;
        this.address = a;
        this.port = p;
        this.hasFile = f;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public AtomicInteger getBytesDownloadedFrom() {
        return bytesDownloadedFrom;
    }

    public BitSet getReceivedParts() {
        return receivedParts;
    }

    public AtomicBoolean getInterested() {
        return interested;
    }
}