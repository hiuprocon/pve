/* vec3d.h */
#ifndef VEC3D
#define VEC3D

typedef struct {
  double x;
  double y;
  double z;
} vec3d;

void setXYZToVec3d(double x,double y,double z,vec3d *ret);
void setArrayToVec3d(const double xyz[],vec3d *ret);
void setStrToVec3d(const char *str,vec3d *ret);
void setVec3dToVec3d(const vec3d *v,vec3d *ret);

void add(const vec3d *u,const vec3d *v,vec3d *ret);
void sub(const vec3d *u,const vec3d *v,vec3d *ret);
void scale(const vec3d *v,double s,vec3d *ret);
double dot(const vec3d *u,const vec3d *v);
void cross(const vec3d *u,const vec3d *v,vec3d *ret);
double length(const vec3d *v);
double lengthSquared(const vec3d *v);
void normalize(vec3d *v);
void rotate(vec3d *v,const vec3d *rot);
void simpleRotateY(vec3d *v,double ry);
int epsilonEquals(const vec3d *u,const vec3d *v,double eps);

void printVec3d(const vec3d *v);
void printlnVec3d(const vec3d *v);

#endif
