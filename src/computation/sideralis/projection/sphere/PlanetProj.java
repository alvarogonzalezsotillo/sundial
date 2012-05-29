package computation.sideralis.projection.sphere;

import computation.sideralis.math.MathFunctions;
import computation.sideralis.object.PlanetObject;
import computation.sideralis.object.SkyObject;


/**
 *
 * @author Bernard
 */
public class PlanetProj extends Projection {
    /** The space coordinate of the planets */
    private double x,y,z;
    /** The id of the planets - used to calculate for which planet we need to calculate perturbation factor */
    private short id;
    /** Variables for pertubation factor for giant planets*/
    private double A,B,C,D,lCor,R;
    /**
     * Creates a new instance of Planet
     * @param planet the planet object describing the planet (name, mag, ...)
     */
    public PlanetProj(PlanetObject planet, short id) {
        super(planet);
        this.id = id;
        A = B = C = D = 0;
    }
    /**
     * Calculate the position of the planet
     */
    public void calculate() {
        int k;
        double L,e,i,M1,a;
        double smallO,largeO;
        double T;
        double alpha,delta;
        PlanetObject planet = (PlanetObject)getObject();
        a = planet.a;

        // Calcul de T
        T = myPosition.getTemps().getT();
        // Calcul de ...
        L = (planet.La0 + planet.La1*T + planet.La2*T*T)%360;
        e = (planet.ea0 + planet.ea1*T + planet.ea2*T*T)%360;
        i = (planet.ia0 + planet.ia1*T + planet.ia2*T*T)%360;
        smallO = (planet.oa0 + planet.oa1*T + planet.oa2*T*T)%360;
        largeO = (planet.wa0 + planet.wa1*T + planet.wa2*T*T)%360;
        M1 = (planet.Ma0 + planet.Ma1*T + planet.Ma2*T*T)%360;

        
        // Add pertubations
        switch (id) {
            case SkyObject.JUPITER:
                pertubationJupiter(T);
                break;
            case SkyObject.SATURNE:
                perturbationSaturne(T);
                break;
            case SkyObject.NEPTUNE:
                perturbationNeptune(T);
                break;
            case SkyObject.URANUS:
                perturbationUranus(T);
                break;
            default:
                A = B = C = D = 0;
        }
        L = L + A;
        M1 = M1 + A -C/e;
        e = e + B/1000000;
        a = a + D/1000000;

        // Calcul de E
        double E1,E0,e0;
        E0 = M1;
        e0 = Math.toDegrees(e);
        for (k=0;k<10;k++) {
            E0 = E0+(M1+e0*Math.sin(Math.toRadians(E0))-E0)/(1-e*Math.cos(Math.toRadians(E0)));
        }
        E1 = E0;
        // ==================================
        // === Calcul de v (true anomaly) ===
        double nu;
        nu = (1+e)/(1-e);
        nu = Math.sqrt(nu)*Math.tan(Math.toRadians(E1/2));
        nu = 2*MathFunctions.arctan(nu, true);
        // =====================================
        // === Calcul de r (distance to sun) ===
        double r;
        //r = a*(1-e*e)/(1+e*Math.cos(v)) + R;                                  // Can be calculated also this way.
        r = a*(1-e*Math.cos(Math.toRadians(E1))) + R;

        // ========================================
        // === Calculate the spatial coordinate ===
        x = r*(Math.cos(largeO) * Math.cos(smallO+nu) - Math.sin(largeO)*Math.cos(i)*Math.sin(smallO+nu));
        y = r*(Math.sin(largeO) * Math.cos(smallO+nu) + Math.cos(largeO)*Math.cos(i)*Math.sin(smallO+nu));
        z = r*Math.sin(i)*Math.sin(smallO+nu);
        //System.out.println(" x="+x+" y="+y+" z="+z);

        // ============================
        // === Calcul de u (radian) ===
        double u;
        u = Math.toRadians(L) + nu - Math.toRadians(M1) - Math.toRadians(largeO);
        // ==========================================
        // === Calcul de l (longitude ecliptique) ===
        double l;
        l = Math.cos(Math.toRadians(i))*Math.sin(u);
        l = MathFunctions.arctan(l/Math.cos(u), l>=0?true:false);
        l = Math.toRadians(largeO) + l;
        l = l + Math.toRadians(lCor);
        // ===================
        // === Calcul de b ===
        double b;
        b = Math.sin(u)*Math.sin(Math.toRadians(i));
        b = MathFunctions.arcsin(b);
        // ======================================
        // === Calcul de R et theta du soleil ===
        RSoleil = Projection.getRSun();
        //theta = Projection.getThetaSun();
        // =======================
        // === Calcul de lambda ===
        double lambda;
        double N,DD;
        N = r * Math.cos(b)*Math.sin(l-theta);
        DD = r * Math.cos(b)*Math.cos(l-theta)+RSoleil;
        lambda = MathFunctions.arctan(N/DD, N>=0?true:false);
        lambda += theta;
        // Calcul de delta;
//        dist = N*N + DD*DD + r*Math.sin(b)*r*Math.sin(b);
        dist = (float)Math.sqrt(N*N + DD*DD + r*Math.sin(b)*r*Math.sin(b));
        // Calcul de beta
        double beta;
        beta = MathFunctions.arcsin(r/(double)dist*Math.sin(b));
        // Calcul de alpha et delta
        double sinD, tanA,tanAp;
        double eps;
        eps = 23.452994 - 0.0130125*T - 0.00000164*T*T + 0.000000503*T*T*T;
        sinD = Math.sin(beta)*Math.cos(Math.toRadians(eps)) + Math.cos(beta)*Math.sin(Math.toRadians(eps))*Math.sin(lambda);
        tanAp = (Math.sin(lambda)*Math.cos(Math.toRadians(eps))-Math.tan(beta)*Math.sin(Math.toRadians(eps)));
        tanA = tanAp/Math.cos(lambda);
        alpha = MathFunctions.arctan(tanA, tanAp>=0?true:false);    
        delta = MathFunctions.arcsin(sinD);

        planet.setAscendance((float)Math.toDegrees(alpha)/15);
        planet.setDeclinaison((float)Math.toDegrees(delta));
        calculateHorizontalCoordinate(planet.getAscendance(),planet.getDeclinaison(),false);
    }
    /**
     * Calculate pertubation factors for Jupiter
     * @param T the number of century since 1900
     */
    private void pertubationJupiter(double T) {
        double u;
        double P,Q,V,dzeta;

        lCor = R = 0;
        // Calcul de u
        u = T/5 +0.1;

        P = Math.toRadians(237.475 + 3034.9061*T);
        Q = Math.toRadians(265.916 + 1222.1139*T);
        V = 5*Q - 2*P;
        dzeta = Q - P;

        A =  (0.3314-0.0103*u-0.0047*u*u)*Math.sin(V)
            +(0.0032-0.0644*u+0.0021*u*u)*Math.cos(V)
            +0.0136*Math.sin(dzeta)+0.0185*Math.sin(2*dzeta)+0.0067*Math.sin(3*dzeta)
            +(0.0073*Math.sin(dzeta) + 0.0064*Math.sin(2*dzeta) - 0.0338*Math.cos(dzeta))*Math.sin(Q)
            -(0.0357*Math.sin(dzeta) + 0.0063*Math.cos(dzeta) + 0.0067*Math.cos(2*dzeta))*Math.cos(Q);

        B =   (361 + 13*u)*Math.sin(V)+(129-58*u)*Math.cos(V)
            + (128*Math.cos(dzeta)-676*Math.sin(dzeta)-111*Math.sin(2*dzeta))*Math.sin(Q)
            + (146*Math.sin(dzeta)-82+607*Math.cos(dzeta)+99*Math.cos(2*dzeta)+51*Math.cos(3*dzeta))*Math.cos(Q)
            - (96*Math.sin(dzeta)+100*Math.cos(dzeta))*Math.sin(2*Q)
            - (96*Math.sin(dzeta)-102*Math.cos(dzeta))*Math.cos(2*Q);

        C =   (0.0072-0.0031*u)*Math.sin(V) - 0.0204*Math.cos(V)
            + (0.0073*Math.sin(dzeta)+0.0340*Math.cos(dzeta)+0.0056*Math.cos(2*dzeta))*Math.sin(Q)
            + (0.0378*Math.sin(dzeta)+0.0062*Math.sin(2*dzeta)-0.0066*Math.cos(dzeta))*Math.cos(Q)
            - 0.0054*Math.sin(dzeta)*Math.sin(2*Q)+0.0055*Math.cos(dzeta)*Math.cos(2*Q);

        D =  -263*Math.cos(V) + 205*Math.cos(dzeta) + 693*Math.cos(2*dzeta) + 312*Math.cos(3*dzeta)
            + 299*Math.sin(dzeta)*Math.sin(Q)
            + (204*Math.sin(2*dzeta)-337*Math.cos(dzeta))*Math.cos(Q);
    }

