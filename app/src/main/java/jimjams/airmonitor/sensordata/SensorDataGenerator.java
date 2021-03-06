package jimjams.airmonitor.sensordata;

import java.util.ArrayList;

/**
 * Generates random sensor sensor data to test app. This class uses the singleton pattern and a
 * private constructor. Use {@link #getInstance()} to access the <code>SensorDataGenerator</code>
 * object.
 */
public class SensorDataGenerator {

    /**
     * Used to identify source class for log
     */
    private String className = getClass().getSimpleName();

    /**
    * The current instance of <code>SensorDataGenerator</code>
    */
   private static SensorDataGenerator instance = null;

   /**
    * Chance for a given DataCategory to be used returned by {@link #getData()}.
    * This value can be adjusted to allow different arrangements of test data.
    * Value should be from 0 - 1.
    */
   private final static double RETURN_CHANCE = 1;

   /**
    * Data categories to be generated
    */
   DataCategory[] dataCats = {
      new DataCategory("Carbon monoxide", "co", 0D, 1D, 2, "ppm"),
      new DataCategory("Temperature", "temp", 0D, 95D, 0, "\u00B0"),
      new DataCategory("Humidity", "humid", 10D, 100D, 0, "%"),
      new DataCategory("Nitrogen dioxide", "no2", 0D, 2D, 1, "ppm")
   };

   /**
    * Constructor. Private to prevent unauthorized instantiation.
    */
   private SensorDataGenerator() {
   }

   /**
    * Returns the current instance of <code>SensorDataGenerator</code>.
    * @return The current instance of <code>SensorDataGenerator</code>
    */
   public static SensorDataGenerator getInstance() {
      if(instance == null) {
         instance = new SensorDataGenerator();
      }
      return instance;
   }

   /**
    * Returns sensor data as an array of <code>SensorData</code> objects. Categories for which there
    * is no data are not returned.
    * @return Sensor data
    */
   public ArrayList<SensorData> getData(String sensorData) {
      ArrayList<SensorData> data = new ArrayList<>();
      // PARSE string with ';' as the delimiter between different sensors (set on Arduino code) JPM
      String[] tokens = sensorData.split(";");

      // ATTEMPT to get sound level
      // NOT WORKING! JPM
        /*
        sMeter.start();
        data.add(new SensorData("Sound Level", "Sound Level", sMeter.getAmplitude(), "DB"));
        sMeter.stop();
        */

      //for(DataCategory dataCat: dataCats) {
      for(String value: tokens){
         // FURTHER parse tokens in to bits using ':' delimiter breaking single sensor
         // data in to name, value and units (array 0, 1 and 2 respectively [set in Arduino code])
         String[] bits = value.split(":");
         // ADD data to List by INSTANTIATING SensorData object
          // Log.d(className, Arrays.toString(bits));
          try {
              data.add(new SensorData(bits[0], bits[0], Double.parseDouble(bits[1]), 0, bits[2]));
          }
          catch(ArrayIndexOutOfBoundsException aioobe) {
              // Do nothing
          }
      }
      // RETURN ArrayList containing sensor data
      return data;

   }

    /**
     * Returns sensor data as an array of <code>SensorData</code> objects. Categories for which
     * there is no data are not returned.
     * @return Sensor data
     */
    public ArrayList<SensorData> getData() {
        ArrayList<SensorData> data = new ArrayList<>();
        for(DataCategory dataCat : dataCats) {
            if(rand(0, 1) < RETURN_CHANCE) {
                data.add(new SensorData(dataCat.displayName, dataCat.shortName,
                        rand(dataCat.min, dataCat.max), dataCat.decimalPlaces,
                        dataCat.unit));
            }
        }
        return data;
    }

   /**
     * Generates a random double in the specified range.
     * @param min Minimum value
     * @param max Maximum value
     * @return random double in the range (min, max)
     */
    private double rand(double min, double max) {
        double rand = Math.random();
        rand *= (max - min);
        rand += min;
        return rand;
    }

    /**
     * Groups data for a single type of reading. Used to construct <code>SensorData</code>. Instance
     * variable are package-private to allow
     * access from <code>SensorDataGenerator</code>.
     */
    private static class DataCategory {

        /**
         * Display name for the data
         */
        String displayName;

        /**
         * Short name for the data
         */
        String shortName;

        /**
         * Minimum value
         */
        double min;

        /**
         * Maximum value
         */
        double max;

        /**
         * Number of decimal places to be displayed
         */
        int decimalPlaces;

        /**
         * Unit of measure
         */
        String unit;

        /**
         * Constructor.
         *
         * @param displayName   Display name for the data
         * @param shortName     Short name for the data
         * @param min           Minimum value
         * @param max           Maximum value
         * @param decimalPlaces Number of decimal places to be displayed
         * @param unit          Unit of measure
         */
        DataCategory(String displayName, String shortName, double min, double max,
                int decimalPlaces, String unit) {
            this.displayName = displayName;
            this.shortName = shortName;
            this.min = min;
            this.max = max;
            this.decimalPlaces = decimalPlaces;
            this.unit = unit;
        }
    }
}