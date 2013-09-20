#include "my_socket.h"
#include "sample_base.h"
#include "abstract.h"

/* Simulation step time */
const double dt = 1.0/30.0;
/* Location of the elevator bottom */
const vec3d elevatorBottom = {0.0,1.0,0.0};
/* Location of the elevator top */
const vec3d elevatorTop = {0.0,15.0,0.0};
/* Location of the switch1 */
const vec3d switch1 = {0.0,0.5,-11.0};
/* Location of the switch2 */
const vec3d switch2 = {0.0,0.5,11.0};
/* Location of the goal1 */
const vec3d goal1 = {-10.0,15.5,0.0};
/* Location of the goal2 */
const vec3d goal2 = {10.0,15.5,0.0};

/* current simulation time */
double currentTime;
/* counter of simulation step */
int counter;
/* Location of this car */
vec3d loc;
/* Rotation of this car */
vec3d rot;
/* Front unit vector of this car */
vec3d front;
/* Left unit vector of this car */
vec3d left;
/* Old location of this car */
vec3d oldLoc;
/* Velocity of this car */
vec3d vel;
/* number of jewels */
int jewels_count;
/* Jewel Set */
struct jewel jewels[20];

void init_car(int port) {
  currentTime = 0.0;
  counter = 0;
  setXYZToVec3d(0.0, 0.0, 0.0, &loc);
  setXYZToVec3d(0.0, 0.0, 0.0, &rot);
  setXYZToVec3d(0.0, 0.0, 0.0, &front);
  setXYZToVec3d(0.0, 0.0, 0.0, &left);
  setXYZToVec3d(0.0, 0.0, 0.0, &oldLoc);
  setXYZToVec3d(0.0, 0.0, 0.0, &vel);
  init_my_socket(port);
}

void makeJewelSet(char *data) {
  //TODO
  jewels[0].id = "jA1.0";
  setXYZToVec3d(1,2,3,&(jewels[0].loc));
  jewels_count=1;
}

void state_check() {
  char *msg;
  setVec3dToVec3d(&loc, &oldLoc);
  msg = my_send("getLoc");
  setStrToVec3d(msg,&loc);
  msg = my_send("getRev");
  setStrToVec3d(msg,&rot);
  setXYZToVec3d(0,0,1,&front);
  rotate(&front,&rot);
  //simpleRotateY(&front,rot.y);
  setXYZToVec3d(1,0,0,&left);
  rotate(&left,&rot);
  //simpleRotateY(&left,rot.y);
  sub(&loc,&oldLoc,&vel);
  scale(&vel,1.0/dt,&vel);
  msg = my_send("searchJewels");
  makeJewelSet(msg);
}

int make_events_basic(struct event *events) {
  char *msg;
  int events_count = 0;
  msg = my_send("receiveMessages");
  // TODO

  if (jewels_count==0) {
    events[events_count].id = CLEAR_EVENT;
    events_count++;
  }
  events[events_count].id = MESSAGE_EVENT;
  events[events_count].message = "test";
  events_count++;
  return events_count;
}

void start() {
  int i, event_count;
  struct event events[100];
  while (1) {
    state_check();
    event_count = make_events(events);
    for (i=0;i<event_count;i++) {
      process_event(&events[i]);
    }
    move();
    my_send("stepForward");
    currentTime += dt;
    counter++;
  }
}

/* int make_events(struct event *events); */
/* void process_event(struct event *events); */
/* void move(); */

void go_to_destination(vec3d *v) {
}

void go_to_destination_with_jewels(vec3d *v) {
}

void back_to_destination(vec3d *v) {
}

void stop_car() {
}
