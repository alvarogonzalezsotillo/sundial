package computation.geoastro;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   sundata148.java

import java.awt.*;
import java.util.Date;

import computation.LatLon;
import computation.geom.AzimutAltitude;

public class compute extends Canvas
{

	public double computeJulianDate(Date dat, LatLon latLon){
        int hours = dat.getHours();
        int minutes = dat.getMinutes();
        int seconds = dat.getSeconds();
        int date = dat.getDate();
        int month = dat.getMonth();
        month++;
        int year = dat.getYear();
        year += 1900;
        offset = -dat.getTimezoneOffset() / 60;

        double UT = (double)(hours - offset) + (double)minutes / 60D + (double)seconds / 3600D;
        double JD = Jul_Date(date, month, year, UT);
        return JD;
	}
	
	public AzimutAltitude myCompute(Date dat, LatLon latLon ){
        int hours = dat.getHours();
        int minutes = dat.getMinutes();
        int seconds = dat.getSeconds();
        int date = dat.getDate();
        int month = dat.getMonth();
        month++;
        int year = dat.getYear();
        year += 1900;
        offset = -dat.getTimezoneOffset() / 60;

        double UT = (double)(hours - offset) + (double)minutes / 60D + (double)seconds / 3600D;
        double JD = Jul_Date(date, month, year, UT);

        double latitude = latLon.getLat();
        double longitude = latLon.getLon();

        
        double T = (JD - 2451545D) / 36525D;
        RA = RightAscension(T) / 15D;
        double GHA = sun_GHA(date, month, year, UT);
        GHA %= 360D;
        double eqt = eot(date, month, year, UT); 
        double H = sun_elev(JD, latitude, longitude, declin, RA);
        double AZ = Azimut(declin, latitude, -longitude, GHA, H);

        return new AzimutAltitude(AZ,H);
	}
	
	public compute(){
	}
	
