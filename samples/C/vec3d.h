/* vec3d.h */
#ifndef VEC3D
#define VEC3D

typedef struct {
  double x;
  double y;
  double z;
} vec3d;

void setXYZToVec3d(double x,double y,double z,vec3d *ret);
void setArrayToVec3d(double xyz[],vec3d *ret);
void setStrToVec3d(char *str,vec3d *ret);
void setVec3dToVec3d(vec3d *v,vec3d *ret);

void add(vec3d *u,vec3d *v,vec3d *ret);
void sub(vec3d *u,vec3d *v,vec3d *ret);
void scale(vec3d *v,double s,vec3d *ret);
double dot(vec3d *u,vec3d *v);
void cross(vec3d *u,vec3d *v,vec3d *ret);
double length(vec3d *v);
double lengthSquared(vec3d *v);
void normalize(vec3d *v);
void rotate(vec3d *v,vec3d *rot);
void simpleRotateY(vec3d *v,double ry);
int epsilonEquals(vec3d *u,vec3d *v,double eps);

void printVec3d(vec3d *v);
void printlnVec3d(vec3d *v);

#endif
