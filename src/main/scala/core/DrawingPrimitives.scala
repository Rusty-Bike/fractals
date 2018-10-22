package core

import core.Drawing.Turtle

object DrawingPrimitives {
  case class Point(x: Int, y: Int) {
    def moveRight(amount: Int): Point = copy(x = x + amount)
    def moveLeft(amount: Int):  Point = copy(x = x - amount)
    def moveDown(amount: Int):  Point = copy(y = y + amount)
    def moveUp(amount: Int):    Point = copy(y = y - amount)
  }

  case class Vec2(x: Double, y: Double) {
    def *(k: Double)    = Vec2(x * k, y * k)
    def +(other: Vec2)  = Vec2(x + other.x, y + other.y)
    def length: Double  = Math.sqrt(x * x + y * y)
    def normalize: Vec2 = Vec2((x / length).toInt, (y / length).toInt)

    def rotate(degrees: Double): Vec2 = {
      val radians = degrees * (Math.PI / 180)
      val nx      = x * Math.cos(radians) - y * Math.sin(radians)
      val ny      = x * Math.sin(radians) + y * Math.cos(radians)
      Vec2(nx, ny)
    }

    def toPoint = Point(x.toInt, y.toInt)
  }

  object Vec2 {
    def apply(p: Point): Vec2 = {
      Vec2(p.x, p.y)
    }
  }

  case class LineSegment(start: Point, end: Point)

  case class Triangle(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      Turtle(bottomLeftPoint, -60, List())
        .forward(length)
        .setHeading(60)
        .forward(length)
        .setHeading(-180)
        .forward(length)
        .path
    }
  }

  case class Square(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      Turtle(bottomLeftPoint, -90, List())
        .forward(length)
        .setHeading(0)
        .forward(length)
        .setHeading(90)
        .forward(length)
        .setHeading(-180)
        .forward(length)
        .path
    }
  }

  case class H(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      Turtle(bottomLeftPoint, -90, List())
        .forward(length / 2)
        .setLocation(bottomLeftPoint.moveRight(length))
        .forward(length / 2)
        .setLocation(bottomLeftPoint.moveUp(length / 4))
        .setHeading(0)
        .forward(length)
        .path
    }
  }
}
