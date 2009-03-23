package coilcubes;

/* GeomCoilTree.java (created on Jan 26, 2008) */
import coilcircles.*;
import coilmaps.*;
import behaviorism.BehaviorismDriver;
import behaviors.Behavior;
import coilmaps.GeomCoilTreeMap.DirEnum;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;
import geometry.Colorf;
import geometry.GeomRect;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.ImageIcon;
import renderers.State;
import utils.Utils;

/**
 *
 * @author Angus Forbes
 */
public class GeomCoilTree extends GeomRect
{
  //longer cutSpeed times make it appear more oozing and spreading...
  //smaller times are quick and jarring
  //final public long cutSpeed = 5000L; //100L;

  final public long cutSpeed = 500L; //100L;
  final public long MIN_cutSpeedWhenReforming = 250L; //100L;
  final public long MAX_cutSpeedWhenReforming = 1500L; //100L;
  final public long cutSpeedWhenReforming = 250L; //100L;
  final public long cutPause = 0L; //50L;
  public boolean isReforming = false; //triggers draw mode
//
//  public static enum DirEnum
//  {
//    HORIZONTAL, VERTICAL
//  };
  List<CoilNode> leafNodes = new CopyOnWriteArrayList<CoilNode>();
  List<CoilNode> cutNodes = new CopyOnWriteArrayList<CoilNode>();
  float west, east, south, north;
  CoilNode rootNode;
  boolean isInitialized;

  /**
   * Constructs a GeomCoilTreeMap at a specified position with the specified dimensions
   * covering a specified range of the earth.
   * @param x 
   * @param y
   * @param z
   * @param w
   * @param h
   */
  public GeomCoilTree(float x, float y, float z, float w, float h)
  {
    super(x, y, z, w, h);
    setColor(.5f, .5f, .5f);
    init();

    this.state = new State();
  }

  public void init()
  {
    this.rootNode = null;
    isInitialized = false;


    this.rootNode = new CoilNode(0, null, 0f, 0f, w, h);
    this.rootNode.dir = DirEnum.HORIZONTAL;
    this.rootNode.cut = .5f;
    this.rootNode.nextCut = .5f;

  }

  /**
   * Increments the current count of a particular leaf node, which then propogates 
   * the increment recursively to its ancestors.
   * @param node The leaf node whose count is being incremented.
   */
  public void incrementCount(CoilNode node)
  {
    incrementCount(node, false);
  }

  /**
   * Recursive increment count method. If firstTime == true, then each leaf node
   * is given a count of 1, which is then propogated to its ancestors. So, the leaf node
   * would have count = 1, its parent count = 2, its granparent count = 4, etc. 
   * [ 2^(branch depth - current depth) ]. If firstTime == false, then we have optional parameters
   * to tweak the count increment and to limit the maximum count of any one node. 
   * @param node
   * @param firstTime 
   */
  public void incrementCount(CoilNode node, boolean firstTime)
  {
    int countIncrease = 5;

    //this doesn't work -- doesn't handle parents, just leaves!
    //if (node.count > 100)
    //{
    //return;
    //}

    node.count++;

    if (!firstTime)
    {
      node.count += countIncrease;
    //System.out.println("node = " + node + ", node.count = " + node.count);
    }

    CoilNode parentNode = node.parent;

    while (parentNode != null)
    {
      parentNode.count++;

      if (!firstTime)
      {
        parentNode.count += countIncrease;
      }
      parentNode = parentNode.parent;
    }

    if (!firstTime)
    {
      updateCuts(node);
    }

  //System.out.println("node.count = " + node.count);
  //updatePositions(rootNode); //this now gets called in draw method...
  }

  public void incrementCount(CoilNode node, int increment)
  {
    int countIncrease = increment;

    //this doesn't work -- doesn't handle parents, just leaves!
    //if (node.count > 100)
    //{
    //return;
    //}

    node.count += countIncrease;

    CoilNode parentNode = node.parent;

    while (parentNode != null)
    {
      parentNode.count += countIncrease;
      parentNode = parentNode.parent;
    }

    updateCuts(node);

  //updatePositions(rootNode); //this now gets called in draw method...
  }

