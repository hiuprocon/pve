#include <iostream>
//#include <typeinfo>
#include "SampleBase.h"
#include "Vec3d.h"
using namespace std;

/*
 * This enum represents mods of the car (Sample2).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S2Mode {
    GO_TO_WAITING_POINT = 0,
    WAIT_UNTIL_MESSAGE = 1,
    GO_TO_SWITCH = 2,
    BACK_TO_WAITING_POINT = 3
};

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class ArrivalWaitingPointEvent : public Event {};
class ArrivalSwitchEvent : public Event {};

// Location of waiting point.
static const Vec3d waitingPoint(0.0,0.0,30.0);

/*
 * Sample2 is a program which controls the blue car in the
 * simulation environment. This car operates the elevator
 * on demand of the red car. Basic functions are implemented
 * in the SampleBase class which is extended by this Sample2 class.
 */
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
    void backToWaitingPoint();
private:
    // The mode of this car
    S2Mode mode;
};

/*
 * The constructor of Sample2. super(20000) means
 * that the blue car is controled through port 20000
 * (computer networking).
 */
Sample2::Sample2(int port) : SampleBase(port) {
    mode = GO_TO_WAITING_POINT;
}

/*
 * The destructor of Sample2.
 */
Sample2::~Sample2() {
}

/*
 * Check the situations of this car and create some
 * events then call processEvent() method. To process
 * general events, this method call super.stateCheck()
 * at first.
 */
void Sample2::stateCheck() {
    SampleBase::stateCheck();
    Vec3d tmpV;

    tmpV = waitingPoint - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalWaitingPointEvent());

    tmpV = switch1 - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalSwitchEvent());
}

/*
 * Decide the next mode in consideration of the previous
 * mode and the given event. The process is based on FSM
 * (finite state machine). This method implements a strategy
 * of this car.
 */
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
            mode = BACK_TO_WAITING_POINT;
        } else if (message=="pushSwitch") {
            mode = GO_TO_SWITCH;
        }
    } else if ((mode==BACK_TO_WAITING_POINT)
      &&(dynamic_cast<ArrivalWaitingPointEvent*>(e))) {
        mode = WAIT_UNTIL_MESSAGE;
    } else {
      //cout << "Unprocessed event. " << endl;
    }

    delete e;
}

/*
 * Control the car in accordance with the mode of the car.
 */
void Sample2::move() {
//cout << "Sample2: mode=" << mode << endl;
    switch(mode) {
    case GO_TO_WAITING_POINT: goToWaitingPoint(); break;
    case WAIT_UNTIL_MESSAGE: waitUntilMessage(); break;
    case GO_TO_SWITCH: goToSwitch(); break;
    case BACK_TO_WAITING_POINT: backToWaitingPoint(); break;
    default: cout << "Sample2: Unknown mode?" << endl;
    }
}

// The following methods implement processes for each mode.

void Sample2::goToWaitingPoint() {
    goToDestination(waitingPoint);
}

void Sample2::waitUntilMessage() {
    stopCar();
}

void Sample2::goToSwitch() {
    goToDestination(switch1);
}

void Sample2::backToWaitingPoint() {
    backToDestination(waitingPoint);
}

/*
 * The start point of Sample2.
 */
int main() {
  Sample2 s2(20000);
  s2.start();

  return 0;
}
