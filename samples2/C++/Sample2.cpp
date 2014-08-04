#include <iostream>
#include "SampleBase.h"
#include "Vec3d.h"
using namespace std;

/*
 * This enum represents mods of the car (Sample2).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S2Mode {
    DEVELOP_STRATEGY1 = 0,
    GO_TO_TARGET_BURDEN = 1,
    DEVELOP_STRATEGY2 = 2,
    GO_TO_VIA_POINT = 3,
    GET_ON_ELEVATOR = 4,
    BACK_TO_VIA_POINT2 = 5,
    GO_TO_SWITCH = 6,
    GO_TO_VIA_POINT3 = 7,
    END = 8,
};

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class StrategyDevelopedEvent : public Event {};
class HoldingBurdenEvent : public Event {};
class NotHoldingBurdenEvent : public Event {};
class ArrivalViaPoint1Event : public Event {};
class ArrivalElevatorBottomEvent : public Event {};
class ArrivalViaPoint2Event : public Event {};
class ArrivalViaPoint3Event : public Event {};
class ArrivalSwitchEvent : public Event {};

//For convenience, this car goes by way of via points.
static const Vec3d viaPointA( 30,0, 62.5);
static const Vec3d viaPointB(-30,0, 62.5);

/*
 * Sample2 is a program which controls the blue car in the
 * simulation environment. This car carries all burdens.
 * Basic functions are implemented in the SampleBase class
 * which is extended by this Sample2 class.
 */
class Sample2 : public SampleBase {
public:
    Sample2(int port);
    ~Sample2();
    void stateCheck();
    void processEvent(Event *e);
    void move();
    void developStrategy1();
    void goToTargetBurden();
    void developStrategy2();
    void goToViaPoint1();
    void getOnElevator();
    void backToViaPoint2();
    void goToSwitch();
    void goToViaPoint3();
    void end();
private:
    // The mode of this car.
    S2Mode mode;
    // The following variables are targets.
    string targetBurden;
    Vec3d targetBurdenLoc;
    Vec3d targetViaPoint1;
    Vec3d targetViaPoint2;
    Vec3d targetViaPoint3;
    string lastMessage;
};

/*
 * The constructor of Sample2. super(20000) means
 * that the red car is controled through port 20000
 * (computer networking).
 */
Sample2::Sample2(int port) : SampleBase(port) {
    mode = DEVELOP_STRATEGY1;
    targetBurden = "none";
    targetViaPoint1.x = 1000000.0;
    targetViaPoint2.x = 1000000.0;
    targetViaPoint3.x = 1000000.0;
    lastMessage = "dummy";
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

    // the car holds the burden?
    if (targetBurden!="none") {
        targetBurdenLoc = burdenSet.get(targetBurden);
        bool hold = true;
        tmpV = targetBurdenLoc - loc;
        if (tmpV.length()>2.0) {
            hold = false;
        } else {
            tmpV.normalize();
            if (tmpV * front < 0.6)
                hold = false;
        }
        if (hold==true)
            processEvent(new HoldingBurdenEvent());
        else
            processEvent(new NotHoldingBurdenEvent());
    }

    // car has arrived at the via point?
    if (targetViaPoint1.x < 10000.0) {
        tmpV = targetViaPoint1 - loc;
        if (tmpV.length()<2.0)
            processEvent(new ArrivalViaPoint1Event());
    }

    // car has arrived at the elevator bottom?
    tmpV = elevatorBottom - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalElevatorBottomEvent());

    // car has arrived at the via point2?
    if (targetViaPoint2.x < 10000.0) {
        tmpV = targetViaPoint2 - loc;
        if (tmpV.length()<2.0)
            processEvent(new ArrivalViaPoint2Event());
    }

    // car has arrived at the switch?
    tmpV = switch1 - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalSwitchEvent());

    // car has arrived at the via point3?
    if (targetViaPoint3.x < 10000.0) {
        tmpV = targetViaPoint3 - loc;
        if (tmpV.length()<2.0)
            processEvent(new ArrivalViaPoint3Event());
    }
}

/*
 * Decide the next mode in consideration of the previous
 * mode and the given event. The process is based on FSM
 * (finite state machine). This method implements a strategy
 * of this car.
 */