  public void incrementCount(CoilNode node, int increment, long speed)
  {
    int countIncrease = increment;

    //this doesn't work -- doesn't handle parents, just leaves!
    //if (node.count > 100)
    //{
    //return;
    //}

    node.count += countIncrease;

    CoilNode parentNode = node.parent;

    while (parentNode != null)
    {
      parentNode.count += countIncrease;
      parentNode = parentNode.parent;
    }

    updateCuts(node, speed);

  //updatePositions(rootNode); //this now gets called in draw method...
  }

  /**
   * Update a CoilNode's cut to a specific percentage (must be between 0f and 1f). This 
   * version of updateCut is used by reformToUniform(). The speed of the cut is defined 
   * by the global parameter "cutSpeed".
   * 
   * @param node The node whose cut percentage is being updated
   * @param percentage A value between 0f (side1 is fully closed) and 1f (side1 is fully open).
   */
  public void updateCut(CoilNode node, float percentage)
  {
    if (node.isLeaf == true)
    {
      return; //leaf nodes are not cut...
    }

    //Behavior bcc = BehaviorCoilCut.coilCut(node, Utils.nowPlusMillis(50L), cutSpeed, percentage - node.cut);
    Behavior bcc = BehaviorCoilCut.coilCut(node, Utils.nowPlusMillis(50L), cutSpeedWhenReforming, percentage - node.cut);
    this.attachBehavior(bcc);
  }

  /**
   * Update the CoilNode's cut percentage based on its children's counts.
   * The speed of the cut is defined by the global parameter "cutSpeed".
   * 
   * @param node The node whose cut percentage is being updated.
   */
  public void updateCut(CoilNode node)
  {
    if (node.isLeaf == true)
    {
      return; //leaf nodes are not cut...
    }

    float nextPos = (float) ((float) node.side1.count / (float) (node.side1.count + node.side2.count));
    Behavior bcc = BehaviorCoilCut.coilCut(node, Utils.nowPlusMillis(50L), cutSpeed, nextPos - node.cut);

    this.attachBehavior(bcc);

  //System.out.println("updating parent..., moving cut to " + nextPos + 
  //				" " + parentNode.side1.count + "/" + (parentNode.side1.count + parentNode.side2.count) );
  }

  /**
   * Update a CoilNode's parent to a new percentage based upon the number of counts
   * in the current node and its sibling. Is invoked by incrementCount when a leaf node 
   * receives a new event. The updateCut call propagates recurdively back up to the
   * rootNode of this GeomCoilTreeMap.
   * 
   * @param node The CoilNode whose count has changed (ie, first invoked using a leaf node).
   */
  public void updateCuts(CoilNode node)
  {
    CoilNode parentNode = node.parent;

    if (parentNode != null)
    {
      float nextCut = (float) ((float) parentNode.side1.count / (float) (parentNode.side1.count + parentNode.side2.count));

      //System.out.println("\n parentnextCut / nextCut = " + parentNode.nextCut + "/" + nextCut );
      Behavior bcc = BehaviorCoilCut.coilCut(parentNode, Utils.nowPlusMillis(50L), cutSpeed,
        nextCut - parentNode.nextCut);

      parentNode.nextCut = nextCut;
      this.attachBehavior(bcc);


      //System.out.println("updating parent..., moving cut to " + nextCut + 
      //				" " + parentNode.side1.count + "/" + (parentNode.side1.count + parentNode.side2.count) );

      //Utils.sleep(0L); //cutPause);

      updateCuts(parentNode);
    }
  }

  public void updateCuts(CoilNode node, long speed)
  {
    CoilNode parentNode = node.parent;

    if (parentNode != null)
    {
      float nextCut = (float) ((float) parentNode.side1.count / (float) (parentNode.side1.count + parentNode.side2.count));

      //System.out.println("\n parentnextCut / nextCut = " + parentNode.nextCut + "/" + nextCut );
      Behavior bcc = BehaviorCoilCut.coilCut(parentNode, Utils.nowPlusMillis(50L), speed,
        nextCut - parentNode.nextCut);

      parentNode.nextCut = nextCut;
      this.attachBehavior(bcc);

      //System.out.println("updating parent..., moving cut to " + nextCut +
      //				" " + parentNode.side1.count + "/" + (parentNode.side1.count + parentNode.side2.count) );

      //Utils.sleep(0L); //cutPause);

      updateCuts(parentNode, speed);
    }
  }

