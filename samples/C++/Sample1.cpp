#include <iostream>
#include "SampleBase.h"
#include "Vec3d.h"
using namespace std;

enum S1Mode {
    DETERMINE_TARGET_JEWEL = 0,
    GO_TO_TARGET_JEWEL = 1,
    DETERMINE_WHITCH_VIA_POINT = 2,
    GO_TO_VIA_POINT = 3,
    GET_ON_ELEVATOR = 4,
    WAIT_UNTIL_TOP = 5,
    GO_TO_GOAL = 6,
    BACK_TO_ELEVATOR_TOP = 7,
    WAIT_UNTIL_BOTTOM = 8,
    GO_TO_VIA_POINT2 = 9,
    END = 10,
};

class DetermineTargetJewelEvent : public Event {};
class HoldingJewelEvent : public Event {};
class NotHoldingJewelEvent : public Event {};
class DetermineViaPointEvent : public Event {};
class ArrivalViaPoint1Event : public Event {};
class ArrivalElevatorBottomEvent : public Event {};
class ArrivalElevatorTopEvent : public Event {};
class ArrivalGoalEvent : public Event {};
class ArrivalViaPoint2Event : public Event {};


static const Vec3d viaPointA( 30,0, 0);
static const Vec3d viaPointB(-30,0, 0);

class Sample1 : public SampleBase {
public:
    Sample1(int port);
   ~Sample1();
    void stateCheck();
    void processEvent(Event *e);
    void move();
    void determineTargetJewel();
    void goToTargetJewel();
    void determineWitchViaPoint();
    void goToViaPoint1();
    void getOnElevator();
    void waitUntilTop();
    void goToGoal();
    void backToElevatorTop();
    void waitUntilBottom();
    void goToViaPoint2();
    void end();
private:
    S1Mode mode;
    string targetJewel;
    Vec3d targetJewelLoc;
    Vec3d targetViaPoint1;
    Vec3d targetViaPoint2;
    Vec3d targetGoal;
};

Sample1::Sample1(int port) : SampleBase(port) {
    mode = DETERMINE_TARGET_JEWEL;
    targetJewel = "none";
    targetViaPoint1.x = 1000000.0;
    targetViaPoint2.x = 1000000.0;
    targetGoal.x = 1000000.0;
}

Sample1::~Sample1() {
}

void Sample1::stateCheck() {
    SampleBase::stateCheck();
    Vec3d tmpV;

    // the car holds the jewel?
    if (targetJewel!="none") {
      Vec3d *retV = jewelSet.get(targetJewel);
      if (retV!=0)
          targetJewelLoc = *(retV);
      bool hold = true;
      if (retV==0) {
        hold = false;
      } else {
        tmpV = targetJewelLoc - loc;
        if (tmpV.length()>2.0) {
          hold = false;
        } else {
          tmpV.normalize();
          if (tmpV * front < 0.6)
            hold = false;
        }
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

void Sample1::processEvent(Event *e) {
    char *s;
    if ((mode==DETERMINE_TARGET_JEWEL)
      &&(dynamic_cast<DetermineTargetJewelEvent*>(e))) {
        mode = GO_TO_TARGET_JEWEL;
    } else if ((mode==GO_TO_TARGET_JEWEL)
      &&(dynamic_cast<HoldingJewelEvent*>(e))) {
        mode = DETERMINE_WHITCH_VIA_POINT;
        s = socket->send((char*)"sendMessage wait");
cout << "Sample1:sendMessage(wait):" << s << endl;
    } else if ((mode==DETERMINE_WHITCH_VIA_POINT)
      &&(dynamic_cast<DetermineViaPointEvent*>(e))) {
        mode = GO_TO_VIA_POINT;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<ArrivalViaPoint1Event*>(e))) {
        mode = GET_ON_ELEVATOR;
    } else if ((mode==GO_TO_VIA_POINT)
      &&(dynamic_cast<NotHoldingJewelEvent*>(e))) {
        mode = DETERMINE_TARGET_JEWEL;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<ArrivalElevatorBottomEvent*>(e))) {
        mode = WAIT_UNTIL_TOP;
        s = socket->send((char*)"sendMessage pushSwitch");
cout << "Sample1:sendMessage(pushSwitch):" << s << endl;
    } else if ((mode==GET_ON_ELEVATOR)
      &&(dynamic_cast<NotHoldingJewelEvent*>(e))) {
        mode = DETERMINE_TARGET_JEWEL;
    } else if ((mode==WAIT_UNTIL_TOP)
      &&(dynamic_cast<ArrivalElevatorTopEvent*>(e))) {
        mode = GO_TO_GOAL;
    } else if ((mode==GO_TO_GOAL)
      &&(dynamic_cast<ArrivalGoalEvent*>(e))) {
        mode = BACK_TO_ELEVATOR_TOP;
    } else if ((mode==BACK_TO_ELEVATOR_TOP)
      &&(dynamic_cast<ArrivalElevatorTopEvent*>(e))) {
        mode = WAIT_UNTIL_BOTTOM;
        s = socket->send((char*)"sendMessage wait");
cout << "Sample1:sendMessage(wait):" << s << endl;
    } else if ((mode==BACK_TO_ELEVATOR_TOP)
      &&(dynamic_cast<ClearedEvent*>(e))) {
        mode = END;
    } else if ((mode==WAIT_UNTIL_BOTTOM)
      &&(dynamic_cast<ArrivalElevatorBottomEvent*>(e))) {
        mode = GO_TO_VIA_POINT2;
    } else if ((mode==GO_TO_VIA_POINT2)
      &&(dynamic_cast<ArrivalViaPoint2Event*>(e))) {
        mode = DETERMINE_TARGET_JEWEL;
    } else if ((true)&&(dynamic_cast<MessageEvent*>(e))) {
        string message = (dynamic_cast<MessageEvent*>(e))->message;
        cout << "Sample1: Message received?: " << message << endl;
    } else {
      //cout << "Unprocessed event." << endl;
    }
}

void Sample1::move() {
//cout << "Sample1:mode=" << mode << endl;
  switch(mode) {
  case DETERMINE_TARGET_JEWEL: determineTargetJewel(); break;
  case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
  case DETERMINE_WHITCH_VIA_POINT: determineWitchViaPoint(); break;
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

void Sample1::determineTargetJewel() {
    targetJewel = jewelSet.getNearest(loc);
    //if (targetJewel!=null)...
    processEvent(new DetermineTargetJewelEvent());
}

void Sample1::goToTargetJewel() {
    if (targetJewelLoc.x<10000.0)
        goToDestination(targetJewelLoc);
}

void Sample1::determineWitchViaPoint() {
    if (loc.x>0.0) {
        targetViaPoint1 = viaPointA;
        targetGoal = goal1;
        targetViaPoint2 = viaPointB;
    } else {
        targetViaPoint1 = viaPointB;
        targetGoal = goal2;
        targetViaPoint2 = viaPointA;
    }
    processEvent(new DetermineViaPointEvent());
}

void Sample1::goToViaPoint1() {
    goToDestinationWithJewel(targetViaPoint1);
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

int main() {
  Sample1 s1(10000);
  s1.start();

  return 0;
}
