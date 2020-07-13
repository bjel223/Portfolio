/*
*CS371 Final Project Client Program
*Written by: Benjamin Ellis
*Due Date: 12/16/2019
*/
#include <iostream>
#include <string>
#include <WinSock2.h>
#include <Windows.h>
#include <WS2tcpip.h>
#include <stdlib.h>
#include <stdio.h>


#pragma comment (lib, "Ws2_32.lib")
#pragma comment (lib, "Mswsock.lib")
#pragma comment (lib, "AdvApi32.lib")

#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "27015"

using namespace std;

int main() {
	WSADATA wsaData;
	SOCKET connectSocket = INVALID_SOCKET;
	struct addrinfo* result = NULL;
	struct addrinfo* ptr = NULL;
	struct addrinfo hints;
	string tempMessage;
	int iResult;
	string tempIP;
	PCSTR serverIP;
	PCSTR port;
	string tempPort;
	bool ignoreCheck = true;

	cout << "Welcome to the Client Portion." << endl;
	//Get user input for the server IP and Port number
	cout << "Enter Server IP: ";
	cin >> tempIP;
	serverIP = tempIP.c_str();
	cout << "Enter Port: ";
	cin >> tempPort;
	port = tempPort.c_str();
	
	//Start WSAStartup
	iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		cout << "WSAStartup Failed." << endl;
		return 1;
	}

	ZeroMemory(&hints, sizeof(hints));
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;

	//Get appropriate information based on user provided IP and Port information
	iResult = getaddrinfo(serverIP, port, &hints, &result);
	if (iResult != 0) {
		cout << "getaddrinfo Failed." << endl;
		WSACleanup();
		return 1;
	}

	//Create the socket and connect to the server
	for (ptr = result; ptr != NULL; ptr = ptr->ai_next) {
		connectSocket = socket(ptr->ai_family, ptr->ai_socktype, ptr->ai_protocol);
		if (connectSocket == INVALID_SOCKET) {
			cout << "Socket Failed." << endl;
			WSACleanup();
			return 1;
		}

		iResult = connect(connectSocket, ptr->ai_addr, (int)ptr->ai_addrlen);
		if (connectSocket == SOCKET_ERROR) {
			closesocket(connectSocket);
			connectSocket = INVALID_SOCKET;
			continue;
		}
		break;
	}

	freeaddrinfo(result);

	//Check if we were able to connect to the server
	if (connectSocket == INVALID_SOCKET) {
		cout << "Unable to connect to server." << endl;
		WSACleanup();
		return 1;
	}

	//This loop contains all the interaction of the chat feature. Initially the client waits for the server to start the conversation, then the two alternate in sending/receiving messages.
	do {
		string yourMessage;
		string theirMessage;
		char tempMessage[DEFAULT_BUFLEN];
		int messageBufLen = DEFAULT_BUFLEN;
		cout << "Waiting for Server..." << endl;
		iResult = recv(connectSocket, tempMessage, messageBufLen, 0);
		theirMessage = (string)tempMessage;
		cout << "Server Message: " << theirMessage.substr(0,iResult) << endl;
		cout << "Your message: ";
		if (ignoreCheck) {
			cin.ignore();
			ignoreCheck = false;
		}
		cin.clear();
		getline(cin,yourMessage);
		//Users can use '!quit' to end their connection with the server.
		if (!((string)yourMessage).compare("!quit")) {
			break;
		}
		iResult = send(connectSocket, yourMessage.c_str(), ((string)yourMessage).length(), 0);
		cout << "Message Sent to Server." << endl;
	} while (iResult > 0);

	iResult = shutdown(connectSocket, SD_SEND);
	if (iResult == SOCKET_ERROR) {
		cout << "Shutdown failed." << WSAGetLastError() << endl;
		closesocket(connectSocket);
		WSACleanup();
		return 1;
	}

	//In the case that nothing went wrong, here we close the socket and cleanup before ending the program.
	closesocket(connectSocket);
	WSACleanup();

	return 0;
}