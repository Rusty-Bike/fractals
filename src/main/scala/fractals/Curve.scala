package fractals

import core.DrawingPrimitives.{LineSegment, Vec2}

sealed trait Curve {

  type Depth = Int
  type Iterations = Int

}

object Koch extends Curve {

  def curve(iterations: Iterations, degrees: Double = 60)(currentDepth: Int, start: Vec2, dir: Vec2, length: Double): Seq[LineSegment] = {

    val stop: Depth => Boolean = _ == iterations

    def loop(depth: Depth, segment: KochSegment): Seq[LineSegment] =
      if (stop(depth)) {
        Seq(segment.toLine)
      } else {
        segment.divide
          .map(loop(depth + 1, _))
          .reduce(_ ++ _)
      }

    loop(currentDepth, KochSegment(start, dir, length, degrees))
  }

}


object Dragon extends Curve {

  def curve(iterations: Iterations)(currentDepth: Int, start: Vec2, dir: Vec2, length: Double): Seq[LineSegment] = {

    val stop: Depth => Boolean = _ == iterations

    def loop(depth: Depth, segment: DragonCurveSegment, direction: Turn): Seq[LineSegment] =
      if (stop(depth)) {
        Seq(segment.toLine)
      } else {
        val curve = segment.divide(direction)
        loop(depth + 1, curve.head, Right) ++ loop(depth + 1, curve.tail.head, Left)
      }

    loop(currentDepth, DragonCurveSegment(start, dir, length), Right)
  }

}

object MinkowskiSausage extends Curve {

  def curve(iterations: Iterations)(currentDepth: Int, start: Vec2, dir: Vec2, length: Double): Seq[LineSegment] = {
    val stop: Depth => Boolean = _ == iterations

    def loop(depth: Depth, segment: MinkowskiSausageSegment): Seq[LineSegment] =
      if (stop(depth)) {
        Seq(segment.toLine)
      } else {
        segment.divide
          .map(loop(depth + 1, _))
          .reduce(_ ++ _)
      }

    loop(currentDepth, MinkowskiSausageSegment(start, dir, length))
  }

}
