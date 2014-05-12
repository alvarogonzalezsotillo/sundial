package computation.sideralis.data;

import computation.sideralis.LocalizationSupport;
import computation.sideralis.location.Position;
import computation.sideralis.object.PlanetObject;
import computation.sideralis.object.SkyObject;
import computation.sideralis.object.StarObject;
import computation.sideralis.projection.sphere.MessierProj;
import computation.sideralis.projection.sphere.MoonProj;
import computation.sideralis.projection.sphere.PlanetProj;
import computation.sideralis.projection.sphere.Projection;
import computation.sideralis.projection.sphere.StarProj;
import computation.sideralis.projection.sphere.SunProj;



/**
 * This class calculates manages the position of all objects in the sky (from stars to moon, ...).
 * Input are the catalog and position. Output is the X and Y coordinate on a virtual screen of each stars.
 * @author Bernard
 */
public class Sky implements Runnable {
    /** A variable to store the progress of in calculation of positions */
    private int progress;
    /** The projection (height, azimuth) of all my stars */
    private final StarProj[] starsProj;
    /** The projection of the moon */
    private final MoonProj moonProj;
    /** The projection of the sun */
    private final SunProj sunProj;
    /** The projection of planets */
    private final PlanetProj mercuryProj,venusProj,marsProj,jupiterProj,saturnProj,uranusProj,neptuneProj,earthProj;
    /** The Messier objects */
    private final MessierProj[] messierProj;
    
    /** a reference to my position and time */
    private final Position myPosition;

    /** The moon description */
    private final SkyObject moonObject;
    /** The sun description */
    private final SkyObject sunObject;
    /** The Mercury planet description */
    private final PlanetObject mercuryObject;
    /** The Venus planet description */
    private final PlanetObject venusObject;
    /** The Mars planet description */
    private final PlanetObject marsObject;
    /** The Saturn planet description */
    private final PlanetObject saturnObject;
    /** The Jupiter planet description */
    private final PlanetObject jupiterObject;
    /** The Uranus planet description */
    private final PlanetObject uranusObject;
    /** The Neptune planet description */
    private final PlanetObject neptuneObject;
    /** The Earth planet description */
    private final PlanetObject earthObject;
    /** A table storing all planets objects */
    private final PlanetObject[] planetObjects;
    /** A table storing all projections of planets */
    private final PlanetProj[] planetProj;
    /** Indicate if the calculation of projection is done */
    private boolean calculationDone;

    /** Number of planets */
    public static final short NB_OF_PLANETS = 7;
    /**
     * Creates a new instance of Sky (so all objects in the sky)
     * @param myPosition the position of the user
     */
    public Sky(Position myPosition) {
        int i,i1,i2;
        // Create all stars - We cumulate the 2 catalogs
        starsProj = new StarProj[StarCatalogConst.getNumberOfStars()+StarCatalogMag.getNumberOfStars()];
        for (i=i1=i2=0;i<starsProj.length;i++) {
            if (i<StarCatalogConst.getNumberOfStars())
                starsProj[i] = new StarProj(StarCatalogConst.getStar(i1++));
            else
                starsProj[i] = new StarProj(StarCatalogMag.getStar(i2++));
        }

        // Creates a moon
        moonObject = new SkyObject(0F, 0F, LocalizationSupport.getMessage("NAME_MOON"), (short)-120);
        moonProj = new MoonProj(moonObject);

        // create a sun
        sunObject = new SkyObject(0F, 0F, LocalizationSupport.getMessage("NAME_SUN"), (short)-267);
        sunProj = new SunProj(sunObject);

        // Create mercury
        mercuryObject = new PlanetObject(0F, 0F, LocalizationSupport.getMessage("NAME_MERCURY"), (short)-23,
                178.179078, 149474.07078, 0.0003011, 0, 0.3870986, 0.20561421, 0.00002046, -0.000000030, 0,
                7.002881, 0.0018608, -0.0000183, 0, 28.753753, 0.3702806, 0.0001208, 0,
                47.145944, 1.1852083, 0.0001739, 0, 102.27938, 149472.51529, 0.000007);
        mercuryProj = new PlanetProj(mercuryObject,SkyObject.MERCURE);

        // Create venus
        venusObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_VENUS"),(short)-46,
                342.767053, 58519.21191, 0.0003097, 0, 0.7233316, 0.00682069, -0.00004774, 0.000000091, 0,
                3.393631, 0.0010058, -0.0000010, 0, 54.384186, 0.5081861, -0.0013864, 0,
                75.779647, 0.8998500, 0.0004100, 0, 212.60322, 58517.80387, 0.001286);
        venusProj = new PlanetProj(venusObject,SkyObject.VENUS);