  /**
   * @return A list of each leaf node.
   */
  public List<CoilNode> getLeafNodes()
  {
    return leafNodes;
  }

  /** 
   * @return A unique List of the parent CoilNodes of each leaf node. 
   * That is, since side1 and side2 both have the same parent,
   * this method makes sure to include it only once.
   */
  public List<CoilNode> getLeafNodeParents()
  {
    Set<CoilNode> parentSet = new HashSet<CoilNode>();

    for (CoilNode leaf : leafNodes)
    {
      parentSet.add(leaf.parent);
    }

    return new ArrayList<CoilNode>(parentSet);
  }

  public void updateColors()
  {
    int minCnt = 400; //200

    float div = 500f; //500f;
    //float incr = (1f - CebProperties.landColor.r) / div;
    //float incg = (1f - CebProperties.landColor.g) / div;
    //float incb = (1f - CebProperties.landColor.b) / div;
    float incr = (CebProperties.brightLandColor.r - CebProperties.landColor.r) / div;
    float incg = (CebProperties.brightLandColor.g - CebProperties.landColor.g) / div;
    float incb = (CebProperties.brightLandColor.b - CebProperties.landColor.b) / div;

    float tempCnt;

    for (CoilNode leaf : leafNodes)
    {

      if (leaf.isLand == false && leaf.count <= 1)
      {
        continue;
      }

//      if (leaf.count > 1)
//      {
//        System.out.println("leaf = " + leaf);
//      }

      if (leaf.count > 1 && leaf.count < minCnt)
      {
        tempCnt = minCnt;
      }
      else
      {
        tempCnt = leaf.count;
      }

      leaf.color.r = CebProperties.landColor.r + (incr * (float) tempCnt);
      leaf.color.g = CebProperties.landColor.g + (incg * (float) tempCnt);
      leaf.color.b = CebProperties.landColor.b + (incb * (float) tempCnt);

      if (leaf.color.r > 1f)
      {
        leaf.color.r = 1f;
      }
      if (leaf.color.g > 1f)
      {
        leaf.color.g = 1f;
      }
      if (leaf.color.b > 1f)
      {
        leaf.color.b = 1f;
      }
    }

  }

  /**
   * Called by the draw method to recurse through the entire tree to calculate the
   * current positions of each CoilNode based upon their parent's position and 
   * cut percentage.
   * @param parent
   */
  private void updatePositions(CoilNode parent)
  {
    if (parent.isLeaf == true)
    {
      return;
    }

    switch (parent.dir)
    {
      case HORIZONTAL:
        parent.side1.setPos(parent.x, parent.y,
          parent.w, parent.h * parent.cut);
        parent.side2.setPos(parent.x, parent.y + (parent.h * parent.cut),
          parent.w, parent.h - (parent.h * parent.cut));

        break;

      case VERTICAL:
        parent.side1.setPos(parent.x, parent.y,
          parent.w * parent.cut, parent.h);
        parent.side2.setPos(parent.x + (parent.w * parent.cut), parent.y,
          parent.w - (parent.w * parent.cut), parent.h);

        break;
    }

    updatePositions(parent.side1);
    updatePositions(parent.side2);
  }

  /**
   * Calculate the initial positions of each CoilNode.
   * @param parent
   */
  public void initializePositions(CoilNode parent)
  {
    updatePositions(parent);
  }

  /**
   * Initialize the lat/lng of each CoilNode recursively based upon their initial position
   * within the GeomCoilTreeMap.
   * @param node
   */
  public void initializeLngLats(CoilNode node)
  {
    float xperc1 = (node.x) / this.w;
    float xperc2 = (node.x + node.w) / this.w;
    float yperc1 = (node.y) / this.h;
    float yperc2 = (node.y + node.h) / this.h;

    node.west = this.west + ((this.east - this.west) * xperc1);
    node.east = this.west + ((this.east - this.west) * xperc2);
    node.south = this.south + ((this.north - this.south) * yperc1);
    node.north = this.south + ((this.north - this.south) * yperc2);

    if (node.isLeaf == true)
    {
      return;
    }

    initializeLngLats(node.side1);
    initializeLngLats(node.side2);
  }

