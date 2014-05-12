package computation.sideralis.location;

import computation.sideralis.LocalizationSupport;
import computation.sideralis.object.CityObject;


/**
 * A class used to define the position of the user and the time.
 *
 * @author Bernard
 */
public class Position {

    /** Longitude expressed in d m s */
    private double longi;
    /** Latitude expressed in d m s */
    private double lat;
    /** Altitude of the user expressed in meter */
    private short alt;
    /** The time */
    private final Temps myTime;
    /** The city selected */
    private short cityIndex;
    /** CityList */
    public static final CityObject[] cities = {new CityObject(LocalizationSupport.getMessage("CITY_ADELAIDE"), -34.933, 138.600),
        new CityObject(LocalizationSupport.getMessage("CITY_AIX"), 43.531, 5.423),
        new CityObject(LocalizationSupport.getMessage("CITY_ALES"), 44.133, 4.083),
        new CityObject(LocalizationSupport.getMessage("CITY_AMSTERDAM"), 52.35, 4.917),
        new CityObject(LocalizationSupport.getMessage("CITY_ANNECY"), 45.9, 6.117),
        new CityObject(LocalizationSupport.getMessage("CITY_ATHENS"), 37.983, 23.733),
        new CityObject(LocalizationSupport.getMessage("CITY_BAGHDAD"), 33.339, 44.394),
        new CityObject(LocalizationSupport.getMessage("CITY_BANGALORE"), 12.983, 77.583),
        new CityObject(LocalizationSupport.getMessage("CITY_BANGKOK"), 13.750, 100.517),
        new CityObject(LocalizationSupport.getMessage("CITY_BEIJING"), 39.9, 116.413),
        new CityObject(LocalizationSupport.getMessage("CITY_BERLIN"), 52.517, 13.4),
        new CityObject(LocalizationSupport.getMessage("CITY_BERN"), 46.917, 7.467),
        new CityObject(LocalizationSupport.getMessage("CITY_BOGOTA"), 4.6, -74.083),
        new CityObject(LocalizationSupport.getMessage("CITY_BREST"), 48.4, -4.483),
        new CityObject(LocalizationSupport.getMessage("CITY_BRISBANE"), -27.500, 153.017),
        new CityObject(LocalizationSupport.getMessage("CITY_BRUSSELS"), 50.833, 4.333),
        new CityObject(LocalizationSupport.getMessage("CITY_BUCHAREST"), 44.433, 26.1),
        new CityObject(LocalizationSupport.getMessage("CITY_BUDAPEST"), 47.5, 19.083),
        new CityObject(LocalizationSupport.getMessage("CITY_BUENOS_AIRES"), -34.613, -58.47),
        new CityObject(LocalizationSupport.getMessage("CITY_CAIRO"), 30.05, 31.25),
        new CityObject(LocalizationSupport.getMessage("CITY_CALCUTTA"), 22.533, 88.367),
        new CityObject(LocalizationSupport.getMessage("CITY_CAPE_TOWN"), -33.917, 18.417),
        new CityObject(LocalizationSupport.getMessage("CITY_CHICAGO"), 41.850, -87.650),
        new CityObject(LocalizationSupport.getMessage("CITY_CHONGQING"), 29.550, 106.532),
        new CityObject(LocalizationSupport.getMessage("CITY_COPENHAGEN"), 55.667, 12.583),
        new CityObject(LocalizationSupport.getMessage("CITY_KRAKOW"), 50.067, 19.950),
        new CityObject(LocalizationSupport.getMessage("CITY_DARWIN"), -12.467, 130.833),
        new CityObject(LocalizationSupport.getMessage("CITY_DHAKA"), 23.723, 90.409),
        new CityObject(LocalizationSupport.getMessage("CITY_DUSSELDORF"), 51.224, 6.802),
        new CityObject(LocalizationSupport.getMessage("CITY_DUBLIN"), 53.333, -6.250),
        new CityObject(LocalizationSupport.getMessage("CITY_DELHI"), 28.667, 77.217),
        new CityObject(LocalizationSupport.getMessage("CITY_GDYNIA"), 54.533, 18.533),
        new CityObject(LocalizationSupport.getMessage("CITY_GUADALAJARA"), 20.6, -103.4),
        new CityObject(LocalizationSupport.getMessage("CITY_GUANGZHOU"), 23.117, 113.25),
        new CityObject(LocalizationSupport.getMessage("CITY_HELSINKI"), 60.6, 21.433),
        new CityObject(LocalizationSupport.getMessage("CITY_HOUSTON"), 29.763, -95.363),
        new CityObject(LocalizationSupport.getMessage("CITY_ISLAMABAD"), 33.7, 73.167),
        new CityObject(LocalizationSupport.getMessage("CITY_ISTANBUL"), 41.017, 28.967),
        new CityObject(LocalizationSupport.getMessage("CITY_JERUSALEM"), 31.767, 35.233),
        new CityObject(LocalizationSupport.getMessage("CITY_JAKARTA"), -6.167, 106.8),
        new CityObject(LocalizationSupport.getMessage("CITY_KARACHI"), 24.867, 67.050),
        new CityObject(LocalizationSupport.getMessage("CITY_KUALA"), 30.13, 101.7),
        new CityObject(LocalizationSupport.getMessage("CITY_HONG_KONG"), 22.283, 114.150),
        new CityObject(LocalizationSupport.getMessage("CITY_LAGOS"), 6.453, 3.396),
        new CityObject(LocalizationSupport.getMessage("CITY_LAHORE"), 31.571, 74.313),
        new CityObject(LocalizationSupport.getMessage("CITY_LIMA"), -12.050, -77.050),
        new CityObject(LocalizationSupport.getMessage("CITY_LISBON"), 38.717, -9.133),
        new CityObject(LocalizationSupport.getMessage("CITY_LONDON"), 51.517, -0.105),
        new CityObject(LocalizationSupport.getMessage("CITY_LOS_ANGELES"), 34.052, -118.243),
        new CityObject(LocalizationSupport.getMessage("CITY_LUXEMBOURG"), 49.612, 6.130),
        new CityObject(LocalizationSupport.getMessage("CITY_MADRID"), 40.4, -3.683),
        new CityObject(LocalizationSupport.getMessage("CITY_MELBOURNE"), -34.817, 144.967),
        new CityObject(LocalizationSupport.getMessage("CITY_MEXICO_CITY"), 19.4, -99.05),
        new CityObject(LocalizationSupport.getMessage("CITY_MONTAUBAN"), 44.017, 1.350),
        new CityObject(LocalizationSupport.getMessage("CITY_MOSCOW"), 55.75, 37.583),
        new CityObject(LocalizationSupport.getMessage("CITY_MUMBAI"), 18.967, 72.833),
        new CityObject(LocalizationSupport.getMessage("CITY_MUNICH"), 48.15, 11.583),
        new CityObject(LocalizationSupport.getMessage("CITY_NAIROBI"), -1.283, 36.817),
        new CityObject(LocalizationSupport.getMessage("CITY_NEW_YORK"), 40.714, -74.006),
        new CityObject(LocalizationSupport.getMessage("CITY_NICE"), 43.7, 7.25),
        new CityObject(LocalizationSupport.getMessage("CITY_NOUMEA"), -22.27, 166.44),
        new CityObject(LocalizationSupport.getMessage("CITY_OSLO"), 59.917, 10.75),
        new CityObject(LocalizationSupport.getMessage("CITY_OTTAWA"), 45.417, -75.700),
        new CityObject(LocalizationSupport.getMessage("CITY_OUAGADOUGOU"), 12.366, -1.518),
        new CityObject(LocalizationSupport.getMessage("CITY_PARIS"), 48.867, 2.333),
        new CityObject(LocalizationSupport.getMessage("CITY_PERTH"), -31.933, 115.833),
        new CityObject(LocalizationSupport.getMessage("CITY_PHILADELPHIA"), 39.952, -75.164),
        new CityObject(LocalizationSupport.getMessage("CITY_POZNAN"), 52.400, 16.917),
        new CityObject(LocalizationSupport.getMessage("CITY_PRAGUE"), 50.083, 14.467),
        new CityObject(LocalizationSupport.getMessage("CITY_RABAT"), 34.033, -6.833),
        new CityObject(LocalizationSupport.getMessage("CITY_REYKJAVIK"), 64.150, -21.950),
        new CityObject(LocalizationSupport.getMessage("CITY_RIO_DE_JANEIRO"), -22.9, -43.233),
        new CityObject(LocalizationSupport.getMessage("CITY_ROME"), 41.9, 12.483),
        new CityObject(LocalizationSupport.getMessage("CITY_SAN_FRANCISCO"), 37.775, -122.418),
        new CityObject(LocalizationSupport.getMessage("CITY_SANTIAGO"), -33.45, -70.667),
        new CityObject(LocalizationSupport.getMessage("CITY_SAO_PAULO"), -23.533, -46.617),
        new CityObject(LocalizationSupport.getMessage("CITY_SEOUL"), 37.567, 127.0),
        new CityObject(LocalizationSupport.getMessage("CITY_SHANGHAI"), 31.222, 121.458),
        new CityObject(LocalizationSupport.getMessage("CITY_SHENYANG"), 41.8, 123.45),
        new CityObject(LocalizationSupport.getMessage("CITY_SOFIA"), 42.683, 23.317),
        new CityObject(LocalizationSupport.getMessage("CITY_STOCKHOLM"), 59.333, 18.050),
        new CityObject(LocalizationSupport.getMessage("CITY_SYDNEY"), -33.883, 151.217),
        new CityObject(LocalizationSupport.getMessage("CITY_TAIWAN"), 23.213, 120.179),
        new CityObject(LocalizationSupport.getMessage("CITY_TEHRAN"), 35.672, 51.424),
        new CityObject(LocalizationSupport.getMessage("CITY_TIANJIN"), 39.133, 117.2),
        new CityObject(LocalizationSupport.getMessage("CITY_TOKYO"), 35.7, 139.767),
        new CityObject(LocalizationSupport.getMessage("CITY_VARSOVIE"), 52.25, 21),
        new CityObject(LocalizationSupport.getMessage("CITY_VIENNA"), 48.2, 16.367),
        new CityObject(LocalizationSupport.getMessage("CITY_WUHAN"), 30.583, 114.267),
        new CityObject(LocalizationSupport.getMessage("CITY_ZAGREB"), 45.8, 16),
    };

