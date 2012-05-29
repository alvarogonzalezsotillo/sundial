package computation.sideralis.projection.sphere;

import java.util.Calendar;

import computation.sideralis.location.Position;
import computation.sideralis.math.MathFunctions;
import computation.sideralis.object.SkyObject;


/**
 * This is the parent class for all object in the sky.
 * @author Bernard
 */
public class Projection {
    /** A reference to my position (defined as static to save memory) */
    protected static Position myPosition;
    /** The time used to calculate the precession, defined as static to save computing time */
    private static Calendar cal;
    /** Various variables used several time in calculation, defined as static to save computing time */
    private static double longitudeDiv15,latitudeInRad,HS;
    /** A reference to the object */
    protected final SkyObject object;
    /** The calculated height*/
    protected double hau;
    /** The calculated azimuth */
    protected double az;
    /** A flag indicating if it is visible or not */
    protected boolean visible;
    /** R value for the sun */
    static protected double RSoleil;
    /** Theta value for the sun */
    static protected double theta,thetaApp,epsilon;
    /** The distance from earth of this object */
    protected float dist;
    /** The precession values */
    protected double dAlpha,dDelta; 
    /** Angle horaire */
    protected double H;


    /**
     * Creates a new instance of Projection
     * @param object the description of the object
     */
    public Projection(SkyObject object) {
        this.object = object;
    }
    /**
     * Return the object which was used to create the projection object
     * @return the object linker with its projection
     */
    public SkyObject getObject() {
        return object;
    }

    /**
     * Return value of R for the sun
     * @return R
     */
    static public double getRSun() {
        return RSoleil;
    }
    /**
     * Return value of theta for the sun
     * @return theta
     */
    static public double getThetaSun() {
        return theta;
    }
    /**
     * Return the distance from earth to object
     * @return distance in km or ua from earth to object
     */
    public float getDistance() {
        return dist;
    }
    /** 
     * Return the delta value to be added to RA from catalog to get RA for current time
     * @return delta RA
     */
    public double getDAlpha() {
        return dAlpha;
    }
    /** 
     * Return the delta value to be added to Dec from catalog to get dec for current time
     * @return delta Dec
     */
    public double getDDelta() {
        return dDelta;
    }
    /**
     * Return the hour angle
     * @return H the hour angle.
     */
    public double getH() {
        return H;
    }
    /**
     * Calculate horizontal coordinate of the star
     */
    public void calculate() {
        calculateHorizontalCoordinate(object.getAscendance(),object.getDeclinaison(),true);
    }
    /**
     * Calculate the R and theta value for the sun
     * @param T the time
     */
    static public void calculateParamSun(double T) {
        int k;

        // Calcul de R et Omega du soleil
        double E0;
        double eSoleil,e0Soleil,ESoleil,MSoleil;
        double LSoleil,CSoleil;
        double sigmaSoleil;
        double a,b,c,d,e,h;
        
        eSoleil = 0.01675104 - 0.0000418*T - 0.000000126*T*T;
        MSoleil = 358.47583 + 35999.04975*T - 0.000150*T*T - 0.0000033*T*T*T;
        
        E0 = MSoleil;
        e0Soleil = Math.toDegrees(eSoleil);
        for (k=0;k<8;k++) {
            E0 = E0+(MSoleil+e0Soleil*Math.sin(Math.toRadians(E0))-E0)/(1-eSoleil*Math.cos(Math.toRadians(E0)));
        }        
        
        ESoleil = E0;
        RSoleil = 1.0000002*(1-eSoleil*Math.cos(Math.toRadians(ESoleil)));
        LSoleil = 279.69668 + 36000.76892*T + 0.0003025*T*T;
        
        
        // Calcul de equation du centre C (p55) du soleil
        MSoleil = Math.toRadians(MSoleil);
        CSoleil = (1.919460 - 0.004789*T - 0.000014*T*T)*Math.sin(MSoleil)
                    + (0.020094 - 0.000100*T)*Math.sin(2*MSoleil)
                    + 0.000293*Math.sin(3*MSoleil);
        
        sigmaSoleil = 259.18-1934.142*T;
        sigmaSoleil = Math.toRadians(sigmaSoleil);
        
        // Calcul de epsilon
        epsilon = 23.452294 - 0.0130125*T - 0.00000164*T*T + 0.000000503*T*T;
        epsilon += 0.00256*Math.cos(sigmaSoleil);
        epsilon = Math.toRadians(epsilon);
        
        // Calcul de Theta (p56)        
        theta = LSoleil + CSoleil;
        
        a = Math.toRadians(153.23 + 22518.7541*T);
        b = Math.toRadians(216.57 + 45037.5082*T);
        c = Math.toRadians(312.69 + 32964.3577*T);
        d = Math.toRadians(350.74 + 445267.1142*T - 0.00144*T*T);
        e = Math.toRadians(231.19 + 20.20*T);
        h = Math.toRadians(353.40 + 65928.7155*T);
        
        theta = theta + 0.00134*Math.cos(a) + 0.00154*Math.cos(b) + 0.00200*Math.cos(c) + 0.00179*Math.sin(d) + 0.00178*Math.sin(e);
        RSoleil = RSoleil + 0.00000543*Math.sin(a) + 0.00001575*Math.sin(b) + 0.00001627*Math.sin(c) + 0.00003076*Math.cos(d) + 0.00000927*Math.sin(h);
        
        thetaApp = theta - 0.00569 - 0.00479*Math.sin(sigmaSoleil);             // De la longitude vrai a la longitude moyenne
        
        theta %= 360;
        theta = Math.toRadians(theta);

        thetaApp %= 360;
        thetaApp = Math.toRadians(thetaApp);
        //System.out.println("Theta moy: " + theta + " - Theta app: "+ thetaApp);
    }
    /**
     * Calculate the precession values. Values are returned in member of the class dAlpha and dDelta
     * @param alpha the alpha value of the object
     * @param delta the dela value of the object
     */
    private void calculatePrecession(double alpha,double delta) {
        double m,n,T;

        // Calculate precession

        T = (cal.get(Calendar.YEAR)-1900)/100;
        m = 3.07234+0.00186*T;
        n = 20.0468 - 0.0085*T;
        dAlpha = m+n/15*Math.sin(Math.toRadians(alpha*15))*Math.tan(Math.toRadians(delta));
        dDelta = n*Math.cos(Math.toRadians(alpha*15));
    }
    /**
     * Calculate the horizontal coordinate (azimuth and height) from ascendance, declinaison, time and position
     * @param alpha the ascendance of the object (in h m s)
     * @param delta the declinaison of the object (in d m s)
     * @param precession true if precession should be calculated (for stars or Messier), else false (for planets and sun)
     */
    public void calculateHorizontalCoordinate(double alpha, double delta,boolean precession) {
        double sinH,cosH,sinT,tanD,cosT,cosD,sinHau;
        double sinD, tanA;
        double T;

        if (precession) {
            calculatePrecession(alpha,delta);
            // Modify alpha and delta values
            T = (cal.get(Calendar.YEAR)-2000);
            alpha += dAlpha*T/3600;                                                 // dAlpha is given in s
            delta += dDelta*T/3600;                                                 // dDelta is given in s too
        }

        // Calculate hour angle
        H = HS + longitudeDiv15;
        H = H - alpha;
        
        // Convert H in radian
        H = H * 15;                                                             // One hour equals 15 degrees
        H = Math.toRadians(H);
        
        // Calculate hauteur and azimuth of the star
        sinH = Math.sin(H);
        cosH = Math.cos(H);
        sinT = Math.sin(latitudeInRad);
        tanD = Math.tan(delta / 180.0 * Math.PI);
        cosT = Math.cos(latitudeInRad);
        tanA = sinH/(cosH*sinT-tanD*cosT);                                      // (7.5)
        
        sinD = Math.sin(Math.toRadians(delta));
        cosD = Math.cos(Math.toRadians(delta));
        sinHau = sinT*sinD+cosT*cosD*cosH;                                      // (7.6)
        
        hau = MathFunctions.arcsin(sinHau);
        az = MathFunctions.arctan(tanA,sinH>=0?true:false);
        az += Math.PI/2;                                                    // to have North on top of the screen
        
        // Add refraction atmospherique
        if (hau>0)
            hau = addRefractionAtmospherique(hau);
        // Is the star below or above the horizon ?
        if (hau>0)
            visible = true;                                                     // The star is above the horizon
        else
            visible = false;                                                    // The star is below the horizon
    }