  /**
   * Initializes the CoilTree with a specified depth of uniformly divided CoilNodes. 
   * @param maxLevel
   */
  public void initializeCoilTree(int minLevel, int maxLevel)
  {
    initializeCoilTree(minLevel, maxLevel, .5f, .5f);
  }

  public void initializeCoilTree(int maxLevel)
  {
    initializeCoilTree(maxLevel, maxLevel, .5f, .5f);
  }

  /**
   * Initialilzes the CoilTree so that the depth of any branch is between minLevel and maxLevel,
   * and the cut percentage of any CoilNode is the range of minCut and maxCut.
   * @param minLevel The minimum depth of any branch of this tree.
   * @param maxLevel The maximum depth of any branch of this tree.
   * @param minCut The minimum cut percentage for any CoilNode in this tree.
   * @param maxCut The maximum cut percentage for any CoilNode in this tree.
   */
  public void initializeCoilTree(int minLevel, int maxLevel, float minCut, float maxCut)
  {

    initializeCoilNodes(rootNode, 1, minLevel, maxLevel, minCut, maxCut);

    for (CoilNode leafNode : leafNodes)
    {
      incrementCount(leafNode, true);
    }

    initializePositions(rootNode);
    //initializeLngLats(rootNode);

    isInitialized = true;
  }

  /**
   * Recursively defines the CoilNodes up to a specified depth with a specified cut percentage.
   * 
   * @param parent
   * @param level
   * @param minLevel
   * @param maxLevel
   * @param minCut
   * @param maxCut
   */
  private void initializeCoilNodes(CoilNode parent, int level, int minLevel, int maxLevel, float minCut, float maxCut)
  {
    cutNodes.add(parent);

    parent.isLeaf = false;

    switch (parent.dir)
    {
      case HORIZONTAL:
        parent.side1 = new CoilNode(level, parent, Utils.randomFloat(minCut, maxCut), DirEnum.VERTICAL);
        parent.side2 = new CoilNode(level, parent, Utils.randomFloat(minCut, maxCut), DirEnum.VERTICAL);
        break;
      case VERTICAL:
        parent.side1 = new CoilNode(level, parent, Utils.randomFloat(minCut, maxCut), DirEnum.HORIZONTAL);
        parent.side2 = new CoilNode(level, parent, Utils.randomFloat(minCut, maxCut), DirEnum.HORIZONTAL);
        break;
    }

    if (level == maxLevel || (level >= minLevel && Utils.random() > .5))
    {
      //calc color...
      parent.side1.isLeaf = true;
      leafNodes.add(parent.side1);
      parent.side2.isLeaf = true;
      leafNodes.add(parent.side2);

      return;
    }

    initializeCoilNodes(parent.side1, level + 1, minLevel, maxLevel, minCut, maxCut);
    initializeCoilNodes(parent.side2, level + 1, minLevel, maxLevel, minCut, maxCut);
  }

  public void resetCuts()
  {
    List<CoilNode> nodes1 = flatten();
    for (CoilNode node : nodes1)
    {
      node.cut = .5f;
      node.nextCut = .5f;
    }

    isReforming = false;
  }

  /**
   * Rewinds the CoilTree to uniform positions. The speed of the rewinding is
   * specified by the global parameters cutSpeed and cutPause.	 
   */
  public long reformToUniform()
  {
    isReforming = true;

    long now = System.nanoTime();
    long inc = Utils.millisToNanos(cutPause);
    final List<CoilNode> nodes = flatten();
    long stTime = now;
    long timeToSleep = 0L;

    for (CoilNode node : nodes)
    {
      node.count = 0;
    }

    for (CoilNode leafNode : leafNodes)
    {
      incrementCount(leafNode, true);
    }

    Collections.shuffle(nodes);
    for (CoilNode node : nodes)
    {
      if (node.cut != .5f)
      {

        if (node.isLeaf == true)
        {
          continue;
        }

        //Behavior bcc = BehaviorCoilCut.coilCut(node, Utils.nowPlusMillis(50L), cutSpeed, percentage - node.cut);
        Behavior bcc = BehaviorCoilCut.coilCut(node,
          stTime,
          Utils.randomLong(MIN_cutSpeedWhenReforming, MAX_cutSpeedWhenReforming), .5f - node.cut);
        this.attachBehavior(bcc);

        stTime += inc;
        timeToSleep += inc;
      }
    }


    return Utils.nanosToMillis(timeToSleep) + cutSpeedWhenReforming;




  /*
  for (CoilNode node : nodes)
  {
  if (node.cut != .5f)
  {
  timeToSleep += cutPause;
  }
  }
  
  
  Thread thread = new Thread()
  {
  public void run()
  {
  for (CoilNode node : nodes)
  {
  node.count = 0;
  }
  
  for (CoilNode leafNode : leafNodes)
  {
  incrementCount(leafNode, true);
  }
  
  Collections.shuffle(nodes);
  for (CoilNode node : nodes)
  {
  if (node.cut != .5f)
  {
  updateCut(node, .5f);
  Utils.sleep(cutPause);
  }
  }
  
  
  }
  };
  
  thread.start();
   */
  //return timeToSleep + 1000L + cutSpeedWhenReforming;
  }

