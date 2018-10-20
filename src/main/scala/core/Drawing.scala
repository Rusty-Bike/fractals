package core

import core.DrawingPrimitives.{LineSegment, Point}

object Drawing {
  case class Turtle(location: Point, heading: Int, path: List[LineSegment]) {
    def left(amount: Int):         Turtle = copy(heading  = heading - amount)
    def right(amount: Int):        Turtle = copy(heading  = heading + amount)
    def setLocation(point: Point): Turtle = copy(location = point)
    def setHeading(heading: Int):  Turtle = copy(heading  = heading)

    def forward(amount: Int): Turtle = {

      println(s"Turtle.forward(${Int}")
      println(s"Location: (${location.x}, ${location.y})")

      val xAdj = (amount * Math.cos(Math.toRadians(heading))).toInt
        val yAdj = (amount * Math.sin(Math.toRadians(heading))).toInt

      println(s"Adjustment = (${xAdj}, ${yAdj})")

      val newPoint = Point(
        location.x + xAdj,
        location.y + yAdj
      )

      println(newPoint)

      val newLine = LineSegment(location, newPoint)

      copy(location = newPoint, path = path ::: List(newLine))

    }
  }
}