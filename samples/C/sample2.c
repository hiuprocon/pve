#include <stdio.h>
#include "sample_base.h"
#include "abstract.h"

/* Mode CONST */
#define GO_TO_WAITING_POINT 201
#define WAIT_UNTIL_MESSAGE  202
#define GO_TO_SWITCH        203

/* Event CONST */
#define ArrivalWaitingPointEvent 201
#define ArrivalSwitchEvent       202

/* The mode of this car. */
int mode = GO_TO_WAITING_POINT;

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
  printf("GAHA2!\n");
  init_car(20000);
  start();
}
