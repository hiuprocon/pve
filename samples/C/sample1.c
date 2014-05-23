#include <stdio.h>
#include "sample_base.h"
#include "abstract.h"
#include "vec3d.h"
#include "my_socket.h"

/* Mode CONST */
#define DEVELOP_STRATEGY1 101
#define GO_TO_TARGET_JEWEL 102
#define DEVELOP_STRATEGY2 103
#define GO_TO_VIA_POINT 104
#define GET_ON_ELEVATOR 105
#define WAIT_UNTIL_TOP 106
#define GO_TO_GOAL 107
#define BACK_TO_ELEVATOR_TOP 108
#define WAIT_UNTIL_BOTTOM 109
#define GO_TO_VIA_POINT2 110
#define END 111

/* Event CONST */
const int STRATEGY_DEVELOPED_EVENT = 101;
const int HOLDING_JEWEL_EVENT = 102;
const int NOT_HOLDING_JEWEL_EVENT = 103;
const int ARRIVAL_VIA_POINT1_EVENT = 104;
const int ARRIVAL_ELEVATOR_BOTTOM_EVENT = 105;
const int ARRIVAL_ELEVATOR_TOP_EVENT = 106;
const int ARRIVAL_GOAL_EVENT = 107;
const int ARRIVAL_VIA_POINT2_EVENT = 108;

/* For Convenience, this car goes by way of via points. */
const vec3d viaPointA = { 30,0,62.5};
const vec3d viaPointB = {-30,0,62.5};

/* The mode of this car. */
int mode = DEVELOP_STRATEGY1;

/* The following variables are targets. */
char targetJewel[10];
vec3d targetJewelLoc;
vec3d targetViaPoint1;
vec3d targetViaPoint2;
vec3d targetGoal;

void make_events() {
  vec3d tmpV;
  struct event e;
  int hold, isError;
  make_events_basic();

  // the car holds the jewel?
  isError = get_jewel_loc(targetJewel,&targetJewelLoc);
  hold = 1;
  if (isError==ERROR) {
    hold = 0;
  } else {
    v3sub(&targetJewelLoc,&loc,&tmpV);
    if (v3length(&tmpV)>2.0) {
      hold = 0;
    } else {
      v3normalize(&tmpV);
      if (v3dot(&tmpV,&front)<0.6)
        hold = 0;
    }
  }
  if (hold==1) {
    e.id = HOLDING_JEWEL_EVENT;
    process_event(&e);
  } else {
    e.id = NOT_HOLDING_JEWEL_EVENT;
    process_event(&e);
  }

  // car has arrived at the via point?
  v3sub(&targetViaPoint1,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_VIA_POINT1_EVENT;
    process_event(&e);
  }

  // car has arrived at the elevator bottom?
  v3sub(&elevatorBottom,&loc,&tmpV);
  if (v3length(&tmpV)<1.0) {
    e.id = ARRIVAL_ELEVATOR_BOTTOM_EVENT;
    process_event(&e);
  }

  // car has arrived at the elevator top?
  v3sub(&elevatorTop,&loc,&tmpV);
  if (v3length(&tmpV)<1.0) {
    e.id = ARRIVAL_ELEVATOR_TOP_EVENT;
    process_event(&e);
  }

  // car has arrived at the goal?
  v3sub(&targetGoal,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_GOAL_EVENT;
    process_event(&e);
  }

  // car has arrived at the via point?
  v3sub(&targetViaPoint2,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_VIA_POINT2_EVENT;
    process_event(&e);
  }
}

