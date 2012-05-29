package computation.sideralis.object;

/**
 * The SkyObject is the parent class for all objects in the sky.
 * They are mainly defined by their ascendance and declinaison
 * @author Bernard
 */
public class SkyObject {
    /** Right ascension expressed in h m s */
    protected float asc;
    /** Declinaison expressed in d m s */
    protected float dec;
    /** Reference ID */
    protected final String name;
    /** Magnitude apparente */
    protected float mag;
    /** All types of objects */
    public final static short NONE = -1;
    public final static short STAR = 0;
    public final static short MESSIER = 1;
    public final static short SUN = 2;
    public final static short MOON = 3;
    public final static short PLANET = 4;
    public final static short CONSTELLATION = 5;
    /** All planets */
    public final static short MERCURE = 0;
    public final static short VENUS = 1;
    public final static short MARS = 2;
    public final static short JUPITER = 3;
    public final static short SATURNE = 4;
    public final static short URANUS = 5;
    public final static short NEPTUNE = 6;
    public final static short EARTH = 7;


    /**
     * Constructor for SkyObject
     * @param asc
     * @param dec
     * @param name
     * @param mag
     */
    public SkyObject(float asc, float dec, String name, float mag) {
        this.asc = asc;
        this.dec = dec;
        this.name = name;
        this.mag = mag;
    }
    /**
     * Return the ascendance of the object
     * @return ascendance of the object
     */
    public double getAscendance() {
        return (double)asc;
    }
    /**
     * Return the declinaison of the object
     * @return declinaison of the object
     */
    public double getDeclinaison() {
        return (double)dec;
    }
    /**
     * Set ascendance
     * @param asc new value of ascendance
     */
    public void setAscendance(float asc) {
        this.asc = asc;
    }
    /**
     * Set declinaison
     * @param dec new value of declinaison
     */
    public void setDeclinaison(float dec) {
        this.dec = dec;
    }
    /**
     * Return the name of the object
     * @return the name of the object
     */
    public String getName() {
        return name;
    }
    /**
     * Return the magnitude of this star
     * @return magnitude of the star
     */
    public float getMag() {
        return mag;
    }


}
