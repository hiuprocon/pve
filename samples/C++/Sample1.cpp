#include <iostream>
#include "SampleBase.h"
#include "Vec3d.h"
using namespace std;

/*
 * This enum represents mods of the car (Sample1).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S1Mode {
    DEVELOP_STRATEGY1 = 0,
    GO_TO_TARGET_JEWEL = 1,
    DEVELOP_STRATEGY2 = 2,
    GO_TO_VIA_POINT = 3,
    GET_ON_ELEVATOR = 4,
    WAIT_UNTIL_TOP = 5,
    GO_TO_GOAL = 6,
    BACK_TO_ELEVATOR_TOP = 7,
    WAIT_UNTIL_BOTTOM = 8,
    GO_TO_VIA_POINT2 = 9,
    END = 10,
};

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class StrategyDevelopedEvent : public Event {};
class HoldingJewelEvent : public Event {};
class NotHoldingJewelEvent : public Event {};
class ArrivalViaPoint1Event : public Event {};
class ArrivalElevatorBottomEvent : public Event {};
class ArrivalElevatorTopEvent : public Event {};
class ArrivalGoalEvent : public Event {};
class ArrivalViaPoint2Event : public Event {};

//For convenience, this car goes by way of via points.
static const Vec3d viaPointA( 30,0, 62.5);
static const Vec3d viaPointB(-30,0, 62.5);

/*
 * Sample1 is a program which controls the red car in the
 * simulation environment. This car carries all jewels.
 * Basic functions are implemented in the SampleBase class
 * which is extended by this Sample1 class.
 */
class Sample1 : public SampleBase {
public:
    Sample1(int port);
    ~Sample1();
    void stateCheck();
    void processEvent(Event *e);
    void move();
    void developStrategy1();
    void goToTargetJewel();
    void developStrategy2();
    void goToViaPoint1();
    void getOnElevator();
    void waitUntilTop();
    void goToGoal();
    void backToElevatorTop();
    void waitUntilBottom();
    void goToViaPoint2();
    void end();
private:
    // The mode of this car.
    S1Mode mode;
    // The following variables are targets.
    string targetJewel;
    Vec3d targetJewelLoc;
    Vec3d targetViaPoint1;
    Vec3d targetViaPoint2;
    Vec3d targetGoal;
};

/*
 * The constructor of Sample1. super(10000) means
 * that the red car is controled through port 10000
 * (computer networking).
 */
Sample1::Sample1(int port) : SampleBase(port) {
    mode = DEVELOP_STRATEGY1;
    targetJewel = "none";
    targetViaPoint1.x = 1000000.0;
    targetViaPoint2.x = 1000000.0;
    targetGoal.x = 1000000.0;
}

/*
 * The destructor of Sample1.
 */
Sample1::~Sample1() {
}

/*
 * Check the situations of this car and create some
 * events then call processEvent() method. To process
 * general events, this method call super.stateCheck()
 * at first.
 */
void Sample1::stateCheck() {
    SampleBase::stateCheck();
    Vec3d tmpV;

    // the car holds the jewel?
    if (targetJewel!="none") {
        targetJewelLoc = jewelSet.get(targetJewel);
        bool hold = true;
        tmpV = targetJewelLoc - loc;
        if (tmpV.length()>2.0) {
            hold = false;
        } else {
            tmpV.normalize();
            if (tmpV * front < 0.6)
                hold = false;
        }
        if (hold==true)
            processEvent(new HoldingJewelEvent());
        else
            processEvent(new NotHoldingJewelEvent());
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

    // car has arrived at the elevator top?
    tmpV = elevatorTop - loc;
    if (tmpV.length()<1.0)
        processEvent(new ArrivalElevatorTopEvent());

    // car has arrived at the goal?
    if (targetGoal.x < 10000.0) {
        tmpV = targetGoal - loc;
        if (tmpV.length()<2.0)
            processEvent(new ArrivalGoalEvent());
    }

    // car has arrived at the via point?
    if (targetViaPoint2.x < 10000.0) {
        tmpV = targetViaPoint2 - loc;
        if (tmpV.length()<2.0)
            processEvent(new ArrivalViaPoint2Event());
    }
}

/*
 * Decide the next mode in consideration of the previous
 * mode and the given event. The process is based on FSM
 * (finite state machine). This method implements a strategy
 * of this car.
 */
void Sample1::processEvent(Event *e) {
    string s;
    if ((mode==DEVELOP_STRATEGY1)
      &&(dynamic_cast<StrategyDevelopedEvent*>(e))) {
        mode = GO_TO_TARGET_JEWEL;
    } else if ((mode==GO_TO_TARGET_JEWEL)
      &&(dynamic_cast<HoldingJewelEvent*>(e))) {
        mode = DEVELOP_STRATEGY2;
        s = socket->send("sendMessage wait");
cout << "Sample1:sendMessage(wait):" << s << endl;
    } else if ((mode==DEVELOP_STRATEGY2)
      &&(dynamic_cast<StrategyDevelopedEvent*>(e))) {
        mode = GO_TO_VIA_POINT;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<ArrivalViaPoint1Event*>(e))) {
        mode = GET_ON_ELEVATOR;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<NotHoldingJewelEvent*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<ArrivalElevatorBottomEvent*>(e))) {
        mode = WAIT_UNTIL_TOP;
        s = socket->send("sendMessage pushSwitch");
cout << "Sample1:sendMessage(pushSwitch):" << s << endl;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<NotHoldingJewelEvent*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((mode==WAIT_UNTIL_TOP)
      &&(dynamic_cast<ArrivalElevatorTopEvent*>(e))) {
        mode = GO_TO_GOAL;
    } else if ((mode==GO_TO_GOAL)
      &&(dynamic_cast<ArrivalGoalEvent*>(e))) {
        mode = BACK_TO_ELEVATOR_TOP;
    } else if ((mode==BACK_TO_ELEVATOR_TOP)
      &&(dynamic_cast<ArrivalElevatorTopEvent*>(e))) {
        mode = WAIT_UNTIL_BOTTOM;
        s = socket->send("sendMessage wait");
cout << "Sample1:sendMessage(wait):" << s << endl;
    } else if ((mode==BACK_TO_ELEVATOR_TOP)
      &&(dynamic_cast<ClearedEvent*>(e))) {
        mode = END;
    } else if ((mode==WAIT_UNTIL_BOTTOM)
      &&(dynamic_cast<ArrivalElevatorBottomEvent*>(e))) {
        mode = GO_TO_VIA_POINT2;
    } else if ((mode==GO_TO_VIA_POINT2)
      &&(dynamic_cast<ArrivalViaPoint2Event*>(e))) {
        mode = DEVELOP_STRATEGY1;
    } else if ((true)&&(dynamic_cast<MessageEvent*>(e))) {
        string message = (dynamic_cast<MessageEvent*>(e))->message;
        cout << "Sample1: Message received?: " << message << endl;
    } else {
      //cout << "Unprocessed event." << endl;
    }

    delete e;
}

