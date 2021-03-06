#include <stdio.h>
#include <string.h>
#include "vec3d.h"
#include "sample_base.h"
#include "abstract.h"

/* Mode CONST */
#define GO_TO_WAITING_POINT   201
#define WAIT_UNTIL_MESSAGE    202
#define GO_TO_SWITCH          203
#define BACK_TO_WAITING_POINT 204

/* Event CONST */
#define ARRIVAL_WAITING_POINT_EVENT 201
#define ARRIVAL_SWITCH_EVENT        202

/* Location of waiting point. */
const vec3d waitingPoint = {0,0,30};

/* The mode of this car. */
int mode = GO_TO_WAITING_POINT;

void make_events() {
  vec3d tmpV;
  struct event e;
  make_events_basic();

  // car has arrived at the waiting point?
  v3sub(&waitingPoint,&loc,&tmpV);
  if (v3length(&tmpV)<1.0) {
    e.id = ARRIVAL_WAITING_POINT_EVENT;
    process_event(&e);
  }

  // car has arrived at the switch?
  v3sub(&switch1,&loc,&tmpV);
  if (v3length(&tmpV)<1.0) {
    e.id = ARRIVAL_SWITCH_EVENT;
    process_event(&e);
  }
}

void process_event(struct event *e) {
  if (mode==GO_TO_WAITING_POINT && e->id==ARRIVAL_WAITING_POINT_EVENT) {
    mode = WAIT_UNTIL_MESSAGE;
  } else if (mode==GO_TO_SWITCH && e->id==ARRIVAL_SWITCH_EVENT) {
    mode = WAIT_UNTIL_MESSAGE;
  } else if (mode==WAIT_UNTIL_MESSAGE && e->id==MESSAGE_EVENT) {
printf("sample2:receiveMessage: %s\n",e->message);
    if (strcmp(e->message,"wait")==0) {
      mode = BACK_TO_WAITING_POINT;
    } else if (strcmp(e->message,"pushSwitch")==0) {
      mode = GO_TO_SWITCH;
    }
  } else if (mode==BACK_TO_WAITING_POINT && e->id==ARRIVAL_WAITING_POINT_EVENT) {
    mode = WAIT_UNTIL_MESSAGE;
  } else {
    //printf("Unprocessed event: id=%d\n",e->id);
  }
}

void go_to_waiting_point() {
  vec3d v;
  if (check_all_conflict(&loc,&waitingPoint,NULL)) {
printf("GAHA:CONFLICT\n");
    v3sub(&waitingPoint,&loc,&v);
    v3simpleRotateY(&v,45);
    v3add(&v,&loc,&v);
    go_to_destination(&v);
  } else {
    go_to_destination(&waitingPoint);
  }
}

void wait_until_message() {
  stop_car();
}

void go_to_switch() {
  go_to_destination(&switch1);
}

void back_to_waiting_point() {
  back_to_destination(&waitingPoint);
}

void move() {
//printf("mode=%d\n",mode);
  switch(mode) {
  case GO_TO_WAITING_POINT:   go_to_waiting_point();   break;
  case WAIT_UNTIL_MESSAGE:    wait_until_message();    break;
  case GO_TO_SWITCH:          go_to_switch();          break;
  case BACK_TO_WAITING_POINT: back_to_waiting_point(); break;
  default: printf("sample2: Unknown mode?%d\n",mode);
  }
}

int main(void) {
  printf("start blue car!\n");
  init_car(20000);
  start();
}
