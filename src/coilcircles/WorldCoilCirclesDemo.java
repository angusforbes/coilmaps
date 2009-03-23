/* WorldCoilMapsDemo.java (created on Aug 3, 2008) */

package coilcircles;

import coilmaps.*;
import behaviorism.BehaviorismDriver;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import geometry.Colorf;
import geometry.GeomRect;
import geometry.media.GeomImage;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import renderers.State;
import utils.DebugTimer;
import utils.FileUtils;
import utils.Utils;
import worlds.WorldGeom;

/**
 *
 * @author Angus Forbes
 */
public class WorldCoilCirclesDemo extends WorldGeom
{
  public int MIN_TREE_DEPTH = 8; //12; //12;
  public int MAX_TREE_DEPTH = 8; //12; //12;
  GeomCoilTree geomCoilTree;
  //properties
  public int numEventsToProcess = 500;
  public long minLengthOfAnimation = 60000L; //60000L;
  public long maxLengthOfAnimation = 120000L; //120000L;
  public long pauseTimeBeforeAnimation = 1000L;
  public long pauseTimeAfterAnimation = 14000L;
  public long pauseTimeAfterReforming = 5000L;
  public long minLoopSleep = 250L;
  //ConnectorCEBClient database;

  public static void main(String[] args)
  {
    //load in application specific properties
    Properties properties = loadPropertiesFile("behaviorism.properties");

    WorldGeom world = new WorldCoilCirclesDemo();
    new BehaviorismDriver(world, properties);
  }

  @Override
  public void setWorldParams(Properties properties)
  {

  }

  public void setUpWorld()
  {
    initializeTree();
  }

  public void initializeTree()
  {
    Utils.sleep(100);
    state = new State();

    state.BLEND = false;
    state.DEPTH_TEST = false;

    GeomRect worldRect = getWorldRect();
    float stw = (worldRect.w);
    float stx = worldRect.anchor.x;
    float sth = worldRect.h;
    float sty = worldRect.y;

    geomCoilTree = new GeomCoilTree(stx, sty, 0f, stw, sth);
    geomCoilTree.initializeCoilTree(MIN_TREE_DEPTH, MAX_TREE_DEPTH);
    addGeom(geomCoilTree, true);

    List<CoilNode> leafs = geomCoilTree.getLeafNodes();

    List<File> flickrFiles = FileUtils.getFilesFromDirectory(
      FileUtils.toCrossPlatformFilename("data/flickr_images/"));

    List<BufferedImage> images = new ArrayList<BufferedImage>();
    for (File f : flickrFiles)
    {
      System.out.println("file = " + f);
      BufferedImage bi = FileUtils.loadBufferedImageFromFile(f);
      images.add(bi);
    }

  Collections.shuffle(images);
  
    int imageNum = 0;
    for (CoilNode leaf : leafs)
    {

      TextureData txture = TextureIO.newTextureData(images.get(imageNum++), false);

      GeomImage gi = new GeomImage(textureData, 0f, 0f, 0f, 1f, 1f);
      gi.registerMouseoverableObject(geomCoilTree);
      gi.registerClickableObject(geomCoilTree);
      leaf.setTextureData(txture);

      geomCoilTree.addGeom(gi);
    }
    CoilNode eventCoilNode = null;
    //loop forever through animation...
    //while (true)
    {
      /*
      //System.out.println("\n\nin next loop...");
      //wait some time before starting again
      Utils.sleep(pauseTimeBeforeAnimation);

      //get most recent events and shuffle them
      long animationMS = Utils.randomLong(minLengthOfAnimation, maxLengthOfAnimation);
      long baseSleep = (long) ((float) (animationMS) / (float) locationDatas.size());
      long loopSleep;

      //loop through each event
      for (LocationData locationData : locationDatas)
      {
      //System.out.println(locationData.city);

      float lng = (float) locationData.lng;
      float lat = (float) locationData.lat;

      if (lng == 0f && lat == 0f)
      {
      //System.out.println("breaking... lng/lat = 0f/0f... ");
      continue;
      }

      //find appropriate tree for current event's lng/lat
      currentGct = GeomCoilTreeMap.findCoilTreeUsingLngLat(lng, lat, geomCoilTree, gct2, gct3);

      if (currentGct == null) //this could happen because we are truncating the latitudes of the map to ignore antartica, etc.
      {
      System.out.println("couldn't find GeomCoilTree for lng/lat " + lng + "/" + lat);
      continue;
      }

      //find appropriate leaf node within the current CoilTree
      eventCoilNode = currentGct.findLeafNodeUsingLngLat(lng, lat);

      if (eventCoilNode == null) //actually, this should never happen
      {
      System.out.println("couldn't find a CoilNode for " + lng + "/" + lat);
      continue;
      }

      //this is to make sure island cells are correctly colored (eg, hawaii cell is initially colored blue)
      if (!eventCoilNode.isLand)
      {
      eventCoilNode.color = new Colorf(CebProperties.landColor);
      }

      //check to see if we've already come across this event's city, otherwise create it and add it
      cityData = cityHash.get(locationData.locationId);
      if (cityData == null)
      {
      //System.out.println("NEW CITY : <" + locationData.city + ">");
      cityData = new CityData(locationData.city);
      cityData.setPercentages(lng, lat,
      eventCoilNode.west, eventCoilNode.east, eventCoilNode.south, eventCoilNode.north);

      currentGct.addCityToNode(cityData, eventCoilNode);

      cityHash.put(locationData.locationId, cityData);
      }
      else
      {
      //reset the color of the city's text, since it most likely has faded by now.
      cityData.resetTextRGBA();
      }
      //System.out.println("city = " + cityData + " lng/lat " + lng + "/" + lat);

      //increment the total number of events registered to this CoilNode (which causes the movement of the cells)
      currentGct.incrementCount(eventCoilNode, false);


      //sleep for an appropriate amount of time
      loopSleep = Utils.randomLong(baseSleep - (long) (baseSleep * .5f), baseSleep + (long) (baseSleep * .5f));
      //System.out.println("loopSleep = " + loopSleep);

      Utils.sleep(Math.max(minLoopSleep, loopSleep));
      //Utils.sleep(3000);
      }

      //now we've added all of the events to the map. pause a bit and then rewind...
      Utils.sleep(pauseTimeAfterAnimation);

      //trigger rewinding for all CoilTrees; keeping track of how long it will take
      long reformTime = 0L;
      long tryTime;
      tryTime = geomCoilTree.reformToUniform();
      if (tryTime > reformTime)
      {
      reformTime = tryTime;
      }
      tryTime = gct2.reformToUniform();
      if (tryTime > reformTime)
      {
      reformTime = tryTime;
      }
      tryTime = gct3.reformToUniform();
      if (tryTime > reformTime)
      {
      reformTime = tryTime;
      }

      //pause until all rewinding is finished, plus a little bit more time
      Utils.sleep(reformTime + pauseTimeAfterReforming);

      //reset all cuts in CoilTrees, this also removes the city geoms from the tree
      geomCoilTree.resetCuts();
      gct2.resetCuts();
      gct3.resetCuts();

      //flush all cities after each loop
      locationDatas.clear();
      cityHash.clear();

      
      //reset to original map colors
      geomCoilTree.assignColorsToLeafs("americas.png");
      gct2.assignColorsToLeafs("africa.png");
      gct3.assignColorsToLeafs("asia.png");

       */
    }

  }

