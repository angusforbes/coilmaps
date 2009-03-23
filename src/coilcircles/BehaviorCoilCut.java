/* BehaviorCoilCut.java (created on Aug 3, 2008) */

package coilcircles;

import coilmaps.*;
import geometry.Geom;
import behaviors.Behavior.LoopEnum;
import behaviors.geom.continuous.BehaviorGeomContinuous;

/**
 *
 * @author Angus Forbes
 */
public class BehaviorCoilCut extends BehaviorGeomContinuous
{
  CoilNode node = null;

  public static BehaviorCoilCut coilCut(
    CoilNode node,
    long startTime,
    long lengthMS,
    float range)
  {
    return new BehaviorCoilCut(node,
      new ContinuousBehaviorBuilder(startTime, lengthMS).ranges(new float[]
      {
        range
      }).loop(LoopEnum.ONCE));
  }

  public BehaviorCoilCut(CoilNode node, ContinuousBehaviorBuilder builder)
  {
    super(builder);
    this.node = node;
  }

  public void updateGeom(Geom g)
  {
    node.cut += offsets[0];
  }
}

/*
public BehaviorCoilCut(long startNano, long lengthMS, LoopEnum loopBehavior,
float changex, CoilNode node)
{
init(startNano, lengthMS, loopBehavior, 0f, changex, 0f, node);
}
 */
/*
public void init(long startNano, long lengthMS, LoopEnum loopBehavior, 
float minx, float maxx, 
float startPercent, CoilNode node)
{
if (startPercent < 0f || startPercent > 1f)
{
System.err.println("startPercent must be between 0f and 1f!");
}

this.node = node;

BehaviorismDriver.renderer.currentWorld.registerBehavior(this);
this.startPercent = startPercent;

this.lengthNano = Utils.millisToNanos(lengthMS);

this.startTime = startNano; //nothing will happen before the startTime

//determine what will happen after startTime... ie for startPercent != 0f
this.startNano = this.startTime - (long)(this.lengthNano * startPercent);
this.lastCheck = (long)(this.lengthNano * startPercent);

this.range_x = maxx - minx;

this.loopBehavior = loopBehavior;

setAccelerationPoints();

relativeStartNano = 0L;
relativeEndNano = lengthNano;
}
 */
/*
public float getOffset()
{
return offset_x;
}

@Override
public void resetOffsets()
{
offset_x = 0f;
}
 */
/*
@Override
public void change(Geom g)
{
node.cut += offset_x;
}
 */
/*
@Override
protected void addToOffsets(float percentage, int direction)
{
offset_x += (range_x * percentage * direction);
}	

@Override
protected void subtractFromOffsets(float percentage, int direction)
{
offset_x -= (range_x * percentage * direction);
}	
 */

