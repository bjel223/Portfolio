*client and server created by Benjamin Ellis for the purpose of completing CS371 and graduating on time.*

-My client/server chat functions with both client and server asking the user for ip and port information.
-The server can be initialized to work as localhost (127.0.0.1) and 0.0.0.0. to bind to available IPs.
-The client must input the ip necessary to connect to the server.
-The default port number used for this application was '27015'
-The client can close their connection and end the application with the command '!quit' when chatting.
-Ending the client connection will not close the server.
-This implementation of the client/server utilizes sockets via winsock. This is because sys/socket.h seems
to be a unix based thing and I only have windows machines.
