package core

import core.Drawing.Turtle

/**
 * Contains Primitive types and shapes which build upon those types.
 */
object DrawingPrimitives {

  /**
   * Represents a point and operations for moving it.
   * @param x The x coordinate of the point.
   * @param y The y coordinate of the point.
   */
  case class Point(x: Int, y: Int) {
    def moveRight(amount: Int): Point = copy(x = x + amount)
    def moveLeft(amount: Int):  Point = copy(x = x - amount)
    def moveDown(amount: Int):  Point = copy(y = y + amount)
    def moveUp(amount: Int):    Point = copy(y = y - amount)
  }

  /**
   * Represents a two dimensional vector with overloaded operators for
   * performing common vector operations.
   *
   * @param x The x component of the vector
   * @param y The y component of the vector
   */
  case class Vec2(x: Double, y: Double) {
    /**
     * Performs scalar multiplication.
     * @param k The scalar value to multiply the vector by
     * @return  A new vector instance containing the result.
     */
    def *(k: Double):   Vec2 = Vec2(x * k, y * k)

    /**
     * Adds this vector to another vector
     * @param other Another vector
     * @return Returns a new vector instance with the result.
     */
    def +(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)

    /**
     * Calculates the length of the vector
     * @return The length of the vector
     */
    def length(): Double  = Math.sqrt(x * x + y * y)

    /**
     * Calculates the unit vector of the vector
     * @return Returns the unit vector of the vector
     */
    def normalize: Vec2 = {
      val l = length()

      Vec2(
        (x / l).toInt,
        (y / l).toInt
      )
    }

    /**
     * Rotates the vector by 'degrees' degrees
     * @param degrees The number of degrees to rotate the vector
     * @return Returns a new vector which has been rotated
     */
    def rotate(degrees: Double): Vec2 = {
      val radians = degrees * (Math.PI / 180)
      val nx      = x * Math.cos(radians) - y * Math.sin(radians)
      val ny      = x * Math.sin(radians) + y * Math.cos(radians)
      Vec2(nx, ny)
    }

    /**
     * Returns a Point containing the x and y components of the vector
     * @return The x and y components of the vector as a Point
     */
    def toPoint: Point = Point(x.toInt, y.toInt)
  }

  object Vec2 {
    /**
     * Converts a point to a vector
     * @param p The point to convert
     * @return Returns the Vector representation of the Point
     */
    def apply(p: Point): Vec2 = {
      Vec2(p.x, p.y)
    }
  }

  /**
   * Represents a line segment from one point to another.
   * @param start The point at the beginning of the line segment
   * @param end The point at the end of the line segment
   */
  case class LineSegment(start: Point, end: Point)

  /**
   * Represents an equilateral triangle with the ability to render it to a list
   * of line segments.
   *
   * @param bottomLeftPoint The bottom left point of the triangle
   * @param length The length of each side of the triangle
   */
  case class Triangle(bottomLeftPoint: Point, length: Int) {
    /**
     * Renders the triangle to a list of line segments
     * @return The series of line segments
     */
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

  /**
   * Represents a square
   * @param bottomLeftPoint The bottom left point of the square
   * @param length The length of each side of the square
   */
  case class Square(bottomLeftPoint: Point, length: Int) {
    /**
     * Renders the Square to a list of line segments
     * @return The list of line segments
     */
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

  /**
   * Represents the shape of an'H'
   * @param bottomLeftPoint The bottom left point of the 'H'
   * @param length The length of each side of the 'H'
   */
  case class H(bottomLeftPoint: Point, length: Int) {
    /**
     * Renders the 'H' to a list of line segments
     * @return The list of line segments
     */
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
