/* SampleBase.h */
#ifndef SAMPLE_BASE
#define SAMPLE_BASE

#include <string>
#include <map>
#include "Vec3d.h"
#include "MySocket.h"
using namespace std;

class Event {
public:
    virtual ~Event(){}
};

class ClearedEvent : public Event {
};

class MessageEvent : public Event {
public:
    MessageEvent(string msg);
    string message;
};

class JewelSet {
public:
    JewelSet();
    ~JewelSet();
    void load(const char *str);
    int size();
    Vec3d *get(const string id);
    string getNearest(const Vec3d v);
private:
    map<string, Vec3d*> jewels;
};

// Simulation step time
static const double dt = 1.0/30.0;
// Location of the elevator bottom
static const Vec3d elevatorBottom(0,1.0,0);
// Location of the elevator top
static const Vec3d elevatorTop(0,15.0,0);
// Location of the switch1
static const Vec3d switch1(0,0.5,-11);
// Location of the switch2
static const Vec3d switch2(0,0.5,11);
// Location of the goal1
static const Vec3d goal1(-10,15.5,0);
// Location of the goal2
static const Vec3d goal2( 10,15.5,0);

class SampleBase {
public:
    SampleBase(int port);
    virtual ~SampleBase();
    virtual void stateCheck();
    virtual void processEvent(Event *e) = 0;
    virtual void move() = 0;
    void start();
    void goToDestination(Vec3d v);
    void goToDestinationWithJewel(Vec3d v);
    void backToDestination(Vec3d v);
    void stopCar();
protected:
    // socket
    MySocket *socket;
    // current simulation time
    double currentTime;
    // counter of simulation step
    int counter;
    // Location of this car
    Vec3d loc;
    // Rotation of this car
    Vec3d rot;
    // Front unit vector of this car
    Vec3d front;
    // Left unit vector of this car
    Vec3d left;
    // Old location of this car
    Vec3d oldLoc;
    // Velocity of this car
    Vec3d vel;
    // Manager of jewels
    JewelSet jewelSet;
};

#endif
