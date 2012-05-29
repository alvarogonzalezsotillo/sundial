package computation.sideralis.object;

import computation.sideralis.data.ConstellationCatalog;

/**
 *
 * @author Bernard
 */
public class StarObject extends SkyObject {
    /** Reference ID */
    private short HR;
    /** Distance from earth */
    private float dist;
    /** bayerName + constName */
    private short bayerNameConstName;
    
    public static final String STAR_NAME[] = {"","\u03B1","\u03B11","\u03B12","Bayer","\u03B2","\u03B21","\u03B22","\u03C7","\u03C71","\u03C72","\u03B4","\u03B41",
            "\u03B42","\u03B43","\u03B5","\u03B51","\u03B7","\u03B72","\u03B3","\u03B31","\u03B32","\u03B9","\u03B91","\u03B92","\u03BA","\u03BA1","\u03BA2","\u03BB","\u03BB1",
            "\u03BC","\u03BC1","\u03BC2","\u03BD","\u03BD1","\u03BD2","\u03BD3","\u03C9","\u03C91","\u03C92","\u03BF","\u03BF1","\u03BF2","\u03C6","\u03C61","\u03C62","\u03C63","\u03C0",
            "\u03C01","\u03C02","\u03C03","\u03C04","\u03C05","\u03C06","\u03C8","\u03C81","\u03C82","\u03C83","\u03C1","\u03C11","\u03C3","\u03C32","\u03C4","\u03C41","\u03C42","\u03C43",
            "\u03C44","\u03C45","\u03C46","\u03C48","\u03C49","\u03B8","\u03B81","\u03B82","\u03C5","\u03C51","\u03C52","\u03C54","\u03BE","\u03BE1","\u03BE2","\u03B6","\u03B61","\u03B62","",
    };

    /** 
     * Creates a new instance of Star 
     * @param asc ascendance
     * @param dec declinaison
     * @param mag magnitude
     * @param HR HR reference
     * @param bayerNameConstName Bayer's name and constellation name
     * @param dist Distance from earth
     * @param name Name
     */
    public StarObject(float asc, float dec, float mag, short HR,short bayerNameConstName, float dist, String name) {
        super(asc,dec,name,mag);
        this.HR = HR;
        this.dist = dist;
        this.bayerNameConstName = bayerNameConstName; //(short)((bN<<8)+cN);        
    }
    /**
     * Return the HR value of this star
     * @return HR id
     */
    public short getHR() {
        return HR;
    }
    /** 
     * Return the bayer name of the star
     * @return name of the star
     */
    public String getBayerName() {
        return STAR_NAME[(bayerNameConstName>>8)];
    }
    /** 
     * Return the constellation index of the star 
     * @return constellation index
     */
    public short getConstellation() {
        return ((short)(bayerNameConstName&0xff));
    }
    /** 
     * Return the name of the constellation to which the star belongs to 
     * @return constellation's name
     */
    public String getConstellationName() {
        return ConstellationCatalog.constNames[(short)(bayerNameConstName&0xff)][ConstellationCatalog.ABBR];
    }
    /**
     * Return the distance from earth for this star
     * @return distance from earth in light year
     */
    public float getDistance() {
        return dist;
    }
}
