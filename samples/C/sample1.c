#include <stdio.h>
#include "sample_base.h"
#include "abstract.h"

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

int make_events(struct event *events) {
  int event_count;
  event_count = make_events_basic(events);
  return event_count;
}

void process_event(struct event *events) {
}

void move() {
}

int main(void) {
  printf("GAHA1!\n");
  init_car(10000);
  start();
}
