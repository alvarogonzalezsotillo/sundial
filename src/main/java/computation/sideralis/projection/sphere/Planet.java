/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package computation.sideralis.projection.sphere;

import java.util.Calendar;
import java.util.Date;

import computation.sideralis.location.Temps;
import computation.sideralis.math.MathFunctions;

/**
 *
 * @author Bernard
 */
public class Planet {
    private double a = 5.20260;
    private double e = 0.04849;
    private double i = 1.303;
    private double smallO = 273.867;
    private double largeO = 100.464;
    private double M0 = 20.020;
//    private double n = 0.083056;

    private Temps myTemps;
    private double jj;


//    private double a = 0.38710;
//    private double e = 0.20563;
//    private double i = 7.005;
//    private double smallO = 29.125;
//    private double largeO = 48.331;
//    private double M0 = 174.795;
//    private double n = 4.092317;

    public Planet() {
        myTemps = new Temps();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        Date d = cal.getTime();
        myTemps.calculateTimeOffset(d);
        myTemps.adjustDate();
        myTemps.calculateJourJulien();
        jj =  myTemps.getJJ();

    }

    public void calculate() {
        // =====================================
        // === 1- Calculate the mean anomaly ===
        double n;
        double M;
        n = 0.9856076686 / (a * Math.sqrt(a));
//        M = M0 + n * 1460.5;
        M = M0 + n * (jj - 2451545);
        M=Math.toRadians(M);
        System.out.println(" n="+n+" M="+Math.toDegrees(M)+" (d-d0)="+(jj - 2451545));

        // =====================================
        // === 2- Calculate the true anomaly ===
        double nu;
        double E;
        double tmp;

//        E = M;
//        for (int k=0;k<10;k++) {
//            E = M + e*Math.sin(E);
//        }
//        System.out.println(" E="+E);

        E = M;
        for (int k=0;k<5;k++) {
            E = E+(M+e*Math.sin(E)-E)/(1-e*Math.cos(E));
        }
        System.out.println(" E="+E);


        tmp = Math.sqrt((1+e)/(1-e)) * Math.tan(E/2);
        System.out.println(" tmp="+tmp);
        nu = 2 * MathFunctions.arctan(tmp, true);

        System.out.println(" nu="+Math.toDegrees(nu));

        // ==============================================
        // === 3- Calculate the distance r to the Sun ===
        double r;
        
        r = a * (1-e*e);
        tmp = (1 + e * Math.cos(nu));
        r = r / tmp;
        
        System.out.println(" r="+r);

        // ======================================================================
        // === 4- Calculate the rectangular heliocentric ecliptic coordinates ===

        double x,y,z;
        largeO = Math.toRadians(largeO);
        smallO = Math.toRadians(smallO);
        i= Math.toRadians(i);
        x = r*(Math.cos(largeO) * Math.cos(smallO+nu) - Math.sin(largeO)*Math.cos(i)*Math.sin(smallO+nu));
        y = r*(Math.sin(largeO) * Math.cos(smallO+nu) + Math.cos(largeO)*Math.cos(i)*Math.sin(smallO+nu));
        z = r*Math.sin(i)*Math.sin(smallO+nu);

        System.out.println(" x="+x+" y="+y+" z="+z);


    }

    public void calculat2() {
        double F,G,H,P,Q,R;
        double eps;

        eps = 23.4457889;

        F = Math.cos(largeO);
        G = Math.sin(largeO)*Math.cos(eps);
        H = Math.sin(largeO)*Math.sin(eps);
        P = -Math.sin(largeO)*Math.cos(i);
        Q = Math.cos(largeO)*Math.cos(i)*Math.cos(eps)-Math.sin(i)*Math.sin(eps);
        R = Math.cos(largeO)*Math.cos(i)*Math.sin(eps)+Math.sin(i)*Math.cos(eps);

        double A,B,C;
        double a,b,c;

        a = Math.sqrt(F*F + P*P);
        b = Math.sqrt(G*G + Q*Q);
        c = Math.sqrt(H*H + R*R);
        A = MathFunctions.arctan(F/P, F>=0?true:false);
        B = MathFunctions.arctan(G/Q, G>=0?true:false);
        C = MathFunctions.arctan(H/R, H>=0?true:false);


    }
}
