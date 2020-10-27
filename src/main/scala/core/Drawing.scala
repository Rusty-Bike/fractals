package core

import core.DrawingPrimitives.{LineSegment, Point}

object Drawing {

  /**
   * A Turtle graphics implementation, which renders to a list of line segments
   *
   * @param location The starting location
   * @param heading  The direction that the turtle is facing
   * @param path     The path that the turtle has taken so far
   */
  case class Turtle(location: Point, heading: Int, path: List[LineSegment]) {
    def left(amount: Int):         Turtle = copy(heading  = heading - amount)
    def right(amount: Int):        Turtle = copy(heading  = heading + amount)
    def setLocation(point: Point): Turtle = copy(location = point)
    def setHeading(heading: Int):  Turtle = copy(heading  = heading)

    /**
     * Moves the turtle forward in whatever direction he is facing, and adds
     * a line segment of the move to the path.
     *
     * @param amount The number of steps (pixels) to take
     * @return Returns a copy of the turtle with an updated path including the
     *         move.
     */
    def forward(amount: Int): Turtle = {
      val xAdj = (amount * Math.cos(Math.toRadians(heading))).toInt
      val yAdj = (amount * Math.sin(Math.toRadians(heading))).toInt

      val newPoint = Point(
        location.x + xAdj,
        location.y + yAdj
      )

      val newLine = LineSegment(location, newPoint)

      copy(
        location = newPoint,
        path     = path ::: List(newLine)
      )
    }
  }
}