    compute(Date dat, double LAT, double LONG, boolean myDemo, int myFont)
    {
        str = new String[30];
        deg = '\260';
        demo = false;
        myFontColor = new Color(myFont, myFont, myFont);
        double latitude = LAT;
        double longitude = LONG;
        int hours = dat.getHours();
        int minutes = dat.getMinutes();
        int seconds = dat.getSeconds();
        int date = dat.getDate();
        int month = dat.getMonth();
        month++;
        int year = dat.getYear();
        year += 1900;
        offset = -dat.getTimezoneOffset() / 60;
        rs = new Rise_Set(dat, latitude, -longitude);
        str[17] = "Sunrise    " + rs.rise_String();
        str[18] = "Sunset     " + rs.set_String();
        double UT = (double)(hours - offset) + (double)minutes / 60D + (double)seconds / 3600D;
        int utHours = hours - offset;
        if(utHours < 0)
            utHours += 24;
        if(utHours > 24)
            utHours -= 24;
        double JD = Jul_Date(date, month, year, UT);
        double T = (JD - 2451545D) / 36525D;
        RA = RightAscension(T) / 15D;
        double GHA = sun_GHA(date, month, year, UT);
        GHA %= 360D;
        double eqt = eot(date, month, year, UT);
        double H = sun_elev(JD, latitude, longitude, declin, RA);
        double AZ = Azimut(declin, latitude, -longitude, GHA, H);
        double distance = radius(JD);
        double GMST = GM_sidereal_Time(JD);
        double LMST = 24D * frac((GMST - LONG / 15D) / 24D);
        int hour_LMST = (int)LMST;
        double minutes_LMST = (LMST - (double)hour_LMST) * 60D;
        int min_LMST = (int)minutes_LMST;
        int seconds_LMST = (int)Math.round((minutes_LMST - (double)min_LMST) * 60D);
        String s;
        if(hour_LMST > 9)
            s = "";
        else
            s = "0";
        String LMST_Str = "Local Sidereal Time   " + s + hour_LMST;
        if(min_LMST > 9)
            s = "";
        else
            s = "0";
        LMST_Str = LMST_Str + ":" + s + min_LMST + ":";
        if(seconds_LMST > 9)
            s = "";
        else
            s = "0";
        LMST_Str = LMST_Str + s + seconds_LMST;
        s = String.valueOf(Math.abs(latitude));
        if(latitude > 0.0D)
            s = s + deg + " N";
        else
            s = s + deg + " S";
        str[4] = "Latitude   " + s;
        s = String.valueOf(Math.abs(longitude));
        if(longitude > 0.0D)
            s = s + deg + " W";
        else
            s = s + deg + " E";
        str[5] = "Longitude  " + s;
        str[7] = dat.toString();
        if(-dat.getTimezoneOffset() / 60 >= 0)
            str[6] = "Time Zone  UT + " + Math.abs(dat.getTimezoneOffset() / 60) + " h";
        else
            str[6] = "Time Zone   UT - " + Math.abs(dat.getTimezoneOffset() / 60) + " h";
        if(utHours > 9)
            s = "";
        else
            s = "0";
        str[8] = "UT         " + s + utHours;
        if(minutes > 9)
            s = ":";
        else
            s = ":0";
        str[8] = str[8] + s + minutes;
        if(seconds > 9)
            s = ":";
        else
            s = ":0";
        str[8] = str[8] + s + seconds;
        s = String.valueOf((double)Math.round(100000D * JD) / 100000D);
        str[9] = "Jul. Day   " + s;
        str[10] = LMST_Str;
        s = String.valueOf((double)Math.round(10D * GHA) / 10D);
        str[11] = "Greenwich Hour Angle  " + s + deg;
        double diff = Math.abs(eqt) - (double)(int)Math.abs(eqt);
        long min = (int)Math.round(diff * 60D);
        if(min == 60L)
        {
            min = 0L;
            if(eqt >= 0.0D)
                eqt++;
            else
                eqt--;
        }
        if(min > 9L)
            s = ":";
        else
            s = ":0";
        String eqtStr = (int)eqt + s + min + " min";
        if(eqt < 0.0D && (int)eqt == 0)
            eqtStr = "-" + (int)eqt + s + min + " min";
        str[12] = "Equation of Time      " + eqtStr;
        s = String.valueOf((double)Math.round(10D * H) / 10D);
        str[13] = "Elevation    " + s + deg;
        s = String.valueOf((double)Math.round(100D * Math.abs(declin)) / 100D);
        if(declin > 0.0D)
            s = s + deg + " N";
        else
            s = s + deg + " S";
        s = s + " = " + (int)Math.abs(declin) + deg + " " + (int)Math.round(60D * frac(Math.abs(declin))) + "'";
        if(declin > 0.0D)
            s = s + " N";
        else
            s = s + " S";
        str[14] = "Declination  " + s;
        s = String.valueOf((double)Math.round(10D * AZ) / 10D);
        str[15] = "Azimuth      " + s + deg;
        s = String.valueOf((double)Math.round(100000D * distance) / 100000D);
        str[16] = "Distance     " + s + " AU";
        repaint();
    }

    public double sunL(double T)
    {
        double tau = T / 10D;
        double L = ((280.46645669999998D + 360007.6982779D * tau + 0.030320280000000002D * tau * tau + (tau * tau * tau) / 49931D) - (tau * tau * tau * tau) / 15299D) + (tau * tau * tau * tau * tau) / 1988000D;
        L %= 360D;
        if(L < 0.0D)
            L += 360D;
        return L;
    }

    public double deltaPSI(double T)
    {
        double K = 0.017453292519943295D;
        double LS = sunL(T);
        double LM = 218.31649999999999D + 481267.88130000001D * T;
        LM %= 360D;
        if(LM < 0.0D)
            LM += 360D;
        double omega = (125.04452000000001D - 1934.1362610000001D * T) + 0.0020707999999999998D * T * T + (T * T * T) / 450000D;
        double deltaPsi = (-17.199999999999999D * Math.sin(0.017453292519943295D * omega) - 1.3200000000000001D * Math.sin(0.034906585039886591D * LS) - 0.23000000000000001D * Math.sin(0.034906585039886591D * LM)) + 0.20999999999999999D * Math.sin(0.034906585039886591D * omega);
        deltaPsi /= 3600D;
        return deltaPsi;
    }