        // Create Mars
        marsObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_MARS"),(short)-29,
                293.737334, 19141.69551, 0.0003107, 0, 1.5236883, 0.09331290, 0.000092064, -0.000000077, 0,
                1.850333, -0.0006750, 0.0000126, 0, 285.431761, 1.0697667, 0.0001313, 0.00000414,
                48.786442, 0.7709917, -0.0000014, -0.00000533, 319.51913, 19139.85475, 0.000181);
        marsProj = new PlanetProj(marsObject,SkyObject.MARS);

        // Create Jupiter
        jupiterObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_JUPITER"),(short)-29,
                238.049257, 3036.301986, 0.0003347, -0.00000165, 5.202561, 0.04833475, 0.000164180, -0.0000004676, -0.000000017,
                1.308736, -0.0056961, 0.0000039, 0,273.277558, 0.5994317, 0.00070405, 0.00000508,
                99.443414, 1.0105300, 0.00035222, -0.00000851, 225.32833, 3034.69202, -0.000722);
        jupiterProj = new PlanetProj(jupiterObject,SkyObject.JUPITER);

        // Create Saturn
        saturnObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_SATURN"),(short)-4,
                266.564377, 1223.509884, 0.0003245, -0.0000058, 9.554747, 0.05589232, -0.00034550, -0.000000728, 0.00000000074,
                2.492519, -0.0039189, -0.00001549, 0.00000004, 338.307800, 1.0852207, 0.00097854, 0.00000992,
                112.790414, 0.8731951, -0.00015218, -0.00000531, 175.46622, 1221.55147, -0.000502);
        saturnProj = new PlanetProj(saturnObject,SkyObject.SATURNE);

        // Create Uranus
        uranusObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_URANUS"),(short)55,
                244.197470, 429.863546, 0.0003160, -0.00000060, 19.21814, 0.0463444, -0.00002658, 0.000000077, 0,
                0.772464, 0.0006253, 0.0000395, 0, 98.071581, 0.9857650, -0.0010745, -0.00000061,
                73.477111, 0.4986678, 0.0013117, 0, 72.6488, 428.37911, 0.000079);
        uranusProj = new PlanetProj(uranusObject,SkyObject.URANUS);

        // Create Neptune
        neptuneObject = new PlanetObject(0F,0F,LocalizationSupport.getMessage("NAME_NEPTUNE"),(short)78,
                84.457994, 219.885914, 0.0003205, -0.00000060, 30.10957, 0.00899704, 0.000006330, -0.00000002, 0,
                1.779242, -0.0095436, -0.0000091, 0, 276.045975, 0.3256394, 0.00014095, 0.000004113,
                130.681389, 1.0989350, 0.00024987, -0.000004718, 37.7306, 218.46134 , -0.000070);
        neptuneProj = new PlanetProj(neptuneObject,SkyObject.NEPTUNE);

        // Create Earth
        earthObject = new PlanetObject(0F, 0F, LocalizationSupport.getMessage("NAME_EARTH"), (short)0,
                279.69668+180, 36000.76892, 0.00030255, 0, 1.0000002, 0.01675104, -0.0000418, -0.000000126, 0,
                0, 0, 0, 0, 287.511505, 0.5647920, 0.00013610, 0.000003333,
                175.105679, -0.2416582, 0.00000794, -0.000000028, 358.47583, 35999.04975, -0.000150);
        earthProj = new PlanetProj(earthObject,SkyObject.EARTH);

        // Create all solar system objects
        planetObjects = new PlanetObject[NB_OF_PLANETS];
        planetObjects[0] = mercuryObject;
        planetObjects[1] = venusObject;
        planetObjects[2] = marsObject;
        planetObjects[3] = jupiterObject;
        planetObjects[4] = saturnObject;
        planetObjects[5] = uranusObject;
        planetObjects[6] = neptuneObject;

        planetProj = new PlanetProj[NB_OF_PLANETS];
        planetProj[0] = mercuryProj;
        planetProj[1] = venusProj;
        planetProj[2] = marsProj;
        planetProj[3] = jupiterProj;
        planetProj[4] = saturnProj;
        planetProj[5] = uranusProj;
        planetProj[6] = neptuneProj;

        // Create the messier objects
        messierProj = new MessierProj[MessierCatalog.getNumberOfObjects()];
        for (i=0;i<messierProj.length;i++) {
            messierProj[i] = new MessierProj(MessierCatalog.getObject(i));
        }
        
        calculationDone = false;
        this.myPosition = myPosition;
        Projection.setPosition(myPosition);                                     // Set the static field myPosition in Projection objects
    }
    /**
     * Return one of the star from all stars
     * @param i the index of the star required
     * @return the ith star in the list of star
     */
    public StarProj getStar(int i) {
        return starsProj[i];
    }
    /**
     * Return all stars
     * @return a reference to the starsProj object
     */
    public StarProj[] getStarsProj() {
        return starsProj;
    }

    /**
     * Return one of the Messier object from the catalog
     * @param i the index of the Messier object required
     * @return the ith MessierProj object in the messierProj table
     */
    public MessierProj getMessier(int i) {
        return messierProj[i];
    }
    /**
     * Return the table of projection object for planet
     * @return planetProj
     */
    public PlanetProj[] getSystemSolarProj() {
        return planetProj;
    }
    /**
     * Return a moon object
     * @return a reference to the moon projection object
     */
    public MoonProj getMoon() {
        return moonProj;
    }
    /**
     * Return a sun object
     * @return a reference to the sun projection object
     */
    public SunProj getSun() {
        return sunProj;
    }
    /**
     * Return a earth object
     * @return a reference to the earth projection object
     */
    public PlanetProj getEarth() {
        return earthProj;
    }
    /**
     * Return a planet
     * @param i the index of the planet (Sky.SATURN or Sky.VENUS or ...)
     * @return the PlanetProj object of the index in the planetProj table
     */
    public PlanetProj getPlanet(int i) {
        return planetProj[i];
    }
    /**
     * Return a boolean indicating if the calculation of position of objects is done
     * @return true if calculation of position is done, false if it is ongoing
     */
    public boolean isCalculationDone() {
        return calculationDone;
    }
    /**
     * To set the boolean indicating if the calculation of projection is done
     * @param calculationDone
     */
    public void setCalculationDone(boolean calculationDone) {
        this.calculationDone = calculationDone;
    }

    /**
     * This is the thread which is called every (xx s) and which add time, calculate new time variables and calculates the star position and other objects
     */
    public void run() {
        calculate();
    }
    /**
     * Calculate all time variables
     */
    public void calculateTimeVariables() {
        myPosition.getTemps().adjustDate();
        myPosition.getTemps().calculateJourJulien();
        myPosition.getTemps().calculateHS();
        Projection.calculateParamSun(myPosition.getTemps().getT());
        Projection.calcStaticVar();
    }
    /**
     * Add time, calculate new time variables and calculates the stars position and all other objects
     */
    public void calculate() {
    	setProgress(0);
        int size = (1 + starsProj.length + messierProj.length + 2 + planetProj.length)/100;
        int step = 0;
        // ---------------------------------------------------
        // --- Recalculate global variables linked to time ---
    	calculateTimeVariables();
        step += 1;
        setProgress(step/size);
        // -----------------------------------
        // --- Calculate position of stars ---
        for (int i=0;i<starsProj.length;i++) {
            try {
                starsProj[i].calculate();
            } catch(ArrayIndexOutOfBoundsException ex) {
                System.out.println(i+" "+starsProj.length);
                ex.printStackTrace();
            }
            if (i%100 == 0) {
                step += 100;
                setProgress(step/size);
            }
        }

        // ----------------------------------
        // --- Calculate position of moon ---
        moonProj.calculate();
        // ---------------------------------
        // --- Calculate position of sun ---
        sunProj.calculate();

        // -------------------------------------
        // --- Calculate position of planets ---
        for (int i=0;i<planetProj.length;i++) {
            planetProj[i].calculate();
        }
        step += (2+planetProj.length);
        setProgress(step/size);

        // -----------------------------------
        // --- Calculate position of earth ---
        earthProj.calculate();

        // ---------------------------------------------
        // --- Calculate position of Messier objects ---
        for (int i=0;i<messierProj.length;i++) {
            messierProj[i].calculate();
            if (i%20 == 0){
                step += 20;
                setProgress(step/size);
            }
        }
    	setProgress(100);
        calculationDone = true;
    }
    /**
     * Return the progress index in the calculation of the projection of all object
     * @return a value between 0 and 100. 0 means beginning of calculation, 100 means ending of calculation
     */
    public int getProgress() {
        return progress;
    }
    /**
     * Used to reset the progress indicator
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

}
