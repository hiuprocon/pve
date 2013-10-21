#include <iostream>
#include <iomanip>
#include <sstream>
#include "MySocket.h"
#include "SampleBase.h"
using namespace std;

/*
 * This event is used for informing that one car
 * sent a message to another car. And this event
 * contains the string of the message.
 */
MessageEvent::MessageEvent(string msg) {
    message = msg;
}

/*
 * Constructs empty JewelSet.
 */
JewelSet::JewelSet() {
}

/*
 * The destructor of JewelSet.
 */
JewelSet::~JewelSet() {
    for (map<string, Vec3d*>::iterator itr = jewels.begin(); itr != jewels.end(); itr++) {
        delete itr->second;
    }
}

/*
 * Loads coodinates of jewels from the server response.
 */
void JewelSet::load(const string str) {
    for (map<string, Vec3d*>::iterator itr = jewels.begin(); itr != jewels.end(); itr++) {
        delete itr->second;
    }
    jewels.clear();
    istringstream is(str);
    int n;
    is >> n;
    char id[16];
    double x,y,z;
    for (int i=0;i<n;i++) {
        is >> id >> x >> y >> z;
        jewels[id] = new Vec3d(x,y,z);
    }
}

/*
 * Returns the number of jewels.
 */
int JewelSet::size() {
    return jewels.size();
}

/*
 * Returns the coodinate of the jewel indicated by given id.
 */
Vec3d JewelSet::get(const string id) {
    if (jewels.count(id)==0) {
        Vec3d dummy;
        return dummy; // This must not be execute.
    } else
        return *(jewels[id]);
}

/*
 * Returns the id of the jewel which is nearest to given coodinate.
 */
string JewelSet::getNearest(const Vec3d v) {
    string retId;
    double min = 1.0e+100;

    for (map<string, Vec3d*>::iterator itr = jewels.begin(); itr != jewels.end(); itr++) {
        string id = itr->first ;
        Vec3d loc = *(itr->second);
        double d = (v-loc).length();
        if (d<min) {
            min = d;
            retId = id;
        }
    }
    return retId;
}

/*
 * The constructor of SampleBase.
 */
SampleBase::SampleBase(int port) {
    socket = new MySocket(port);
    currentTime = 0.0;
    counter = 0;
}

/*
 * THe destructor of SampleBase.
 */
SampleBase::~SampleBase() {
    delete socket;
}

/*
 * Check current situations. In subclasses of SampleBase,
 * this method could be overrided to check another situations
 * and/or create some events then call processEvent() method.
 */
void SampleBase::stateCheck() {
    Vec3d vTmp;

    oldLoc = loc;
    string ret = socket->send("getLoc");
    loc.set(ret);
    ret = socket->send("getRev");
    rot.set(ret);
    front.set(0,0,1);
    front.rotate(rot);
    //front.simpleRotateY(rot.y);
    left.set(1,0,0);
    left.rotate(rot);
    //left.simpleRotateY(rot.y);
    vel = loc - oldLoc;
    vel *= 1.0/dt;
    ret = socket->send("searchJewels");
    jewelSet.load(ret);
    if (jewelSet.size()==0)
        processEvent(new ClearedEvent());
    ret = socket->send("receiveMessages");
    string rets(ret);
    if (rets.size()>9) {
        rets = rets.substr(10);
        int idx = rets.find(",");
        while(idx!=-1) {
            processEvent(new MessageEvent(rets.substr(0,idx)));
            rets = rets.substr(idx);
        }
        processEvent(new MessageEvent(rets));
    }
}

/*
 * Main loop of this program.
 */
void SampleBase::start() {
    while(true) {
        stateCheck();
        move();
        socket->send("stepForward");
        currentTime += dt;
        counter++;
    }
}

/*
 * Drive this car to the given location (v).
 */
void SampleBase::goToDestination(Vec3d v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d tmpV;
    tmpV = v - loc;
    tmpV.normalize();

    if (tmpV * front < 0.0)
        steering = 3.0;
    else
        steering = -3.0 * (tmpV * left);
    if (steering<0.1 && steering > -0.1)
        power = 1.0 * (tmpV * front);

    ostringstream oss;
    oss << "drive " << setprecision(16) << power << " " << steering;
    socket->send(oss.str().c_str());
}

/*
 * Drive this car to the given location (v) with jewels.
 */
void SampleBase::goToDestinationWithJewel(Vec3d v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d tmpV;
    tmpV = v - loc;
    tmpV.normalize();

    if (tmpV * front < 0.0)
        steering = 3.0;
    else
        steering = -3.0 * (tmpV * left);
    power = 0.5;//1.0 * (tmpV * front);

    ostringstream oss;
    oss << "drive " << setprecision(16) << power << " " << steering;
    socket->send(oss.str().c_str());
}

/*
 * Back this car to the given location (v).
 */
void SampleBase::backToDestination(Vec3d v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d tmpV;
    tmpV = v - loc;
    tmpV.normalize();

    if (tmpV * front > 0.0)
        steering = 3.0;
    else
        steering = 3.0 * (tmpV * left);
    if (steering<0.1 && steering > -0.1)
        power = 0.3 * (tmpV * front);

    ostringstream oss;
    oss << "drive " << setprecision(16) << power << " " << steering;
    socket->send(oss.str().c_str());
}

/*
 * Stop this car!
 */
void SampleBase::stopCar(){
    socket->send("drive 0 0");
}

