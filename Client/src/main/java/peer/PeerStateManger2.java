package peer;

import java.time.Period;
import java.util.List;

public class PeerStateManger2 extends Peer{

    List<Peer> kNeighborPeers;

    public PeerStateManger2(Peer peer, List<Peer> kNeighborPeers) {
        super(peer.id, peer.address, peer.port, peer.hasFile);
        kNeighborPeers = kNeighborPeers;
    }

    public void start(){
        //TODO -: initializing connection and continue all the steps
    }
}
