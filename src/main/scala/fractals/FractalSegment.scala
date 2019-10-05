package fractals

import core.DrawingPrimitives.{LineSegment, Vec2}



sealed trait FractalSegment {
  val start: Vec2
  val dir: Vec2
  val length: Double

  def toLine: LineSegment = {
    val end = start + dir * length
    LineSegment(start.toPoint, end.toPoint)
  }

}

/**
  * One of the segments of the Koch Curve: _/\_
  * Has direction so that when it divides, it grows new segments in the right places
  */
final case class KochSegment(start: Vec2, dir: Vec2, length: Double) extends FractalSegment {

  // Divide into 4 segments _/\_
  def divide: Seq[KochSegment] = {
    val newLength = length / 3
    Seq(
      KochSegment(start, dir, newLength),
      KochSegment(start + dir * newLength, dir.rotate(-60), newLength),
      KochSegment(start + dir * newLength + dir.rotate(-60) * newLength, dir.rotate(60), newLength),
      KochSegment(start + dir * (newLength * 2), dir, newLength)
    )
  }
}

final case class DragonCurveSegment(start: Vec2, dir: Vec2, length: Double) extends FractalSegment {

  def divide(rotationDir: Turn): Seq[DragonCurveSegment] = {
    val newLength = length / Math.sqrt(2)
    rotationDir match {
      case Right => rotateSegment(-45, newLength)
      case Left => rotateSegment(45, newLength)
    }
  }

  def rotateSegment(degrees: Int, newLength: Double): Seq[DragonCurveSegment] =
    Seq(
      DragonCurveSegment(start, dir.rotate(degrees), newLength),
      DragonCurveSegment(start + dir.rotate(degrees) * newLength, dir.rotate(- degrees), newLength)
    )

}

sealed trait Turn

case object Right extends Turn
case object Left extends Turn