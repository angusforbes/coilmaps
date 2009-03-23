/* CebEventData.java : Jul 14, 2008 : Angus Forbes */
package coilmaps;

import data.Data;
import data.DataEnum;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Angus Forbes
 */
public class CebEventData extends Data {

   public enum EventDataEnum {

      TIMESTAMP, TAGS, TITLE, SECTOR, PRACTICE, PROGRAM, CONTENT, LNG, LAT
   };
   public int id;
   public long timestamp;
   public List<String> tags;
   public String title;
   public String sector;
   public String practice;
   public String program;
   public int userId;
   public String content;
   public double lng;
   public double lat;
	 public String city;

	 public CebEventData(int id, long timestamp, List<String> tags,
           String title, String sector, String practice, String program, int userId, String content,
           double lng, double lat, String city) {
      this.id = id;
      this.timestamp = timestamp;
      this.tags = tags;
      this.title = title;
      this.sector = sector;
      this.practice = practice;
      this.program = program;
      this.userId = userId;
      this.content = content;
      this.lng = lng;
      this.lat = lat;
			this.city = city;
      //is this even useful to use anymore???
      this.dataType = DataEnum.CEB_EVENT_DATA;
   }

   @Override
   public String toString() {
      String s = "CebEventData (" + id + ") : " + timestamp + "\n";
      s += "tags: " + Arrays.toString(tags.toArray()) + "\n";
      s += "title: " + title + "\n";
      s += "content: " + content + "\n";
      s += "sector: " + sector + "\n";
      s += "practice: " + practice + "\n";
      s += "program: " + program + "\n";
			s += "city: " + city +"\n";
      s += "lng/lat: " + lng + "/" + lat;
      return s;
   }

   public String getValue(EventDataEnum ede) {
      switch (ede) {
         case PROGRAM:
            return program;
         case PRACTICE:
            return practice;
         case SECTOR:
            return sector;
         case TITLE:
            return title;
         case CONTENT:
            return content;
         default:
            return null;
      }
   }
}
