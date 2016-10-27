"Digital signature implementation"

This is an example of how digital signature works by simulating a client-server conection.

What this program does:
Client hashes file and sends it to the server through sockets.
Server recieves the file.
Server confirms checksum for non repudiation.
Server timestamps the File. 
Server sends the file with the timestamp and the public key to the client.
Client checks public key.
