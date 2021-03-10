// Store Peer state
import java.net.Socket;

public class Peer {
	public int id;
	public String address;
	public int port;
	public boolean hasFile;
	public byte[] bitField;
	public boolean unChoked;
	public long dlSpeed;
	public ConnectionHandle cHandle;
	public Socket socket;
	public boolean up;

    public Peer(int id, String a, int p, boolean f) {
		this.id = id;
		this.address = a;
		this.port = p;
		this.hasFile = f;
	}

	public Client getClientID(PeerDirection direction) {
		switch (direction){
			case Downloader:
				return myClient;
			case Uploader:
				return peerCleint;
		}
	}

//	L :candidate peers to unchoke
//	p : peer who conducts unchoking
//	k : number of unchoked peers
//	d : number of unchoked downloaders
//	m : peak traffic demand
//	c : current traffic vector

	public Peer [] choke(Peer [] L, Peer p, int k, int d, int m, int c){

    	if (L.length<=k){
    		return L;
		}

	}




}
