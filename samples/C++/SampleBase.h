/* SampleBase.h */
#ifndef SAMPLE_BASE
#define SAMPLE_BASE

#include <string>
#include <map>
#include <vector>
#include "Vec3d.h"
#include "MySocket.h"
using namespace std;

/*
 * This header file contains definitions of Events, JewelSet and SambleBase.
 */

/*
 * A base of classes used for informing various events.
 */
class Event {
public:
    virtual ~Event(){}
};

/*
 * This event is used for informing that all jewels are
 * collected successfully.
 */
class ClearedEvent : public Event {
};

/*
 * This event is used for informing that one car
 * sent a message to another car. And this event
 * contains the string of the message.
 */
class MessageEvent : public Event {
public:
    MessageEvent(string msg);
    string message;
};

/*
 * JewelSet manages ids and coodinates of jewels.
 */
class JewelSet {
public:
    JewelSet();
    ~JewelSet();
    void load(const string str);
    int size();
    Vec3d get(const string id);
    vector<string> getIDs();
    vector<Vec3d> getVectors();
    string getNearest(const Vec3d v);
private:
    map<string, Vec3d*> jewels;
};

// Simulation step time
static const double dt = 1.0/30.0;
// Location of the elevator bottom
static const Vec3d elevatorBottom(0,0,62.5);
// Location of the elevator top
static const Vec3d elevatorTop(0,15.0,62.5);
// Location of the switch1
static const Vec3d switch1(0,0.0,-11+62.5);
// Location of the switch2
static const Vec3d switch2(0,0.0,11+62.5);
// Location of the goal1
static const Vec3d goal1(-10,15.0,62.5);
// Location of the goal2
static const Vec3d goal2( 10,15.0,62.5);

/*
 *   SampleBase is a super class of Sample1 and Sample2.
 * Sample1 and Sample2 have common functions. This SampleBase class
 * implements these common functions. At the first part of
 * this source file, location data of static objects,
 * general informations of the car are defined. At the end of 
 * this source file, methods are defined which control the
 * car in various situations.
 *   The point of this sample program is that the selection of
 * a car mode is based on "Finite State Machine (so called FSM)".
 * In an infinite loop, two methods are called repeatedly.
 * The methods are stateCheck() method and move() method.
 * In stateCheck() method, some events are created according to
 * the situation of the car. Then these events are passed to
 * processEvent() method. This triggers mode changes of
 * the car. move() method controls the car in accordance with
 * the mode of the car.
 */
class SampleBase {
public:
    SampleBase(int port);
    virtual ~SampleBase();
    virtual void stateCheck();
    virtual void processEvent(Event *e) = 0;
    virtual void move() = 0;
    void start();
    bool checkConflict(const Vec3d& src,const Vec3d& dest,const Vec3d& point,double dis);
    bool checkAllConflict(const Vec3d& src,const Vec3d& dest,const Vec3d& targetJewelLoc);
    void goToDestination(const Vec3d& v);
    void goToDestinationWithJewel(const Vec3d& v);
    void backToDestination(const Vec3d& v);
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
    // Location of obstacle1
    Vec3d obstacle1;
    // Location of obstacle2
    Vec3d obstacle2;
};

#endif
