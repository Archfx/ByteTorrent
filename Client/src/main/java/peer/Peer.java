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
	public Object lockMyNeighbors;
	public int numNeighbors;
	public int[] neighbors;
	public boolean[] isInterested;
	public boolean isMyFile;
	public boolean dlFinished;
	public int unchokeInterval;

	public int[] allPeerID;
	public Socket[] allPeerSockets;


	public Peer(int id, String a, int p, boolean f) {
		this.id = id;
		this.address = a;
		this.port = p;
		this.hasFile = f;
	}





}

class Choke implements Runnable {
	//Choke algorithm to select neighbours interested in data based on the download rates of them.

	Peer P;
	private Thread t = null;

//	get IDs of connected peers
	int[] getPeerId(float[] peerID) {
		int l = peerID.length;
		float[] sortedPeerID = peerID.clone();
		Arrays.sort(sortedPeerID);
		int[] indices = new int[l];
		for (int index = 0; index < l; index++) {
			indices[index] = Arrays.binarySearch(sortedPeerID, peerID[index]);
		}
		return indices;
	}

	@Override
	public void run() {  //this function implements the multi-threading run interface.

		List<Integer> list = new ArrayList<Integer>();
		int num_peer = P.port;//.length;
		int ind = -1;
		for (int i = 0; i < num_peer; i++) {
			list.add(i);
		}
		while (P.choked) {
			synchronized (P.lockMyNeighbors) { //lock the object

				if (P.neighbors == null) {
					continue;
				}

				int[] cn = P.neighbors.clone();  //current neighbor.
				Arrays.sort(cn);
//				Randmly selecting neighbors
				if (P.isMyFile || P.dlFinished) {
//					shuffle the list
					java.util.Collections.shuffle(list);
					int cnt = 0;
					for (int i = 0; i < num_peer && cnt < P.numNeighbors; i++) {
						ind = list.get(i); //index of the peer to be selected.
						if (P.isInterested[ind] && P.allPeerSockets[ind] != null) {
							P.neighbors[cnt++] = P.allPeerID[ind];
							//send unchoke message to previously choked peer.
							if (Arrays.binarySearch(cn, P.allPeerID[ind]) < 0) {  ///not int the previous neighbor list.
//								Message msg = new Message();
//								msg.length = 0; //no payload.
//								msg.type = 1; //unchoke.
//								msg.payload = null; //no payload.
//								P.print("Unchoke " + P.allPeerID[ind] + " randomly.");
//								P.send(P.allPeerID[ind], msg);
							}
						}
					}

				} else {
					//selecting based on the download speeds.
					int[] order = getPeerId(P.dlSpeed);
//					selected
					int cnt = 0;
					for (int i = 0; i < num_peer && cnt < P.numNeighbors; i++) {
						ind = order[i];
						if (P.isInterested[ind] && P.allPeerID[ind] != P.id) {
							P.neighbors[cnt++] = P.allPeerID[ind];
//							sending the unchoke msg to previously choked peer
							if (Arrays.binarySearch(cn, P.allPeerID[ind]) < 0) {
//								Message msg = new Message();
//								msg.length = 0; //no payload.
//								msg.type = 1; //unchoke.
//								msg.payload = null; //no payload.
//								P.send(P.allPeerID[ind], msg);
//								P.print("Based on downloading rate peer " + P.allPeerID[ind] + " unchoked.");
							}
						}
					}

				}
//				sending the choke messege to neighbors based on their history
				Arrays.sort(Arrays.copyOfRange(P.neighbors, 0, P.numNeighbors));
				for (int i = 0; i < P.numNeighbors; i++) {
					if (Arrays.binarySearch(P.neighbors, cn[i]) < 0) {
//						Message msg = new Message();
//						msg.length = 0; //no payload.
//						msg.type = 0; //choke.
//						msg.payload = null; //no payload.
//						P.send(P.allPeerID[ind], msg);
					}
				}
			}
//			//print log
//			if(P.neighbors[0]!=-1){
//				String neighbor_list = "";
//				for(int i = 0;i<P.neighbors.length-1;i++) neighbor_list += P.neighbors[i]+" ";
//				P.log("Peer ["+P.id+"] has the preferred neighbors [ "+neighbor_list+"].");
//			}

			try {
				Thread.sleep(1000 * P.unchokeInterval);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void start(Peer p) { // starting the thread
		P = p;
		if (t == null) {
			t = new Thread(this, "choke thread for peer : " + P.id);
			t.start();
		}
	}
}
