/* “ú–{Œê“ü‚ê‚ÄSJIS‚ÉŒÅ’è */
#include <stdio.h>
#include <winsock2.h>
#include "my_socket.h"

/* size of buffer */
#define BUF_LEN 256
#define MSG_BUF_LEN 2048

/* A sock for my_socket */
SOCKET sock;
/* A buffer for a recieved message */
char *recieve_buf;


/* Initialize my_socket. */
int init_my_socket(int port)
{
    WSADATA wsaData;
    struct sockaddr_in server;

    WSAStartup(MAKEWORD(2,0), &wsaData);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.S_un.S_addr = inet_addr("127.0.0.1");

    connect(sock, (struct sockaddr *)&server, sizeof(server));

    recieve_buf = (char*)malloc(sizeof(char)*MSG_BUF_LEN);

    return 0;
}

/* Send a one-line message, and receive a one-line message. */
char *my_send(char msg[]) {
    int pos = 0;
    char msg2[256];

    sprintf(msg2,"%s\n",msg);
    send(sock, msg2, strlen(msg2), 0);

    while (1){
        char buf[BUF_LEN];
        int read_size,i;
        memset(buf,0,BUF_LEN);
        read_size = recv(sock, buf, BUF_LEN, 0);
        if ( read_size > 0 ){
            for (i=0;i<read_size;i++) {
                if (buf[i]!='\n') {
                    recieve_buf[pos+i] = buf[i];
                } else {
                    //recieve_buf[pos+i] = buf[i];
                    pos += i;
                    goto OUT1;
                }
            }
            pos += i;
        } else {
            break;
        }
    }
OUT1:
    recieve_buf[pos] = '\0';
    return recieve_buf;
}

/* Close the my_socket. */
void my_close() {
    WSACleanup();
    free(recieve_buf);
}

