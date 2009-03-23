package coilmaps;

/* CityData.java (created on Aug 4, 2008) */



import behaviors.geom.continuous.BehaviorRGBA;
import data.Data;
import geometry.Colorf;
import geometry.GeomRect;
import geometry.text.GeomText2;
import javax.vecmath.Point3f;
import renderers.State;
import utils.Utils;

/**
 *
 * @author Angus Forbes
 */
public class CityData extends Data
{
  //long textFadeSpeed = 10000L;
  long textFadeSpeed = 2000L;
  long textStartFadeTime = 1000L;
  //Colorf textFadeVec = new Colorf(-.6f, .2f, .2f, -.4f);
  Colorf textFadeVec = new Colorf(-1f, -1f, -1f, -1f);
  
	String name;
	float xperc;
	float yperc;
  GeomRect cityGeom;
  GeomText2 textGeom;
  BehaviorRGBA textBehaviorRGBA;
	public CityData(String name)
	{
		this.name = name;
    //cityGeom = new GeomRect(0f, 0f, 0f, .015f, .015f);
    cityGeom = new GeomRect(-10f, -10f, 0f, CebProperties.CITY_SIZE, CebProperties.CITY_SIZE);

    cityGeom.setColor(CebProperties.cityColor);
    cityGeom.state = new State();
    cityGeom.state.BLEND = false;

    //.13f * 2f
    textGeom = GeomText2.newGeomTextConstrainedByHeight(name, new Point3f(-10f,-10f,0f), CebProperties.CITY_TEXT_SIZE);
    textGeom.justifyX = 0;
    textGeom.setColor(CebProperties.cityTextColor);
    textGeom.state = new State();
    textGeom.state.BLEND = true;
    //textGeom.state.DEPTH_TEST = false;
    
    //textGeom.backgroundColor = new Colorf(f,0f);

    textBehaviorRGBA = BehaviorRGBA.colorChange(textGeom, 
      Utils.nowPlusMillis(textStartFadeTime), textFadeSpeed,
      textFadeVec);
	}
	
  
  public void resetTextRGBA()
  {
    textBehaviorRGBA.interruptImmediately();

    textGeom.setColor(CebProperties.cityTextColor);
    
    textBehaviorRGBA = BehaviorRGBA.colorChange(textGeom, 
      Utils.nowPlusMillis(textStartFadeTime), textFadeSpeed,
      textFadeVec);
  }
  
	public void setPercentages(float lng, float lat, float w, float e, float s, float n)
	{
		xperc = (lng - w) / (e - w);
		yperc = (lat - s) / (n - s);
	}

	@Override
	public String toString()
	{
		return "City: " + name + " xperc/yperc : " + xperc + "/" + yperc; 
	}
}
