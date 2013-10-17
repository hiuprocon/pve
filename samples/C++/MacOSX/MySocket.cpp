#include <iostream>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/param.h>
#include <sys/uio.h>
#include <unistd.h>
#include "MySocket.h"
#include <arpa/inet.h> //なんで？？？
using namespace std;

/* size of buffer */
#define BUF_LEN 256
#define MSG_BUF_LEN 2048

int s;

MySocket::MySocket(int port) {
    /* A structure for a socket */
    struct sockaddr_in server;

    /* 0 clear the structure */
    bzero(&server, sizeof(server));

    server.sin_family = AF_INET;
    /* IP address */
    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    //server.sin_addr.s_addr = inet_addr("10.13.114.1");
    /* port number */
    server.sin_port = htons(port);
    /* create a socket */
    if ( ( s = socket(AF_INET, SOCK_STREAM, 0) ) < 0 ){
        cerr << "failed to create a socket." << endl;
    }
    /* connect to a server */
    if ( connect(s, (struct sockaddr *)&server, sizeof(server)) == -1 ){
        cerr << "failed to connect." << endl;
    }

    receive_buf = (char*)malloc(sizeof(char)*MSG_BUF_LEN);
}

MySocket::~MySocket() {
    close(s);
    free(receive_buf);
}

char *MySocket::send(const char msg[]) {
    int pos = 0;

    string msg2(msg);
    msg2 += "\n";
    write(s, msg2.c_str(), msg2.size());

    while (1){
        char buf[BUF_LEN];
        int read_size,i;
        memset(buf,0,BUF_LEN);
        read_size = read(s, buf, BUF_LEN);
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

