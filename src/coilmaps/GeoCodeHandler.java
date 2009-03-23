package coilmaps;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import javax.vecmath.Point3f;

/**
 *
 * @author gregoryshear
 */
public class GeoCodeHandler {

   private final static String ENCODING = "UTF-8";
   private final static String KEY = "ABQIAAAAnfs7bKE82qgb3Zc2YyS-oBT2yXp_ZAY8_ufC3CFXhHIE1NvwkxSySz_REpPq-4WZA27OwgbtyR3VcA";

   public static class Location {

      public String lon; //x-axis
      public String lat; //y-axis

      public Location(String lon, String lat) {
         this.lon = lon;
         this.lat = lat;
      }

      public Location(double lon, double lat) {
         this.lon = "" + lon;
         this.lat = "" + lat;
      }

      public double lat() {
         return Double.parseDouble(lat);
      }

      public double lng() {
         return Double.parseDouble(lon);
      }

      @Override
      public String toString() {
         return "Lng: " + lon + ", Lat: " + lat;
      }
   }

   public static Location getLocation(String address) throws IOException {
      BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://maps.google.com/maps/geo?q=" + URLEncoder.encode(address, ENCODING) + "&output=csv&key=" + KEY).openStream()));
      String line;
      Location location = null;
      int statusCode = -1;
      while ((line = in.readLine()) != null) {
			// Format: 200,6,42.730070,-73.690570
         statusCode = Integer.parseInt(line.substring(0, 3));
         if (statusCode == 200) {
            //angus - i changed this because it is driving me crazy that 
            //in different parts of the program we order lng and lat in different ways.
            //At least for now the program will always order coordinates
            //like so: LONG(x-axis)/LAT(y-axis)
            String lat = line.substring("200,6,".length(), line.indexOf(',', "200,6,".length()));
            String lng = line.substring(line.indexOf(',', "200,6,".length()) + 1, line.length());
            location = new Location(lng, lat);
         }
      }
      if (location == null) {
         switch (statusCode) {
            case 400:
               throw new IOException("Bad Request");
            case 500:
               throw new IOException("Unknown error from Google Encoder");
            case 601:
               throw new IOException("Missing query");
            case 602:
               return null;
            case 603:
               throw new IOException("Legal problem");
            case 604:
               throw new IOException("No route");
            case 610:
               throw new IOException("Bad key");
            case 620:
               throw new IOException("Too many queries");
         }
      }
      return location;
   }
}//   public static void main(String[] argv) throws Exception {
//      System.out.println(GeoCodeHandler.getLocation("New York"));
//   }
//}