    /** 
     * Creates a new instance of Position 
     */
    public Position() {
 //       setCity(41);                                                            // Kuala Lumpur
        setCity(59);                                                          // Nice
        myTime = new Temps();
        alt = 105;                                                              // altitude of the user
        myTime.calculateJourJulien();
        myTime.calculateHS();
    }

    /**
     * Return the longitude of my position in d m s
     * @return the longitude from where I am looking at the stars
     */
    public double getLongitude() {
        int tmp;
        double ret;
        tmp = (int) (longi * 10000);
        ret = (double) tmp / 10000;
        return ret;
    }

    /**
     * Return the latitude of my position in d m s
     * @return the latitude from where I am looking at the stars
     */
    public double getLatitude() {
        int tmp;
        double ret;
        tmp = (int) (lat * 10000);
        ret = (double) tmp / 10000;
        return ret;
    }

    /**
     * Set the longitude and latitude according to city 
     * @param idx index of the city
     */
    public void setCity(int idx) {
        lat = cities[idx].getLatitude();
        longi = cities[idx].getLongitude();
        cityIndex = (short) idx;
    }

    /** 
     * Set the longitude
     * @param l longitude
     */
    public void setLongitude(double l) {
        longi = l % 180;
    }

    /** 
     * Set the latitude
     * @param l latitude
     */
    public void setLatitude(double l) {
        lat = l % 90;
    }

    /**
     * Return the altitude of the user
     * @return altitude of the user
     */
    public short getAltitude() {
        return alt;
    }

    /**
     * Return the current time
     * @return the Temps object
     */
    public Temps getTemps() {
        return myTime;
    }

    /**
     * Return the list of cities. The user can choose between all these cities to set the viewing point of the stars.
     * This function is a static function
     * @return A table of string representing the names of the cities
     */
    public static String[] getCityList() {
        String[] citiesNames;
        citiesNames = new String[cities.length];
        for (int i = 0; i < citiesNames.length; i++) {
            citiesNames[i] = cities[i].getName();
        }
        return citiesNames;
    }

    /**
     * Return the selected city
     * @return index to the selected city
     */
    public short getSelectedCity() {
        return cityIndex;
    }

    /**
     *
     */
    public String toString() {
        String s;
        s = "L: " + longi + " l: " + lat;
        return s;
    }
}
