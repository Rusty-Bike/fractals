package fractals

import core.DrawingPrimitives._

object Fractals {

  type Fractal = (Int, Int, Int, Point) => List[LineSegment]

  def sierpinski(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val triangle = Triangle(bottomLeftPoint, length)

    if (currentDepth == iterations) {
      triangle.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 2

      // Call ourselves again on the three sub-triangles
      val bottomLeftTriangle  = sierpinski(newDepth, iterations, newLength, bottomLeftPoint)
      val bottomRightTriangle = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength))
      val topCenterTriangle   = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength / 2).moveUp(newLength))

      // Merge and return the list of lines
      bottomLeftTriangle ::: topCenterTriangle ::: bottomRightTriangle
    }
  }

  def vicsek(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val square = Square(bottomLeftPoint, length)

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox      = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength))
      val centerLeftBox  = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength))
      val centerRightBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength * 2))
      val bottomBox      = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength))
      val topBox         = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength).moveUp(newLength * 2))

      // Merge and return the list of lines
      bottomBox ::: centerBox ::: centerLeftBox ::: topBox ::: centerRightBox
    }
  }

  def vicsekx(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val square = Square(bottomLeftPoint, length)

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox      = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength))
      val topLeftBox     = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2))
      val topRightBox    = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2).moveRight(newLength * 2))
      val bottomLeftBox  = vicsekx(newDepth, iterations, newLength, bottomLeftPoint)
      val bottomRightBox = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength * 2))

      // Merge and return the list of lines
      centerBox ::: bottomLeftBox ::: topRightBox ::: topLeftBox ::: bottomRightBox
    }
  }

  def cantorDust(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val square = Square(bottomLeftPoint, length)
    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the four inner squares
      val bottomLeft = cantorDust(newDepth, iterations, newLength, bottomLeftPoint)
      val topLeft = cantorDust(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2))
      val topRight = cantorDust(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2).moveRight(newLength * 2))
      val bottomRight = cantorDust(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength * 2))

      // Merge and return the list of lines
      bottomLeft ::: topLeft ::: topRight ::: bottomRight

    }
  }

  def kochCurve(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val start = Vec2(bottomLeftPoint)
    val rightDir = Vec2(1, 0)
    Koch.curve(iterations)(currentDepth, start, rightDir, length).toList
  }

  def kochSnowflake(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {

    val rightDir = Vec2(1, 0)
    val leftDir = Vec2(-1, 0)
    val upRightDir = rightDir.rotate(-60)
    val downRightDir = rightDir.rotate(60)

    val actualBottomLeft = Vec2(bottomLeftPoint) + upRightDir * (length / 3)

    val newLength = length * 2 / 3

    val kochCurve = Koch.curve(iterations) _

    kochCurve(currentDepth, actualBottomLeft + rightDir * newLength, leftDir, newLength).toList ++
      kochCurve(currentDepth, actualBottomLeft, upRightDir, newLength) ++
      kochCurve(currentDepth, actualBottomLeft + upRightDir * newLength, downRightDir, newLength)
  }


  def tree(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {

    val branchAngle = 20

    def loop(depth: Int, length: Int, start: Vec2, dir: Vec2): List[LineSegment] =
      if (depth == iterations)
        List(LineSegment(start.toPoint, (start + (dir * length)).toPoint))
      else {
        val newLength = length * 2 / 3
        val end = start + dir * length

        val trunk = LineSegment(start.toPoint, end.toPoint)
        val right = loop(depth + 1, newLength, end, dir.rotate(branchAngle))
        val left = loop(depth + 1, newLength, end, dir.rotate(-branchAngle))

        trunk +: right ::: left
      }

    val up = Vec2(0, -1)
    val start = Vec2(bottomLeftPoint.copy(x = length / 2))

    loop(currentDepth, length / 3, start, up)
  }

  def sierpinskiCarpet(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {

    def getActualBottomLeftPoint(currentDepth: Int, bottomLeftPoint: Point) = {
      if(currentDepth == 0) {
        Point(length / 3, length * 2 / 3)
      } else {
        bottomLeftPoint
      }
    }
    def getActualLength(currentDepth: Int, length: Int) = {
      if(currentDepth == 0) {
        length / 3
      } else {
        length
      }
    }

    //In the very first iteration we need to calculate the proper beginning position and size of the first square
    val actualBottomLeftPoint = getActualBottomLeftPoint(currentDepth, bottomLeftPoint)
    val actualLength = getActualLength(currentDepth, length)

    val square = Square(actualBottomLeftPoint, actualLength)

    //Second condition ensures we don't try to calculate squares that are too small to form
    if(currentDepth == iterations || actualLength < 3) {
      square.toLines
    } else {
      val newDepth = currentDepth + 1
      val newLength = actualLength / 3

      // Call ourselves again on the eight sub-squares
      val leftBox         = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveUp(actualLength * 1 / 3).moveLeft(actualLength * 2 / 3))
      val topLeftBox      = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveUp(actualLength * 4 / 3).moveLeft(actualLength * 2 / 3))
      val topBox          = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveUp(actualLength * 4 / 3).moveRight(actualLength * 1 / 3))
      val topRightBox     = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveUp(actualLength * 4 / 3).moveRight(actualLength * 4 / 3))
      val rightBox        = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveUp(actualLength * 1 / 3).moveRight(actualLength * 4 / 3))
      val bottomRightBox  = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveDown(actualLength * 2 / 3).moveRight(actualLength * 4 / 3))
      val bottomBox       = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveDown(actualLength * 2 / 3).moveRight(actualLength * 1 / 3))
      val bottomLeftBox   = sierpinskiCarpet(newDepth, iterations, newLength, actualBottomLeftPoint.moveDown(actualLength * 2 / 3).moveLeft(actualLength * 2 / 3))

      //Merge and return the list of lines
      square.toLines ::: leftBox  ::: topLeftBox ::: topBox ::: topRightBox ::: rightBox ::: bottomRightBox ::: bottomBox ::: bottomLeftBox
    }
  }
}
