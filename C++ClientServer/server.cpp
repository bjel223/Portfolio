/*
*CS371 Final Project Server Program
*Written by: Benjamin Ellis
*Due Date: 12/16/2019
*/

#include <iostream>
#include <WinSock2.h>
#include <windows.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>

#pragma comment (lib, "Ws2_32.lib")
#pragma comment (lib, "Mswsock.lib")
#pragma comment (lib, "AdvApi32.lib")

//Default values for buffer length and port number.
#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "27015"

using namespace std;

int main() {
	WSADATA wsaData;
	int iResult;
	SOCKET listenSocket = INVALID_SOCKET;
	SOCKET clientSocket = INVALID_SOCKET;
	struct addrinfo* result = NULL;
	struct addrinfo hints;
	bool ignoreCheck = true;
	string tempIP;
	PCSTR port;
	string tempPort;
	PCSTR serverIP;

	cout << "Welcome to the Server Portion." << endl;
	//Get user input for IP and Port information
	cout << "Enter IP: ";
	cin >> tempIP;
	serverIP = tempIP.c_str();
	cout << "Enter Port: ";
	cin >> tempPort;
	port = tempPort.c_str();

	//This outer loop allows the server to continue running if a client connects and disconnects.
	do {
		iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
		if (iResult != 0) {
			cout << "Startup failed." << endl;
			return 1;
		}

		ZeroMemory(&hints, sizeof(hints));
		hints.ai_family = AF_INET;
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_protocol = IPPROTO_TCP;
		hints.ai_flags = AI_PASSIVE;

		//Get necessary information from the user provided ip and port number
		iResult = getaddrinfo(serverIP, port, &hints, &result);
		if (iResult != 0) {
			cout << "getaddrinfo Failed." << endl;
			WSACleanup();
			return 1;
		}
		//Initialize the listenSocket so the server can look for connecting clients.
		listenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
		if (listenSocket == INVALID_SOCKET) {
			cout << "listenSocket Failed." << endl;
		}

		cout << "Socket Created." << endl;

		//Bind the listenSocket
		iResult = bind(listenSocket, result->ai_addr, (int)result->ai_addrlen);
		if (iResult == SOCKET_ERROR) {
			cout << "Bind Failed." << endl << WSAGetLastError() << endl;
			freeaddrinfo(result);
			closesocket(listenSocket);
			WSACleanup();
			return 1;
		}
		cout << "Bind complete." << endl;

		freeaddrinfo(result);

		//Start listening for a connection.
		iResult = listen(listenSocket, SOMAXCONN);
		if (iResult == SOCKET_ERROR) {
			cout << "Listen Failed." << endl;
			closesocket(listenSocket);
			WSACleanup();
			return 1;
		}
		cout << "Listening for connection." << endl;
		//Attempt to accept a connection with a client
		clientSocket = accept(listenSocket, NULL, NULL);
		if (clientSocket == INVALID_SOCKET) {
			cout << "Accept Failed." << endl;
			closesocket(listenSocket);
			WSACleanup();
			return 1;
		}
		
		//listenSocket no longer needed now that we are connected with the clientSocket
		closesocket(listenSocket);
		cout << "Connected." << endl;

		//This inner loop handles the chat mechanics. The server starts the conversation with a message and then alternates with the client. Clients can disconnect without the server shutting down.
		do {
			string yourMessage;
			string theirMessage;
			char tempMessage[DEFAULT_BUFLEN];
			int messageBufLen = DEFAULT_BUFLEN;
			cout << "Your Message: ";
			if (ignoreCheck) {
				cin.ignore();
				ignoreCheck = false;
			}
			cin.clear();
			getline(cin, yourMessage);
			iResult = send(clientSocket, yourMessage.c_str(), yourMessage.length(), 0);
			cout << "Message Sent to Client." << endl << "Waiting for Client..." << endl;
			iResult = recv(clientSocket, tempMessage, messageBufLen, 0);
			theirMessage = (string)tempMessage;
			cout << "Client Message: " << theirMessage.substr(0,iResult) << endl;
		} while (iResult > 0);

		iResult = shutdown(clientSocket, SD_SEND);
		if (iResult == SOCKET_ERROR) {
			cout << "Shutdown Failed." << endl;
			closesocket(clientSocket);
			WSACleanup();
			return 1;
		}

		//Close sockets and cleanup if nothing went wrong and we're done.
		closesocket(clientSocket);
		WSACleanup();
	} while (1);
	return 0;
}