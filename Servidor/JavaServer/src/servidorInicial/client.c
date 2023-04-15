// Client side C/C++ program to demonstrate Socket
// programming
#include <stdio.h>
#include <winsock2.h>
#pragma comment(lib, "ws2_32.lib")
#define PORT 8080

int main(int argc, char const* argv[]) {
    WSADATA wsa;
    SOCKET client_fd;
    struct sockaddr_in serv_addr;
    char* hello = "Over";
    char buffer[1024] = { 0 };
        // Initialize Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("WSAStartup failed. Error Code : %d", WSAGetLastError());
        return 1;
    }

    // Create socket
    if ((client_fd = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("Could not create socket : %d", WSAGetLastError());
    }

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Convert IPv4 and IPv6 addresses from text to binary
    // form
    if (inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr)
        <= 0) {
        printf(
            "\nInvalid address/ Address not supported \n");
        return -1;
    }

    // Connect to server
    if (connect(client_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
        printf("\nConnection Failed \n");
        return -1;
    }

    // Send message to server
    send(client_fd, hello, strlen(hello), 0);
    printf("Hello message sent\n");

    // Receive message from server
    int valread;
    if ((valread = recv(client_fd, buffer, 1024, 0)) == SOCKET_ERROR) {
        printf("recv failed with error code : %d", WSAGetLastError());
    }
    printf("%s\n", buffer);

    // closing the connected socket
    closesocket(client_fd);

    // Cleanup Winsock
    WSACleanup();

    return 0;
}