  /**
   * Returns a list view of all nodes in the tree.
   * @return a List of CoilNodes.
   */
  public List<CoilNode> flatten()
  {
    List<CoilNode> nodes = new ArrayList<CoilNode>();
    flatten(rootNode, nodes);
    return nodes;
  }

  /**
   * Recursively gathers each node in the tree. Called by the public version of flatten().
   * @param node
   * @param nodes
   */
  private void flatten(CoilNode node, List<CoilNode> nodes)
  {
    //System.out.println("adding... " + node);
    nodes.add(node);

    if (node.isLeaf == true)
    {
      return;
    }
    else
    {
      flatten(node.side1, nodes);
      flatten(node.side2, nodes);
    }
  }

  /** 
   * Prints out the entire tree, starting from the rootNode down.
   */
  public void printTree()
  {
    printNodes(rootNode);
  }

  /**
   * Recursively prints out the specified node and its descendants.
   * @param node The top node of the branch we wish to print. 
   */
  public void printNodes(CoilNode node)
  {
    System.out.println(node);

    if (node.side1 != null)
    {
      System.out.println("" + Utils.tabs(node.level) + "side 1 :");
      printNodes(node.side1);
    }
    if (node.side2 != null)
    {
      System.out.println("" + Utils.tabs(node.level) + "side 2 :");
      printNodes(node.side2);
    }
  }

  /**
   * Given a list of multiple GeomCoilTrees return the first one that contains
   * the specified latlng.
   * @param lng
   * @param lat
   * @param trees
   * @return
   */
  public static GeomCoilTree findCoilTreeUsingLngLat(float lng, float lat, GeomCoilTree... trees)
  {
    for (GeomCoilTree gct : trees)
    {
      if (lng >= gct.west && lng < gct.east && lat >= gct.south && lat < gct.north)
      {
        return gct;
      }
    }

    return null;
  }

  /**
   * Determine the unique leaf node containing the specified latlng.
   * @param lng
   * @param lat
   * @return A leaf node.
   */
  public CoilNode findLeafNodeUsingLngLat(float lng, float lat)
  {
    for (CoilNode leaf : leafNodes)
    {
      if (lng >= leaf.west && lng < leaf.east && lat >= leaf.south && lat < leaf.north)
      {
        return leaf;
      }
    }

    return null;
  }

  /**
   * Determine the unique leaf node at the specified position. 
   * @param x
   * @param y
   * @return A leaf node.
   */
  public CoilNode findLeafNode(float x, float y)
  {
    return findLeafNode(rootNode, x, y);
  }

  /**
   * Determine the unique leaf node at the specified position, checking 
   * only the leaf nodes descended from a specified CoilNode. 
   * @param x
   * @param y
   * @return A leaf node.
   */
  public CoilNode findLeafNode(CoilNode node, float x, float y)
  {
    CoilNode which = null;

    switch (node.dir)
    {
      case VERTICAL:
        //System.out.print("is col (" + col + ") < " + node.side2.col + "? ");
        if (x < node.side2.x)
        {
          //System.out.println("...yes, use side1 ");
          which = node.side1;
        }
        else
        {
          //System.out.println("...no, use side2 ");
          which = node.side2;
        }
        break;

      case HORIZONTAL:
        //System.out.print("is row (" + row + ") < " + node.side2.row + "? ");
        if (y < node.side2.y)
        {
          //System.out.println("...yes, use side1 ");
          which = node.side1;
        }
        else
        {
          //System.out.println("...no, use side2 ");
          which = node.side2;
        }
        break;
    }

    if (which.isLeaf == true)
    {
      return which;
    }
    else
    {
      return findLeafNode(which, x, y);
    }
  }

