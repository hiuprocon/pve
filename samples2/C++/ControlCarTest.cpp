#include <iostream>
#include <string>
#include "MySocket.h"
using namespace std;

/*
 * A test program for controring a car.
 */
int main() {
    // Create a simplified socket for a car.
    //   port:10000 -> Car1
    //   port:20000 -> Car2
    MySocket socket(10000);

    // Get the location of the car, and print it.
    string msg = socket.send("getLoc");
    cout << msg << endl;

    // Send a command to control the engine and the steering.
    //   Engine output : 1
    //   Steering      : 3(clockwise direction)
    // Then print the result of the command.
    msg = socket.send("drive 1 3");
    cout << msg << endl;

    // Sleep for 10 seconds.(The car continues to drive.)
    for (int i=0;i<30*10;i++) {
        msg = socket.send("stepForward");
        cout << msg << endl;
    }

    // Send a command to stop the car.
    msg = socket.send("drive 0 0");
    cout << msg << endl;

    // Get the location of the car, and print it once more.
    msg = socket.send("getLoc");
    cout << msg << endl;

    return 0;
}
