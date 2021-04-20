package edu.ufl.cise.bytetorrent.service;

import edu.ufl.cise.bytetorrent.config.CommonConfig;
import edu.ufl.cise.bytetorrent.model.Peer;
import edu.ufl.cise.bytetorrent.model.message.MessageGenerator;
import edu.ufl.cise.bytetorrent.util.LoggerUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChokeManagementService {

    // private Object lockMyNeighbors;

//    Peer myNode = PeerManagerPlatform.my_node;

    

    // TODO -: choking unckoking logic goes here

    //    k = prefered number of peers to unchoke
    //    p = unchoking interval
    //    m = optimally unchoke interval

    // public List<Peer> choke (List<Peer> allPeers){
    public void choke (List<Peer> allPeers){

        List<Peer> unchokeList = new ArrayList<Peer>();
        boolean isPreferredNeighborsChanged = false;

        int num_peer = allPeers.size();

        System.out.println(" Starting selecting k peers to send the file ");
                    synchronized (allPeers)
            { //lock the peerlist

				// Randomly selecting neighbors when download of file completed.
                // if (myNode.isDoneDonwloading()) {
                if (FileManagementService.hasCompleteFile()) {
//					shuffle the list
                    Collections.shuffle(allPeers);
                    int nP = 0; // numbers of peers selected

                    for (int i = 0; i < num_peer ; i++) {
                        if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getSocket() != null) {
                            Peer unChokedPeer = allPeers.get(i);
                            if(nP < CommonConfig.getNumberOfdPreferredNeighbors()){
                                if(unChokedPeer.getChoked()){
                                    unChokedPeer.setChoked(false);
                                    unChokedPeer.getConnectionHandler().sendMessage(MessageGenerator.unChoke());
                                }
                                unchokeList.add(unChokedPeer);
                                isPreferredNeighborsChanged = true;

                            }
                            else {
                                unChokedPeer.setChoked(true);
                                unChokedPeer.getConnectionHandler().sendMessage(MessageGenerator.choke());
                            }
                            nP++;
                        }

                    } 
                }
                else {
                    // selecting based on the download speeds when file download is not complete.
                    allPeers.sort(Comparator.comparing(Peer::getDlSpeed));
                    // selecting the peers
                    float nP = 0; // numbers of peers selected

                    for (int i = 0; i < num_peer ; i++) {
                        if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getAllPeerID() != null) {
                            Peer unChokedPeer = allPeers.get(i);
                            if(nP < CommonConfig.getNumberOfdPreferredNeighbors()){
                                if(unChokedPeer.getChoked()){
                                    unChokedPeer.setChoked(false);
                                    unChokedPeer.getConnectionHandler().sendMessage(MessageGenerator.unChoke());
                                }
                                unchokeList.add(unChokedPeer);
                                isPreferredNeighborsChanged = true;
                            }
                            else {
                                unChokedPeer.setChoked(true);
                                unChokedPeer.getConnectionHandler().sendMessage(MessageGenerator.choke());
                            }
                            nP++;
                        }
                        }
                    }

                }
            if (isPreferredNeighborsChanged) {
                LoggerUtil.LogChangeNeighbors((ArrayList<Peer>) unchokeList);
                isPreferredNeighborsChanged = false;
            }

        // return unchokeList;
    }



    // public List<Peer> chokeOpt (List<Peer> allPeers){
    public void chokeOpt (List<Peer> allPeers){


        // List<Peer> optUnchokeList = new ArrayList<Peer>();

        int num_peer = allPeers.size();

        System.out.println(" Starting optimum peer to send the file ");
            // synchronized (lockMyNeighbors) { //lock the object
            synchronized (allPeers) {

//				shuffle the list
                Collections.shuffle(allPeers);
                int nP = 0; // numbers of peers  to be selected selected
                for (int i = 0; i < num_peer && nP < 1; i++) {
                    if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getSocket() != null) {
                        // optUnchokeList.add(allPeers.get(i));
                        nP++;
                        Peer unChokedPeer = allPeers.get(i);
                        if(unChokedPeer.getChoked()){
                            unChokedPeer.setChoked(false);
                            unChokedPeer.getConnectionHandler().sendMessage(MessageGenerator.unChoke());
                            LoggerUtil.LogOptUnchokeNeighbor(String.valueOf(unChokedPeer.getPeerId()));
                        }
                    }
                }
            }
        }

}