    /**
    * Calculate pertubation factors for Jupiter
    * @param T the number of century since 1900
    */
   private void perturbationSaturne(double T) {
        double u;
        double P,Q,V,dzeta;

        lCor = R = 0;
        // Calcul de u
        u = T/5 +0.1;

        P = Math.toRadians(237.475 + 3034.9061*T);
        Q = Math.toRadians(265.916 + 1222.1139*T);
        V = 5*Q - 2*P;
        dzeta = Q - P;

        A =   (-0.8142+0.0181*u+0.0167*u*u)*Math.sin(V)
            + (-0.0105+0.1609*u-0.0041*u*u)*Math.cos(V)
               -0.01488*Math.sin(dzeta) - 0.0408*Math.sin(2*dzeta)-0.0152*Math.sin(3*dzeta)
            + ( 0.0089*Math.sin(dzeta) - 0.0165*Math.sin(2*dzeta))*Math.sin(Q)
            + ( 0.0813*Math.cos(dzeta) + 0.0150*Math.cos(2*dzeta))*Math.sin(Q)
            + ( 0.0856*Math.sin(dzeta) + 0.0253*Math.cos(dzeta) + 0.0144*Math.cos(2*dzeta))*Math.cos(Q)
            + 0.0092*Math.sin(2*dzeta)*Math.sin(2*Q);

        B =   (-793 + 255*u)*Math.sin(V)+(1338+123*u)*Math.cos(V)
            + 1241*Math.sin(Q) + (39-62*u)*Math.sin(dzeta)*Math.sin(Q)
            + (2660*Math.cos(dzeta)-469*Math.cos(2*dzeta)-187*Math.cos(3*dzeta) -82*Math.cos(4*dzeta))*Math.sin(Q)
            - (1270*Math.sin(dzeta)+420*Math.sin(2*dzeta)+150*Math.sin(3*dzeta))*Math.cos(Q)
            - 62*Math.sin(4*dzeta)*Math.cos(Q)
            + (221*Math.sin(dzeta)-221*Math.sin(2*dzeta)-57*Math.sin(3*dzeta))*Math.sin(2*Q)
            - (278*Math.cos(dzeta)-202*Math.cos(2*dzeta))*Math.sin(2*Q)
            - (284*Math.sin(dzeta)+159*Math.cos(dzeta))*Math.cos(2*Q)
            + (216*Math.cos(2*dzeta)+56*Math.cos(3*dzeta))*Math.cos(2*Q);

        C =   (0.0771+0.0072*u)*Math.sin(V)
            + (0.0458-0.0148*u)*Math.cos(V)
            - (0.0758*Math.sin(dzeta)+0.0248*Math.sin(2*dzeta)+0.0086*Math.sin(3*dzeta))*Math.sin(Q)
            - (0.0726+0.1504*Math.cos(dzeta)-0.0269*Math.cos(2*dzeta)-0.0101*Math.cos(3*dzeta))*Math.cos(Q)
            - (0.0136*Math.sin(dzeta)-0.0136*Math.cos(2*dzeta))*Math.sin(2*Q)
            - (0.0137*Math.sin(dzeta)-0.0120*Math.sin(2*dzeta))*Math.cos(2*Q)
            + (0.0149*Math.cos(dzeta)-0.0131*Math.cos(2*dzeta))*Math.cos(2*Q);

        D =   2933*Math.cos(V) + 33629*Math.cos(dzeta) -3081*Math.cos(2*dzeta)
            - 1423*Math.cos(3*dzeta) - 671*Math.cos(4*dzeta)
            + (1098-2812*Math.sin(dzeta)+688*Math.sin(2*dzeta))*Math.sin(Q)
            + (2138*Math.cos(dzeta)-999*Math.cos(2*dzeta)-642*Math.cos(3*dzeta))*Math.sin(Q)
            - 890*Math.cos(Q)
            + (2206*Math.sin(dzeta)-1590*Math.sin(2*dzeta)-647*Math.sin(3*dzeta))*Math.cos(Q)
            + (2885*Math.cos(dzeta)+2172*Math.cos(2*dzeta))*Math.cos(Q)
            - 778*Math.cos(dzeta)*Math.sin(2*Q)-856*Math.sin(dzeta)*Math.cos(2*Q);
   }
    /**
     * Calculate pertubation factors for Jupiter
     * @param T the number of century since 1900
     */
    private void perturbationUranus(double T) {
        double h,S,dzeta,eta,theta0;
        double u;
        double P,Q;

        // Calcul de u
        u = T/5 +0.1;

        h = Math.toRadians(284.02 + 8.51*T);
        S = Math.toRadians(243.52 + 428.47*T);
        P = Math.toRadians(237.475 + 3034.9061*T);
        Q = Math.toRadians(265.916 + 1222.1139*T);
        dzeta = S - P;
        eta = S -Q;
        theta0 = Math.toRadians(200.25 - 209.98*T);

        A = 0.8463*Math.sin(h) + 0.0360*Math.sin(2*h) + (0.0822-0.0068*u)*Math.cos(h);
        B = 2098*Math.cos(h) - 335*Math.sin(h) + 131*Math.cos(2*h);
        C = 0.1203*Math.sin(h) + 0.0195*Math.cos(h) + 0.0062*Math.sin(2*h);
        D = -3825*Math.cos(h);

        lCor =(0.0101 - 0.0010*u)*Math.sin(S+eta)
            - (0.0386 - 0.0020*u)*Math.cos(S+eta)
            + (0.0350 - 0.0010*u)*Math.cos(2*S+eta)
            - 0.0148*Math.sin(dzeta) + 0.0099*Math.sin(theta0) + 0.0088*Math.sin(2*theta0);

        R = - 0.02595 + 0.00498*Math.cos(dzeta)
            - 0.00123*Math.cos(S) + 0.00335*Math.cos(eta)
            + (0.00579*Math.cos(S) - 0.00116*Math.sin(S) + 0.00139*Math.cos(2*S))*Math.sin(eta)
            + (0.00135*Math.cos(S) + 0.00570*Math.sin(S) + 0.00139*Math.sin(2*S))*Math.cos(eta)
            + 0.00090*Math.cos(2*theta0) + 0.00089*(Math.cos(theta0)-Math.cos(3*theta0));
        //System.out.println("Uranus: A="+A+" B="+B+" C="+C+" D="+D+" L="+lCor+" R="+R);

    }
    /**
     * Calculate pertubation factors for Jupiter
     * @param T the number of century since 1900
     */
    private void perturbationNeptune(double T) {
        double dzeta, eta;
        double h,theta0;
        
        h = Math.toRadians(284.02 + 8.51*T);
        theta0 = Math.toRadians(200.25 - 209.98*T);

        dzeta = Math.toRadians(153.71 + 2816.42*T);
        eta = Math.toRadians(182.15 + 1003.62*T);
        
        A = -0.5898*Math.sin(h) - 0.0561*Math.cos(h) - 0.0243*Math.sin(2*h);
        B = 439*Math.sin(h) + 426*Math.cos(h) + 113*Math.sin(2*h) + 109*Math.cos(2*h);
        C = 0.0240*Math.sin(h) - 0.0253*Math.cos(h);
        D = -817*Math.sin(h) + 8189*Math.cos(h) + 781*Math.cos(2*h);
        
        lCor = 0.0096*Math.sin(dzeta) + 0.0052*Math.sin(eta);
        
        R = -0.04060 + 0.00499*Math.cos(dzeta) + 0.00274*Math.cos(eta)
            +0.00204*Math.cos(theta0) + 0.00105*Math.cos(2*theta0);
        //System.out.println("Neptune: A="+A+" B="+B+" C="+C+" D="+D+" L="+lCor+" R="+R);

    }
    /**
     * Return the x coordinate (heliocentric) of the planet
     * @return x coordinate
     */
    public double getX() {
        return x*10;
    }
    /**
     * Return the y coordinate (heliocentric) of the planet
     * @return y coordinate
     */
    public double getY() {
        return y*10;
    }
    /**
     * Return the z coordinate (heliocentric) of the planet
     * @return z coordinate
     */
    public double getZ() {
        return z*10;
    }

}
