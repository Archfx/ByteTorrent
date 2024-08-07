# ByteTorrent

Experimental implementation of BitTorrent protocol.

## Build and run:

Build first:
```bash
mvn package
```

To run all processes via ssh:
```bash
java -cp Client/target/Client-1.0-SNAPSHOT.jar edu.ufl.cise.bytetorrent.StartRemotePeers
```

To run individual PeerProcess:
```bash
java -cp Client/target/Client-1.0-SNAPSHOT.jar edu.ufl.cise.bytetorrent.PeerProcess 1001
```

## Handshake message

The handshake consists of three parts: handshake header, zero bits, and peer ID. The length of the handshake message is 32 bytes. The handshake header is 18-byte string
‘P2PFILESHARINGPROJ’, which is followed by 10-byte zero bits, which is followed by 4-byte peer ID which is the integer representation of the peer ID.

```shell
 +----------------------------------------+
 | handshake header | zero bits | peer ID |
 +----------------------------------------+
```
## Actual message

After handshaking, each peer can send a stream of actual messages. An actual message consists of 4-byte message length field, 1-byte message type field, and a message payload with variable size.

```shell
 +-------------------------------------------------+
 | message length | message type | message payload |
 +-------------------------------------------------+
```
The 4-byte message length specifies the message length in bytes. It does not include the length of the message length field itself. The 1-byte message type field specifies the type of the message.

There are eight types of messages.
```shell
  +--------------+-------+
 | message type | value |
 +--------------+-------+
 | choke             0  |
 +----------------------+
 | unchoke           1  |
 +----------------------+
 | interested        2  |
 +----------------------+
 | not interested    3  |
 +----------------------+
 | have              4  |
 +----------------------+
 | bitfield          5  |
 +----------------------+
 | request           6  |
 +----------------------+
 | piece             7  |
 +----------------------+
```

## choke and unchoke
The number of concurrent connections on which a peer uploads its pieces is limited. At a moment, each peer uploads its pieces to at most k preferred neighbors and 1 optimistically
unchoked neighbor. The value of k is given as a parameter when the program starts. Each peer uploads its pieces only to preferred neighbors and an optimistically unchoked
neighbor. We say these neighbors are unchoked and all other neighbors are choked. Each peer determines preferred neighbors every p seconds. Suppose that the unchoking interval is p . Then every p seconds, peer A reselects its preferred neighbors. To make the decision, peer A calculates the downloading rate from each of its neighbors, respectively, during the previous unchoking interval. Among neighbors that are interested
in its data, peer A picks k neighbors that has fed its data at the highest rate. If more than two peers have the same rate, the tie should be broken randomly. Then it unchokes those
preferred neighbors by sending ‘unchoke’ messages and it expects to receive ‘request’ messages from them. If a preferred neighbor is already unchoked, then peer A does not
have to send ‘unchoke’ message to it. All other neighbors previously unchoked but notselected as preferred neighbors at this time should be choked unless it is an optimistically
unchoked neighbor. To choke those neighbors, peer A sends ‘choke’ messages to them and stop sending pieces.
If peer A has a complete file, it determines preferred neighbors randomly among those that are interested in its data rather than comparing downloading rates.
Each peer determines an optimistically unchoked neighbor every m seconds. We say mis the optimistic unchoking interval. Every m seconds, peer A reselects an optimistically
unchoked neighbor randomly among neighbors that are choked at that moment but are interested in its data. Then peer A sends ‘unchoke’ message to the selected neighbor and
it expects to receive ‘request’ messages from it. Suppose that peer C is randomly chosen as the optimistically unchoked neighbor of peer
A. Because peer A is sending data to peer C, peer A may become one of peer C’s preferred neighbors, in which case peer C would start to send data to peer A. If the rate
at which peer C sends data to peer A is high enough, peer C could then, in turn, become one of peer A’s preferred neighbors. Note that in this case, peer C may be a preferred
neighbor and optimistically unchoked neighbor at the same time. This kind of situation is allowed. In the next optimistic unchoking interval, another peer will be selected as an
optimistically unchoked neighbor.
