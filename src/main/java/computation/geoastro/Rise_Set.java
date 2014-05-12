package computation.geoastro;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   sundata148.java

import java.util.Date;

class Rise_Set
{

    public Rise_Set(Date myDat, double myLat, double myLong)
    {
        dat = myDat;
        date = dat.getDate();
        month = dat.getMonth();
        year = dat.getYear();
        hours = dat.getHours();
        minutes = dat.getMinutes();
        seconds = dat.getSeconds();
        currentTime = (double)hours + (double)minutes / 60D + (double)seconds / 3600D;
        latitude = myLat;
        longitude = myLong;
        locOffset = -dat.getTimezoneOffset() / 60;
        RISE = false;
        SETT = false;
        for(int i = -locOffset; i < -locOffset + 24; i++)
        {
            riseset(date, month + 1, year + 1900, i);
            if(RISE && SETT)
                break;
        }

        if(RISE || SETT)
        {
            if(RISE)
            {
                UTRISE = UTRISE + (double)locOffset;
                if(UTRISE > 24D)
                    UTRISE = UTRISE - 24D;
                if(UTRISE < 0.0D)
                    UTRISE = UTRISE + 24D;
            }
            if(SETT)
            {
                UTSET = UTSET + (double)locOffset;
                if(UTSET > 24D)
                    UTSET = UTSET - 24D;
                if(UTSET < 0.0D)
                    UTSET = UTSET + 24D;
            }
            if(RISE)
                hRise = UTRISE;
            if(SETT)
                hSet = UTSET;
            if(RISE)
                riseStr = makeTimeString("", hRise);
            else
            if(ABOVE)
            {
                riseStr = "Visible";
                setStr = "all day";
            } else
            {
                riseStr = "Invisible";
                setStr = "all day";
            }
            if(SETT)
                setStr = makeTimeString("", hSet);
            else
            if(ABOVE)
            {
                riseStr = "Visible";
                setStr = "all day";
            } else
            {
                riseStr = "Invisible";
                setStr = "all day";
            }
        } else
        if(ABOVE)
        {
            riseStr = "Visible";
            setStr = "all day";
        } else
        {
            riseStr = "Invisible";
            setStr = "all day";
        }
    }

    String rise_String()
    {
        return riseStr;
    }

    String set_String()
    {
        return setStr;
    }

    public void riseset(int date, int month, int year, double HOUR)
    {
        double K = 0.017453292519943295D;
        double sh = Math.sin(-0.014543828656868749D);
        double JD = Jul_Date(date, month, year, HOUR);
        double dec = sunDecRA(1, JD);
        double ra = sunDecRA(2, JD);
        Y0 = sin_elev(JD, latitude, -longitude, dec, ra) - sh;
        double jdPlus = Jul_Date(date, month, year, HOUR + 1.0D);
        dec = sunDecRA(1, jdPlus);
        ra = sunDecRA(2, jdPlus);
        yPlus = sin_elev(jdPlus, latitude, -longitude, dec, ra) - sh;
        double jdMinus = Jul_Date(date, month, year, HOUR - 1.0D);
        dec = sunDecRA(1, jdMinus);
        ra = sunDecRA(2, jdMinus);
        yMinus = sin_elev(jdMinus, latitude, -longitude, dec, ra) - sh;
        ABOVE = yMinus > 0.0D;
        QUAD();
        switch(NZ)
        {
        case 0: // '\0'
        default:
            break;

        case 1: // '\001'
            if(yMinus < 0.0D)
            {
                UTRISE = HOUR + zero1;
                RISE = true;
            } else
            {
                UTSET = HOUR + zero1;
                SETT = true;
            }
            break;

        case 2: // '\002'
            if(YE < 0.0D)
            {
                UTRISE = HOUR + zero2;
                UTSET = HOUR + zero1;
            } else
            {
                UTRISE = HOUR + zero1;
                UTSET = HOUR + zero2;
            }
            RISE = true;
            SETT = true;
            break;
        }
    }

