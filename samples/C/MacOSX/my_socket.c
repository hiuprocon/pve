#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/param.h>
#include <sys/uio.h>
#include <unistd.h>
#include "my_socket.h"

/* size of buffer */
#define BUF_LEN 256
#define MSG_BUF_LEN 2048

/* A file descriptor for my_socket */
int s;
/* A buffer for a recieved message */
char *recieve_buf;


/* Initialize my_socket. */
int init_my_socket(int port)
{
    /* A structure for a socket */
    struct sockaddr_in server;

    /* 0 clear the structure */
    bzero(&server, sizeof(server));

    server.sin_family = AF_INET;
    /* IP address */
    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    /* port number */
    server.sin_port = htons(port);
    /* create a socket */
    if ( ( s = socket(AF_INET, SOCK_STREAM, 0) ) < 0 ){
        fprintf(stderr, "failed to create a socket.\n");
        return 1;
    }
    /* connect to a server */
    if ( connect(s, (struct sockaddr *)&server, sizeof(server)) == -1 ){
        fprintf(stderr, "failed to connect.\n");
        return 1;
    }

    recieve_buf = (char*)malloc(sizeof(char)*MSG_BUF_LEN);
}

/* Send a one-line message, and receive a one-line message. */
char *my_send(char msg[]) {
    int pos = 0;
    char msg2[256];

    sprintf(msg2,"%s\n",msg);
    write(s, msg2, strlen(msg2));

    while (1){
        char buf[BUF_LEN];
        int read_size,i;
        read_size = read(s, buf, BUF_LEN);
        if ( read_size > 0 ){
            for (i=0;i<read_size;i++) {
                if (buf[i]=='\n') {
                    goto OUT1;
                } else if (buf[i]=='\r') {
                    // do nothing
                } else {
                    recieve_buf[pos] = buf[i];
                    pos++;
                }
            }
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
    close(s);
    free(recieve_buf);
}

