package computation.sideralis.object;

/**
 *
 * @author Bernard
 */
public class PlanetObject extends SkyObject {

    public double La0, La1, La2, La3;
    public double a;
    public double ea0, ea1, ea2, ea3;
    public double ia0, ia1, ia2, ia3;
    public double oa0, oa1, oa2, oa3;
    public double wa0, wa1, wa2, wa3;
    public double Ma0, Ma1, Ma2;
    private float dist;

    /**
     * The constructor of a planet object
     * @param asc ascension
     * @param dec declinaison
     * @param name name
     * @param mag magnitude
     * @param La0 Indirectly mean anomaly (coeff1)
     * @param La1 Indirectly mean anomaly (coeff2)
     * @param La2 Indirectly mean anomaly (coeff3)
     * @param La3 Indirectly mean anomaly (coeff4)
     * @param aa0 Size of the orbit
     * @param ea0 Eccentricity (coeff 1)
     * @param ea1 Eccentricity (coeff 2)
     * @param ea2 Eccentricity (coeff 3)
     * @param ea3 Eccentricity (coeff 4)
     * @param ia0 Inclination (coeff 1)
     * @param ia1 Inclination (coeff 2)
     * @param ia2 Inclination (coeff 3)
     * @param ia3 Inclination (coeff 4)
     * @param oa0 Small omega (coeff 1)
     * @param oa1 Small omega (coeff 2)
     * @param oa2 Small omega (coeff 3)
     * @param oa3 Small omega (coeff 4)
     * @param wa0 Large omega - ecliptic longitude (coeff 1)
     * @param wa1 Large omega - ecliptic longitude (coeff 2)
     * @param wa2 Large omega - ecliptic longitude (coeff 3)
     * @param wa3 Large omega - ecliptic longitude (coeff 4)
     * @param Ma0 Mean anomaly (coeff 1)
     * @param Ma1 Mean anomaly (coeff 2)
     * @param Ma2 Mean anomaly (coeff 3)
     */
    public PlanetObject(float asc, float dec, String name, short mag,
            double La0, double La1, double La2, double La3,
            double aa0,
            double ea0, double ea1, double ea2, double ea3,
            double ia0, double ia1, double ia2, double ia3,
            double oa0, double oa1, double oa2, double oa3,
            double wa0, double wa1, double wa2, double wa3,
            double Ma0, double Ma1, double Ma2) {

        super(asc, dec, name, mag);
        this.La0 = La0;
        this.La1 = La1;
        this.La2 = La2;
        this.La3 = La3;
        this.a = aa0;
        this.ea0 = ea0;
        this.ea1 = ea1;
        this.ea2 = ea2;
        this.ea3 = ea3;
        this.ia0 = ia0;
        this.ia1 = ia1;
        this.ia2 = ia2;
        this.ia3 = ia3;
        this.oa0 = oa0;
        this.oa1 = oa1;
        this.oa2 = oa2;
        this.oa3 = oa3;
        this.wa0 = wa0;
        this.wa1 = wa1;
        this.wa2 = wa2;
        this.wa3 = wa3;
        this.Ma0 = Ma0;
        this.Ma1 = Ma1;
        this.Ma2 = Ma2;
    }
    /**
     * Return the magnitude of the object
     * @return mag as a float
     */
    public float getMag() {
        return (float) mag / 10;
    }

    /**
     * Return the distance in kilo ly from earth
     * @return the distance as a float in kly
     */
    public float getDist() {
        return dist;
    }
}