  /** 
   * Assigns land or sea colors to each CoilNode in the tree based on 
   * a specifed map image whose coordinates must match the coordinates 
   * of this GeomCoilTreeMap.
   * @param filename
   */
  public void assignColorsToLeafs(String filename)
  {
    BufferedImage bimage = Utils.toBufferedImage(new ImageIcon("data/images/" + filename).getImage());
    int iw = bimage.getWidth();
    int ih = bimage.getHeight();

    for (CoilNode leaf : leafNodes)
    {
      float xp1 = leaf.x / rootNode.w;
      float xp2 = (leaf.x + leaf.w) / rootNode.w;
      float yp1 = 1f - ((leaf.y + leaf.h) / rootNode.h);

      float yp2 = 1f - (leaf.y / rootNode.h);
      //System.out.printf("PERCENTS xp1:%f xp2:%f yp1:%f yp2:%f %n", xp1, xp2, yp1, yp2);

      int px1 = (int) (xp1 * (float) iw);
      int px2 = (int) (xp2 * (float) iw);
      int py1 = (int) (yp1 * (float) ih);
      int py2 = (int) (yp2 * (float) ih);

      //System.out.printf("PIXELS x1:%d x2:%d y1:%d y2:%d %n%n", px1, px2, py1, py2);
      int landTally = 0;
      int seaTally = 0;

      for (int col = px1; col < px2; col++)
      {
        for (int row = py1; row < py2; row++)
        {
          int argb = bimage.getRGB(col, row);
          //int alpha = (argb >> 24) & 0xff;
          int red = (argb >> 16) & 0xff;
          //int green = (argb >> 8) & 0xff;
          //int blue = argb & 0xff;

          if (red < 190) //if darker than med gray
          {
            landTally++;
          }
          else
          {
            seaTally++;
          }
        }
      }

      int totalTally = landTally + seaTally;
      float percent = ((float) landTally / (float) totalTally);
      if (percent > .01f)
      {
        leaf.color = new Colorf(CebProperties.landColor);
        leaf.isLand = true;
      }
      else
      {
        leaf.color = new Colorf(CebProperties.seaColor);
        leaf.isLand = false;
      }
    }

  }
  boolean drawLines = true;
  boolean drawRects = true;
  boolean drawCities = true;
  boolean drawNames = true;

  @Override
  public void draw(GL gl, GLU glu, float offset)
  {
    //super.draw(gl, glu, offset);

    if (rootNode == null)
    {
      return;
    }
    if (isInitialized == false)
    {
      return;
    }
    if (isReforming == false)
    {
      //updateColors();
    }

    updatePositions(rootNode);


    for (CoilNode node : leafNodes)
    {
      if (node.textureData != null)
      {
        if (node.texture == null)
        {
          node.texture = TextureIO.newTexture(node.textureData);
        }
        gl.glEnable(GL.GL_TEXTURE_2D);

        node.texture.bind();

        gl.glColor4fv(node.color.array(), 0);

        gl.glBegin(gl.GL_QUADS);

        TextureCoords tc = node.texture.getImageTexCoords();

        gl.glTexCoord2f(tc.left(), tc.bottom());
        gl.glVertex3f(node.x, node.y, offset);
        gl.glTexCoord2f(tc.right(), tc.bottom());
        gl.glVertex3f(node.x + node.w, node.y, offset);
        gl.glTexCoord2f(tc.right(), tc.top());
        gl.glVertex3f(node.x + node.w, node.y + node.h, offset);
        gl.glTexCoord2f(tc.left(), tc.top());
        gl.glVertex3f(node.x, node.y + node.h, offset);

        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
      }
    }



    //state.BLEND = false;

    if (drawLines == true)
    {
      float lineOffset = .001f;
      //draw lines
      gl.glLineWidth(.5f);
      //gl.glColor4f(r, g, b, a);
      gl.glColor4fv(CebProperties.lineColor.array(), 0);
      gl.glBegin(gl.GL_LINES);

      for (CoilNode node : cutNodes)
      {
        switch (node.dir)
        {
          case HORIZONTAL:

            gl.glVertex3f(node.side2.x, node.side2.y, lineOffset);
            gl.glVertex3f(node.side2.x + node.side2.w, node.side2.y, lineOffset);
            break;
          case VERTICAL:
            gl.glVertex3f(node.side2.x, node.side2.y, lineOffset);
            gl.glVertex3f(node.side2.x, node.side2.y + node.side2.h, lineOffset);
            break;
        }
      }
      gl.glEnd();

      /*

      //also draw line around entire tree?
      gl.glLineWidth(.25f);
      //gl.glColor4f(1f, 0f,0f,1f);

      gl.glBegin(gl.GL_LINE_LOOP);

      gl.glVertex3f(0f, 0f, 0f);
      gl.glVertex3f(w, 0f, 0f);
      gl.glVertex3f(w, 0f, 0f);
      gl.glVertex3f(w, h, 0f);
      gl.glVertex3f(w, h, 0f);
      gl.glVertex3f(0f, h, 0f);

      gl.glEnd();
       */
    }

  }

