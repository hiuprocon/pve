import java.util.*;

/*
 * JewelSet manages ids and coodinates of jewels.
 */
public class JewelSet {
    HashMap<String,Vector> jewels;

    /*
     * Constructs empty JewelSet.
     */
    public JewelSet() {
        jewels = new HashMap<String,Vector>();
    }

    /*
     * Loads coodinates of jewels from the server response.
     */
    public void load(String info) {
        jewels.clear();
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

    /*
     * Returns the number of jewels.
     */
    public int size() {
        return jewels.size();
    }

    /*
     * Returns the coodinate of the jewel indicated by given id.
     */
    public Vector get(String id) {
        return jewels.get(id);
    }

    /*
     * Returns the all id of jewels.
     */
    public Set<String> getIDs() {
        return jewels.keySet();
    }

    /*
     * Returns the all vectors of jewels.
     */
    public Collection<Vector> getVectors() {
        return jewels.values();
    }

    /*
     * Returns the id of the jewel which is nearest to given coodinate.
     */
    public String getNearest(Vector v) {
        double min = Double.MAX_VALUE;
        String retId = null;
        Vector tmpV = new Vector();
        for (String id : jewels.keySet()) {
            Vector jv = jewels.get(id);
            if (v.x * jv.x < 0.0)
                continue;
            tmpV.sub(jv,v);
            double l = tmpV.lengthSquared();
            if (min>l) {
                retId = id;
                min = l;
            }
        }
        return retId;
    }
}