  @Override
  public boolean checkKeys(final boolean[] keys, final boolean[] keysPressing)
  {
    if (keys[KeyEvent.VK_A])
    {
      if (keysPressing[KeyEvent.VK_A] == false)
      {
        this.geomCoilTree.reformToUniform();
        keysPressing[KeyEvent.VK_A] = true;
      }
      return true;
    }

    if (keys[KeyEvent.VK_B])
    {
      if (keysPressing[KeyEvent.VK_B] == false)
      {
        this.geomCoilTree.resetCuts();
        keysPressing[KeyEvent.VK_B] = true;
      }
      return true;
    }

    if (keys[KeyEvent.VK_Z])
    {
      if (keysPressing[KeyEvent.VK_Z] == false)
      {
        keysPressing[KeyEvent.VK_Z] = true;

        Thread t = new Thread()
        {

          public void run()
          {
            DebugTimer dt = new DebugTimer();

            System.out.println("ZZZZZZZZZZZ");
            List<CoilNode> leafNodes = geomCoilTree.getLeafNodes();
            Collections.shuffle(leafNodes);
            int maxTry = 21;
            int num = 0;

            Integer[] ints = Utils.randomArrayOfInts(100, 1000, maxTry);

            for (CoilNode leaf : leafNodes)
            {
              if (++num >= maxTry)
              {
                break;
              }
              dt.resetTime();
              geomCoilTree.incrementCount(leaf, ints[num], 200);
              System.out.println("num = " + num + " took " + dt.resetTime() + " ms");

            }
            Utils.sleep(200);
            num = 0;
            for (CoilNode leaf : leafNodes)
            {
              if (++num >= maxTry)
              {
                break;
              }
              dt.resetTime();
              geomCoilTree.incrementCount(leaf, -ints[num], 20000);
              System.out.println("num = " + num + " took " + dt.resetTime() + " ms");
            }
          }
        };

        t.start();
      }
      return true;
    }

    if (keys[KeyEvent.VK_S])
    {
      if (keysPressing[KeyEvent.VK_S] == false)
      {
        keysPressing[KeyEvent.VK_S] = true;

        Thread t = new Thread()
        {

          public void run()
          {
            List<CoilNode> leafNodes = geomCoilTree.getLeafNodes();

            //while (true)
            {
              for (int i = 0; i < 200; i++)
              {

                int which = Utils.randomInt(0, leafNodes.size() - 1);
                CoilNode leaf = leafNodes.get(which);


                //geomCoilTree.highlightLeaf(leaf, 100, 4000);
                geomCoilTree.highlightLeaf(leaf, 50, Utils.randomLong(30, 30));

                //Utils.sleep(500);
                //Utils.sleep(250);

                Utils.sleep(Utils.randomLong(2, 50));
              }

              //Utils.sleep(Utils.randomLong(1000, 5000));

            }
          }
        };

        t.start();
      }
      return true;
    }

    return false;
  }
}
