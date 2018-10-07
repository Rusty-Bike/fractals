package fractals

import core.DrawingPrimitives.{LineSegment, Vec2}

/**
  * One of the segments of the Koch Curve: _/\_
  * Has direction so that when it divides, it grows new segments in the right places
  */
case class KochSegment(start: Vec2, dir: Vec2, length: Double) {
  def toLine: LineSegment = {
    val end = start + dir * length
    LineSegment(start.toPoint, end.toPoint)
  }

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
