#include <stdio.h>
#include "sample_base.h"
#include "abstract.h"
#include "vec3d.h"

/* Mode CONST */
#define DETERMINE_TARGET_JEWEL 101
/* ... */

/* Event CONST */
const int DetermineTargetJewelEvent = 101;
/* ... */

/* For Convenience, this car goes by way of via points. */
const vec3d viaPointA = { 30,0,0};
const vec3d viaPointB = {-30,0,0};

/* The mode of this car. */
int mode = DETERMINE_TARGET_JEWEL;

/* The following variables are targets. */
char *targetJewel;
vec3d targetJewelLoc;
vec3d targetViaPoint1;
vec3d targetViaPoint2;
vec3d targetGoal;

void make_events() {
  make_events_basic();
  // TODO
}

void process_event(struct event *e) {
}

void move() {
  char *id;
  vec3d jLoc;
  get_nearest_jewel(&loc,id,&jLoc);
  printf("GAHA: ");
  printlnVec3d(&jLoc);
}

int main(void) {
  printf("start red car!\n");
  init_car(10000);
  start();
}
