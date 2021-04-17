package peer;

import java.util.List;

public class PeerStateManger extends Peer{

    private List<Peer> kNeighborPeers;
    private ChokeManager chokeManager;
    private InterestNotInterestManager interestNotInterestManager;

    public PeerStateManger(Peer thisPeer, List<Peer> kNeighborPeers) {
        super(thisPeer);
        kNeighborPeers = kNeighborPeers;
        init();

    }

    private void init() {
        chokeManager = new ChokeManager();
    }

    public void start(){
        //TODO -: initializing connection and continue all the steps
        chokeManager.choke();
        interestNotInterestManager.isInterested();
    }
}
