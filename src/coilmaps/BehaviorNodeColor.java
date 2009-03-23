/* BehaviorNodeColor.java ~ Jan 28, 2009 */

package coilmaps;

import behaviors.geom.continuous.BehaviorGeomContinuous;
import geometry.Geom;

/**
 *
 * @author angus
 */
public class BehaviorNodeColor extends BehaviorGeomContinuous
{
  CoilNode node = null;

  public static BehaviorNodeColor nodeColor(
    CoilNode node,
    long startTime,
    long lengthMS,
    float range)
  {
    return new BehaviorNodeColor(node,
      new ContinuousBehaviorBuilder(startTime, lengthMS).ranges(new float[]
      {
        range
      }).loop(LoopEnum.ONCE));
  }

  public BehaviorNodeColor(CoilNode node, ContinuousBehaviorBuilder builder)
  {
    super(builder);
    this.node = node;
  }

  public void updateGeom(Geom g)
  {
    node.color.a += offsets[0];
  }
}

