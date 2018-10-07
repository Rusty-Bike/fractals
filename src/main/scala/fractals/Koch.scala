package fractals

import core.DrawingPrimitives.{LineSegment, Vec2}


object Koch {

  type Depth = Int
  type Iterations = Int

  def curve(iterations: Iterations)(currentDepth: Int, start: Vec2, dir: Vec2, length: Double): Seq[LineSegment] = {

    val stop: Depth => Boolean = _ == iterations

    def loop(depth: Depth, segment: KochSegment): Seq[LineSegment] =
      if (stop(depth)) {
        Seq(segment.toLine)
      } else {
        segment.divide
          .map(loop(depth + 1, _))
          .reduce(_ ++ _)
      }

    loop(currentDepth, KochSegment(start, dir, length))
  }

}
