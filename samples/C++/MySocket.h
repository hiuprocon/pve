/* MySocket.h */
#ifndef MY_SOCKET
#define MY_SOCKET

#include <string>
using namespace std;

/*
 * MySocket is a simplyfied socket for one-line text messaging.
 */
class MySocket {
public:
    MySocket(int port);
    ~MySocket();
    string send(const string);
private:
    // socket
    char *receive_buf;
};

#endif
