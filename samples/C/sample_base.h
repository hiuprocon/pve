/* sample_base.h */
#ifndef SAMPLE_BASE
#define SAMPLE_BASE

#include "vec3d.h"

/* Event CONST */
#define CLEAR_EVENT 1
#define MESSAGE_EVENT 2

struct jewel {
  char *id;
  vec3d loc;
};

struct event {
  int id;
  char *message;
};

/* Simulation step time */
extern const double dt;
/* Location of the elevator bottom */
extern const vec3d elevatorBottom;
/* Location of the elevator top */
extern const vec3d elevatorTop;
/* Location of the switch1 */
extern const vec3d switch1;
/* Location of the switch2 */
extern const vec3d switch2;
/* Location of the goal1 */
extern const vec3d goal1;
/* Location of the goal2 */
extern const vec3d goal2;

/* current simulation time */
extern double currentTime;
/* counter of simulation step */
extern int counter;
/* Location of this car */
extern vec3d loc;
/* Rotation of this car */
extern vec3d rot;
/* Front unit vector of this car */
extern vec3d front;
/* Left unit vector of this car */
extern vec3d left;
/* Old location of this car */
extern vec3d oldLoc;
/* Velocity of this car */
extern vec3d vel;
/* number of jewels */
extern int jewels_count;
/* Jewel Set */
extern struct jewel jewels[20];

void init_car(int port);
int make_events_basic(struct event *events);

void go_to_destination(vec3d *v);
void go_to_destination_with_jewels(vec3d *v);
void back_to_destination(vec3d *v);
void stop_car();

#endif
