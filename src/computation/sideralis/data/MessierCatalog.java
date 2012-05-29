package computation.sideralis.data;

import computation.sideralis.object.MessierObject;



/**
 * This class contains all the data for the definition of the Messier object
 * @author Bernard
 */
public class MessierCatalog {
    /** The definition of all Messier objects */
    private static final MessierObject[] messierObject = {
        new MessierObject(5.575F,22.0166666666667F,(short)84,6.3F,"M 1"),
        new MessierObject(21.5583333333333F,-0.816666666666667F,(short)65,36F,"M 2"),
        new MessierObject(13.7033333333333F,28.3833333333333F,(short)64,31F,"M 3"),
        new MessierObject(16.3933333333333F,-26.5333333333333F,(short)59,7F,"M 4"),
        new MessierObject(15.31F,2.08333333333333F,(short)58,23F,"M 5"),
        new MessierObject(17.6683333333333F,-32.2166666666667F,(short)42,2F,"M 6"),
        new MessierObject(17.8983333333333F,-34.8166666666667F,(short)33,1F,"M 7"),
        new MessierObject(18.0633333333333F,-24.3833333333333F,(short)58,6.5F,"M 8"),
        new MessierObject(17.32F,-18.5166666666667F,(short)79,26F,"M 9"),
        new MessierObject(16.9516666666667F,-4.1F,(short)66,13F,"M 10"),
        new MessierObject(18.8516666666667F,-6.26666666666667F,(short)58,6F,"M 11"),
        new MessierObject(16.7866666666667F,-1.95F,(short)66,18F,"M 12"),
        new MessierObject(16.695F,36.4666666666667F,(short)59,22F,"M 13"),
        new MessierObject(17.6266666666667F,-3.25F,(short)76,27F,"M 14"),
        new MessierObject(21.5F,12.1666666666667F,(short)64,33F,"M 15"),
        new MessierObject(18.3133333333333F,-13.7833333333333F,(short)60,7F,"M 16"),
        new MessierObject(18.3466666666667F,-16.1833333333333F,(short)60,5F,"M 17"),
        new MessierObject(18.3316666666667F,-17.1333333333333F,(short)69,6F,"M 18"),
        new MessierObject(17.0433333333333F,-26.2666666666667F,(short)72,27F,"M 19"),
        new MessierObject(18.0383333333333F,-23.0333333333333F,(short)63,2.2F,"M 20"),
        new MessierObject(18.0766666666667F,-22.5F,(short)59,3F,"M 21"),
        new MessierObject(18.6066666666667F,-23.9F,(short)51,10F,"M 22"),
        new MessierObject(17.9466666666667F,-19.0166666666667F,(short)55,4.5F,"M 23"),
        new MessierObject(18.7533333333333F,-9.4F,(short)80,5F,"M 26"),
        new MessierObject(19.9933333333333F,22.7166666666667F,(short)81,1.25F,"M 27"),
        new MessierObject(18.4083333333333F,-24.8666666666667F,(short)69,18F,"M 28"),
        new MessierObject(20.3983333333333F,38.5333333333333F,(short)66,7.2F,"M 29"),
        new MessierObject(21.6733333333333F,-23.1833333333333F,(short)75,25F,"M 30"),
        new MessierObject(0.711666666666667F,41.2666666666667F,(short)35,2500F,"M 31"),
        new MessierObject(0.711666666666667F,40.8666666666667F,(short)82,2900F,"M 32"),
        new MessierObject(1.565F,30.65F,(short)57,2590F,"M 33"),
        new MessierObject(2.7F,42.7833333333333F,(short)52,1.4F,"M 34"),
        new MessierObject(6.14833333333333F,24.3333333333333F,(short)51,2.8F,"M 35"),
        new MessierObject(5.60166666666667F,34.1333333333333F,(short)60,4.1F,"M 36"),
        new MessierObject(5.87333333333333F,32.55F,(short)56,4.6F,"M 37"),
        new MessierObject(5.47833333333333F,35.8333333333333F,(short)64,4.2F,"M 38"),
        new MessierObject(21.5366666666667F,48.4333333333333F,(short)46,0.3F,"M 39"),
        new MessierObject(6.78333333333333F,-20.7333333333333F,(short)45,2.4F,"M 41"),
        new MessierObject(5.59F,-5.45F,(short)40,1.6F,"M 42"),
        new MessierObject(5.59333333333333F,-5.26666666666667F,(short)90,1.6F,"M 43"),
        new MessierObject(8.66833333333333F,19.9833333333333F,(short)31,0.5F,"M 44"),
        new MessierObject(7.69666666666667F,-14.8166666666667F,(short)61,5.4F,"M 46"),
        new MessierObject(7.61F,-14.5F,(short)44,1.6F,"M 47"),
        new MessierObject(8.23F,-5.8F,(short)58,1.5F,"M 48"),
        new MessierObject(12.4966666666667F,8F,(short)84,60000F,"M 49"),
        new MessierObject(7.05333333333333F,-8.33333333333333F,(short)59,3F,"M 50"),
        new MessierObject(13.5F,47.2666666666667F,(short)96,37000F,"M 51"),
        new MessierObject(23.4033333333333F,61.5833333333333F,(short)69,7F,"M 52"),
        new MessierObject(13.215F,18.1666666666667F,(short)77,56F,"M 53"),
        new MessierObject(18.9183333333333F,-30.4833333333333F,(short)77,83F,"M 54"),
        new MessierObject(19.6666666666667F,-30.9666666666667F,(short)70,17F,"M 55"),
        new MessierObject(19.2766666666667F,30.1833333333333F,(short)83,32F,"M 56"),
        new MessierObject(18.8933333333333F,33.0333333333333F,(short)90,2.3F,"M 57"),
        new MessierObject(12.6283333333333F,11.8166666666667F,(short)98,60000F,"M 58"),
        new MessierObject(12.7F,11.65F,(short)98,60000F,"M 59"),
        new MessierObject(12.7283333333333F,11.55F,(short)88,60000F,"M 60"),
        new MessierObject(12.365F,4.46666666666667F,(short)97,60000F,"M 61"),
        new MessierObject(17.02F,-30.1166666666667F,(short)66,22F,"M 62"),
        new MessierObject(13.2633333333333F,42.0333333333333F,(short)86,37000F,"M 63"),
        new MessierObject(12.945F,21.6833333333333F,(short)85,12000F,"M 64"),
        new MessierObject(11.315F,13.0833333333333F,(short)93,35000F,"M 65"),
        new MessierObject(11.3366666666667F,12.9833333333333F,(short)90,35000F,"M 66"),
        new MessierObject(8.84F,11.8166666666667F,(short)69,2.25F,"M 67"),
        new MessierObject(12.6583333333333F,-26.75F,(short)82,32F,"M 68"),
        new MessierObject(18.5233333333333F,-32.35F,(short)77,25F,"M 69"),
        new MessierObject(18.72F,-32.3F,(short)81,28F,"M 70"),
        new MessierObject(19.8966666666667F,18.7833333333333F,(short)83,12F,"M 71"),
        new MessierObject(20.8916666666667F,-12.5333333333333F,(short)94,53F,"M 72"),
        new MessierObject(20.9833333333333F,-12.6333333333333F,(short)90,2500F,"M 73"),
        new MessierObject(1.61166666666667F,15.7833333333333F,(short)92,35000F,"M 74"),
        new MessierObject(20.1016666666667F,-21.9166666666667F,(short)86,58F,"M 75"),
        new MessierObject(1.705F,51.5666666666667F,(short)120,3.4F,"M 76"),
        new MessierObject(1.705F,51.5833333333333F,(short)120,60000F,"M 76"),
        new MessierObject(2.71166666666667F,-0.0166666666666667F,(short)88,1.6F,"M 77"),
        new MessierObject(5.77833333333333F,0.05F,(short)80,40F,"M 78"),
        new MessierObject(5.40833333333333F,-24.55F,(short)80,27F,"M 79"),
        new MessierObject(16.2833333333333F,-22.9833333333333F,(short)72,11000F,"M 80"),
        new MessierObject(9.92666666666667F,69.0666666666667F,(short)69,11000F,"M 81"),
        new MessierObject(9.93F,69.6833333333333F,(short)84,10000F,"M 82"),
        new MessierObject(13.6166666666667F,-29.8666666666667F,(short)76,10000F,"M 83"),
        new MessierObject(12.4183333333333F,12.8833333333333F,(short)93,60000F,"M 84"),
        new MessierObject(12.4233333333333F,18.1833333333333F,(short)92,60000F,"M 85"),
        new MessierObject(12.4366666666667F,12.95F,(short)92,60000F,"M 86"),
        new MessierObject(12.5133333333333F,12.4F,(short)86,60000F,"M 87"),
        new MessierObject(12.5333333333333F,14.4166666666667F,(short)95,60000F,"M 88"),
        new MessierObject(12.595F,12.55F,(short)98,60000F,"M 89"),
        new MessierObject(12.6133333333333F,13.1666666666667F,(short)95,60000F,"M 90"),
        new MessierObject(12.59F,14.5F,(short)102,60000F,"M 91"),
        new MessierObject(17.285F,43.1333333333333F,(short)65,25F,"M 92"),
        new MessierObject(7.74333333333333F,-23.8666666666667F,(short)62,4.5F,"M 93"),
        new MessierObject(12.8483333333333F,41.1166666666667F,(short)82,14500F,"M 94"),
        new MessierObject(10.7333333333333F,11.7F,(short)97,38000F,"M 95"),
        new MessierObject(10.78F,11.8166666666667F,(short)92,38000F,"M 96"),
        new MessierObject(11.2466666666667F,55.0166666666667F,(short)112,2.6F,"M 97"),
        new MessierObject(12.23F,14.9F,(short)101,60000F,"M 98"),
        new MessierObject(12.3133333333333F,14.4166666666667F,(short)98,60000F,"M 99"),
        new MessierObject(12.3816666666667F,15.8166666666667F,(short)94,60000F,"M 100"),
        new MessierObject(14.0533333333333F,54.35F,(short)77,24000F,"M 101"),
        new MessierObject(1.55333333333333F,60.7F,(short)74,8F,"M 103"),
        new MessierObject(12.6666666666667F,-11.6166666666667F,(short)83,50000F,"M 104"),
        new MessierObject(10.7966666666667F,12.5833333333333F,(short)93,38000F,"M 105"),
        new MessierObject(12.3166666666667F,47.3F,(short)83,25000F,"M 106"),
        new MessierObject(16.5416666666667F,-13.05F,(short)81,20F,"M 107"),
        new MessierObject(11.1916666666667F,55.6666666666667F,(short)101,45000F,"M 108"),
        new MessierObject(11.96F,53.3833333333333F,(short)98,55000F,"M 109"),
        new MessierObject(0.673333333333333F,41.6833333333333F,(short)80,2200F,"M 110"),
    };

    /**
     * Return the number of object in the catalog
     * @return number of objects in catalog
     */
    public static int getNumberOfObjects() {
        return messierObject.length;
    }

    /**
     * Return one object of the catalog
     * @param i index of the object in the catalog
     * @return the ith object in the catalog
     */
    public static MessierObject getObject(int i) {
        return messierObject[i];
    }
}
