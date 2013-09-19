/* vec3d.h */
#ifndef VEC3D
#define VEC3D

typedef struct {
  double x;
  double y;
  double z;
} vec3d;

vec3d makeVec3dFromXYZ(double x,double y,double z);
vec3d makeVec3dFromArray(double xyz[]);
vec3d makeVec3dFromStr(char *str);

vec3d add(vec3d u,vec3d v);
vec3d sub(vec3d u,vec3d v);
vec3d scale(vec3d v,double s);
double dot(vec3d u,vec3d v);
vec3d cross(vec3d u,vec3d v);
double length(vec3d v);
double lengthSquared(vec3d v);
vec3d normalize(vec3d v);
vec3d rotate(vec3d v,vec3d rot);
vec3d simpleRotateY(vec3d v,double ry);
int epsilonEquals(vec3d u,vec3d v,double eps);

void printVec3d(vec3d v);
void printlnVec3d(vec3d v);

#endif
