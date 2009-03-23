package coilmaps;

/* LocationData.java ~ Sep 17, 2008 */



import data.Data;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

/**
 *
 * @author angus
 */
public class LocationData extends Data
{
  public int locationId;
  public double lng;
  public double lat;
  public String city;

  public LocationData(int locationId, double lng, double lat, String city)
  {
    this.locationId = locationId;
    this.lng = lng;
    this.lat = lat;
    this.city = city;
  }

   
  public static List<LocationData> getDummyLocationData(int dummies)
  {
    List<LocationData> locationDatas = new ArrayList<LocationData>();

    for (int i = 0; i < dummies; i++)
    {
       locationDatas.add(new LocationData(i, Utils.random(-180f, 180f), 
         Utils.random(-90f, 90f), Utils.randomString(4,12)));
    }

    return locationDatas;
  
  }
  

}