    public double EPS(double T)
    {
        double K = 0.017453292519943295D;
        double LS = sunL(T);
        double LM = 218.31649999999999D + 481267.88130000001D * T;
        double eps0 = 23.43929111111111D - ((46.814999999999998D * T + 0.00059000000000000003D * T * T) - 0.0018129999999999999D * T * T * T) / 3600D;
        double omega = (125.04452000000001D - 1934.1362610000001D * T) + 0.0020707999999999998D * T * T + (T * T * T) / 450000D;
        double deltaEps = ((9.1999999999999993D * Math.cos(0.017453292519943295D * omega) + 0.56999999999999995D * Math.cos(0.034906585039886591D * LS) + 0.10000000000000001D * Math.cos(0.034906585039886591D * LM)) - 0.089999999999999997D * Math.cos(0.034906585039886591D * omega)) / 3600D;
        return eps0 + deltaEps;
    }

    public double eot(int date, int month, int year, double UT)
    {
        double K = 0.017453292519943295D;
        double T = (Jul_Date(date, month, year, UT) - 2451545D) / 36525D;
        double eps = EPS(T);
        double RA = RightAscension(T);
        double LS = sunL(T);
        double deltaPsi = deltaPSI(T);
        double E = (LS - 0.0057182999999999999D - RA) + deltaPsi * Math.cos(0.017453292519943295D * eps);
        if(E > 5D)
            E -= 360D;
        E *= 4D;
        return E;
    }

    public double RightAscension(double T)
    {
        double K = 0.017453292519943295D;
        double L = sunL(T);
        double M = (357.52910000000003D + 35999.050300000003D * T) - 0.00015589999999999999D * T * T - 4.7999999999999996E-007D * T * T * T;
        M %= 360D;
        if(M < 0.0D)
            M += 360D;
        double C = (1.9146000000000001D - 0.0048170000000000001D * T - 1.4E-005D * T * T) * Math.sin(0.017453292519943295D * M);
        C += (0.019993D - 0.000101D * T) * Math.sin(0.034906585039886591D * M);
        C += 0.00029D * Math.sin(0.05235987755982989D * M);
        double theta = L + C;
        double eps = EPS(T);
        eps += 0.0025600000000000002D * Math.cos(0.017453292519943295D * (125.04000000000001D - 1934.136D * T));
        double lambda = theta - 0.0056899999999999997D - 0.0047800000000000004D * Math.sin(0.017453292519943295D * (125.04000000000001D - 1934.136D * T));
        double RA = Math.atan2(Math.cos(0.017453292519943295D * eps) * Math.sin(0.017453292519943295D * lambda), Math.cos(0.017453292519943295D * lambda));
        RA /= 0.017453292519943295D;
        if(RA < 0.0D)
            RA += 360D;
        double delta = Math.asin(Math.sin(0.017453292519943295D * eps) * Math.sin(0.017453292519943295D * lambda));
        delta /= 0.017453292519943295D;
        DEC = delta;
        declin = delta;
        return RA;
    }

    double Jul_Date(int date, int month, int year, double ut)
    {
        double A = 10000D * (double)year + 100D * (double)month + (double)date;
        if(month <= 2)
        {
            month += 12;
            year--;
        }
        double B;
        if(A <= 15821004.1D)
            B = (-2 + (year + 4716) / 4) - 1179;
        else
            B = (year / 400 - year / 100) + year / 4;
        A = 365D * (double)year - 679004D;
        double MJD = A + B + (double)(int)(30.600100000000001D * (double)(month + 1)) + (double)date + ut / 24D;
        double JD = MJD + 2400000.5D;
        
        //System.out.println( "JD:" + JD + "  ut:" + ut + "  date:" + date + "  month:" + month + "  year:" + year );
        //System.out.println( "  A:" + A + "  B:" + B + "  MJD:" + MJD );
        
        return JD;
    }

    double GM_sidereal_Time(double JD)
    {
        double MJD = JD - 2400000.5D;
        double MJD0 = (long)MJD;
        double ut = (MJD - (double)(long)MJD0) * 24D;
        double t_eph = (MJD0 - 51544.5D) / 36525D;
        return 6.6973745579999999D + 1.0027379093D * ut + ((8640184.8128660005D + (0.093104000000000006D - 6.1999999999999999E-006D * t_eph) * t_eph) * t_eph) / 3600D;
    }

    double frac(double x)
    {
        x -= (int)x;
        if(x < 0.0D)
            x++;
        return x;
    }

