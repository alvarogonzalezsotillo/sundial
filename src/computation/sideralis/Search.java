/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package computation.sideralis;

import computation.sideralis.data.ConstellationCatalog;
import computation.sideralis.data.MessierCatalog;
import computation.sideralis.data.Sky;
import computation.sideralis.object.SkyObject;



/**
 * The search class.
 * Store the name of all objects in order to retrieve them easily
 * @author Bernard
 */
public class Search {
    /** A reference to my sky object */
    private final Sky mySky;
    /** This is the index of the objec to be highlighted (from 0 to ? for stars, from 0 to ? for Messier and from 0 to ? for sun, moon or planets */
    private int idxHighlight;
    /** The type of object to highlight */
    private int typeHighlight;
    /** All the names of the searchable objects */
    private String[] listOfSearchableObjects;
    /** The index in the list above + value -1 to indicate no search */
    private short indexSearch;
    /** A flag to indicate to searched object in the center of the screen */
    private boolean flagCentered;
    /** Return status when searching object */
    public static final short NOT_FOUND = 0;
    public static final short FOUND_NOT_VISIBLE = 1;
    public static final short FOUND_VISIBLE = 2;

    /**
     * Constructor
     * @param mySky a reference to the sky
     */
    public Search(Sky mySky) {
        indexSearch = -1;                                                       // By default, no search
        flagCentered = true;
        this.mySky = mySky;
        typeHighlight = SkyObject.NONE;
    }
    /**
     * Return the name of the searched object or "?" if no objects are searched
     * @param idx the index of the object in the name list
     * @return Name of object or "?"
     */
    public String getNameOfSearchableObject(int idx) {
        if (idx>=0)
            return listOfSearchableObjects[idx];
        else
            return "?";                    
    }
    /**
     * Insure that index of search object is valid (between min and max)
     */
    public void boundIndexOfSearchableObject() {
        if (indexSearch>=listOfSearchableObjects.length) {
            indexSearch = -1;
            
        } else if (indexSearch <-1) {
            indexSearch = (short)(listOfSearchableObjects.length-1); 
        }
    }
    /** 
     * Build a list of all objects which can be searched
     */
    public void createListOfObjets() {
        int i, count;
        // =================================
        // === Count how many star names ===
        count = 0;
        for (i = 0; i < mySky.getStarsProj().length; i++) {
            if (mySky.getStar(i).getObject().getName().length() != 0) {
                count++;
            }
        }
        // ===========================
        // === Add Messier objects ===
        count += MessierCatalog.getNumberOfObjects();
        // =============================================
        // === Add main objects (sun, moon, planets) ===
        count += (2 + Sky.NB_OF_PLANETS);
        // ==========================
        // === Add constellations ===
        count += 2*ConstellationCatalog.getNumberOfConstellations();            // 2 for normal names + latin names
        
        // ====================
        // === Create table ===
        listOfSearchableObjects = new String[count];

        // ==================
        // === Fill table ===
        count = 0;
        // Add stars
        for (i = 0; i < mySky.getStarsProj().length; i++) {
            if (mySky.getStar(i).getObject().getName().length() != 0) {
                listOfSearchableObjects[count++] = mySky.getStar(i).getObject().getName();
            }
        }
        // Add Messier
        for (i = 0; i < MessierCatalog.getNumberOfObjects(); i++) {
            listOfSearchableObjects[count++] = MessierCatalog.getObject(i).getName();
        }
        // Add Planets
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_SUN");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_MOON");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_MERCURY");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_VENUS");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_MARS");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_JUPITER");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_SATURN");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_URANUS");
        listOfSearchableObjects[count++] = LocalizationSupport.getMessage("NAME_NEPTUNE");
        // Add constellations
        for (i=0;i<ConstellationCatalog.getNumberOfConstellations();i++) {
            listOfSearchableObjects[count++] = ConstellationCatalog.getConstellation(i).getLatinName();
            listOfSearchableObjects[count++] = ConstellationCatalog.getConstellation(i).getName();
        }
        // ==================
        // === Sort table ===
        int a, b;
        String temp;

        for (a = 0; a < listOfSearchableObjects.length - 1; ++a) {
            for (b = 0; b < listOfSearchableObjects.length - 1; ++b) {
                if (listOfSearchableObjects[b].compareTo(listOfSearchableObjects[b + 1]) > 0) {
                    temp = listOfSearchableObjects[b];
                    listOfSearchableObjects[b] = listOfSearchableObjects[b + 1];
                    listOfSearchableObjects[b + 1] = temp;
                }
            }
        }
    }
    /**
     * Remove all spaces from a string
     * @param s the input string
     * @return the input string without any space inside
     */
    private String removeSpace(String s) {
        int posSpace;
        String retS,s1,s2;
        
        retS = new String(s.trim());
        posSpace = retS.indexOf(" ");
        while (posSpace != -1) {
            s1 = retS.substring(0,posSpace);
            s2 = retS.substring(posSpace+1, retS.length());
            retS = s1.concat(s2);
            posSpace = retS.indexOf(" ");
        }
        return retS;
            
    }
    /**
     * Return the index in listOfSearchableObjects of the object which starts as parameter string
     * Called when typing the first letter of a name
     * @param string the first letters of an object
     * @return An index in listOfSearchableObject or -1 if no object found
     */
    public short search(String string) {
        int j;
        short ret;
        
        string = removeSpace(string);
        
        for (j = 0; j < listOfSearchableObjects.length; j++) {
            String s = removeSpace(listOfSearchableObjects[j].toLowerCase());
            if (s.startsWith(string)) {
                break;
            }
        }
        // And display it if it exists
        if (j != listOfSearchableObjects.length) {
            ret = (short)j;
        } else {
            ret = -1;
        }
        return ret;
    }
    /**
     * To cancel highlight of searched object
     */
    public void clearHighlight() {
        typeHighlight = SkyObject.NONE;
        flagCentered = true;
    }
    /**
     * Highlight the searched object - Called when pressing search
     * @param string name of the object
     * @return indicate if the object was found and if yes, if it is visible or not
     */
    public short setHighlight(String string) {
        int i;
        short flagVisible = FOUND_NOT_VISIBLE;
        typeHighlight = SkyObject.NONE;
        // Identify the object from his name
        // Search for stars
        for (i=0;i<mySky.getStarsProj().length;i++) {
            if (string.equalsIgnoreCase(mySky.getStar(i).getObject().getName())) {
                idxHighlight = i;
                typeHighlight = SkyObject.STAR;
                if (mySky.getStar(i).getHeight()>=0)
                    flagVisible = FOUND_VISIBLE;
                break;
            }
        }
        if (typeHighlight == SkyObject.NONE) {
            String s = string.trim();
            // Search for Messier
            for (i=0;i<MessierCatalog.getNumberOfObjects();i++) {
                if (s.equalsIgnoreCase(MessierCatalog.getObject(i).getName().trim())) {
                    idxHighlight = i;
                    typeHighlight = SkyObject.MESSIER;
                    if (mySky.getMessier(i).getHeight()>=0)
                        flagVisible = FOUND_VISIBLE;
                    break;
                }
            }
            if (typeHighlight == SkyObject.NONE) {
                // Search for planets
                if (string.equalsIgnoreCase(LocalizationSupport.getMessage("NAME_SUN"))) {
                    idxHighlight = 0;
                    typeHighlight = SkyObject.SUN;
                    if (mySky.getSun().getHeight()>=0)
                        flagVisible = FOUND_VISIBLE;
                } else if (string.equalsIgnoreCase(LocalizationSupport.getMessage("NAME_MOON"))) {
                    idxHighlight = 0;
                    typeHighlight = SkyObject.MOON;
                    if (mySky.getMoon().getHeight()>=0)
                        flagVisible = FOUND_VISIBLE;
                } else {
                    for (i=0;i<mySky.getSystemSolarProj().length;i++) {
                        if (string.equalsIgnoreCase(mySky.getPlanet(i).getObject().getName())) {
                            idxHighlight = i;
                            typeHighlight = SkyObject.PLANET;
                            if (mySky.getPlanet(i).getHeight()>=0)
                                flagVisible = FOUND_VISIBLE;
                        }
                    }
                }
                if (typeHighlight == SkyObject.NONE) {
                    // Search for constellations
                    for (i=0;i<ConstellationCatalog.getNumberOfConstellations();i++) {
                        if (string.equals(ConstellationCatalog.getConstellation(i).getLatinName())
                                || string.equals(ConstellationCatalog.getConstellation(i).getName())) {
                            idxHighlight = i;
                            typeHighlight = SkyObject.CONSTELLATION;
                            if (mySky.getStar(ConstellationCatalog.getConstellation(i).getRefStar4ConstellationName()).getHeight()>=0)
                                flagVisible = FOUND_VISIBLE;
                            break;
                        }
                    }
                }
            }
        }
        if (typeHighlight == SkyObject.NONE)
            flagVisible = NOT_FOUND;
        
        if (flagVisible == FOUND_VISIBLE)                                       
            flagCentered = false;                                               // Item will centered
        
        return flagVisible;
    }
    /**
     * Return the number of elements in the search list
     * @return number of searchable objects
     */
    public int getLength() {
        return listOfSearchableObjects.length;        
    }
    /**
     * Return the name of an object at position idx in the list of all searchable object
     * @param idx the index in the list listOfSearchableObject
     * @return the name of the object at position idx
     */
    public String getName(int idx) {
        return listOfSearchableObjects[idx];
    }
    /**
     * Set the index of the object we want to search for. And check that it is inside bound
     * @param idx the index in the list of searchable object
     */
    public void setIndex(int idx) {
        indexSearch = (short)idx; 
        boundIndexOfSearchableObject();
    }
    /**
     * Return the index of the searched object inside the list of name of searchable objects.
     * @return the index in all searchable object names
     */
    public int getIndex() {
        return indexSearch;
    }
    /**
     * Return the type of searched object.
     * @return Type of searched object (SkyObject.STAR or SkyObject.MESSIER or ...)
     */
    public int getTypeHighlight() {
        return typeHighlight;
    }
    /**
     * Return the index of searched object
     * @return index of searched object
     */
    public int getIdxHighlight() {
        return idxHighlight;
    }
    /**
     * A boolean indicating if the screen is centered on the searched object
     * @return true if searched object is at the center of the screen or visible, false else.
     */
    public boolean isFlagCentered() {
        return flagCentered;
    }
    /**
     * Set to true to indicate that the search object is centered in the screen
     * @param flagCentered true or false
     */
    public void setFlagCentered(boolean flagCentered) {
        this.flagCentered = flagCentered;
    }
    
}
