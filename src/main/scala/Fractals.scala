import DrawingPrimitives._

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
    case class KochRetHelper(segments: List[LineSegment], endingPos: Vec2, endingDir: Vec2)

    def drawASmallerKoch(startingPos: Vec2, actualDir: Vec2, iteration: Int, length: Double): KochRetHelper = {
      def calculateKochPart(startingPos: Vec2, actualDir: Vec2, iteration: Int, length: Double): KochRetHelper = {
        if(iteration == 0) {
          val ending = startingPos + actualDir * (length / 3)
          KochRetHelper(List(LineSegment(startingPos.toPoint, ending.toPoint)), ending, actualDir)
        } else {
          drawASmallerKoch(startingPos, actualDir, iteration -1, length / 3)
        }
      }

      val firstSegment = calculateKochPart(startingPos, actualDir, iteration, length)
      val secondSegment = calculateKochPart(firstSegment.endingPos, firstSegment.endingDir.rotate(-60), iteration, length)
      val thirdSegment = calculateKochPart(secondSegment.endingPos, secondSegment.endingDir.rotate(120), iteration, length)
      val fourthSegment = calculateKochPart(thirdSegment.endingPos, thirdSegment.endingDir.rotate(-60), iteration, length)

      KochRetHelper(
        firstSegment.segments ++ secondSegment.segments ++ thirdSegment.segments ++ fourthSegment.segments,
        fourthSegment.endingPos,
        fourthSegment.endingDir
      )
    }

    val start = Vec2(bottomLeftPoint)
    val dir = Vec2(1,0)

    drawASmallerKoch(start, dir, iterations, length).segments
  }

}
