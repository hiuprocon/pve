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

void v3add(const vec3d *u,const vec3d *v,vec3d *ret);
void v3sub(const vec3d *u,const vec3d *v,vec3d *ret);
void v3scale(const vec3d *v,double s,vec3d *ret);
double v3dot(const vec3d *u,const vec3d *v);
void v3cross(const vec3d *u,const vec3d *v,vec3d *ret);
double v3length(const vec3d *v);
double v3lengthSquared(const vec3d *v);
void v3normalize(vec3d *v);
void v3rotate(vec3d *v,const vec3d *rot);
void v3simpleRotateY(vec3d *v,double ry);
int v3equals(const vec3d *u,const vec3d *v);
int v3epsilonEquals(const vec3d *u,const vec3d *v,double eps);

void printVec3d(const vec3d *v);
void printlnVec3d(const vec3d *v);

#endif
