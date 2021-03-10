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
	public float[] dlSpeed;
//	public ConnectionHandle cHandle;
	public Socket socket;
	public boolean up;
	public PeerStateManager psM;


	public Peer(int id, String a, int p, boolean f) {
		this.id = id;
		this.address = a;
		this.port = p;
		this.hasFile = f;
	}





}



