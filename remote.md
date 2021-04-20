# Running remote

First build:
```
mvn package
```
    
Next, be inside UF's network, for example:
```
ssh user@storm.cise.ufl.edu
```             

Clone the repository, called ByteTorrent on `storm`


Now, go transfer the compiled jar to server
```
scp Client/target/Client-1.0-SNAPSHOT.jar storm@cise.ufl.edu:~/ByteTorrent/Client/target
```
Also need to scp lib files:
```
scp -r Client/target/lib user@storm.cise.ufl.edu:~/ByteTorrent/Client/target
```

Next ssh into each server to approve the key exchange:
```
ssh MYUSERNAME@lin114-00.cise.ufl.edu
ssh MYUSERNAME@lin114-01.cise.ufl.edu
ssh MYUSERNAME@lin114-02.cise.ufl.edu
...
```

Next ssh into:
```
ssh MYUSERNAME@lin114-00.cise.ufl.edu
```

Run the program
```
cd ByteTorrent
java -cp Client/target/Client-1.0-SNAPSHOT.jar edu.ufl.cise.bytetorrent.StartRemotePeers
```


