#include <iostream>
#include <winsock2.h>
#include "MySocket.h"
using namespace std;

/* size of buffer */
#define BUF_LEN 256
#define MSG_BUF_LEN 2048

SOCKET sock;

MySocket::MySocket(int port) {
    /* A structure for a socket */
    WSADATA wsaData;
    struct sockaddr_in server;

    WSAStartup(MAKEWORD(2,0), &wsaData);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.S_un.S_addr = inet_addr("127.0.0.1");

    connect(sock, (struct sockaddr *)&server, sizeof(server));

    receive_buf = (char*)malloc(sizeof(char)*MSG_BUF_LEN);
}

MySocket::~MySocket() {
    WSACleanup();
    free(receive_buf);
}

char *MySocket::send(const char msg[]) {
    int pos = 0;

    string msg2(msg);
    msg2 += "\n";
    ::send(sock, msg2.c_str(), msg2.size(), 0);

    while (1){
        char buf[BUF_LEN];
        int read_size,i;
        memset(buf,0,BUF_LEN);
        read_size = recv(sock, buf, BUF_LEN, 0);
        if ( read_size > 0 ){
            for (i=0;i<read_size;i++) {
                if (buf[i]=='\n') {
                    goto OUT1;
                } else if (buf[i]=='\r') {
                    // do nothing
                } else {
                    receive_buf[pos] = buf[i];
                    pos++;
                }
            }
        } else {
            break;
        }
    }
OUT1:
    receive_buf[pos] = '\0';
    return receive_buf;
}

