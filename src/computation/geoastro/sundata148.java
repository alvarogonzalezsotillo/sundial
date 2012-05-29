package computation.geoastro;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   sundata148.java

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.util.Date;


public class sundata148 extends Applet
    implements Runnable
{

    public void init()
    {
        URL url = getDocumentBase();
        myStr = url.toString();
        myStr = myStr + "1234567890123456789012345";
        wwwStr = myStr.substring(0, 27);
        dat = new Date();
        latStr = getParameter("latitude");
        longStr = getParameter("longitude");
        if( latStr == null ){
        	latStr = "40";
        }
        if( longStr == null ){
        	longStr = "3";
        }
        latDouble = Double.valueOf(latStr);
        latitude = latDouble.doubleValue();
        longDouble = Double.valueOf(longStr);
        longitude = longDouble.doubleValue();
        String bgColRed = getParameter("bgRed");
        String bgColGreen = getParameter("bgGreen");
        String bgColBlue = getParameter("bgBlue");
        String fontCol = getParameter("fontGray");
        int bgRed = 128;//Integer.parseInt(bgColRed);
        int bgGreen = 128;//Integer.parseInt(bgColGreen);
        int bgBlue = 128;//Integer.parseInt(bgColBlue);
        fontGray = 128;//Integer.parseInt(fontCol);
        Color myBG = new Color(bgRed, bgGreen, bgBlue);
        setBackground(myBG);
        comp = new compute(dat, latitude, longitude, demo, fontGray);
        latLabel = new Label("Lat. =");
        add(latLabel);
        latField = new TextField(latStr, 6);
        add(latField);
        longLabel = new Label(" Long.=");
        add(longLabel);
        longField = new TextField(longStr, 6);
        add(longField);
        boolean ok = true;
        email = getParameter("email");
        param = getParameter("password");
        usrStr = email;
        userString = email + "  " + dat.toString();
        if(formula(wwwStr, 22) == formula("http://www.GeoAstro.de", 22) || formula(wwwStr, 22) == formula("http://www.geoastro.de", 22) || formula(wwwStr, 21) == formula("http://www.jgiesen.de", 21) || formula(wwwStr, 21) == formula("http://www.j-giesen.de", 21) || formula(wwwStr, 23) == formula("http://www.astropolaris", 23))
        {
            ok = true;
            online = true;
            demo = false;
        } else
        {
            ok = false;
        }
        if(!ok)
        {
            ok = true;/*
            if(email.length() == 0 || Integer.parseInt(param) != formula(email, email.length()))
            {
                ok = false;
            } else
            {
                ok = true;
                demo = false;
            }
            if(wwwStr.substring(0, 7).equals("http://"))
            {
                ok = false;
                demo = true;
            }
            */
            demo = true;
        }
        if(demo)
            versStr = versStr + "   D E M O";
        repaint();
    }

    public double getLatitude(String str)
    {
        if(!str.equals("-"))
        {
            for(int i = 0; i < str.length(); i++)
            {
                c = str.charAt(i);
                if(c == ',')
                    str = str.substring(0, i) + '.' + str.substring(i + 1, str.length());
            }

            for(int i = 0; i < str.length(); i++)
            {
                c = str.charAt(i);
                if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '.' || c == '-')
                    try
                    {
                        latDouble = Double.valueOf(str);
                        latitude = latDouble.doubleValue();
                        if(latitude > 90D)
                            latitude = 90D;
                        if(latitude < -90D)
                            latitude = -90D;
                    }
                    catch(NumberFormatException _ex) { }
            }

        }
        return latitude;
    }

    public double getLongitude(String str)
    {
        if(!str.equals("-"))
        {
            for(int i = 0; i < str.length(); i++)
            {
                c = str.charAt(i);
                if(c == ',')
                    str = str.substring(0, i) + '.' + str.substring(i + 1, str.length());
            }

            for(int i = 0; i < str.length(); i++)
            {
                c = str.charAt(i);
                if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '.' || c == '-')
                    try
                    {
                        longDouble = Double.valueOf(str);
                        longitude = longDouble.doubleValue();
                        if(longitude > 180D)
                            longitude = 180D;
                        if(longitude < -180D)
                            longitude = -180D;
                    }
                    catch(NumberFormatException _ex) { }
            }

        }
        return longitude;
    }

    public void start()
    {
        myThread = new Thread(this);
        myThread.start();
    }

    public void stop()
    {
        myThread.stop();
    }

    public void run()
    {
        do
        {
            dat = new Date();
            latStr = latField.getText();
            latitude = getLatitude(latStr);
            longStr = longField.getText();
            longitude = getLongitude(longStr);
            comp = new compute(dat, latitude, longitude, demo, fontGray);
            repaint();
            try
            {
                Thread.sleep(1000L);
            }
            catch(InterruptedException _ex) { }
        } while(true);
    }

    public int formula(String str, int len)
    {
        long num = 0L;
        for(int i = 0; i < len; i++)
        {
            char c = str.charAt(i);
            long n = i * Character.digit(c, i) * Character.digit(c, 36 - i);
            n = Character.digit(c, 36 - i);
            num += n * n;
        }

        return 801 + (int)num;
    }

    public void paint(Graphics g)
    {
        g.setFont(new Font("Courier", 0, 12));
        g.setColor(Color.red);
        g.drawString(versStr, 30, 50);
        g.setColor(Color.red);
        g.drawRect(1, 1, size().width - 2, size().height - 2);
        g.setColor(Color.black);
        comp.update(g);
    }

    public sundata148()
    {
        versStr = "Sun Data 1.48";
        demo = true;
        online = false;
        deg = '\260';
    }

    String versStr;
    Date dat;
    compute comp;
    Thread myThread;
    TextField latField;
    TextField longField;
    double latitude;
    double longitude;
    String latStr;
    String longStr;
    Double latDouble;
    Double longDouble;
    Label latLabel;
    Label longLabel;
    String bgCol;
    boolean demo;
    boolean online;
    public String myStr;
    public String email;
    public String param;
    public String wwwStr;
    public String usrStr;
    public String userString;
    char deg;
    char c;
    int fontGray;
}
