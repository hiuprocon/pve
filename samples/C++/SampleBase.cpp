#include <iostream>
#include <iomanip>
#include <sstream>
#include "MySocket.h"
#include "SampleBase.h"
using namespace std;

MessageEvent::MessageEvent(string msg) {
    message = msg;
}

JewelSet::JewelSet() {
}

JewelSet::~JewelSet() {
}

void JewelSet::load(const char *str) {
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

int JewelSet::size() {
    return jewels.size();
}

Vec3d *JewelSet::get(const string id) {
    if (jewels.count(id)==0)
        return 0;
    else
        return jewels[id];
}

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

SampleBase::SampleBase(int port) {
    socket = new MySocket(port);
    currentTime = 0.0;
    counter = 0;
}

SampleBase::~SampleBase() {
    delete socket;
}

void SampleBase::stateCheck() {
    Vec3d vTmp;

    oldLoc = loc;
    char *ret = socket->send((char*)"getLoc");
    loc.set(ret);
    ret = socket->send((char*)"getRev");
    rot.set(ret);
    front.set(0,0,1);
    front.rotate(rot);
    //front.simpleRotateY(rot.y);
    left.set(1,0,0);
    left.rotate(rot);
    //left.simpleRotateY(rot.y);
    vel = loc - oldLoc;
    vel *= 1.0/dt;
    ret = socket->send((char*)"searchJewels");
    jewelSet.load(ret);
    if (jewelSet.size()==0)
        processEvent(new ClearedEvent());
    ret = socket->send((char*)"receiveMessages");
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

void SampleBase::start() {
    while(true) {
        stateCheck();
        move();
        socket->send((char*)"stepForward");
        currentTime += dt;
        counter++;
    }
}

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

void SampleBase::stopCar(){
    socket->send((char*)"drive 0 0");
}

