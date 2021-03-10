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

	public void choke(PeerDirection direction, boolean choke){

		Client C = getClientID(direction);

		if (choke) C.choke();
		else C.unchoke();

		if (direction == PeerDirection.uploader){
			if (choke) C.sendMessage(new ChokeMsg());
			else C.sendMessage(new UnchokeMsg());
		}

}
