package computation.sideralis.object;

/**
 *
 * @author Bernard
 */
public class MessierObject extends SkyObject {
    /** The distance from earth */
    private float dist;
    
    /** 
     * Creates a new instance of MessierData
     */
    public MessierObject(float asc, float dec, short mag, float dist, String name) {
        super(asc,dec,name,mag);
        this.dist = dist;
    }
    /**
     * Return the distance in kilo ly from earth
     * @return the distance as a float in kly
     */
    public float getDist() {
        return dist;
    }

    /**
     * Return the magnitude of the object
     * @return mag as a float
     */
    public float getMag() {
        return (float) mag / 10;
    }

}
