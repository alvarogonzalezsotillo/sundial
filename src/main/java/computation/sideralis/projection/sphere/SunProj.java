package computation.sideralis.projection.sphere;

import computation.sideralis.math.MathFunctions;
import computation.sideralis.object.SkyObject;


/**
 *
 * @author Bernard
 */
public class SunProj extends Projection {
    /**
     * Creates a new instance of Sun
     * @param object the description of the sun object (name, mag, ...)
     */
    public SunProj(SkyObject object) {
        super(object);
    }
    /**
     *  Calculate Ascension, declinaison and then height and azimuth among others
     */
    public void calculate() {
        double tanA,tanAn,sinD;
        double alpha,delta;

        // Calcul de tangente alpha
        tanAn = Math.cos(epsilon)*Math.sin(thetaApp);
        tanA = tanAn/Math.cos(thetaApp);
        alpha = MathFunctions.arctan(tanA,tanAn>=0?true:false);
        sinD = Math.sin(epsilon)*Math.sin(thetaApp);
        delta = MathFunctions.arcsin(sinD);

        object.setAscendance((float)Math.toDegrees(alpha)/15);
        object.setDeclinaison((float)Math.toDegrees(delta));
        calculateHorizontalCoordinate(object.getAscendance(),object.getDeclinaison(),false);        
    }
}
