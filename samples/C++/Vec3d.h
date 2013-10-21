/* Vec3d.h */
#ifndef VEC3D
#define VEC3D

#include <iostream>
#include <string>
using namespace std;

/*
 * Euclidean vector.
 */
class Vec3d {
public:
    Vec3d();
    Vec3d(double x,double y,double z);
    Vec3d(double xyz[]);
    Vec3d(string str);

    Vec3d& operator=(const Vec3d& v);
    Vec3d& operator+=(const Vec3d& v);
    Vec3d& operator-=(const Vec3d& v);
    Vec3d& operator*=(double s);
    Vec3d& operator/=(double s);
    Vec3d operator+();
    Vec3d operator-();

    void set(string str);
    void set(double x, double y, double z);
    double length();
    double lengthSquared();
    Vec3d& normalize();
    Vec3d& rotate(const Vec3d& rot);
    Vec3d& simpleRotateY(double ry);
    bool epsilonEquals(const Vec3d& v,double eps);

    double x;
    double y;
    double z;
};

Vec3d operator+(const Vec3d& u,const Vec3d& v);
Vec3d operator-(const Vec3d& u,const Vec3d& v);
double operator*(const Vec3d& u,const Vec3d& v); // inner product(dot)
Vec3d operator*(const Vec3d& v, double s);
Vec3d operator*(double s ,const Vec3d& v);
Vec3d operator/(const Vec3d& v, double s);
Vec3d operator/(const Vec3d& u,const Vec3d& v); // exterior product(cross)
bool operator==(const Vec3d& u,const Vec3d& v); // ??? danger

std::ostream& operator<<(std::ostream& stream, const Vec3d& v);

#endif
