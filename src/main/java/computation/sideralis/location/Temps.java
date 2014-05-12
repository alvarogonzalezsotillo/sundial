package computation.sideralis.location;

import java.util.*;

/**
 * This class represents an object representing the Temps used in this midlet.
 * Once the object is created, it is possible to get the jour julien or the sideral hour.
 * @author Bernard
 */
public class Temps {    
    /** The jour julien */
    private double jourJulien;
    /** Sideral hour */
    private double sideralHour;
    /** The date */
    private Date myDate;
    /** The offset between current time and local time */
    private long offsetTime;

    /** 
     * Creates a new instance of Temps 
     */
    public Temps() {        
        myDate = new Date();
        offsetTime = 0;
    }

    /**
     * Return my date object
     * @return myDate object (Date object)
     */
    public Date getMyDate() {
        return myDate;
    }

    /** 
     * Return the Jour Julien 
     * @return The jour julien
     */
    public double getJJ() {
        return jourJulien;
    }
    /** 
     * Return the Greenwitch sideral hour
     * @return the sideral hour
     */
    public double getHS() {
        return sideralHour;
    }
    /**
     * Return the local sideral hour
     * @param pos the position of user
     * @return the local sideral hour
     */
    public double getLocalHS(Position pos) {
        double hs = sideralHour;
        hs += pos.getLongitude()/15;               // Local sidereal time
        hs = hs%24;
        if (hs<0)
            hs+=24;
        return hs;
    }
    /** 
     * Return the Temps des Ephemerides
     * T = (JJ-2415020)/36525
     * @return temps des ephemerides
     */
    public double getT() {
        double T;
        T = (jourJulien-2415020.0)/36525.0;
        return T;
    }
    /**
     * Return the calendar object associated with myDate
     * @return a calendar object linked to myDate (a Date object)
     */
    public Calendar getCalendar() {
        Calendar cal;

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(myDate);

        return cal;
    }
    /** 
     * Return the time offset
     * @return time offset
     */
    public long getTimeOffset() {
        return offsetTime;
    }
    /** 
     * set the new value of the time offset
     * @value the new time offset
     */
    public void setTimeOffset(long v) {
        offsetTime = v;
    }
    /**
     * Calculate the time offset between current time and requested time
     * @param d the requested time.
     */
    public void calculateTimeOffset(Date d) {
        myDate = new Date();
        long lDate = myDate.getTime();
        offsetTime = d.getTime()-lDate;
    }
    /**
     * Calculate date and time according to time offset and current date and time.
     */
    public void adjustDate() {
        myDate = new Date();
        long lDate = myDate.getTime();
        lDate += offsetTime ;                                                   // Add time offset from current date.
        myDate.setTime(lDate);
        //System.out.println(myDate);
    }
    /**
     * Calculates the Jour Julien
     * @return 
     */
    public double calculateJourJulien() {
        int A,B,C,D;
        int year,month;
        double hour;
        Calendar cal;

        cal = getCalendar();

        // Calcul du jour julien
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+12-Calendar.DECEMBER;                // returned values is starting from 0
        hour = cal.get(Calendar.HOUR_OF_DAY)+cal.get(Calendar.MINUTE)/60.0+cal.get(Calendar.SECOND)/3600.0;
        if (month<3) {
            month+=12;
            year-=1;
        }
        A = year/100;
        B = 2-A+A/4;
        C = (int)((float)365.25*(float)year);
        D = (int)((float)30.6001 * (float)(month+1));
        jourJulien = B + C + D + cal.get(Calendar.DAY_OF_MONTH) + hour/24 + 1720994.5;
        //System.out.println("JJ:"+jourJulien+ " y:"+year+" m:"+month+" h:"+hour + "  dayofmonth:" + cal.get(Calendar.DAY_OF_MONTH) );
        //System.out.println( "  A:" + A + "  B:" + B + "  C:" + C + "  D:" + D );
        
        return jourJulien;
    }
    /**
     * Calculates the sideral hour
     */
    public void calculateHS() {
        double T,HSH;
        double h;
        double m;
        double jj0;
        Calendar cal;

        cal = getCalendar();
        
        h = cal.get(Calendar.HOUR_OF_DAY);
        m = cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)/60.0;

        jj0 = (int)(jourJulien-0.5)+0.5;
        // Temps sideral a Greenwich pour 0h UT
        T = (jj0-2415020.0)/36525.0;
        HSH = 0.276919398+(0.000001075*T+100.0021359)*T;
        sideralHour = (HSH-(int)HSH)*24;
        
        // Temps sideral � Greenwich pour une heure donn�e
        sideralHour = (h+m/60.0)*1.002737908 + sideralHour;
        while (sideralHour>24.0)
            sideralHour -= 24.0;
    }
}
