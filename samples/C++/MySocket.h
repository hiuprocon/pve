/* MySocket.h */
#ifndef MY_SOCKET
#define MY_SOCKET

class MySocket {
public:
    MySocket(int port);
    ~MySocket();
    char *send(const char msg[]);
private:
    // socket
    char *receive_buf;
};

#endif
