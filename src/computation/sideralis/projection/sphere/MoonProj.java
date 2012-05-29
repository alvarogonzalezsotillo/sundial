package computation.sideralis.projection.sphere;

import computation.sideralis.math.MathFunctions;
import computation.sideralis.object.SkyObject;



/**
 *
 * @author Bernard
 */
public class MoonProj extends Projection {
    /** The different phase of the moon */
    public static final short NEW = 0;
    public static final short FIRST = 1;
    public static final short FULL = 2;
    public static final short LAST = 3;
    
    /**
     * Creates a new instance of Moon
     * @param object the moon object describing the moon
     */
    public MoonProj(SkyObject object) {
        super(object);
    }
    /**
     * From T we deduce Lambda and Beta (Chap 27) through L' and M
     * From Lambda and Beta we deduce Alpha and Delta (7.3) and (7.4)
     * From Alpha and Delta we deduce height and azimuth (7.5) and (7.6)
     */
    public void calculate() {
        double T,Lp,M,Mp,D,F,O;
        double l,b,B;
        double e,e2;
        double o1,o2;
        double pi;
        double alpha,delta;

        // Calcul de T = (JJ-2415020.0)/36525
        T = myPosition.getTemps().getT();

        // A partir de T.
        // Calcul de L'
        Lp = 270.434164 + 481267.8831*T - 0.001133*T*T + 0.0000019*T*T*T;
        Lp = Lp % 360;
        // Calcul de M
        M = 358.475833 + 35999.0498*T - 0.000150*T*T - 0.0000033*T*T*T;
        M = M % 360;
        // Calcul de M'
        Mp = 296.104608 + 477198.8491*T + 0.009192*T*T + 0.0000144*T*T*T;
        Mp = Mp % 360;
        // Calcul de D
        D = 350.737486 + 445267.1142*T - 0.001436*T*T + 0.0000019*T*T*T;
        D = D % 360;
        // Calcul de F
        F = 11.250889 + 483202.0251*T - 0.003211*T*T - 0.0000003*T*T*T;
        F = F % 360;
        // Calcul de Omega
        O = 259.183275 - 1934.1420*T + 0.002078*T*T + 0.0000022*T*T*T;
        O = O % 360;
        O = Math.toRadians(O);
        
        // Ajout des termes additifs.
        double s1 = Math.sin(Math.toRadians(51.2+20.2*T));
        double s2 = 0.003964* Math.sin(Math.toRadians(346.560+132.870*T-0.0091731*T*T));
        double s3 = Math.sin(O);

        Lp = Lp + 0.000233*s1 + s2 + 0.001964*s3;
        M = M - 0.001778*s1;
        Mp = Mp + 0.000817*s1 + s2 + 0.002541*s3;
        D = D + 0.002011*s1 + s2 + 0.001964*s3;
        F = F + s2 -0.024691*s3 - 0.004328*Math.sin(O+Math.toRadians(275.05-2.30*T));
        
        // Conversion from degre to radian;
        //Lp = Lp*Math.PI/180.0;
        M = Math.toRadians(M);
        Mp = Math.toRadians(Mp);
        D = Math.toRadians(D);
        F = Math.toRadians(F);
        
        // Calcul de e et e2
        e = 1 - 0.002495*T - 0.00000752*T*T;
        e2 = e*e;
        
        // Calcul de Lambda, Beta et Pi
        l = Lp + 6.288750*Math.sin(Mp)
               + 1.274018*Math.sin(2*D-Mp)
               + 0.658309*Math.sin(2*D)
               + 0.213616*Math.sin(2*Mp)
               - e*0.185596*Math.sin(M)
               - 0.114336*Math.sin(2*F)
               + 0.058793*Math.sin(2*D-2*Mp)
               + e*0.057212*Math.sin(2*D-M-Mp)
               + 0.053320*Math.sin(2*D+Mp)
               + e*0.045874*Math.sin(2*D-M)
               + e*0.041024*Math.sin(Mp-M)
               - 0.034718*Math.sin(D)
               - e*0.030465*Math.sin(M+Mp)
               + 0.015326*Math.sin(2*D-2*F)
               - 0.012528*Math.sin(2*F+Mp)
               - 0.010980*Math.sin(2*F-Mp)
               + 0.010674*Math.sin(4*D-Mp)
               + 0.010034*Math.sin(3*Mp)
               + 0.008548*Math.sin(4*D-2*Mp)
               - e*0.007910*Math.sin(M-Mp+2*D)
               - e*0.006783*Math.sin(2*D+M)
               + 0.005162*Math.sin(Mp-D)
               + e*0.005000*Math.sin(M+D)
               + e*0.004049*Math.sin(Mp-M+2*D)
               + 0.003996*Math.sin(2*Mp+2*D)
               + 0.003862*Math.sin(4*D)
               + 0.003665*Math.sin(2*D-3*Mp)
               + e*0.002695*Math.sin(2*Mp-M)
               + 0.002602*Math.sin(Mp-2*F-2*D)
               + e*0.002396*Math.sin(2*D-M-2*Mp)
               - 0.002349*Math.sin(Mp+D)
               + e2*0.002249*Math.sin(2*D-2*M)
               - e*0.002125*Math.sin(2*Mp+M)
               - e2*0.002079*Math.sin(2*M)
               + e2*0.002059*Math.sin(2*D-Mp-2*M)
               - 0.001773*Math.sin(Mp+2*D-2*F)
               - 0.001595*Math.sin(2*F+2*D)
               + e*0.001220*Math.sin(4*D-M-Mp)
               - 0.001110*Math.sin(2*Mp+2*F)
               + 0.000892*Math.sin(Mp-3*D)
               - e*0.000811*Math.sin(M+Mp+2*D)
               + e*0.000761*Math.sin(4*D-M-2*Mp)
               + e2*0.000717*Math.sin(Mp-2*M)
               + e2*0.000704*Math.sin(Mp-2*M-2*D)
               + e*0.000693*Math.sin(M-2*Mp+2*D)
               + e*0.000598*Math.sin(2*D-M-2*F)
               + 0.000550*Math.sin(Mp+4*D)
               + 0.000538*Math.sin(4*Mp)
               + e*0.000521*Math.sin(4*D-M)
               + 0.000486*Math.sin(2*Mp-D);
        l = l%360;
        
        B = 5.128189*Math.sin(F)
               + 0.280606*Math.sin(Mp+F)
               + 0.277693*Math.sin(Mp-F)
               + 0.173238*Math.sin(2*D-F)
               + 0.055413*Math.sin(2*D+F-Mp)
               + 0.046272*Math.sin(2*D-F-Mp)
               + 0.032573*Math.sin(2*D+F)
               + 0.017198*Math.sin(2*Mp+F)
               + 0.009267*Math.sin(2*D+Mp-F)
               + 0.008823*Math.sin(2*Mp-F)
               + e*0.008247*Math.sin(2*D-M-F)
               + 0.004323*Math.sin(2*D-F-2*Mp)
               + 0.004200*Math.sin(2*D+F+Mp)
               + e*0.003372*Math.sin(F-M-2*D)
               + e*0.002472*Math.sin(2*D+F-M-Mp)
               + e*0.002222*Math.sin(2*D+F-M)
               + e*0.002072*Math.sin(2*D-F-M-Mp)
               + e*0.001877*Math.sin(F-M+Mp)
               + 0.001828*Math.sin(4*D-F-Mp)
               - e*0.001803*Math.sin(F+M)
               - 0.001750*Math.sin(3*F)
               + e*0.001570*Math.sin(Mp-M-F)
               - 0.001487*Math.sin(F+D)
               - e*0.001481*Math.sin(F+M+Mp)
               + e*0.001417*Math.sin(F-M-Mp)
               + e*0.001350*Math.sin(F-M)
               + 0.001330*Math.sin(F-D)
               + 0.001106*Math.sin(F+3*Mp)
               + 0.001020*Math.sin(4*D-F)
               + 0.000833*Math.sin(F+4*D-Mp)
               + 0.000781*Math.sin(Mp-3*F)
               + 0.000670*Math.sin(F+4*D-2*Mp)
               + 0.000606*Math.sin(2*D-3*F)
               + 0.000597*Math.sin(2*D+2*Mp-F)
               + e*0.000492*Math.sin(2*D+Mp-M-F)
               + 0.000450*Math.sin(2*Mp-F-2*D)
               + 0.000439*Math.sin(3*Mp-F)
               + 0.000423*Math.sin(F+2*D+2*Mp)
               + 0.000422*Math.sin(2*D-F-3*Mp)
               - e*0.000367*Math.sin(M+F+2*D-Mp)
               - e*0.000353*Math.sin(M+F+2*D)
               + 0.000331*Math.sin(F+4*D)
               + e*0.000317*Math.sin(2*D+F-M+Mp)
               + e2*0.000306*Math.sin(2*D-2*M-F)
               - 0.000283*Math.sin(Mp+3*F);

        pi = 0.950724
               + 0.051818*Math.cos(Mp)
               + 0.009531*Math.cos(2*D-Mp)
               + 0.007843*Math.cos(2*D)
               + 0.002824*Math.cos(2*Mp)
               + 0.000857*Math.cos(2*D+Mp)
               + e*0.000533*Math.cos(2*D-M)
               + e*0.000401*Math.cos(2*D-M-Mp)
               + e*0.000320*Math.cos(Mp-M)
               - 0.000271*Math.cos(D)
               - e*0.000264*Math.cos(M+Mp)
               - 0.000198*Math.cos(2*F-Mp)
               + 0.000173*Math.cos(3*Mp)
               + 0.000167*Math.cos(4*D-Mp)
               - e*0.000111*Math.cos(M)
               + 0.000103*Math.cos(4*D-2*Mp)
               - 0.000084*Math.cos(2*Mp-2*D)
               - e*0.000083*Math.cos(2*D+M)
               + 0.000079*Math.cos(2*D+2*Mp)
               + 0.000072*Math.cos(4*D)
               + e*0.000064*Math.cos(2*D-M+Mp)
               - e*0.000063*Math.cos(2*D+M-Mp)
               + e*0.000041*Math.cos(M+D)
               + e*0.000035*Math.cos(2*Mp-M)
               - 0.000033*Math.cos(3*Mp-2*D)
               - 0.000030*Math.cos(Mp+D)
               - 0.000029*Math.cos(2*F-2*D)
               - e*0.000029*Math.cos(2*Mp+M)
               + e2*0.000026*Math.cos(2*D-2*M)
               - 0.000023*Math.cos(2*F-2*D+Mp)
               + e*0.000019*Math.cos(4*D-M-Mp);
        
        dist = (float)(6378.14/Math.sin(Math.toRadians(pi)));
               
        
        o1 = 0.0004664*Math.cos(O);
        o2 = 0.0000754*Math.cos(O+Math.toRadians(275.05-2.30*T));
        b = B * (1-o1-o2);
        b = b%360;

        l = Math.toRadians(l);
        b = Math.toRadians(b);
        
        // Calcul of Alpha et Delta (7.3) and (7.4)
        double sinD, tanA,tanAp;
        double eps;
        eps = Math.toRadians(23.452994 - 0.0130125*T - 0.00000164*T*T + 0.000000503*T*T*T);
        sinD = Math.sin(b)*Math.cos(eps) + Math.cos(b)*Math.sin(eps)*Math.sin(l);
        tanAp = (Math.sin(l)*Math.cos(eps)-Math.tan(b)*Math.sin(eps));
        tanA = tanAp/Math.cos(l);
        alpha = MathFunctions.arctan(tanA, tanAp>=0?true:false);    
        delta = MathFunctions.arcsin(sinD);

        //System.out.println(MathFunctions.convert2hms(Math.toDegrees(alpha)/15, false) + "  "+MathFunctions.convert2deg(Math.toDegrees(delta), false));

        object.setAscendance((float)Math.toDegrees(alpha)/15);
        object.setDeclinaison((float)Math.toDegrees(delta));
        calculateHorizontalCoordinate(object.getAscendance(),object.getDeclinaison(),false);
    }
    /**
     * Return the phase of the moon
     * @return 0 (Moon.NEW): new moon, 1 (Moon.FIRST): First quarter, 2 (Moon.FULL): Full moon, 3 (Moon.LAST): last quarter
     */
    public short getPhase() {
        double k;
        short ret;
        double T = myPosition.getTemps().getT();
        
        k = (myPosition.getTemps().getJJ() -2415020.75933 - 0.0001178*T*T + 0.000000155*T*T - 0.00033*Math.sin(Math.toRadians(166.56+123.87*T-0.009173*T*T)))/29.53058868;
        k = k*4+0.5;
        ret = (short)k;
        return (short)(ret%4);
    }
}