/*
 * Control the car in accordance with the mode of the car.
 */
void Sample1::move() {
//cout << "Sample1:mode=" << mode << endl;
    switch(mode) {
    case DEVELOP_STRATEGY1: developStrategy1(); break;
    case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
    case DEVELOP_STRATEGY2: developStrategy2(); break;
    case GO_TO_VIA_POINT: goToViaPoint1(); break;
    case GET_ON_ELEVATOR: getOnElevator(); break;
    case WAIT_UNTIL_TOP: waitUntilTop(); break;
    case GO_TO_GOAL: goToGoal(); break;
    case BACK_TO_ELEVATOR_TOP: backToElevatorTop(); break;
    case WAIT_UNTIL_BOTTOM: waitUntilBottom(); break;
    case GO_TO_VIA_POINT2: goToViaPoint2(); break;
    case END: end(); break;
    default: cout << "Sample1: Unknown mode?" << mode << endl;
    }
}

// The following methods implement processes for each mode.

void Sample1::developStrategy1() {
    targetJewel = jewelSet.getNearest(loc);
    targetJewelLoc = jewelSet.get(targetJewel);
    //if (targetJewelLoc!=NULL)...
    processEvent(new StrategyDevelopedEvent());
}

void Sample1::goToTargetJewel() {
    if (targetJewelLoc.x<10000.0) {
        Vec3d dummy = Vec3d(10000,0,0);
        if (checkAllConflict(loc,targetJewelLoc,dummy)) {
cout << "GAHA:CONFLICT1" << endl;
            Vec3d v = Vec3d(targetJewelLoc);
            v = v - loc;
            v = v.simpleRotateY(45);
            v = v + loc;
            goToDestination(v);
        } else {
            goToDestination(targetJewelLoc);
        }
    }
}

void Sample1::developStrategy2() {
    if (loc.x>0.0) {
        targetViaPoint1 = viaPointA;
        targetGoal = goal1;
        targetViaPoint2 = viaPointB;
    } else {
        targetViaPoint1 = viaPointB;
        targetGoal = goal2;
        targetViaPoint2 = viaPointA;
    }
    processEvent(new StrategyDevelopedEvent());
}

void Sample1::goToViaPoint1() {
    if (checkAllConflict(loc,targetViaPoint1,targetJewelLoc)) {
        cout << "GAHA:CONFLICT2" << endl;
        Vec3d v = Vec3d(targetViaPoint1);
        v = v - loc;
        v.simpleRotateY(45);
        v = v + loc;
        goToDestination(v);
    } else {
        goToDestinationWithJewel(targetViaPoint1);
    }
}

void Sample1::getOnElevator() {
    goToDestinationWithJewel(elevatorBottom);
}

void Sample1::waitUntilTop() {
    stopCar();
}

void Sample1::goToGoal() {
    goToDestinationWithJewel(targetGoal);
}

void Sample1::backToElevatorTop() {
    backToDestination(elevatorTop);
}

void Sample1::waitUntilBottom() {
    stopCar();
}

void Sample1::goToViaPoint2() {
    goToDestination(targetViaPoint2);
}

void Sample1::end() {
    stopCar();
}

/*
 * The start point of Sample1.
 */
int main() {
  Sample1 s1(10000);
  s1.start();

  return 0;
}
