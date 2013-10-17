#include <iostream>
#include "MySocket.h"
using namespace std;

int main() {
  MySocket socket(10000);

  char *msg = socket.send((char*)"drive 1.0 1.0");
  cout << msg << endl;

  for (int i=0;i<30*10;i++) {
      msg = socket.send((char*)"stepForward");
      cout << msg << endl;
  }

  msg = socket.send((char*)"searchJewels");
  cout << msg << endl;

  return 0;
}