    public void QUAD()
    {
        NZ = 0;
        double A = 0.5D * (yMinus + yPlus) - Y0;
        double B = 0.5D * (yPlus - yMinus);
        double C = Y0;
        double XE = -B / (2D * A);
        YE = (A * XE + B) * XE + C;
        double DIS = B * B - 4D * A * C;
        if(DIS >= 0.0D)
        {
            DX = (0.5D * Math.sqrt(DIS)) / Math.abs(A);
            zero1 = XE - DX;
            zero2 = XE + DX;
            if(Math.abs(zero1) <= 1.0D)
                NZ = NZ + 1;
            if(Math.abs(zero2) <= 1.0D)
                NZ = NZ + 1;
            if(zero1 < -1D)
                zero1 = zero2;
        }
    }

    double sunDecRA(int what, double JD)
    {
        double PI2 = 6.2831853071795862D;
        double cos_eps = 0.91748200000000002D;
        double sin_eps = 0.39777800000000002D;
        double T = (JD - 2451545D) / 36525D;
        double M = 6.2831853071795862D * frac(0.99313300000000004D + 99.997360999999998D * T);
        double DL = 6893D * Math.sin(M) + 72D * Math.sin(2D * M);
        double L = 6.2831853071795862D * frac(0.78594529999999996D + M / 6.2831853071795862D + (6191.1999999999998D * T + DL) / 1296000D);
        double SL = Math.sin(L);
        double X = Math.cos(L);
        double Y = 0.91748200000000002D * SL;
        double Z = 0.39777800000000002D * SL;
        double R = Math.sqrt(1.0D - Z * Z);
        double DEC = 57.295779513082323D * Math.atan(Z / R);
        double RA = 7.6394372684109761D * Math.atan(Y / (X + R));
        if(RA < 0.0D)
            RA += 24D;
        if(what == 1)
            return DEC;
        else
            return RA;
    }

    double sin_elev(double JD, double LAT, double LONG, double DEC, double RA)
    {
        double K = 0.017453292519943295D;
        double tau = 15D * (LM_Sidereal_Time(JD, LONG) - RA);
        if(tau < 0.0D)
            tau += 360D;
        return Math.cos(0.017453292519943295D * LAT) * Math.cos(0.017453292519943295D * DEC) * Math.cos(0.017453292519943295D * tau) + Math.sin(0.017453292519943295D * LAT) * Math.sin(0.017453292519943295D * DEC);
    }

    public String makeTimeString(String whatStr, double time)
    {
        String str = "?";
        if(time < 0.0D)
            time += 24D;
        if(time > 24D)
            time -= 24D;
        double diff = time - (double)(int)time;
        int min = (int)Math.round(diff * 60D);
        if(min == 60)
        {
            min = 0;
            time++;
        }
        if(min > 9)
            str = ":";
        else
            str = ":0";
        str = String.valueOf((int)time + str + min);
        if(time < 10D)
            str = "0" + str;
        return whatStr + "  " + str;
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
        return MJD + 2400000.5D;
    }

    double frac(double x)
    {
        x -= (int)x;
        if(x < 0.0D)
            x++;
        return x;
    }

    double LM_Sidereal_Time(double JD, double LONG)
    {
        double GMST = GM_Sidereal_Time(JD);
        return 24D * frac((GMST - LONG / 15D) / 24D);
    }

    double GM_Sidereal_Time(double JD)
    {
        double MJD = JD - 2400000.5D;
        double MJD0 = (long)MJD;
        double ut = (MJD - (double)(long)MJD0) * 24D;
        double t_eph = (MJD0 - 51544.5D) / 36525D;
        return 6.6973745579999999D + 1.0027379093D * ut + ((8640184.8128660005D + (0.093104000000000006D - 6.1999999999999999E-006D * t_eph) * t_eph) * t_eph) / 3600D;
    }

    Date dat;
    int date;
    int month;
    int year;
    double latitude;
    double longitude;
    double Y0;
    double yPlus;
    double yMinus;
    double YE;
    double DX;
    int NZ;
    boolean RISE;
    boolean SETT;
    boolean ABOVE;
    double UTRISE;
    double UTSET;
    double hRise;
    double hSet;
    int locOffset;
    String riseStr;
    String setStr;
    double zero1;
    double zero2;
    double currentTime;
    int hours;
    int minutes;
    int seconds;
    
    public static void main(String[] args) {
		Rise_Set rs = new Rise_Set( new Date(), 42, 3 );
		System.out.println( rs.rise_String()+ "  " + rs.set_String() );
	}
}
