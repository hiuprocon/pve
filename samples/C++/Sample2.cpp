#include <iostream>
//#include <typeinfo>
#include "SampleBase.h"
#include "Vec3d.h"
using namespace std;

enum S2Mode {
    GO_TO_WAITING_POINT = 0,
    WAIT_UNTIL_MESSAGE = 1,
    GO_TO_SWITCH = 2,
};

class ArrivalWaitingPointEvent : public Event {};
class ArrivalSwitchEvent : public Event {};

static const Vec3d waitingPoint(0.0,0.0,20.0);

class Sample2 : public SampleBase {
public:
    Sample2(int port);
   ~Sample2();
    void stateCheck();
    void processEvent(Event *e);
    void move();
    void goToWaitingPoint();
    void waitUntilMessage();
    void goToSwitch();
private:
    S2Mode mode;
    Vec3d destination;
};

Sample2::Sample2(int port) : SampleBase(port) {
    destination = waitingPoint;
    mode = GO_TO_WAITING_POINT;
}

Sample2::~Sample2() {
}

void Sample2::stateCheck() {
    SampleBase::stateCheck();
    Vec3d tmpV;

    tmpV = waitingPoint - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalWaitingPointEvent());

    tmpV = switch2 - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalSwitchEvent());
}

void Sample2::processEvent(Event *e) {
    if ((mode==GO_TO_WAITING_POINT)
      &&(dynamic_cast<ArrivalWaitingPointEvent*>(e))) {
        mode = WAIT_UNTIL_MESSAGE;
    } else if ((mode==GO_TO_SWITCH)
      &&(dynamic_cast<ArrivalSwitchEvent*>(e))) {
        mode = WAIT_UNTIL_MESSAGE;
    } else if ((mode==WAIT_UNTIL_MESSAGE)
      &&(dynamic_cast<MessageEvent*>(e))) {
        string message = (dynamic_cast<MessageEvent*>(e))->message;
        cout << "Sample2:reciveMessage: " << message << endl;
        if (message=="wait") {
            mode = GO_TO_WAITING_POINT;
        } else if (message=="pushSwitch") {
            mode = GO_TO_SWITCH;
        }
    } else {
      //cout << "Unprocessed event. " << endl;
    }
}

void Sample2::move() {
//cout << "Sample2: mode=" << mode << endl;
    switch(mode) {
    case GO_TO_WAITING_POINT: goToWaitingPoint(); break;
    case WAIT_UNTIL_MESSAGE: waitUntilMessage(); break;
    case GO_TO_SWITCH: goToSwitch(); break;
    default: cout << "" << endl;
    }
}

void Sample2::goToWaitingPoint() {
    goToDestination(waitingPoint);
}

void Sample2::waitUntilMessage() {
    stopCar();
}

void Sample2::goToSwitch() {
    goToDestination(switch2);
}

int main() {
  Sample2 s2(20000);
  s2.start();

  return 0;
}
