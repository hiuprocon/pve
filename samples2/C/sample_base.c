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
const vec3d elevatorBottom = {0.0,0.0,62.5};
/* Location of the elevator top */
const vec3d elevatorTop = {0.0,15.0,62.5};
/* Location of the switch1 */
const vec3d switch1 = {0.0,0.0,-11.0+62.5};
/* Location of the switch2 */
const vec3d switch2 = {0.0,0.0,11.0+62.5};
/* Location of the goal1 */
const vec3d goal1 = {-10.0,15.0,62.5};
/* Location of the goal2 */
const vec3d goal2 = {10.0,15.0,62.5};

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
/* number of burdens */
int burdens_count;
/* Burden Set */
struct burden burdens[20];
/* Location of Obstacle1 */
vec3d obstacle1;
/* Location of Obstacle2 */
vec3d obstacle2;

void init_car(int port) {
  currentTime = 0.0;
  counter = 0;
  setXYZToVec3d(0.0, 0.0, 0.0, &loc);
  setXYZToVec3d(0.0, 0.0, 0.0, &rot);
  setXYZToVec3d(0.0, 0.0, 0.0, &front);
  setXYZToVec3d(0.0, 0.0, 0.0, &left);
  setXYZToVec3d(0.0, 0.0, 0.0, &oldLoc);
  setXYZToVec3d(0.0, 0.0, 0.0, &vel);
  setXYZToVec3d(0.0, 0.0, 0.0, &obstacle1);
  setXYZToVec3d(0.0, 0.0, 0.0, &obstacle2);
  init_my_socket(port);
}

void makeBurdenSet(char *data) {
  char *numStr, *idStr, *xStr, *yStr, *zStr;
  int i, num;
  double x, y, z;
  numStr = strtok(data," ");
  num = atoi(numStr);
  burdens_count = 0;
  for (i=0;i<num;i++) {
    idStr = strtok(NULL," ");
    xStr = strtok(NULL," ");
    yStr = strtok(NULL," ");
    zStr = strtok(NULL," ");
    x = atof(xStr);
    y = atof(yStr);
    z = atof(zStr);
    strcpy(burdens[burdens_count].id,idStr);
    setXYZToVec3d(x,y,z,&(burdens[burdens_count].loc));
    burdens_count++;
  }
}

int get_burden_loc(char *id,vec3d *ret) {
  int i;
  for (i=0;i<burdens_count;i++) {
    if (strcmp(id,burdens[i].id)==0) {
      ret->x = burdens[i].loc.x;
      ret->y = burdens[i].loc.y;
      ret->z = burdens[i].loc.z;
      return NO_ERROR;
    }
  }
  return ERROR;
}

int get_nearest_burden(vec3d *pos,char *id,vec3d *ret) {
  int i, idx;
  double len, min;
  vec3d tmpV;

  if (burdens_count==0)
    return ERROR;

  idx = 0;
  min = 1000000.0;
  for (i=0;i<burdens_count;i++) {
    v3sub(pos,&(burdens[i].loc),&tmpV);
    len = v3length(&tmpV);
    if (min>len) {
      idx = i;
      min = len;
    }
  }
  ret->x = burdens[idx].loc.x;
  ret->y = burdens[idx].loc.y;
  ret->z = burdens[idx].loc.z;
  //id = burdens[idx].id;
  strcpy(id,burdens[idx].id);
  return NO_ERROR;
}

void state_check() {
  char *msg, *tmp;
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
  msg = my_send("searchBurdens");
  makeBurdenSet(msg);
  msg = my_send("searchObstacles");
  tmp = strtok(msg," ");
  tmp = strtok(NULL," ");
  tmp = strtok(NULL," ");
  obstacle1.x = atof(tmp);
  tmp = strtok(NULL," ");
  obstacle1.y = atof(tmp);
  tmp = strtok(NULL," ");
  obstacle1.z = atof(tmp);
  tmp = strtok(NULL," ");
  tmp = strtok(NULL," ");
  obstacle2.x = atof(tmp);
  tmp = strtok(NULL," ");
  obstacle2.y = atof(tmp);
  tmp = strtok(NULL," ");
  obstacle2.z = atof(tmp);
}

