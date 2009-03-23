package coilmaps;

/* CoilNode.java (created on Aug 3, 2008) */



import behaviors.Behavior;
import coilmaps.GeomCoilTreeMap.DirEnum;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import geometry.Colorf;
import geometry.media.GeomImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import utils.Utils;

/**
 *
 * @author Angus Forbes
 */
public class CoilNode 
{
	float x, y, w, h;
	float cut;
	float nextCut;
	DirEnum dir;
	CoilNode parent = null;
	CoilNode side1 = null;
	CoilNode side2 = null;
	int count = 0;
	int level;
	boolean isLeaf = false;
	boolean isLand = false;
  boolean isOpen = false;

	float south, east, north, west;

  Behavior behaviorNodeColor = null;
	//really only leaf nodes should have color and cities... make a new subclass to do...
	Colorf color = Colorf.newRandomColor(1f, 1f, .2f);
	List<CityData> cities = new CopyOnWriteArrayList<CityData>();
	GeomImage geomImage = null;
  TextureData textureData = null;
  Texture texture = null;
	
	public CoilNode(int level, CoilNode parent, float cut, DirEnum dir)
	{
		this.level = level;
		this.parent = parent;
		this.nextCut = cut;
		this.cut = cut;
		this.dir = dir;
	}

	public CoilNode(int level, CoilNode parent, float x, float y, float w, float h)
	{
		this.level = level;
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

  public void setTextureData(TextureData textureData)
  {
    this.textureData = textureData;
  }

  public void setImage(GeomImage geomImage)
  {
    this.geomImage = geomImage;
  }
	public void setPos(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public String toString()
	{
		return (Utils.tabs(level) + "CoilNode: x/y/w/h = " + x+"/"+y+"/"+w+"/"+h + "\n" +  
						Utils.tabs(level) + "w/e/s/n = " + west + "/" + east + "/" + south + "/" + north + "\n" + 
						Utils.tabs(level) + "cut is " + dir + " at " + cut + "%, isLeaf = " + isLeaf + " : COUNT = " + count);
	}
}