    private double addRefractionAtmospherique(double h) {
        double R=0;
        double hDeg;
        hDeg = Math.toDegrees(h);
        if (hDeg>15) {
            double tmp = Math.tan(Math.PI/2-h);
            R = (58.276*tmp - 0.0824*tmp*tmp*tmp)/3600;
        } else if (hDeg>4) {
            R = 4.4010-0.9603*MathFunctions.log(hDeg-1.1);
            R *= R/60;
        } else {
            R = 7.5262-2.2204*MathFunctions.log(hDeg+2.6);
            R *= R/60;
        }
        return Math.toRadians(hDeg+R);
    }
    /**
     * Return the height
     * @return the height of the object
     */
    public double getHeight() {
        return hau;
    }
    /**
     * Return the real height as string
     * @return the height as a string formatted like 45°10'
     */
    public String getRealHeightAsString() {
        String s;
        int tmp;
        tmp = (int)(Math.toDegrees(hau));
        s = new Integer(tmp).toString();
        tmp = (int)((Math.toDegrees(hau)-tmp)*60);
        s = s + "°"+tmp+"'";
        return s;
    }
    /**
     * Return the azimuth
     * @return the azimuth of the object
     */
    public double getAzimuth() {
        return az;
    }
    /**
     * Return the real azimuth as string
     */
    public String getRealAzimuthAsString() {
        String s;
        int tmp;
        tmp = (int)(Math.toDegrees(az)+90);
        s = new Integer(tmp%360).toString();
        tmp = (int)((Math.toDegrees(az)+90-tmp)*60);
        s = s + "°"+tmp+"'";
        return s;
    }
    /**
     * Set the static field myPosition
     * @param p a reference to the user position
     */
    public static void setPosition(Position p) {
        myPosition = p;
    }
    /**
     * Set the static field cal
     */
    public static void calcStaticVar() {
        cal = myPosition.getTemps().getCalendar();
        longitudeDiv15 = myPosition.getLongitude()/15;
        latitudeInRad = Math.toRadians(myPosition.getLatitude());
        HS = myPosition.getTemps().getHS();
    }

    public String toString() {
        String s;

        s = "H:"+hau + " / A:"+az;

        return s;
    }
}
