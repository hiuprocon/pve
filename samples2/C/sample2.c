#include <stdio.h>
#include "sample_base.h"
#include "abstract.h"
#include "vec3d.h"
#include "my_socket.h"

/* Mode CONST */
#define DEVELOP_STRATEGY1 201
#define GO_TO_TARGET_JEWEL 202
#define DEVELOP_STRATEGY2 203
#define GO_TO_VIA_POINT 204
#define GET_ON_ELEVATOR 205
#define BACK_TO_VIA_POINT2 206
#define GO_TO_SWITCH 207
#define GO_TO_VIA_POINT3 208
#define END 209

/* Event CONST */
const int STRATEGY_DEVELOPED_EVENT = 201;
const int HOLDING_JEWEL_EVENT = 202;
const int NOT_HOLDING_JEWEL_EVENT = 203;
const int ARRIVAL_VIA_POINT1_EVENT = 204;
const int ARRIVAL_ELEVATOR_BOTTOM_EVENT = 205;
const int ARRIVAL_VIA_POINT2_EVENT = 206;
const int ARRIVAL_VIA_POINT3_EVENT = 207;
const int ARRIVAL_SWITCH_EVENT = 208;

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
vec3d targetViaPoint3;
vec3d targetGoal;

char* lastMessage = "";

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

  // car has arrived at the via point2?
  v3sub(&targetViaPoint2,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_VIA_POINT2_EVENT;
    process_event(&e);
  }

  // car has arrived at the via point3?
  v3sub(&targetViaPoint3,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_VIA_POINT3_EVENT;
    process_event(&e);
  }

  // car has arrived at the switch?
  v3sub(&switch1,&loc,&tmpV);
  if (v3length(&tmpV)<2.0) {
    e.id = ARRIVAL_SWITCH_EVENT;
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
    mode = BACK_TO_VIA_POINT2;
    s = my_send("sendMessage READY");
printf("Sample1:sendMessage(READY):%s\n",s);
  } else if ((mode==GET_ON_ELEVATOR)
           &&(e->id==NOT_HOLDING_JEWEL_EVENT)) {
    mode = DEVELOP_STRATEGY1;
  } else if ((mode==BACK_TO_VIA_POINT2)
           &&(e->id==ARRIVAL_VIA_POINT2_EVENT)) {
    mode = GO_TO_SWITCH;
  } else if ((mode==GO_TO_SWITCH)
           &&(e->id==ARRIVAL_SWITCH_EVENT)) {
    mode = GO_TO_VIA_POINT3;
  } else if ((mode==GO_TO_VIA_POINT3)
           &&(e->id==ARRIVAL_VIA_POINT3_EVENT)) {
    mode = DEVELOP_STRATEGY1;
  } else if (e->id==MESSAGE_EVENT) {
    lastMessage = e->message;
printf("Sample1: Message received: %s\n",e->message);
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
    setVec3dToVec3d(&viaPointA,&targetViaPoint2);
    setVec3dToVec3d(&viaPointB,&targetViaPoint3);
  } else {
    setVec3dToVec3d(&viaPointB,&targetViaPoint1);
    setVec3dToVec3d(&viaPointB,&targetViaPoint2);
    setVec3dToVec3d(&viaPointA,&targetViaPoint3);
  }
  e.id = STRATEGY_DEVELOPED_EVENT;
  process_event(&e);
}

void goToViaPoint1() {
  vec3d v;
  if (check_all_conflict(&loc,&targetViaPoint1,&targetJewelLoc)) {
printf("GAHA:CONFLICT2\n");
    v3sub(&targetViaPoint1,&loc,&v);
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

void backToViaPoint2() {
  back_to_destination(&targetViaPoint2);
}

void goToSwitch() {
  if (strcmp(lastMessage,"READY")==0)
    go_to_destination(&targetViaPoint2);
  else
    stop_car();
}

void goToViaPoint3() {
  if (strcmp(lastMessage,"READY")==0)
    go_to_destination(&targetViaPoint3);
  else
    stop_car();
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
  case BACK_TO_VIA_POINT2: backToViaPoint2(); break;
  case GO_TO_SWITCH: goToSwitch(); break;
  case GO_TO_VIA_POINT3: goToViaPoint3(); break;
  case END: end(); break;
  default: printf("Sample1: Unknown mode? %d\n",mode);
  }
}

int main(void) {
  printf("start red car!\n");
  init_car(10000);
  start();
}
