#include <stdio.h>
#include "my_socket.h"

int main(int argc, char *argv[]){
  char *msg;
  int i;

  init_my_socket(10000);

  msg = my_send("drive 1.0 1.0");
  printf("%s\n",msg);

  for (i=0;i<30*10;i++) {
    msg = my_send("stepForward");
    //printf("%s\n",msg);
  }

  msg = my_send("searchJewels");
  printf("%s\n",msg);

  my_close();

  return 0;
}
