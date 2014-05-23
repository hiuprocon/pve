#include <iostream>
#include <iomanip>
#include <sstream>
#include <math.h>
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
 * Returns the all id of jewels.
 */
vector<string> JewelSet::getIDs() {
    vector<string> array;

    for (map<string, Vec3d*>::iterator itr = jewels.begin(); itr != jewels.end(); itr++) {
        array.push_back(itr->first) ;
    }
    return array;
}

/*
 * Returns the all vectors of jewels.
 */
vector<Vec3d> JewelSet::getVectors() {
    vector<Vec3d> array;

    for (map<string, Vec3d*>::iterator itr = jewels.begin(); itr != jewels.end(); itr++) {
        Vec3d v = Vec3d(*(itr->second));
        array.push_back(v) ;
    }
    return array;
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
    ret = socket->send("searchObstacles");
    istringstream is(ret);
    char buffer[16];
    is.read(buffer,5);
    is >> obstacle1.x >> obstacle1.y >> obstacle1.z;
    is.read(buffer,3);
    is >> obstacle2.x >> obstacle2.y >> obstacle2.z;
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
 * Check if the point could be an obstacle to a forward movement.
 * The route is given as a pair of source and destination.
 * The argument `dis` is used as the threashold of distance
 * between the route and the point.
 */
bool SampleBase::checkConflict(const Vec3d& src,const Vec3d& dest,const Vec3d& point,double dis) {
    Vec3d dir;
    dir = dest - src;
    double dirLength = dir.length();
    if (dirLength!=0.0) dir = (1.0/dirLength) * dir;
    Vec3d pDir;
    pDir = point - src;
    if ((pDir * dir)<0.0)
        return false;
    if ((pDir * dir)>dirLength)
        return false;

    Vec3d dir2 = Vec3d(dir);
    dir2 = dir2.simpleRotateY(90);
    if (fabs(pDir * dir2) < dis)
        return true;
    else
        return false;
}

/*
 * Check existance of obstacles. The route is specified by 
 * source and destination. If targetJewelLoc is given, it is
 * excluded from obstacles. If targetJewelLoc==null, ignored.
 */
bool SampleBase::checkAllConflict(const Vec3d& src,const Vec3d& dest,const Vec3d& targetJewelLoc) {
    vector<Vec3d> array = jewelSet.getVectors();
    for (vector<Vec3d>::iterator itr = array.begin(); itr != array.end(); itr++) {
        Vec3d v = (*itr);
        if (v == src)
            continue;
        if (v == dest)
            continue;
        if (targetJewelLoc.x<1000 && v.epsilonEquals(targetJewelLoc,1.0))
            continue;
        if (checkConflict(src,dest,v,1.5)) {
            return true;
        }
    }
    if (checkConflict(src,dest,obstacle1,3.0))
        return true;
    if (checkConflict(src,dest,obstacle2,3.0))
        return true;
    return false;
}


/*
 * Drive this car to the given location (v).
 */
void SampleBase::goToDestination(const Vec3d& v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d dir;
    dir = v - loc;
    double dis = dir.length();
    if (dis!=0.0) dir = (1.0/dis) * dir;

    double targetVel = dis > 20 ? 20 : dis;
    if ((dir * front) > -0.7) {
        steering = 0.3 * (dir * left);
        power = 300*(targetVel - vel.length());
        power = power > 500 ? 500 : power;
        power = power < -500 ? -500 : power;
    } else {
        steering = 0.2;
        power = 150;
    }

    ostringstream oss;
    oss << "drive " << setprecision(16) << power << " " << steering;
    socket->send(oss.str().c_str());
}

/*
 * Drive this car to the given location (v) with jewels.
 */
void SampleBase::goToDestinationWithJewel(const Vec3d& v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d dir;
    dir = v - loc;
    double dis = dir.length();
    if (dis!=0.0) dir = (1.0/dis) * dir;

    double targetVel = dis > 15 ? 15 : dis;
    if ((dir * front) > -0.7) {
        steering = 0.3 * (dir * left);
        power = 300*(targetVel - vel.length());
        power = power > 300 ? 300 : power;
        power = power < -300 ? -300 : power;
    } else {
        steering = 0.2;
        power = 100;
    }

    ostringstream oss;
    oss << "drive " << setprecision(16) << power << " " << steering;
    socket->send(oss.str().c_str());
}

/*
 * Back this car to the given location (v).
 */
void SampleBase::backToDestination(const Vec3d& v) {
    double power = 0.0;
    double steering = 0.0;

    Vec3d dir;
    dir = v - loc;
    double dis = dir.length();
    if (dis!=0.0) dir = (1.0/dis) * dir;

    double targetVel = dis > 15 ? 15 : dis;
    if ((dir * front) < -0.7) {
        steering = 0.3 * (dir * left);
        power = -300*(targetVel - vel.length());
        power = power > 300 ? 300 : power;
        power = power < -300 ? -300 : power;
    } else {
        steering = 1.0;
        power = 150;
    }

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

