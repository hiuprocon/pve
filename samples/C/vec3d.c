#include <stdio.h>
#define _USE_MATH_DEFINES
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include "vec3d.h"

int isWhiteSpace(char c) {
  if (c==' ' || c=='\t' || c=='\n' || c=='\r')
    return 1;
  else
    return 0;
}

void split(const char *str,double *ret) {
  int i,c,ci;
  char xyz[3][256];

  c=0;ci=0;
  for (i=0;i<strlen(str);i++) {
    xyz[c][ci] = str[i];
    if (isWhiteSpace(str[i])||str[i]=='\0') {
      xyz[c][ci] = '\0';
      if (c==2) { break; }
      c++;
      ci=0;
      while (isWhiteSpace(str[i+1])&&(i<strlen(str))) {
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

void setXYZToVec3d(double x,double y,double z,vec3d *ret) {
  ret->x = x;
  ret->y = y;
  ret->z = z;
}

void setArrayToVec3d(const double xyz[],vec3d *ret) {
  ret->x = xyz[0];
  ret->y = xyz[1];
  ret->z = xyz[2];
}

void setStrToVec3d(const char *str,vec3d *ret) {
  double xyz[3];
  split(str,xyz);
  ret->x = xyz[0];
  ret->y = xyz[1];
  ret->z = xyz[2];
}

void setVec3dToVec3d(const vec3d *v,vec3d *ret) {
  ret->x = v->x;
  ret->y = v->y;
  ret->z = v->z;
}

void v3add(const vec3d *u,const vec3d *v,vec3d *ret) {
  ret->x = u->x + v->x;
  ret->y = u->y + v->y;
  ret->z = u->z + v->z;
}

void v3sub(const vec3d *u,const vec3d *v,vec3d *ret) {
  ret->x = u->x - v->x;
  ret->y = u->y - v->y;
  ret->z = u->z - v->z;
}

void v3scale(const vec3d *v,double s,vec3d *ret) {
  ret->x = s*v->x;
  ret->y = s*v->y;
  ret->z = s*v->z;
}

double v3dot(const vec3d *u,const vec3d *v) {
  return u->x*v->x + u->y*v->y + u->z*v->z;
}

void v3cross(const vec3d *u,const vec3d *v,vec3d *ret) {
  ret->x = u->y*v->z - u->z*v->y;
  ret->y = u->z*v->x - u->x*v->z;
  ret->z = u->z*v->y - u->y*v->x;
}

double v3length(const vec3d *v) {
  return sqrt(v->x*v->x + v->y*v->y + v->z*v->z);
}

double v3lengthSquared(const vec3d *v) {
  return v->x*v->x + v->y*v->y + v->z*v->z;
}

void v3normalize(vec3d *v) {
  double l = v3length(v);
  v->x /= l;
  v->y /= l;
  v->z /= l;
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
void v3rotate(vec3d *v, const vec3d *rot) {
  double q[4],cq[4],vv[4],ret1[4],ret2[4];
  euler2quat(rot->x,rot->y,rot->z,q);
  cq[0]=-q[0]; cq[1]=-q[1]; cq[2]=-q[2]; cq[3]=q[3];
  vv[0]=v->x; vv[1]=v->y; vv[2]=v->z; vv[3]=0.0;
  quatMul(q,vv,ret1);
  quatMul(ret1,cq,ret2);
  v->x = ret2[0];
  v->y = ret2[1];
  v->z = ret2[2];
}

/*
 * Returns a rotated vector of the given vec around the Y axis
 * by the given ry. (Unit=degrees)
 */
void v3simpleRotateY(vec3d *v,double ry) {
  double xx,yy,zz;
  ry *= M_PI/180.0;
  xx = v->x*cos(ry) + v->z*sin(ry);
  yy = v->y;
  zz = -v->x*sin(ry) + v->z*cos(ry);
  v->x = xx;
  v->y = yy;
  v->z = zz;
}

int v3epsilonEquals(const vec3d *u, const vec3d *v,double eps) {
  double dx = u->x - v->x;
  double dy = u->y - v->y;
  double dz = u->z - v->z;
  double diffS = dx*dx + dy*dy + dz*dz;
  if (diffS>(eps*eps))
    return 0;
  else
    return 1;
}


void printVec3d(const vec3d *v) {
  printf("(%f,%f,%f)",v->x,v->y,v->z);
}

void printlnVec3d(const vec3d *v) {
  printf("(%f,%f,%f)\n",v->x,v->y,v->z);
}
