/* my_socket.h */
#ifndef MY_SOCKET
#define MY_SOCKET

/* Initialize my_socket. */
int init_my_socket(int port);

/* Send a one-line message, and receive a one-line message. */
char *my_send(char msg[]);

/* Close the my_socket. */
void my_close();

#endif
