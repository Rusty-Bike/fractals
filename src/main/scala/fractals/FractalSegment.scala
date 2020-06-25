package fractals

import core.DrawingPrimitives.{LineSegment, Vec2}

sealed trait FractalSegment {
  val start:  Vec2
  val dir:    Vec2
  val length: Double

  def toLine: LineSegment = {
    val end = start + dir * length

    LineSegment(
      start.toPoint,
      end.toPoint
    )
  }

}

/**
  * One of the segments of the Koch Curve: _/\_
  * Has direction so that when it divides, it grows new segments in the right places
  */
final case class KochSegment(
  start: Vec2,
  dir: Vec2,
  length: Double,
  degrees: Double
) extends FractalSegment {

  // Divide into 4 segments _/\_
  def divide: Seq[KochSegment] = {
    val cos =  Math.cos(Math.toRadians(degrees))
    val newLength = length / (2 * (1 + cos))
    Seq(
      KochSegment(
        start,
        dir,
        newLength,
        degrees
      ),
      KochSegment(
        start +
          dir *
          newLength,
        dir.rotate(-degrees),
        newLength,
        degrees
      ),
      KochSegment(
        start + dir * newLength + dir.rotate(-degrees) * newLength,
        dir.rotate(degrees),
        newLength, degrees
      ),
      KochSegment(
        start + dir * newLength  * (1 + 2 * cos),
        dir,
        newLength,
        degrees
      )
    )
  }
}

final case class DragonCurveSegment(
 start:  Vec2,
 dir:    Vec2,
 length: Double
) extends FractalSegment {

  def divide(rotationDir: Turn): Seq[DragonCurveSegment] = {
    val newLength = length / Math.sqrt(2)
    rotationDir match {
      case Right => rotateSegment(-45, newLength)
      case Left  => rotateSegment(45, newLength)
    }
  }

  def rotateSegment(
   degrees:   Int,
   newLength: Double
  ): Seq[DragonCurveSegment] =
    Seq(
      DragonCurveSegment(
        start,
        dir.rotate(degrees),
        newLength
      ),
      DragonCurveSegment(
        start + dir.rotate(degrees) * newLength,
        dir.rotate(- degrees),
        newLength
      )
    )
}

final case class MinkowskiSausageSegment(
  start:  Vec2,
  dir:    Vec2,
  length: Double
) extends FractalSegment {

  def divide: Seq[MinkowskiSausageSegment] = {
    val newLength   = length / 4
    val offset      = dir * newLength
    val offsetCW90  = offset.rotate(90)
    val offsetCCW90 = offset.rotate(-90)
    val dirCW90     = dir.rotate(90)
    val dirCCW90    = dir.rotate(-90)

    Seq(
      MinkowskiSausageSegment(
        start,
        dir,
        newLength
      ),
      MinkowskiSausageSegment(
        start + offset,
        dirCCW90,
        newLength
      ),
      MinkowskiSausageSegment(
        start + offset + offsetCCW90,
        dir,
        newLength
      ),
      MinkowskiSausageSegment(
        start + (offset * 2) + offsetCCW90,
        dirCW90,
        newLength
      ),
      MinkowskiSausageSegment(
        start + (offset * 2),
        dirCW90,
        newLength),
      MinkowskiSausageSegment(
        start + (offset * 2) + offsetCW90,
        dir,
        newLength
      ),
      MinkowskiSausageSegment(
        start + (offset * 3) + offsetCW90,
        dirCCW90,
        newLength
      ),
      MinkowskiSausageSegment(
        start + (offset * 3),
        dir,
        newLength
      )
    )
  }
}

sealed trait Turn

case object Right extends Turn
case object Left  extends Turn
