import java.util.*;

/*
 * BurdenSet manages ids and coodinates of burdens.
 */
public class BurdenSet {
    HashMap<String,Vector> burdens;

    /*
     * Constructs empty BurdenSet.
     */
    public BurdenSet() {
        burdens = new HashMap<String,Vector>();
    }

    /*
     * Loads coodinates of burdens from the server response.
     */
    public void load(String info) {
        burdens.clear();
        String[] ss = info.split("\\s");
        int n = Integer.parseInt(ss[0]);
        for (int i=0;i<n;i++) {
            int ii = 1 + 4*i;
            double x = Double.parseDouble(ss[ii+1]);
            double y = Double.parseDouble(ss[ii+2]);
            double z = Double.parseDouble(ss[ii+3]);
            Vector jv = new Vector(x,y,z);
            burdens.put(ss[ii],jv);
        }
    }

    /*
     * Returns the number of burdens.
     */
    public int size() {
        return burdens.size();
    }

    /*
     * Returns the coodinate of the burden indicated by given id.
     */
    public Vector get(String id) {
        return burdens.get(id);
    }

    /*
     * Returns the all id of burdens.
     */
    public Set<String> getIDs() {
        return burdens.keySet();
    }

    /*
     * Returns the all vectors of burdens.
     */
    public Collection<Vector> getVectors() {
        return burdens.values();
    }

    /*
     * Returns the id of the burden which is nearest to given coodinate.
     */
    public String getNearest(Vector v) {
        double min = Double.MAX_VALUE;
        String retId = null;
        Vector tmpV = new Vector();
        for (String id : burdens.keySet()) {
            Vector jv = burdens.get(id);
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