void make_events_basic() {
  char *msg, *tok;
  struct event e;

  msg = my_send("receiveMessages");
  if (strlen(msg)>9) {
    tok = strtok(msg+10,",");
    while (tok!=NULL) {
      e.id = MESSAGE_EVENT;
      e.message = tok;
      process_event(&e);
      tok = strtok(NULL,",");
    }
  }

  if (burdens_count==0) {
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

/* int make_events(); */
/* void process_event(struct event *e); */
/* void move(); */

int check_conflict(const vec3d *src,const vec3d *dest,const vec3d *point,double dis) {
  vec3d dir;
  double dirLength;
  vec3d pDir;
  double d;
  vec3d dir2;

  v3sub(dest,src,&dir);
  dirLength = v3length(&dir);
  if (dirLength!=0.0) v3scale(&dir,1.0/dirLength,&dir);
  v3sub(point,src,&pDir);
  d = v3dot(&pDir,&dir);
  if (d<0.0)
    return 0;
  if (d>dirLength)
    return 0;

  setVec3dToVec3d(&dir,&dir2);
  v3simpleRotateY(&dir2,90);
  d = fabs(v3dot(&pDir,&dir2));
  if (d<dis)
    return 1;
  else
    return 0;
}

int check_all_conflict(const vec3d *src,const vec3d *dest,const vec3d *targetBurdenLoc) {
  int i;
  vec3d v;
  for (i=0;i<burdens_count;i++) {
    v = burdens[i].loc;
    if (v3equals(&v,src))
      continue;
    if (v3equals(&v,dest))
      continue;
    if (targetBurdenLoc!=NULL && v3epsilonEquals(&v,targetBurdenLoc,1.0))
      continue;
    if (check_conflict(src,dest,&v,1.5)) {
      return 1;
    }
  }
  if (check_conflict(src,dest,&obstacle1,3.0))
    return 1;
  if (check_conflict(src,dest,&obstacle2,3.0))
    return 1;
  return 0;
}

void go_to_destination(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  char msg[100];
  vec3d dir;
  double dis;
  double targetVel;

  v3sub(v,&loc,&dir);
  dis = v3length(&dir);
  if (dis!=0.0) v3normalize(&dir);

  targetVel = dis > 20 ? 20 : dis;
  if (v3dot(&dir,&front)>-0.7) {
    steering = 0.3 * v3dot(&dir,&left);
    power = 300*(targetVel - v3length(&vel));
    power = power > 500 ? 500 : power;
    power = power < -500 ? -500 : power;
  } else {
    steering = 0.2;
    power = 150;
  }

  sprintf(msg,"drive %.16f %.16f",power,steering);
  my_send(msg);
}

void go_to_destination_with_burdens(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  char msg[100];
  vec3d dir;
  double dis;
  double targetVel;

  v3sub(v,&loc,&dir);
  dis = v3length(&dir);
  if (dis!=0.0) v3normalize(&dir);

  targetVel = dis > 15 ? 15 : dis;
  if (v3dot(&dir,&front)>-0.7) {
    steering = 0.3 * v3dot(&dir,&left);
    power = 300*(targetVel - v3length(&vel));
    power = power > 300 ? 300 : power;
    power = power < -300 ? -300 : power;
  } else {
    steering = 0.2;
    power = 100;
  }

  sprintf(msg,"drive %.16f %.16f",power,steering);
  my_send(msg);
}

void back_to_destination(const vec3d *v) {
  double power = 0.0;
  double steering = 0.0;
  char msg[100];
  vec3d dir;
  double dis;
  double targetVel;

  v3sub(v,&loc,&dir);
  dis = v3length(&dir);
  if (dis!=0.0) v3normalize(&dir);

  targetVel = dis > 15 ? 15 : dis;
  if (v3dot(&dir,&front)<0.0) {
    steering = 0.3 * v3dot(&dir,&left);
    power = -300*(targetVel - v3length(&vel));
    power = power > 300 ? 300 : power;
    power = power < -300 ? -300 : power;
  } else {
    steering = 1.0;
    power = 150;
  }

  sprintf(msg,"drive %.16f %.16f",power,steering);
  my_send(msg);
}

void stop_car() {
  my_send("drive 0 0");
}
