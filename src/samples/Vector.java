package samples;

import static java.lang.Math.*;

public class Vector {
    public double x;
    public double y;
    public double z;

    public Vector() {
        x = 0.0; y = 0.0; z = 0.0;
    }

    public Vector(double x,double y,double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vector(Vector v) {
        x = v.x; y = v.y; z = v.z;
    }

    public Vector(double[] xyz) {
        x = xyz[0]; y = xyz[1]; z = xyz[2];
    }

    public Vector(String xyz) {
        String[] s = xyz.split("\\s");
        x = Double.parseDouble(s[0]);
        y = Double.parseDouble(s[1]);
        z = Double.parseDouble(s[2]);
    }

    public void set(double x,double y,double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public void set(Vector v) {
        x = v.x; y = v.y; z = v.z;
    }

    public void set(double[] xyz) {
        x = xyz[0]; y = xyz[1]; z = xyz[2];
    }

    public void set(String xyz) {
        String[] s = xyz.split("\\s");
        x = Double.parseDouble(s[0]);
        y = Double.parseDouble(s[1]);
        z = Double.parseDouble(s[2]);
    }

    public Object clone() {
        return new Vector(this);
    }

    public boolean equals(Vector v) {
        return x==v.x && y==v.y && z==v.z;
    }

    public boolean equals(Object o) {
        if (o instanceof Vector)
            return equals((Vector)o);
        else
            return false;
    }

    public boolean epsilonEquals(Vector v,double epsilon) {
        double lid = Math.max(Math.abs(x-v.x),Math.abs(y-v.y));
        lid = Math.max(lid,Math.abs(z-v.z));
        if (lid>epsilon)
            return false;
        else
            return true;
    }

    public String toString() {
        return "("+x+" ,"+y+", "+z+")";
    }

    public void add(Vector v) {
        x += v.x; y += v.y; z += v.z;
    }

    public void add(Vector v1,Vector v2) {
        x += v1.x + v2.x;
        y += v1.y + v2.y;
        z += v1.z + v2.z;
    }

    public void sub(Vector v) {
        x -= v.x; y -= v.y; z -= v.z;
    }

    public void sub(Vector v1,Vector v2) {
        x += v1.x - v2.x;
        y += v1.y - v2.y;
        z += v1.z - v2.z;
    }

    public void scale(double s) {
        x *= s; y *= s; z *= s;
    }

    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double lengthSquared() {
        return x*x + y*y + z*z;
    }

    public void normalize() {
        double l = length();
        x /= 1.0/l; y /= 1.0/l; z /= 1.0/l;
    }

    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public void cross(Vector v) {
        double xx = y*v.z - z*v.y;
        double yy = z*v.x - x*v.z;
        double zz = x*v.y - y*v.x;
        x = xx; y = yy; z = zz;
    }

    public void cross(Vector v1,Vector v2) {
        x = v1.y*v2.z - v1.z*v2.y;
        y = v1.z*v2.x - v1.x*v2.z;
        z = v1.x*v2.y - v1.y*v2.x;
    }

    public static Vector rotate(Vector rot,Vector vec) {
        double[] q = euler2quat(rot.x,rot.y,rot.z);
        double[] cq = new double[]{-q[0],-q[1],-q[2],q[3]};
        double[] v = new double[]{vec.x,vec.y,vec.z,0};
        double[] ret = quatMul(q,v);
        ret = quatMul(ret,cq);
        return new Vector(ret[0],ret[1],ret[2]);
    }
    static double[] euler2quat(double x,double y,double z) {
        double[] ret = new double[]{0,0,0,1};
        x *= Math.PI/180; y *= Math.PI/180; z *= Math.PI/180;
        ret = quatMul(ret,new double[]{0,sin(y/2),0,cos(y/2)});
        ret = quatMul(ret,new double[]{sin(x/2),0,0,cos(x/2)});
        ret = quatMul(ret,new double[]{0,0,sin(z/2),cos(z/2)});
        return ret;
    }
    static double[] quatMul(double[] a,double[] b) {
        double[] ret = new double[4];
        ret[0]= a[3]*b[0] + a[0]*b[3] + a[1]*b[2] - a[2]*b[1];
        ret[1]= a[3]*b[1] - a[0]*b[2] + a[1]*b[3] + a[2]*b[0];
        ret[2]= a[3]*b[2] + a[0]*b[1] - a[1]*b[0] + a[2]*b[3];
        ret[3]= a[3]*b[3] - a[0]*b[0] - a[1]*b[1] - a[2]*b[2];
        return ret;
    }
    public static Vector simpleRotateY(double ry,Vector vec) {
        ry *= Math.PI/180;
        double x = vec.x*cos(ry) + vec.z*sin(ry);
        double y = vec.y;
        double z = -vec.x*sin(ry) + vec.z*cos(ry);
        return new Vector(x,y,z);
    }
}
