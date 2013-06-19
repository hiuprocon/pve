package samples;

import java.util.HashMap;

public class JewelSet {
    HashMap<String,Vector> jewels;

    public JewelSet() {
        jewels = new HashMap<String,Vector>();
    }

    public void load(String info) {
        String[] ss = info.split("\\s");
        int n = Integer.parseInt(ss[0]);
        for (int i=0;i<n;i++) {
            int ii = 1 + 4*i;
            double x = Double.parseDouble(ss[ii+1]);
            double y = Double.parseDouble(ss[ii+2]);
            double z = Double.parseDouble(ss[ii+3]);
            Vector jv = new Vector(x,y,z);
            jewels.put(ss[ii],jv);
        }
    }

    public Vector get(String id) {
        return jewels.get(id);
    }

    public Vector getNearest(Vector v) {
        double min = Double.MAX_VALUE;
        Vector retV = null;
        Vector tmpV = new Vector();
        for (Vector jv : jewels.values()) {
            tmpV.sub(jv,v);
            double l = tmpV.lengthSquared();
            if (min>l) {
                retV = jv;
                min = l;
            }
        }
        return retV;
    }
}
