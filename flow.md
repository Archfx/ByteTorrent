`start/StartRemotePeers.java` reads the `PeerInfo.cfg` file and performs and ssh and starts runs the `PeerProcess.java` with an ID as an argument.

`peer/PeerProcess.java` is the main entry which gets and ID, reads `PeerInfo.cfg` and saves a list of which peers to connect to. It then constructs `PeerProcessThread` which is run in its own thread.

`peer/PeerProcessThread.java` does most of the work. It starts a server socket to establish connections with peers by obtaining handshake messages, and sends the Bitfield payload message.

`peer/PeerStateManager.java` keeps the state of everything a peer is doing. It creates additional threads to find `kNeigherboringPeers`, does `unchocking` processing, etc. 

TODO: `ConnectionHandler.java` waits and receives messages and sends request messages to neighbors, while also checking on file status. It works with PeerStateManager to handle updates and known when or when not to do things.