    void sun(double JD)
    {
        double PI2 = 6.2831853071795862D;
        double cos_eps = 0.91748206200000004D;
        double sin_eps = 0.39777715600000002D;
        double T = (JD - 2451545D) / 36525D;
        double M = 6.2831853071795862D * frac(0.99313300000000004D + 99.997360999999998D * T);
        double DL = 6893D * Math.sin(M) + 72D * Math.sin(2D * M);
        double L = 6.2831853071795862D * frac(0.78594529999999996D + M / 6.2831853071795862D + (6191.1999999999998D * T + DL) / 1296000D);
        double SL = Math.sin(L);
        double X = Math.cos(L);
        double Y = 0.91748206200000004D * SL;
        double Z = 0.39777715600000002D * SL;
        double R = Math.sqrt(1.0D - Z * Z);
        DEC = 57.295779513082323D * Math.atan(Z / R);
        RA = 7.6394372684109761D * Math.atan(Y / (X + R));
        if(RA < 0.0D)
            RA = RA + 24D;
    }

    double LM_sidereal_Time(double JD, double LONG)
    {
        double GMST = GM_sidereal_Time(JD);
        return 24D * frac((GMST - LONG / 15D) / 24D);
    }

    double sun_GHA(int date, int month, int year, double ut)
    {
        double JD = Jul_Date(date, month, year, ut);
        double GMST = GM_sidereal_Time(JD);
        double tau = 15D * (GMST - RA);
        if(tau < 0.0D)
            tau += 360D;
        if(tau < 0.0D)
            tau += 360D;
        return tau;
    }

    double sun_elev(double JD, double LAT, double LONG, double DEC, double RA)
    {
        double K = 0.017453292519943295D;
        double tau = 15D * (LM_sidereal_Time(JD, LONG) - RA);
        if(tau < 0.0D)
            tau += 360D;
        double sinH = Math.cos(0.017453292519943295D * LAT) * Math.cos(0.017453292519943295D * DEC) * Math.cos(0.017453292519943295D * tau) + Math.sin(0.017453292519943295D * LAT) * Math.sin(0.017453292519943295D * DEC);
        return Math.asin(sinH) / 0.017453292519943295D;
    }

    double radius(double JD)
    {
        double K = 0.017453292519943295D;
        double T = (JD - 2415020D) / 36525D;
        double G = 358.47583329999998D + 35999D * T + (179.09999999999999D * T - 0.54000000000000004D * T * T) / 3600D;
        G = 0.017453292519943295D * G;
        double R = ((3.057E-005D - 1.4999999999999999E-007D * T) + (-0.0072741200000000002D + 1.8139999999999999E-005D * T) * Math.cos(G) + (-9.1379999999999996E-005D + 4.5999999999999999E-007D * T) * Math.cos(2D * G)) - 1.4500000000000001E-006D * Math.cos(3D * G);
        return Math.exp(R * Math.log(10D));
    }

    public double Azimut(double dec, double latitude, double longitude, double GHA, double hoehe)
    {
        double K = 0.017453292519943295D;
        double lat_K = latitude * 0.017453292519943295D;
        double hoehe_K = hoehe * 0.017453292519943295D;
        double cosAz = (Math.sin(dec * 0.017453292519943295D) - Math.sin(lat_K) * Math.sin(hoehe_K)) / (Math.cos(hoehe_K) * Math.cos(lat_K));
        double Az = 1.5707963267948966D - Math.asin(cosAz);
        Az /= 0.017453292519943295D;
        if(Math.sin(0.017453292519943295D * (GHA + longitude)) <= 0.0D)
            Az = Az;
        else
            Az = 360D - Az;
        return Az;
    }

    public void paint(Graphics g)
    {
        int r = (int)Math.floor(17D * Math.random());
        g.setColor(Color.black);
        for(int i = 4; i < 17; i++)
            if(!demo)
                g.drawString(str[i], 30, i * 18);
            else
            if(i == r)
                g.drawString("DEMO", 30, i * 18);
            else
                g.drawString(str[i], 30, i * 18);

        for(int i = 17; i < 19; i++)
            g.drawString(str[i], 30, i * 18);

        g.setColor(Color.red);
        g.setFont(new Font("Courier", 0, 10));
        g.drawString("\251 Juergen Giesen 1998-2009", 30, 350);
        g.drawString("http://www.GeoAstro.de", 30, 366);
    }

    double DEC;
    double RA;
    int offset;
    String str[];
    double declin;
    Rise_Set rs;
    char deg;
    boolean demo;
    Color myFontColor;
}