void process_event(struct event *e) {
  char *s;
  if ((mode==DEVELOP_STRATEGY1)
    &&(e->id==STRATEGY_DEVELOPED_EVENT)) {
    mode = GO_TO_TARGET_JEWEL;
  } else if ((mode==GO_TO_TARGET_JEWEL)
           &&(e->id == HOLDING_JEWEL_EVENT)) {
    mode = DEVELOP_STRATEGY2;
  } else if ((mode==DEVELOP_STRATEGY2)
           &&(e->id == STRATEGY_DEVELOPED_EVENT)) {
    mode = GO_TO_VIA_POINT;
  } else if ((mode==GO_TO_VIA_POINT)
           &&(e->id==ARRIVAL_VIA_POINT1_EVENT)) {
    mode = GET_ON_ELEVATOR;
  } else if ((mode==GO_TO_VIA_POINT)
           &&(e->id==NOT_HOLDING_JEWEL_EVENT)) {
    mode = DEVELOP_STRATEGY1;
  } else if ((mode==GET_ON_ELEVATOR)
           &&(e->id==ARRIVAL_ELEVATOR_BOTTOM_EVENT)) {
    mode = WAIT_UNTIL_TOP;
    s = my_send("sendMessage pushSwitch");
printf("Sample1:sendMessage(pushSwitch):%s\n",s);
  } else if ((mode==GET_ON_ELEVATOR)
           &&(e->id==NOT_HOLDING_JEWEL_EVENT)) {
    mode = DEVELOP_STRATEGY1;
  } else if ((mode==WAIT_UNTIL_TOP)
           &&(e->id==ARRIVAL_ELEVATOR_TOP_EVENT)) {
    mode = GO_TO_GOAL;
  } else if ((mode==GO_TO_GOAL)
           &&(e->id==ARRIVAL_GOAL_EVENT)) {
    mode = BACK_TO_ELEVATOR_TOP;
  } else if ((mode==BACK_TO_ELEVATOR_TOP)
           &&(e->id==ARRIVAL_ELEVATOR_TOP_EVENT)) {
    mode = WAIT_UNTIL_BOTTOM;
    s = my_send("sendMessage wait");
printf("Sample1:sendMessage(wait):%s\n",s);
  } else if ((mode==BACK_TO_ELEVATOR_TOP)
           &&(e->id==CLEAR_EVENT)) {
    mode = END;
  } else if ((mode==WAIT_UNTIL_BOTTOM)
           &&(e->id==ARRIVAL_ELEVATOR_BOTTOM_EVENT)) {
    mode = GO_TO_VIA_POINT2;
  } else if ((mode==GO_TO_VIA_POINT2)
           &&(e->id==ARRIVAL_VIA_POINT2_EVENT)) {
    mode = DEVELOP_STRATEGY1;
  } else if (e->id==MESSAGE_EVENT) {
printf("Sample1: Message received?: %s\n",e->message);
  } else {
    //printf("Unprocessed event: "+e->id);
  }
}

void developStrategy1() {
  struct event e;
  get_nearest_jewel(&loc,targetJewel,&targetJewelLoc);
  e.id=STRATEGY_DEVELOPED_EVENT;
  process_event(&e);
}

void goToTargetJewel() {
  vec3d v;
  if (check_all_conflict(&loc,&targetJewelLoc,NULL)) {
printf("GAHA:CONFLICT1\n");
    v3sub(&targetJewelLoc,&loc,&v);
    v3simpleRotateY(&v,45);
    v3add(&v,&loc,&v);
    go_to_destination(&v);
  } else {
    go_to_destination(&targetJewelLoc);
  }
}

void developStrategy2() {
  struct event e;
  if (loc.x>0.0) {
    setVec3dToVec3d(&viaPointA,&targetViaPoint1);
    setVec3dToVec3d(&goal1,&targetGoal);
    setVec3dToVec3d(&viaPointB,&targetViaPoint2);
  } else {
    setVec3dToVec3d(&viaPointB,&targetViaPoint1);
    setVec3dToVec3d(&goal2,&targetGoal);
    setVec3dToVec3d(&viaPointA,&targetViaPoint2);
  }
  e.id = STRATEGY_DEVELOPED_EVENT;
  process_event(&e);
}

void goToViaPoint1() {
  vec3d v;
  if (check_all_conflict(&loc,&targetViaPoint1,&targetJewelLoc)) {
printf("GAHA:CONFLICT2\n");
    v3sub(&targetJewelLoc,&loc,&v);
    v3simpleRotateY(&v,45);
    v3add(&v,&loc,&v);
    go_to_destination_with_jewels(&v);
  } else {
    go_to_destination_with_jewels(&targetViaPoint1);
  }
}

void getOnElevator() {
  go_to_destination_with_jewels(&elevatorBottom);
}

void waitUntilTop() {
  stop_car();
}

void goToGoal() {
  go_to_destination_with_jewels(&targetGoal);
}

void backToElevatorTop() {
  back_to_destination(&elevatorTop);
}

void waitUntilBottom() {
  stop_car();
}

void goToViaPoint2() {
  go_to_destination(&targetViaPoint2);
}

void end() {
  stop_car();
}

void move() {
//printf("GAHA: mode=%d\n",mode);
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
  default: printf("Sample1: Unknown mode? %d\n",mode);
  }
}

int main(void) {
  printf("start red car!\n");
  init_car(10000);
  start();
}
