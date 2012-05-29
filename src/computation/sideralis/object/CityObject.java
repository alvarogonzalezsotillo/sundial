package computation.sideralis.object;

/**
 * A class used to store information about a city.
 * @author Bernard
 */
public class CityObject {
    /** Name of the city */
    private final String name;
    /** Latitude of the city */
    private double latitude;
    /** Longitude of the city */
    private double longitude;
    
    /** 
     * Creates a new instance of City 
     * @param n the name of the city
     * @param la the latitude of the city
     * @param lo the longitude of the city
     */
    public CityObject(String n, double la, double lo) {
        name = n;
        latitude = la;
        longitude = lo;
    }
    /** 
     * Return the latitude of the city
     * @return latitude of the city
     */
    public double getLatitude() {
        return latitude;
    }
    /** 
     * Return the longitude of the city
     * @return longitude of the city
     */
    public double getLongitude() {
        return longitude;
    }
    /** 
     * Return the name of the city
     * @return name of the city
     */
    public String getName() {
        return name;
    } 
}
