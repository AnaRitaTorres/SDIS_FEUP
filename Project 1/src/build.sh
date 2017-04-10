#!/bin/sh
rm -rf bin
mkdir -p bin
javac -Xlint:unchecked -d bin -sourcepath client/TestApp.java server/Peer.java 

mkdir -p bin/META-INF
cat <<EOF >bin/META-INF/Peer.mf
Main-Class: server.Peer
EOF

cat <<EOF >bin/META-INF/TestApp.mf
Main-Class: client.TestApp
EOF