  @Override
  public void doubleClickAction(MouseEvent me)
  {
    System.out.println("in mouseDoubleClick");

    Thread t = new Thread()
    {

      public void run()
      {
        CoilNode leaf = findLeafNode(BehaviorismDriver.mouseListener.mouseGeom.x, BehaviorismDriver.mouseListener.mouseGeom.y);

        if (leaf.isOpen == true)
        {
          System.out.println("IS OPEN");
          incrementCount(leaf, -(leaf.count) + 1);
          if (leaf.behaviorNodeColor != null)
          {
            leaf.behaviorNodeColor.interruptImmediately();
          }
          //leaf.behaviorNodeColor = BehaviorNodeColor.nodeColor(leaf, Utils.now(), 2000L, .2f - leaf.color.a);
          //attachBehavior(leaf.behaviorNodeColor);

          leaf.isOpen = false;
        }
        else
        {
          System.out.println("IS CLOSED");
          incrementCount(leaf, 100);
          leaf.isOpen = true;
          if (leaf.behaviorNodeColor != null)
          {
            leaf.behaviorNodeColor.interruptImmediately();
          }
          Utils.sleep(300);
          //leaf.behaviorNodeColor = BehaviorNodeColor.nodeColor(leaf, Utils.now(), 100L, 1f - leaf.color.a);
          //attachBehavior(leaf.behaviorNodeColor);

        }
      }
    };
    t.start();

  }

  @Override
  public void mouseOverAction(MouseEvent me)
  {
    
    System.out.println("in mouseOver");
    Thread t = new Thread()
    {



        public void run()
        {

   CoilNode leaf = findLeafNode(BehaviorismDriver.mouseListener.mouseGeom.x, BehaviorismDriver.mouseListener.mouseGeom.y);

        if (leaf.isOpen)
        {
          return;
        }

   highlightLeaf(leaf, 1, 1000);

              }
    };
    t.start();

        }

  public void highlightLeaf(final CoilNode leaf, int cnt, long openTime)
  {


        incrementCount(leaf, cnt, 200);

        if (leaf.behaviorNodeColor != null)
        {
          leaf.behaviorNodeColor.interruptImmediately();
        }
        //leaf.behaviorNodeColor = BehaviorNodeColor.nodeColor(leaf, Utils.now(), 1000L, 1f - leaf.color.a);
        //attachBehavior(leaf.behaviorNodeColor);

        Utils.sleep(openTime);

        if (leaf.isOpen)
        {
          return;
        }

        incrementCount(leaf, -cnt, 1000);

        if (leaf.behaviorNodeColor != null)
        {
          leaf.behaviorNodeColor.interruptImmediately();
        }
        //leaf.behaviorNodeColor = BehaviorNodeColor.nodeColor(leaf, Utils.now(), 2000L, .2f - leaf.color.a);
        //attachBehavior(leaf.behaviorNodeColor);

    
  }
}
