#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
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
  char *numStr, *idStr, *xStr, *yStr, *zStr;
  int i, num;
  double x, y, z;
  numStr = strtok(data," ");
  num = atoi(numStr);
  jewels_count = 0;
  for (i=0;i<num;i++) {
    idStr = strtok(NULL," ");
    xStr = strtok(NULL," ");
    yStr = strtok(NULL," ");
    zStr = strtok(NULL," ");
    x = atof(xStr);
    y = atof(yStr);
    z = atof(zStr);
    strcpy(jewels[jewels_count].id,idStr);
    setXYZToVec3d(x,y,z,&(jewels[jewels_count].loc));
    jewels_count++;
  }
}

int get_jewel_loc(char *id,vec3d *ret) {
  int i;
  for (i=0;i<jewels_count;i++) {
    if (strcmp(id,jewels[i].id)) {
      ret->x = jewels[i].loc.x;
      ret->y = jewels[i].loc.y;
      ret->z = jewels[i].loc.z;
      return NO_ERROR;
    }
  }
  return ERROR;
}

int get_nearest_jewel(vec3d *loc,char *id,vec3d *ret) {
  int i, idx;
  double len, min;
  vec3d vTmp;

  if (jewels_count==0)
    return ERROR;

  idx = 0;
  min = 1000000.0;
  for (i=0;i<jewels_count;i++) {
    v3sub(loc,&(jewels[i].loc),&vTmp);
    len = v3length(&vTmp);
    if (min>len) {
      idx = i;
      min = len;
    }
  }
  ret->x = jewels[idx].loc.x;
  ret->y = jewels[idx].loc.y;
  ret->z = jewels[idx].loc.z;
  id = jewels[idx].id;
  return NO_ERROR;
}

void state_check() {
  char *msg;
  setVec3dToVec3d(&loc, &oldLoc);
  msg = my_send("getLoc");
  setStrToVec3d(msg,&loc);
  msg = my_send("getRev");
  setStrToVec3d(msg,&rot);
  setXYZToVec3d(0,0,1,&front);
  v3rotate(&front,&rot);
  //v3simpleRotateY(&front,rot.y);
  setXYZToVec3d(1,0,0,&left);
  v3rotate(&left,&rot);
  //v3simpleRotateY(&left,rot.y);
  v3sub(&loc,&oldLoc,&vel);
  v3scale(&vel,1.0/dt,&vel);
  msg = my_send("searchJewels");
  makeJewelSet(msg);
}

void make_events_basic() {
  char *msg, *tok;
  struct event e;

  msg = my_send("receiveMessages");
  tok = strtok(msg,",");
  tok = strtok(NULL,",");
  while (tok!=NULL) {
    e.id = MESSAGE_EVENT;
    e.message = tok;
    process_event(&e);
    tok = strtok(NULL,",");
  }

  if (jewels_count==0) {
    e.id = CLEAR_EVENT;
    process_event(&e);
  }
}

void start() {
  while (1) {
    state_check();
    make_events();
    move();
    my_send("stepForward");
    currentTime += dt;
    counter++;
  }
}

/* int make_events(struct event *events); */
/* void process_event(struct event *events); */
/* void move(); */

void go_to_destination(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  vec3d tmpV;
  char msg[100];

  v3sub(v,&loc,&tmpV);
  v3normalize(&tmpV);
  if (v3dot(&tmpV,&front)<0.0)
    steering = 3.0;
  else
    steering = -3.0 * v3dot(&tmpV,&left);
  if (fabs(steering)<0.1)
    power = 1.0 * v3dot(&tmpV,&front);

  sprintf(msg,"drive %f %f",power,steering);
  my_send(msg);
}

void go_to_destination_with_jewels(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  vec3d tmpV;
  char msg[100];

  v3sub(v,&loc,&tmpV);
  v3normalize(&tmpV);
  if (v3dot(&tmpV,&front)<0.0)
    steering = 3.0;
  else
    steering = -3.0 * v3dot(&tmpV,&left);
  power = 0.5;//1.0 * v3dot(&tmpV,&front);

  sprintf(msg,"drive %f %f",power,steering);
  my_send(msg);
}

void back_to_destination(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  vec3d tmpV;
  char msg[100];

  v3sub(v,&loc,&tmpV);
  v3normalize(&tmpV);
  if (v3dot(&tmpV,&front)>0.0)
    steering = 3.0;
  else
    steering = 3.0 * v3dot(&tmpV,&left);
  if (fabs(steering)<0.1)
    power = 0.3 * v3dot(&tmpV,&front);

  sprintf(msg,"drive %f %f",power,steering);
  my_send(msg);
}

void stop_car() {
  my_send("drive 0 0");
}
