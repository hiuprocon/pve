#include <iostream>
#include <math.h>
#include "Vec3d.h"
//#include <vector>
#include <string>
#include <stdlib.h>
using namespace std;

int isWhiteSpace(char c) {
  if (c==' ' || c=='\t' || c=='\n' || c=='\r')
    return 1;
  else
    return 0;
}

/*
vector<string> split(const string &str, char delim){
  vector<string> res;
  size_t current = 0, found;
  while((found = str.find_first_of(delim, current)) != string::npos){
    res.push_back(string(str, current, found - current));
    current = found + 1;
  }
  res.push_back(string(str, current, str.size() - current));
  return res;
}
*/

void split(string str,double *ret) {
  int i,c,ci;
  char xyz[3][256];

  c=0;ci=0;
  for (i=0;i<=str.size();i++) {
    xyz[c][ci] = str.at(i);
    if (isWhiteSpace(str.at(i))||str.at(i)=='\0') {
      xyz[c][ci] = '\0';
      if (c==2) { break; }
      c++;
      ci=0;
      while (isWhiteSpace(str.at(i+1))&&i<str.size()) {
        i++;
      }
    } else {
      ci++;
    }
  }

  ret[0] = atof(xyz[0]);
  ret[1] = atof(xyz[1]);
  ret[2] = atof(xyz[2]);
}

Vec3d::Vec3d() {
  x = y = z = 0.0;
}

Vec3d::Vec3d(double x,double y,double z) {
  this->x = x;
  this->y = y;
  this->z = z;
}

Vec3d::Vec3d(double xyz[]) {
  this->x = xyz[0];
  this->y = xyz[1];
  this->z = xyz[2];
}

Vec3d::Vec3d(string str) {
  double xyz[3];
  split(str,xyz);
  this->x = xyz[0];
  this->y = xyz[1];
  this->z = xyz[2];
}

Vec3d& Vec3d::operator=(const Vec3d& v) {
  this->x=v.x;
  this->y=v.y;
  this->z=v.z;
  return *this;
}

Vec3d& Vec3d::operator+=(const Vec3d& v) {
  this->x += v.x;
  this->y += v.y;
  this->z += v.z;
  return *this;
}

Vec3d& Vec3d::operator-=(const Vec3d& v) {
  this->x -= v.x;
  this->y -= v.y;
  this->z -= v.z;
  return *this;
}

Vec3d& Vec3d::operator*=(double s) {
  this->x *= s;
  this->y *= s;
  this->z *= s;
  return *this;
}

Vec3d& Vec3d::operator/=(double s) {
  this->x /= s;
  this->y /= s;
  this->z /= s;
  return *this;
}

Vec3d Vec3d::operator+() {
  return *this;
}

Vec3d Vec3d::operator-() {
  return Vec3d(-x,-y,-z);
}

double Vec3d::length() {
  return sqrt(x*x+y*y+z*z);
}

double Vec3d::lengthSquared() {
  return x*x+y*y+z*z;
}

Vec3d& Vec3d::normalize() {
  double l = length();
  this->x /= l;
  this->y /= l;
  this->z /= l;
  return *this;
}

void quatMul(double *a,double *b, double *ret) {
  ret[0]= a[3]*b[0] + a[0]*b[3] + a[1]*b[2] - a[2]*b[1];
  ret[1]= a[3]*b[1] - a[0]*b[2] + a[1]*b[3] + a[2]*b[0];
  ret[2]= a[3]*b[2] + a[0]*b[1] - a[1]*b[0] + a[2]*b[3];
  ret[3]= a[3]*b[3] - a[0]*b[0] - a[1]*b[1] - a[2]*b[2];
}

