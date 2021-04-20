package edu.ufl.cise.bytetorrent;

import edu.ufl.cise.bytetorrent.config.CommonConfig;
import edu.ufl.cise.bytetorrent.model.Peer;
import edu.ufl.cise.bytetorrent.model.message.Handshake;
import edu.ufl.cise.bytetorrent.service.ChokeManagementService;
import edu.ufl.cise.bytetorrent.service.FileManagementService;
import edu.ufl.cise.bytetorrent.util.LoggerUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;


public class PeerManagerPlatform {

    private Map<Integer, Peer> peers;
    private Peer selfPeer;
    private ServerSocket socket;

    ChokeManagementService myCM = new ChokeManagementService();

    public PeerManagerPlatform(Peer mySelf, Map<Integer, Peer> remotePeers) {
        this.peers = remotePeers;
        this.selfPeer = mySelf;
    }

    public void init() {
        LoggerUtil.LogInfoMessage("Starting peer " + selfPeer.getPeerId());

        new FileManagementService(selfPeer.getPeerId(), selfPeer.isHasFile());

        try {
            selfPeer.setBitField(FileManagementService.getBitField());
            socket = new ServerSocket(selfPeer.getPort());
            System.out.println("Created server for " + selfPeer.getAddress() + ":" + selfPeer.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initServer();
        this.initClient();

        (new Thread() {
            @Override
            public void run() {
                while (!selfPeer.isCompletedDownloading()) {
                    myCM.choke(new ArrayList<Peer>(peers.values()));
                    try {
                        Thread.sleep(1000*CommonConfig.getUnchokingInterval());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Exit choke");
            }
        }).start();

        (new Thread() {
            @Override
            public void run() {
                while (!selfPeer.isCompletedDownloading()) {
                    myCM.chokeOpt(new ArrayList<Peer>(peers.values()));
                    try {
                        Thread.sleep(1000*CommonConfig.getOptimisticUnchokingInterval());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Exit choke opt");
            }
        }).start();
    }


    public void initServer() {
        (new Thread() {
            @Override
            public void run() {
                startListening();
            }
        }).start();

    }

    public void initClient() {
        new Thread() {
            public void run() {
                LoggerUtil.LogInfoMessage("Initializing client");
                while (!selfPeer.isCompletedDownloading()) {
                    try {
                        for (Peer peer : peers.values()) {
                            if (!peer.isUp()) {
                                Socket s = new Socket(peer.getAddress(), peer.getPort());
                                LoggerUtil.LogMakeTcpConnection(String.valueOf(peer.getPeerId()));
                                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                                out.flush();
                                out.writeObject(new Handshake(selfPeer.getPeerId()));
                                out.flush();
                                out.reset();
                                System.out.println("Handshake Message sent to peer " + peer.getPeerId() + " from" + selfPeer.getPeerId());
                                peer.setSocket(s);
                                peer.setUp(true);
                            }
                            // Sleep to not spam
                            Thread.sleep(1000);
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (ConnectException e) {
//                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Exit client threads");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void startListening() {
        while (!socket.isClosed() || !selfPeer.isCompletedDownloading()) {
            try {
                new PeerConnectionHandler(socket.accept(), peers, selfPeer).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Exit server");
        System.exit(0);
    }


}
