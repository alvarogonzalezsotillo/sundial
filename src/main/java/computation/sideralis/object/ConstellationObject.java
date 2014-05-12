package computation.sideralis.object;

import computation.sideralis.data.ConstellationCatalog;

/**
 * This class is used to represent a constellation object
 *
 * @author Bernard
 */
public class ConstellationObject {
    /** A list of stars in a constellation. The list represents couples of stars. Each couple represent a line when drawing the constellation */
    private final short[] listStars;
    /** The index to the name */
    private int idxName;
    /** The star which is used to display the name of the constellation */
    private short constNameStar;
        
    
    /**
     * Creates a new instance of Constellation
     * @param v a vector of short listing the id of the star composing this constellation (this is a list of couple id, each couple represent one branch of the constellation)
     * @param idxName the name of the constellation (index in a table).
     * @param c the id of the star which is used as reference to display the constellation name
     */
    public ConstellationObject(short[] v, int idxName,short c) {
        listStars = v;
        this.idxName = idxName;
        constNameStar = c;
    }
    /**
     * Creates a new instance of Constellation
     * @param idxName the name of the constellation (index in a table)
     * @param c the id of the star which is used as reference to display the constellation name
     */
    public ConstellationObject(int idxName,short c) {
        listStars = null;
        this.idxName = idxName;
        constNameStar = c;
    }
    /**
     * Return the size of constellation, i.e number of branches *2
     * @return number of branches * 2 (or number of star ids) or 0 if the constellation is not drawn
     */
    public int getSizeOfConstellation() {
        if (listStars == null)
            return 0;
        else
            return listStars.length;
    }
    /**
     * Return the HR refence of the i th star in this constellation
     * @return the HR reference id
     */
    public short getHR(int i) {
        return listStars[i];
    }
    /**
     * Return the index of the i th star in this constellation
     * @return the index
     */
    public short getIdx(int i) {
        return listStars[i];
    }
    /**
     * Return the name of the constellation
     * @return the name of the constellation
     */
    public String getName() {
        return ConstellationCatalog.constNames[idxName][ConstellationCatalog.NAME];
    }
    /**
     * Return the name of the constellation in different language
     * @return the name of the constellation
     */
    public String getLatinName() {
        return ConstellationCatalog.constNames[idxName][ConstellationCatalog.LATIN];
    }
    /**
     * Return the HR of the star used as ref to display the name of the constellation
     * @return HR of the star
     */
    public short getRefStar4ConstellationName() {
        return constNameStar;
    }
    /**
     * Return the index to the name
     * @return an index used to display names
     */
    public int getIdxName() {
        return idxName;
    }
}