void euler2quat(double x,double y,double z,double *ret) {
  double ret1[4],ret2[4],ret3[4];
  x *= M_PI/180.0; y *= M_PI/180.0; z *= M_PI/180.0;
  ret1[0]=0.0; ret1[1]=0.0; ret1[2]=0.0; ret1[3]=1.0;
  ret2[0]=0.0; ret2[1]=sin(y/2); ret2[2]=0.0; ret2[3]=cos(y/2);
  quatMul(ret1,ret2,ret3);
  ret2[0]=sin(x/2); ret2[1]=0.0; ret2[2]=0.0; ret2[3]=cos(x/2);
  quatMul(ret3,ret2,ret1);
  ret2[0]=0.0; ret2[1]=0.0; ret2[2]=sin(z/2); ret2[3]=cos(z/2);
  quatMul(ret1,ret2,ret);
}

/*                                                                          
 * Rotate this vector around the origin              
 * by the Euler angles(z-x-y) given rot. (Unit=degrees)                     
 */
Vec3d& Vec3d::rotate(const Vec3d& rot) {
  double q[4],cq[4],v[4],ret1[4],ret2[4];
  euler2quat(rot.x,rot.y,rot.z,q);
  cq[0]=-q[0]; cq[1]=-q[1]; cq[2]=-q[2]; cq[3]=q[3];
  v[0]=x; v[1]=y; v[2]=z; v[3]=0.0;
  quatMul(q,v,ret1);
  quatMul(ret1,cq,ret2);
  x=ret2[0]; y=ret2[1]; z=ret2[2];
  return *this;
}

/*
 * Returns a rotated vector of the given vec around the Y axis
 * by the given ry. (Unit=degrees)
 */
Vec3d& Vec3d::simpleRotateY(double ry) {
  double xx,yy,zz;
  ry *= M_PI/180.0;
  xx = x*cos(ry) + z*sin(ry);
  yy = y;
  zz = -x*sin(ry) + z*cos(ry);
  x = xx; y = yy; z = zz;
  return *this;
}

bool Vec3d::epsilonEquals(const Vec3d& v,double eps) {
  double dx = x - v.x;
  double dy = y - v.y;
  double dz = z - v.z;
  double diffS = dx*dx + dy*dy + dz*dz;
  if (diffS>(eps*eps))
    return false;
  else
    return true;
}

Vec3d operator+(const Vec3d& u,const Vec3d& v) {
  Vec3d w;
  w.x = u.x + v.x;
  w.y = u.y + v.y;
  w.z = u.z + v.z;
  return w;
}

Vec3d operator-(const Vec3d& u,const Vec3d& v) {
  Vec3d w;
  w.x = u.x - v.x;
  w.y = u.y - v.y;
  w.z = u.z - v.z;
  return w;
}

double operator*(const Vec3d& u,const Vec3d& v) {
  return u.x*v.x + u.y*v.y + u.z*v.z;
}

Vec3d operator*(const Vec3d& v, double s) {
  Vec3d w;
  w.x = v.x * s;
  w.y = v.y * s;
  w.z = v.z * s;
  return w;
}

Vec3d operator*(double s,const Vec3d& v) {
  Vec3d w;
  w.x = s * v.x;
  w.y = s * v.y;
  w.z = s * v.z;
  return w;
}

Vec3d operator/(const Vec3d& v, double s) {
  Vec3d w;
  w.x = v.x / s;
  w.y = v.y / s;
  w.z = v.z / s;
  return w;
}

/* exterior product(cross)
 */
Vec3d operator/(const Vec3d& u,const Vec3d& v) {
  Vec3d w;
  w.x = u.y*v.z - u.z*v.y;
  w.y = u.z*v.x - u.x*v.z;
  w.z = u.z*v.y - u.y*v.x;
  return w;
}

/* ??? danger
 */
bool operator==(const Vec3d& u,const Vec3d& v) {
  return (u.x==v.x)&&(u.y==v.y)&&(u.z==v.z);
}

ostream& operator<<(ostream& stream, const Vec3d& v) {
  return stream <<'('<<v.x<<","<<v.y<<","<<v.z<<')';
}