void Sample2::processEvent(Event *e) {
    string s;
    if ((mode==DEVELOP_STRATEGY1)
      &&(dynamic_cast<StrategyDevelopedEvent*>(e))) {
        mode = GO_TO_TARGET_BURDEN;
    } else if ((mode==GO_TO_TARGET_BURDEN)
      &&(dynamic_cast<HoldingBurdenEvent*>(e))) {
        mode = DEVELOP_STRATEGY2;
    } else if ((mode==DEVELOP_STRATEGY2)
      &&(dynamic_cast<StrategyDevelopedEvent*>(e))) {
        mode = GO_TO_VIA_POINT;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<ArrivalViaPoint1Event*>(e))) {
        mode = GET_ON_ELEVATOR;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<NotHoldingBurdenEvent*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<ArrivalElevatorBottomEvent*>(e))) {
        mode = BACK_TO_VIA_POINT2;
        s = socket->send("sendMessage READY");
cout << "Sample2:sendMessage(READY):" << s << endl;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<NotHoldingBurdenEvent*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((mode==BACK_TO_VIA_POINT2)
      &&(dynamic_cast<ArrivalViaPoint2Event*>(e))) {
        mode = GO_TO_SWITCH;
    } else if ((mode==GO_TO_SWITCH)
      &&(dynamic_cast<ArrivalSwitchEvent*>(e))) {
        mode = GO_TO_VIA_POINT3;
        s = socket->send("sendMessage NOT_READY");
cout << "Sample2:sendMessage(NOT_READY):" << s << endl;
    } else if ((mode==GO_TO_VIA_POINT3)
      &&(dynamic_cast<ArrivalViaPoint3Event*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((true)&&(dynamic_cast<MessageEvent*>(e))) {
        lastMessage = (dynamic_cast<MessageEvent*>(e))->message;
        cout << "Sample2: Message received: " << lastMessage << endl;
    } else {
      //cout << "Unprocessed event." << endl;
    }

    delete e;
}

/*
 * Control the car in accordance with the mode of the car.
 */
void Sample2::move() {
//cout << "Sample2:mode=" << mode << endl;
    switch(mode) {
    case DEVELOP_STRATEGY1: developStrategy1(); break;
    case GO_TO_TARGET_BURDEN: goToTargetBurden(); break;
    case DEVELOP_STRATEGY2: developStrategy2(); break;
    case GO_TO_VIA_POINT: goToViaPoint1(); break;
    case GET_ON_ELEVATOR: getOnElevator(); break;
    case BACK_TO_VIA_POINT2: backToViaPoint2(); break;
    case GO_TO_SWITCH: goToSwitch(); break;
    case GO_TO_VIA_POINT3: goToViaPoint3(); break;
    case END: end(); break;
    default: cout << "Sample2: Unknown mode?" << mode << endl;
    }
}

// The following methods implement processes for each mode.

void Sample2::developStrategy1() {
    targetBurden = burdenSet.getNearest(loc);
    targetBurdenLoc = burdenSet.get(targetBurden);
    //if (targetBurdenLoc!=NULL)...
    processEvent(new StrategyDevelopedEvent());
}

void Sample2::goToTargetBurden() {
    if (targetBurdenLoc.x<10000.0) {
        Vec3d dummy = Vec3d(10000,0,0);
        if (checkAllConflict(loc,targetBurdenLoc,dummy)) {
cout << "GAHA:CONFLICT1" << endl;
            Vec3d v = Vec3d(targetBurdenLoc);
            v = v - loc;
            v = v.simpleRotateY(45);
            v = v + loc;
            goToDestination(v);
        } else {
            goToDestination(targetBurdenLoc);
        }
    }
}

void Sample2::developStrategy2() {
    if (loc.x>0.0) {
        targetViaPoint1 = viaPointA;
        targetViaPoint2 = viaPointA;
        targetViaPoint3 = viaPointB;
    } else {
        targetViaPoint1 = viaPointB;
        targetViaPoint2 = viaPointB;
        targetViaPoint3 = viaPointA;
    }
    processEvent(new StrategyDevelopedEvent());
}

void Sample2::goToViaPoint1() {
    if (checkAllConflict(loc,targetViaPoint1,targetBurdenLoc)) {
        cout << "GAHA:CONFLICT2" << endl;
        Vec3d v = Vec3d(targetViaPoint1);
        v = v - loc;
        v.simpleRotateY(45);
        v = v + loc;
        goToDestinationWithBurden(v);
    } else {
        goToDestinationWithBurden(targetViaPoint1);
    }
}

void Sample2::getOnElevator() {
    goToDestinationWithBurden(elevatorBottom);
}

void Sample2::backToViaPoint2() {
    backToDestination(targetViaPoint2);
}

void Sample2::goToSwitch() {
    if (lastMessage=="READY")
        goToDestination(switch1);
    else
        stopCar();
}

void Sample2::goToViaPoint3() {
    if (lastMessage=="GOAL")
        goToDestination(targetViaPoint3);
    else
        stopCar();
}

void Sample2::end() {
    stopCar();
}

/*
 * The start point of Sample2.
 */
int main() {
  Sample2 s2(20000);
  s2.start();

  return 0;
}
